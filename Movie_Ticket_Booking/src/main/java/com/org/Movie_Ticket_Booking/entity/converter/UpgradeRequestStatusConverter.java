package com.org.Movie_Ticket_Booking.entity.converter;

import com.org.Movie_Ticket_Booking.entity.enums.UpgradeRequestStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class UpgradeRequestStatusConverter implements AttributeConverter<UpgradeRequestStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(UpgradeRequestStatus status) {
        if (status == null) {
            return null;
        }
        return status.getValue();
    }

    @Override
    public UpgradeRequestStatus convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return UpgradeRequestStatus.fromValue(dbData);
    }
}
