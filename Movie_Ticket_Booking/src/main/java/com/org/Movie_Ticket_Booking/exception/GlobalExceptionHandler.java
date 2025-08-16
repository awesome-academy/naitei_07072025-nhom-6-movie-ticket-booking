package com.org.Movie_Ticket_Booking.exception;

import com.org.Movie_Ticket_Booking.dto.respone.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse> handleAppException(AppException e, HttpServletRequest request){
        ErrorCode errorCode = e.getErrorCode();
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMassage());
        apiResponse.setPath(request.getRequestURI());
        apiResponse.setTimestamp(LocalDateTime.now().toString());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErrorCode.WRONG_DATATYPE.getCode());
        apiResponse.setMessage(ErrorCode.WRONG_DATATYPE.getMassage());
        apiResponse.setPath(request.getRequestURI());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErrorCode.METHOD_NOTSUPPORT.getCode());
        apiResponse.setMessage(ErrorCode.METHOD_NOTSUPPORT.getMassage());
        apiResponse.setPath(request.getRequestURI());
        apiResponse.setTimestamp(LocalDateTime.now().toString());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> fallback(Exception e, HttpServletRequest request){
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErrorCode.UNIDENTIFIED_ERROR.getCode());
        apiResponse.setMessage(ErrorCode.UNIDENTIFIED_ERROR.getMassage());
        apiResponse.setPath(request.getRequestURI());
        apiResponse.setTimestamp(LocalDateTime.now().toString());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .findFirst()
                .orElse(ErrorCode.REQUEST_INVALID.getMassage());

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErrorCode.REQUEST_INVALID.getCode());
        apiResponse.setMessage(errorMessage);
        apiResponse.setPath(request.getRequestURI());
        apiResponse.setTimestamp(LocalDateTime.now().toString());
        return ResponseEntity.badRequest().body(apiResponse);
    }
}
