package com.org.Movie_Ticket_Booking.dto.respone;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRespone {
    private Long id;
    private String name;
    private String email;
    private String numberPhone;
    private Integer gender;
    private boolean isVerified;
    private LocalDateTime createdAt;
}
