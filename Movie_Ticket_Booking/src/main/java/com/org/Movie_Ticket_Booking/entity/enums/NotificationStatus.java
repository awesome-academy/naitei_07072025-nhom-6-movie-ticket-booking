package com.org.Movie_Ticket_Booking.entity.enums;

import lombok.Getter;
import java.util.stream.Stream;

@Getter
public enum NotificationStatus {
    UNREAD(0), // Chưa đọc
    READ(1);   // Đã đọc

    private final int value;

    NotificationStatus(int value) {
        this.value = value;
    }

    public static NotificationStatus fromValue(int value) {
        return Stream.of(NotificationStatus.values())
                .filter(status -> status.getValue() == value)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid NotificationStatus value: " + value));
    }
}

