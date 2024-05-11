package com.redBus.user.payload;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDetailsDto {

    private String bookingId;
    private String busCompany;
    private String toCity;
    private String fromCity;



    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime departureTime;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime arrivalTime;


    @JsonFormat(pattern = "dd/MM/yyyy")
    @NotNull
    private Date departureDate;

    @JsonFormat(pattern = "dd/MM/yyyy")  //It formats the date and time.
    @NotNull
    private Date arrivalDate;



    private double totalTravelTime;
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private double price;
    private String message;

}


