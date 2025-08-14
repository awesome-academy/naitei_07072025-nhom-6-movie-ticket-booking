package com.org.Movie_Ticket_Booking.entity.converter;

import com.org.Movie_Ticket_Booking.entity.enums.BookingStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class BookingStatusConverter implements AttributeConverter<BookingStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(BookingStatus status) {
        if (status == null) {
            return null;
        }
        return status.getValue();
    }

    @Override
    public BookingStatus convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return BookingStatus.fromValue(dbData);
    }
}

