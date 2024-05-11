package com.redBus.user.controller;


import com.redBus.user.payload.BookingDetailsDto;
import com.redBus.user.payload.PassengerDetails;
import com.redBus.user.service.BookingService;
import com.redBus.util.EmailService;
import com.redBus.util.PdfGenerationService; // Assuming PdfGenerationService is in 'util' package
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final EmailService emailService;
    private final PdfGenerationService pdfGenerationService;

    public BookingController(BookingService bookingService, EmailService emailService, PdfGenerationService pdfGenerationService) {
        this.bookingService = bookingService;
        this.emailService = emailService;
        this.pdfGenerationService = pdfGenerationService;
    }

    @PostMapping
    public ResponseEntity<BookingDetailsDto> bookBus(
            @RequestParam("busId") String busId,
            @RequestParam("ticketId") String ticketId,
            @RequestBody PassengerDetails passengerDetails
    ){
        BookingDetailsDto booking = bookingService.createBooking(busId, ticketId, passengerDetails);

        if (booking != null) {
            String emailSubject = "Booking Confirmed. Booking Id: " + booking.getBookingId();
            String emailBody = "Your booking is confirmed. \nName: " + passengerDetails.getFirstName() + " " + passengerDetails.getLastName();

            try {
                byte[] pdfBytes = pdfGenerationService.generatePdf(booking); // Generate PDF using booking details

                // Sending email with PDF attachment
                emailService.sendEmailWithAttachment(
                        passengerDetails.getEmail(),
                        emailSubject,
                        emailBody,
                        pdfBytes,
                        "Booking_Details.pdf"
                );

                // Assuming emailService.sendEmailWithAttachment is implemented in EmailService
                // This method should send an email with the PDF as an attachment

            } catch (IOException e) {
                // Handle exception (e.g., log error, return appropriate HTTP status)
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<>(booking, HttpStatus.CREATED);
    }

    //http://localhost:8080/api/bookings/{bookingId}
    @DeleteMapping("/{bookingId}")
    public ResponseEntity<BookingDetailsDto> cancelBooking(@PathVariable String bookingId) {
        try {
            BookingDetailsDto cancellationDetails = bookingService.cancelBooking(bookingId);

            // Send cancellation email with PDF attachment to the user
            String emailSubject = "Booking Cancellation. Booking Id: " + cancellationDetails.getBookingId();
            String emailBody = "Your booking has been cancelled. \nBooking Details attached.";

            byte[] pdfBytes = pdfGenerationService.generatePdfForCancellation(cancellationDetails);

            emailService.sendEmailWithAttachment(
                    cancellationDetails.getEmail(),
                    emailSubject,
                    emailBody,
                    pdfBytes,
                    "Booking_Cancellation_Details.pdf"
            );

            return new ResponseEntity<>(cancellationDetails, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

