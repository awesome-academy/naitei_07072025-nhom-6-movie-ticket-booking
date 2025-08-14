package com.org.Movie_Ticket_Booking.entity.enums;

import lombok.Getter;
import java.util.stream.Stream;

@Getter
public enum PromotionUserStatus {
    TRUE(1),    // Đã dùng
    FALSE(2);  // Chưa dùng

    private final int value;

    PromotionUserStatus(int value) {
        this.value = value;
    }

    public static PromotionUserStatus fromValue(int value) {
        return Stream.of(PromotionUserStatus.values())
                .filter(status -> status.getValue() == value)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid PromotionUserStatus value: " + value));
    }
}

