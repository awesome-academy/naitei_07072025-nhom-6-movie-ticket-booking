package com.org.Movie_Ticket_Booking.service.impl;

import com.org.Movie_Ticket_Booking.dto.request.BookingRequest;
import com.org.Movie_Ticket_Booking.dto.respone.BookingHistory;
import com.org.Movie_Ticket_Booking.dto.respone.BookingResponse;
import com.org.Movie_Ticket_Booking.entity.*;
import com.org.Movie_Ticket_Booking.entity.enums.BookingStatus;
import com.org.Movie_Ticket_Booking.entity.enums.PaymentStatus;
import com.org.Movie_Ticket_Booking.exception.AppException;
import com.org.Movie_Ticket_Booking.exception.ErrorCode;
import com.org.Movie_Ticket_Booking.repository.*;
import com.org.Movie_Ticket_Booking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {
    private BookingRepository bookingRepository;
    private UserRepository userRepository;
    private ShowtimeRepository showtimeRepository;
    private SeatRepository seatRepository;
    private PromotionRepository promotionRepository;
    private BookingSeatRepository bookingSeatRepository;
    private PaymentRepository paymentRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, UserRepository userRepository
            , ShowtimeRepository showtimeRepository, SeatRepository seatRepository
            , PromotionRepository promotionRepository, BookingSeatRepository bookingSeatRepository
            , PaymentRepository paymentRepository){
        this.bookingRepository=bookingRepository;
        this.userRepository=userRepository;
        this.showtimeRepository=showtimeRepository;
        this.seatRepository=seatRepository;
        this.promotionRepository=promotionRepository;
        this.bookingSeatRepository=bookingSeatRepository;
        this.paymentRepository=paymentRepository;
    }

    @Transactional
    public BookingResponse bookTicket(BookingRequest bookingRequest, User user) {
        bookingRequest.setUserId(user.getId());
        // Lấy thông tin Showtime và các Seat đã chọn
        Showtime showtime = showtimeRepository.findById(bookingRequest.getShowtimeId())
                .orElseThrow(() -> new AppException(ErrorCode.SHOWTIME_NOT_FOUND));

        Set<Seat> selectedSeats = new HashSet<>();
        Set<Long> seatIds = bookingRequest.getSeatIds();
        if(seatIds==null){
            throw new AppException(ErrorCode.DATA_NOT_FOUND);
        }
        for (Long id : seatIds) {
            selectedSeats.add(seatRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.SEAT_NOT_FOUND)));
        }

        // Kiểm tra các ghế đã bị đặt
        Set<BookingSeat> bookedSeats = bookingSeatRepository.findBySeatIdInAndBooking_Showtime(
                seatIds, showtime
        );

        if (!bookedSeats.isEmpty()) {
            Set<String> bookedSeatNumbers = bookedSeats.stream()
                    .map(bookingSeat -> bookingSeat.getSeat().getSeatNumber())
                    .collect(Collectors.toSet());
            System.out.println(bookedSeats);
            throw new AppException(ErrorCode.BOOKING_SEAT_UNAVAILABLE);
        }

        // Tính toán tổng tiền và tạo các bản ghi BookingSeat
        BigDecimal totalAmount = BigDecimal.ZERO;
        Set<BookingSeat> bookingSeats = new HashSet<>();

        for (Seat seat : selectedSeats) {
            // Giá của ghế = giá vé cơ bản + giá ghế vip/thường
            BigDecimal seatPrice = showtime.getPrice().add(seat.getTypeSeat().getExtraPrice());

            BookingSeat bookingSeat = BookingSeat.builder()
                    .seat(seat)
                    .cost(seatPrice)
                    .build();
            bookingSeats.add(bookingSeat);
            totalAmount = totalAmount.add(seatPrice);
        }
        Payment payment = Payment.builder().amount(totalAmount).status(PaymentStatus.PENDING).build();
        Set<Payment> payments = new HashSet<>();
        payments.add(payment);

        // Áp dụng khuyến mãi

        // Tạo và lưu bản ghi Booking
        Booking booking = Booking.builder()
                .user(userRepository.findById(bookingRequest.getUserId()).get())
                .showtime(showtime)
                .status(BookingStatus.PENDING_PAYMENT)
                .createdAt(LocalDateTime.now())
                .payments(payments)
                .build();
        booking = bookingRepository.save(booking);

        payment.setBooking(booking);
        paymentRepository.save(payment);

        // Cập nhật và lưu các bản ghi BookingSeat với booking_id vừa tạo
        for (BookingSeat bookingSeat : bookingSeats) {
            bookingSeat.setBooking(booking);
            this.bookingSeatRepository.save(bookingSeat);
        }

        BookingResponse bookingResponse = BookingResponse.builder()
                .bookingId(booking.getId())
                .createdAt(LocalDateTime.now())
                .totalAmount(totalAmount)
                .movieTitle(booking.getShowtime().getMovie().getTitle())
                .seatNumbers(bookingSeats.stream()
                        .map(BookingSeat::getSeat)
                        .map(Seat::getSeatNumber)
                        .collect(Collectors.toSet()))
                .room(booking.getShowtime().getRoom().getName())
                .cinema(booking.getShowtime().getRoom().getCinema().getName())
                .showtimeInfo("Start time: " +booking.getShowtime().getStartTime()+"  End time: "+booking.getShowtime().getEndTime())
                .build();
        return bookingResponse;
    }

    @Override
    public List<BookingHistory> findBookingByUserId(Long id) {
        return this.bookingRepository.findByUserId(id).stream()
                .map(b-> BookingHistory.builder()
                        .id(b.getId())
                        .startTime(b.getShowtime().getStartTime())
                        .bookingStatus(b.getStatus())
                        .createdAt(b.getCreatedAt())
                        .movieName(b.getShowtime().getMovie().getTitle())
                        .build()).toList();
    }
}
