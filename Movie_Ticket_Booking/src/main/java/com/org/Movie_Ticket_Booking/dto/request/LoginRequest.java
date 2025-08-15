package com.org.Movie_Ticket_Booking.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "Email hoặc số điện thoại không được để trống")
    @Pattern(
            regexp = "^(?:[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}|[0-9]{10})$",
            message = "Email hoặc số điện thoại không hợp lệ"
    )
    private String username;

    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;
}
