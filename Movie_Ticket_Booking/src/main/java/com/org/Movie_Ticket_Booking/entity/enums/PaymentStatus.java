package com.org.Movie_Ticket_Booking.entity.enums;

import lombok.Getter;
import java.util.stream.Stream;

@Getter
public enum PaymentStatus {
    PENDING(0),    // Đang xử lý thanh toán
    COMPLETED(1),  // Thanh toán thành công
    FAILED(2),     // Thanh toán thất bại
    CANCELLED(3);  // Thanh toán bị hủy

    private final int value;

    PaymentStatus(int value) {
        this.value = value;
    }

    public static PaymentStatus fromValue(int value) {
        return Stream.of(PaymentStatus.values())
                .filter(status -> status.getValue() == value)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid PaymentStatus value: " + value));
    }
}

