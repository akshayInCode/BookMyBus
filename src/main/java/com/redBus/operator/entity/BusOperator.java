package com.redBus.operator.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalTime;
import java.util.Date;

@Entity
@Table(name = "bus_operators")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusOperator {

    @Id
    @Column(name = "bus_id")
    private String busId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bus_id")
    private TicketCost ticketCost;

    @Column(name = "bus_number")
    private String busNumber;
    @Column(name = "bus_operator_company_name")
    private String busOperatorCompanyName;
    @Column(name = "bus_driver_name")
    private String driverName;
    @Column(name="support_staff")
    private String supportStaff;
    @Column(name="number_of_seats")
    private int  numberSeats;
    @Column(name = "departure_city")
    private String  departureCity;
    @Column(name="arrival_city")
    private String arrivalCity;

    //@JsonFormat(pattern = "HH:mm:ss")
    @Column(name = "departure_time")
    private LocalTime departureTime;

    //@JsonFormat(pattern = "HH:mm:ss")
    @Column(name = "arrival_time")
    private LocalTime arrivalTime;

    //@JsonFormat(pattern = "dd/MM/yyyy")
   //@JsonDeserialize(using= CustomDateDeserializer.class)
    @Temporal(TemporalType.DATE)
    @Column(name = "departure_date")
    private Date departureDate;

    //@JsonFormat(pattern = "dd/MM/yyyy")
    //@JsonDeserialize(using= CustomDateDeserializer.class)
    @Temporal(TemporalType.DATE)
    @Column(name = "arrival_date")
    private Date arrivalDate;

    @Column(name = "total_travel_time")
    private double totalTravelTime;
    @Column(name = "bus_type")
    private String busType;
    @Column(name = "amenities")
    private String amenities;


}

