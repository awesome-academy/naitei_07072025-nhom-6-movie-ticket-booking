package com.org.Movie_Ticket_Booking.mapper;

import com.org.Movie_Ticket_Booking.dto.request.UserRegister;
import com.org.Movie_Ticket_Booking.dto.respone.RegisterRespone;
import com.org.Movie_Ticket_Booking.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    // Đây là map từ request của UserRegister sang User Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isVerified", constant = "false")
    @Mapping(target = "verificationToken", ignore = true)
    @Mapping(target = "verificationExpiry", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    User toEntity(UserRegister request);

    // Đây là map từ User sang Respone để trả về cho client
    @Mapping(target = "verified", expression = "java(user.getIsVerified())")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    RegisterRespone toRegisterRespone(User user);
}
