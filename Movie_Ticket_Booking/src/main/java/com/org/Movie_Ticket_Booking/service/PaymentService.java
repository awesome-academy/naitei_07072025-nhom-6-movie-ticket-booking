package com.org.Movie_Ticket_Booking.service;

import com.org.Movie_Ticket_Booking.dto.respone.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;

public interface PaymentService {

    ApiResponse<?> dopay(Long booking_id, HttpServletRequest request) throws UnsupportedEncodingException;

    void changeStatus(boolean response, Long id);
}
