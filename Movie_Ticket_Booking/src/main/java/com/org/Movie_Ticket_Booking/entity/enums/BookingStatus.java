package com.org.Movie_Ticket_Booking.entity.enums;

import lombok.Getter;
import java.util.stream.Stream;

@Getter
public enum BookingStatus {
    PENDING_PAYMENT(0), // Đang chờ thanh toán
    PAID(1),            // Đã thanh toán
    CANCELLED(2);       // Đã hủy

    private final int value;

    BookingStatus(int value) {
        this.value = value;
    }

    public static BookingStatus fromValue(int value) {
        return Stream.of(BookingStatus.values())
                .filter(status -> status.getValue() == value)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid BookingStatus value: " + value));
    }
}

