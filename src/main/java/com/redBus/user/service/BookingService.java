package com.redBus.user.service;


import com.redBus.operator.entity.BusOperator;
import com.redBus.operator.entity.TicketCost;
import com.redBus.operator.repository.BusOperatorRepository;
import com.redBus.operator.repository.TicketCostRepository;
import com.redBus.user.entity.Booking;
import com.redBus.user.payload.BookingDetailsDto;
import com.redBus.user.payload.BusListDto;
import com.redBus.user.payload.PassengerDetails;
import com.redBus.user.repository.BookingRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookingService {

    @Value("${stripe.api.key}") // does reading from application.properties file
    private String stripeApiKey;

    private final BusOperatorRepository busOperatorRepository;
    private final TicketCostRepository ticketCostRepository;
    private final BookingRepository bookingRepository;

    public BookingService(BusOperatorRepository busOperatorRepository, TicketCostRepository ticketCostRepository, BookingRepository bookingRepository) {
        this.busOperatorRepository = busOperatorRepository;
        this.ticketCostRepository = ticketCostRepository;
        this.bookingRepository = bookingRepository;
    }


    public BookingDetailsDto cancelBooking(String bookingId) {
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isPresent()) {
            Booking booking = optionalBooking.get();

            Optional<BusOperator> optionalBus = busOperatorRepository.findById(booking.getBusId());
            if (optionalBus.isPresent()) {
                BusOperator bus = optionalBus.get();
                int remainingSeats = bus.getNumberSeats() + 1; // Increment seats as ticket is cancelled
                bus.setNumberSeats(remainingSeats);
                busOperatorRepository.save(bus); // Save the updated seat count
            }

            bookingRepository.delete(booking);


            BookingDetailsDto cancellationDetails = new BookingDetailsDto();
            cancellationDetails.setBookingId(booking.getBookingId());
            cancellationDetails.setMessage("Booking cancelled successfully.");
            // Populate additional fields related to the canceled booking
            cancellationDetails.setFromCity(booking.getFromCity());
            cancellationDetails.setToCity(booking.getToCity());
            cancellationDetails.setDepartureDate(booking.getDepartureDate());
            cancellationDetails.setArrivalDate(booking.getArrivalDate());
            cancellationDetails.setDepartureTime(booking.getDepartureTime());
            cancellationDetails.setArrivalTime(booking.getArrivalTime());
            cancellationDetails.setTotalTravelTime(booking.getTotalTravelTime());
            cancellationDetails.setBusCompany(booking.getBusCompany());
            cancellationDetails.setFirstName(booking.getFirstName());
            cancellationDetails.setLastName(booking.getLastName());
            cancellationDetails.setEmail(booking.getEmail());
            cancellationDetails.setMobile(booking.getMobile());
            cancellationDetails.setPrice(booking.getPrice());
            return cancellationDetails;
        } else {
            throw new IllegalArgumentException("Booking not found for provided bookingId: " + bookingId);
        }
    }

    public BookingDetailsDto createBooking(String busId, String ticketId, PassengerDetails passengerDetails) {
        BusOperator bus = busOperatorRepository.findById(busId)
                .orElseThrow(() -> new IllegalArgumentException("BusOperator not found for provided busId: " + busId));

        // Check if the departure date is in the past
        Date currentDate = new Date();
        if (bus.getDepartureDate().before(currentDate)) {
            throw new IllegalStateException("Cannot book tickets for a previous date.");
        }

        TicketCost ticketCost = ticketCostRepository.findByTicketId(ticketId);
        if (ticketCost == null) {
            throw new IllegalArgumentException("TicketCost not found for provided ticketId: " + ticketId);
        }

        // Check if there are available seats
        if (bus.getNumberSeats() <= 0) {
            throw new IllegalStateException("No available seats on this bus.");
        }

        // Reduce the number of available seats
        int remainingSeats = bus.getNumberSeats() - 1;
        bus.setNumberSeats(remainingSeats);
        busOperatorRepository.save(bus); // Save the updated seat count



        double price = ticketCost.getPrice(); // Use the price directly from TicketCost entity

        String paymentIntent = createPaymentIntent((int)ticketCost.getCost());

        if(paymentIntent!=null) {


            Booking booking = new Booking();
            String bookingId = UUID.randomUUID().toString();  // Generate booking Id
            booking.setBookingId(bookingId);
            booking.setBusId(busId);
            booking.setTicketId(ticketId);
            booking.setToCity(bus.getArrivalCity());
            booking.setFromCity(bus.getDepartureCity());
            booking.setDepartureDate(bus.getDepartureDate());
            booking.setArrivalDate(bus.getArrivalDate());
            booking.setDepartureTime(bus.getDepartureTime());
            booking.setArrivalTime(bus.getArrivalTime());
            booking.setTotalTravelTime(bus.getTotalTravelTime());
            booking.setBusCompany(bus.getBusOperatorCompanyName());
            booking.setPrice(price);
            booking.setFirstName(passengerDetails.getFirstName());
            booking.setLastName(passengerDetails.getLastName());
            booking.setEmail(passengerDetails.getEmail());
            booking.setMobile(passengerDetails.getMobile());

            Booking ticketCreatedDetails = bookingRepository.save(booking);


            BookingDetailsDto dto = new BookingDetailsDto();
            dto.setBookingId(ticketCreatedDetails.getBookingId());
            dto.setFromCity(ticketCreatedDetails.getFromCity());
            dto.setToCity(ticketCreatedDetails.getToCity());
            dto.setDepartureTime(ticketCreatedDetails.getDepartureTime());
            dto.setArrivalTime(ticketCreatedDetails.getArrivalTime());
            dto.setDepartureDate(ticketCreatedDetails.getDepartureDate());
            dto.setArrivalDate(ticketCreatedDetails.getArrivalDate());
            dto.setTotalTravelTime(ticketCreatedDetails.getTotalTravelTime());
            dto.setFirstName(ticketCreatedDetails.getFirstName());
            dto.setLastName(ticketCreatedDetails.getLastName());
            dto.setPrice(ticketCreatedDetails.getPrice());
            dto.setEmail(ticketCreatedDetails.getEmail());
            dto.setMobile(ticketCreatedDetails.getMobile());
            dto.setBusCompany(ticketCreatedDetails.getBusCompany());
            dto.setMessage("Booking Confirmed!");

            return dto;
        }else{
            System.out.println("Error!!");
        }
        return  null;
    }

    public String createPaymentIntent( Integer amount){
        Stripe.apiKey=stripeApiKey;

        try{
            PaymentIntent intent=PaymentIntent.create(
                    new PaymentIntentCreateParams.Builder()
                            .setCurrency("usd")
                            .setAmount((long)amount *100) // amount in cents
                            .build()
            );
            return  generateResponse(intent.getClientSecret());

        } catch(StripeException e){
            return  generateResponse("Error creating PaymentIntent:"+e.getMessage());
        }
    }

    private String generateResponse(String clientSecret){
        return  "{\"clientSecret\":\""+ clientSecret +"\"}";
    }


}



