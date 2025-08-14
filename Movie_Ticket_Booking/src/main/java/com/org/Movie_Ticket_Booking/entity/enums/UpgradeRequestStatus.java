package com.org.Movie_Ticket_Booking.entity.enums;

import lombok.Getter;
import java.util.stream.Stream;

@Getter
public enum UpgradeRequestStatus {
    PENDING(0),   // Chờ duyệt
    APPROVED(1),  // Đã chấp nhận
    REJECTED(2);  // Từ chối

    private final int value;

    UpgradeRequestStatus(int value) {
        this.value = value;
    }

    public static UpgradeRequestStatus fromValue(int value) {
        return Stream.of(UpgradeRequestStatus.values())
                .filter(status -> status.getValue() == value)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid UpgradeRequestStatus value: " + value));
    }
}

