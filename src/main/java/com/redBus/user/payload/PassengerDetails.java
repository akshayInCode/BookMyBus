package com.redBus.user.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassengerDetails {

    private String firstName;
    private String lastName;
    private String email;
    private String mobile;

}
