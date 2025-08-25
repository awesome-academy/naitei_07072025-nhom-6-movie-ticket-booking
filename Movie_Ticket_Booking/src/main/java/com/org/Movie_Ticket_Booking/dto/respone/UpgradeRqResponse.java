package com.org.Movie_Ticket_Booking.dto.respone;

import com.org.Movie_Ticket_Booking.entity.enums.UpgradeRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpgradeRqResponse {
    private Long upgradeRequestId;
    private String userName; // Admin use
    private String email; // Admin use
    private LocalDateTime sentDate;
    private String cinemaName;
    private String address;
    private String description;
    private UpgradeRequestStatus status;
}
