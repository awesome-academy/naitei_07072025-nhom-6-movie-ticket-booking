package com.org.Movie_Ticket_Booking.controller.api;

import com.org.Movie_Ticket_Booking.dto.respone.ApiResponse;
import com.org.Movie_Ticket_Booking.service.BookingService;
import com.org.Movie_Ticket_Booking.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/payment")
public class VNPayController {
    private PaymentService paymentService;
    private BookingService bookingService;

    @Autowired
    public VNPayController(PaymentService paymentService, BookingService bookingService){
        this.paymentService=paymentService;
        this.bookingService=bookingService;
    }

    @GetMapping("/pay")
    public ApiResponse<?> doPay(@RequestParam("id") Long booking_id, HttpServletRequest request) throws UnsupportedEncodingException {
        return this.paymentService.dopay(booking_id, request);
    }

    @GetMapping("/transaction")
    public ApiResponse<?> transaction(
            @RequestParam(value = "vnp_Amount") String amount,
            @RequestParam(value = "vnp_BankCode") String bankCode,
            @RequestParam(value = "vnp_OrderInfo") String orderInfo,
            @RequestParam(value = "vnp_ResponseCode") String responseCode
    ){
        this.paymentService.changeStatus(responseCode.equals("00"), Long.parseLong(orderInfo));
        ApiResponse response = new ApiResponse();
        if (responseCode.equals("00")) {
            response.setTimestamp(LocalDateTime.now().toString());
            response.setCode(200);
            response.setMessage("Payment completed");
        }else{
            response.setTimestamp(LocalDateTime.now().toString());
            response.setCode(500);
            response.setMessage("Payment failed");
        }
        return response;
    }

}
