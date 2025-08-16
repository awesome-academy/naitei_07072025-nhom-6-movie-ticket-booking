package com.org.Movie_Ticket_Booking.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserRegister {
    @NotBlank(message = "Tên không được để trống")
    private String name;

    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;

    @NotBlank
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank
    @Pattern(regexp = "^(0[0-9]{9})$", message = "Số điện thoại không hợp lệ")
    private String numberPhone;

    @NotNull(message = "Ngày sinh không được để trống")
    private LocalDate birthDay;

    @NotNull(message = "Giới tính không được để trống")
    private Integer gender;

    @NotBlank
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*]).{8,}$",
            message = "Mật khẩu phải >= 8 ký tự, gồm chữ hoa, số và ký tự đặc biệt"
    )
    private String password;
}
