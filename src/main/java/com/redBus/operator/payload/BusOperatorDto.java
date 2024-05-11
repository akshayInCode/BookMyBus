package com.redBus.operator.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.redBus.operator.entity.TicketCost;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.*;
import java.time.LocalTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusOperatorDto {

    @NotBlank
    private String busId;

    @NotBlank
    private String busNumber;

    @NotBlank
    private String busOperatorCompanyName;

    @Size(min=4, message = "Driver name should be atleast 4 characters")
    private String driverName;

    @NotBlank
    private String supportStaff;

    @Positive
    private int numberSeats;

    @NotBlank
    private String departureCity;

    @NotBlank
    private String arrivalCity;

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

    @PositiveOrZero
    private double totalTravelTime;

    @NotBlank
    private String busType;

    private String amenities;

    private TicketCost ticketCost;  // mapped result of ticketcost table.

}

