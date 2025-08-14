package com.org.Movie_Ticket_Booking.entity.converter;

import com.org.Movie_Ticket_Booking.entity.enums.NotificationStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class NotificationStatusConverter implements AttributeConverter<NotificationStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(NotificationStatus status) {
        if (status == null) {
            return null;
        }
        return status.getValue();
    }

    @Override
    public NotificationStatus convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return NotificationStatus.fromValue(dbData);
    }
}

