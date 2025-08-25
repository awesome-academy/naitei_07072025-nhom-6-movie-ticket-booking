package com.org.Movie_Ticket_Booking.mapper;

import com.org.Movie_Ticket_Booking.dto.request.UpgradeRqDTO;
import com.org.Movie_Ticket_Booking.dto.respone.UpgradeRqResponse;
import com.org.Movie_Ticket_Booking.entity.UpgradeRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UpgradeRequestMapper {
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "status", expression = "java(com.org.Movie_Ticket_Booking.entity.enums.UpgradeRequestStatus.PENDING)")
    @Mapping(target = "user", ignore = true)
    UpgradeRequest toEntity(UpgradeRqDTO upgradeRqDTO);

    @Mapping(target = "upgradeRequestId", source = "id")
    @Mapping(target = "sentDate",        source = "createdAt")
    @Mapping(target = "userName", ignore = true)
    @Mapping(target = "email", ignore = true)
    UpgradeRqResponse toClientResponse(UpgradeRequest upgradeRequest);
}
