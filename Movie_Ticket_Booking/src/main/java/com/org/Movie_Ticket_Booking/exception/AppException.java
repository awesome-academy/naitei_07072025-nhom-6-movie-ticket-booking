package com.org.Movie_Ticket_Booking.exception;

import lombok.Data;

@Data
public class AppException extends RuntimeException{
    private ErrorCode errorCode;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMassage());
        this.errorCode=errorCode;
    }
}
