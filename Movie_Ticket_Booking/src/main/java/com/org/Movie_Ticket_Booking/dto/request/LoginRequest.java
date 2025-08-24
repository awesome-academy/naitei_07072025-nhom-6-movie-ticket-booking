package com.org.Movie_Ticket_Booking.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "{validation.username.notblank}")
    @Pattern(
            regexp = "^(?:[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}|[0-9]{10})$",
            message = "{validation.username.invalid}"
    )
    private String username;

    @NotBlank(message = "{validation.password.notblank}")
    private String password;
}
