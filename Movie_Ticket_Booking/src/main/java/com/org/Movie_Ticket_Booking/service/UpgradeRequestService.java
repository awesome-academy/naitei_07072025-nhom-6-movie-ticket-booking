package com.org.Movie_Ticket_Booking.service;

import com.org.Movie_Ticket_Booking.dto.request.UpgradeRqDTO;
import com.org.Movie_Ticket_Booking.dto.respone.UpgradeRqResponse;

public interface UpgradeRequestService {
    UpgradeRqResponse createUpgradeRequest(UpgradeRqDTO upgradeRqDTO);
}
