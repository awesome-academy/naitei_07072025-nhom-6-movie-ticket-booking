package com.org.Movie_Ticket_Booking.service.impl;

import com.org.Movie_Ticket_Booking.dto.respone.ApiResponse;
import com.org.Movie_Ticket_Booking.entity.Booking;
import com.org.Movie_Ticket_Booking.entity.enums.BookingStatus;
import com.org.Movie_Ticket_Booking.entity.enums.PaymentStatus;
import com.org.Movie_Ticket_Booking.exception.AppException;
import com.org.Movie_Ticket_Booking.exception.ErrorCode;
import com.org.Movie_Ticket_Booking.repository.BookingRepository;
import com.org.Movie_Ticket_Booking.repository.PaymentRepository;
import com.org.Movie_Ticket_Booking.service.PaymentService;
import com.org.Movie_Ticket_Booking.utils.VNPay;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

@Service
public class PaymentServiceImpl implements PaymentService {
    private BookingRepository bookingRepository;
    private PaymentRepository paymentRepository;

    @Autowired
    public PaymentServiceImpl(BookingRepository bookingRepository, PaymentRepository paymentRepository){
        this.bookingRepository=bookingRepository;
        this.paymentRepository=paymentRepository;
    }

    @Override
    public ApiResponse<?> dopay(Long booking_id, HttpServletRequest request) throws UnsupportedEncodingException {
        Booking booking = this.bookingRepository.findById(booking_id).orElseThrow(()-> new AppException(ErrorCode.DATA_NOT_FOUND));
        BigDecimal totalAmount=booking.getPayments().stream().toList().get(0).getAmount();
        return VNPay.doPay(totalAmount.longValue(), request, booking_id);
    }

    @Transactional
    @Override
    public void changeStatus(boolean response, Long id) {
        if(response==true){
            Booking booking= this.bookingRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.UNIDENTIFIED_ERROR));
            booking.setStatus(BookingStatus.PAID);
            booking.getPayments().stream().toList().get(0).setStatus(PaymentStatus.COMPLETED);
        }
    }
}
