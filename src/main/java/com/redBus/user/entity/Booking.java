package com.redBus.user.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.Date;

@Entity
@Table(name = "bookings")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Booking {

    @Id
    @Column(name = "booking_id")
    private String bookingId;

    @Column(name="bus_id")
    private String busId;

    @Column(name="ticket_id")
    private String ticketId;

    @Column(name="bus_company")
    private String busCompany;

    @Column(name = "to_city")
    private String toCity;

    @Column(name = "from_city")
    private String fromCity;

    @Temporal(TemporalType.DATE)
    @Column(name="departure_date")
    private Date departureDate;

    @Temporal(TemporalType.DATE)
    @Column(name="arrival_date")
    private Date arrivalDate;

    @Column(name="departure_time")
    private LocalTime departureTime;

    @Column(name="arrival_time")
    private LocalTime arrivalTime;

    @Column(name = "total_travel_time")
    private double totalTravelTime;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    private String email;
    private String mobile;
    private double price;

}
