package com.org.Movie_Ticket_Booking.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpgradeRqDTO {
    private Long userId;
    @NotBlank(message = "Tên rạp không được để trống")
    private String cinemaName;
    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;
    private String description;
}
