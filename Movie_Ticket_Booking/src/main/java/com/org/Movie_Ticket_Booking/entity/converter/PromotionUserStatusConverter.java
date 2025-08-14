package com.org.Movie_Ticket_Booking.entity.converter;

import com.org.Movie_Ticket_Booking.entity.enums.PromotionUserStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PromotionUserStatusConverter implements AttributeConverter<PromotionUserStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(PromotionUserStatus status) {
        if (status == null) {
            return null;
        }
        return status.getValue();
    }

    @Override
    public PromotionUserStatus convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return PromotionUserStatus.fromValue(dbData);
    }
}

