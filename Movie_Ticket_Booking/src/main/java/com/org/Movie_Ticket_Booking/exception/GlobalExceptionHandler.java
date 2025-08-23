package com.org.Movie_Ticket_Booking.exception;

import com.org.Movie_Ticket_Booking.dto.respone.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    private boolean isApiRequest(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/api/");
    }

    private ResponseEntity<ApiResponse> buildApiResponse(
            int code, String message, String path
    ) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(code);
        apiResponse.setMessage(message);
        apiResponse.setPath(path);
        apiResponse.setTimestamp(LocalDateTime.now().toString());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    private ModelAndView buildViewResponse(String viewName, String message, String path) {
        ModelAndView mav = new ModelAndView(viewName);
        mav.addObject("message", message);
        mav.addObject("path", path);
        return mav;
    }
    @ExceptionHandler(AppException.class)
    public Object handleAppException(AppException e, HttpServletRequest request){
        ErrorCode errorCode = e.getErrorCode();
        if (isApiRequest(request)) {
            return buildApiResponse(errorCode.getCode(), errorCode.getMessage(), request.getRequestURI());
        } else {
            return buildViewResponse("error", errorCode.getMessage(), request.getRequestURI());
        }
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Object handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        if (isApiRequest(request)) {
            return buildApiResponse(ErrorCode.WRONG_DATATYPE.getCode(),
                    ErrorCode.WRONG_DATATYPE.getMessage(),
                    request.getRequestURI());
        } else {
            return buildViewResponse("error", ErrorCode.WRONG_DATATYPE.getMessage(), request.getRequestURI());
        }
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Object handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        if (isApiRequest(request)) {
            return buildApiResponse(ErrorCode.METHOD_NOTSUPPORT.getCode(),
                    ErrorCode.METHOD_NOTSUPPORT.getMessage(),
                    request.getRequestURI());
        } else {
            return buildViewResponse("error", ErrorCode.METHOD_NOTSUPPORT.getMessage(), request.getRequestURI());
        }
    }

    @ExceptionHandler(Exception.class)
    public Object fallback(Exception e, HttpServletRequest request){
        if (isApiRequest(request)) {
            return buildApiResponse(ErrorCode.UNIDENTIFIED_ERROR.getCode(),
                    ErrorCode.UNIDENTIFIED_ERROR.getMessage(),
                    request.getRequestURI());
        } else {
            return buildViewResponse("error", ErrorCode.UNIDENTIFIED_ERROR.getMessage(), request.getRequestURI());
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleValidationExceptions(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .findFirst()
                .orElse(ErrorCode.REQUEST_INVALID.getMessage());

        if (isApiRequest(request)) {
            return buildApiResponse(ErrorCode.REQUEST_INVALID.getCode(), errorMessage, request.getRequestURI());
        } else {
            return buildViewResponse("error", errorMessage, request.getRequestURI());
        }
    }

    @ExceptionHandler(IOException.class)
    public Object handleIOException(IOException ex, HttpServletRequest request) {
        if (isApiRequest(request)) {
            return buildApiResponse(ErrorCode.FILE_ERROR.getCode(), ErrorCode.FILE_ERROR.getMessage(), request.getRequestURI());
        } else {
            return buildViewResponse("error", ErrorCode.FILE_ERROR.getMessage(), request.getRequestURI());
        }
    }

    @ExceptionHandler(org.springframework.web.multipart.MultipartException.class)
    public Object handleMultipartException(org.springframework.web.multipart.MultipartException ex, HttpServletRequest request) {
        if (isApiRequest(request)) {
            return buildApiResponse(ErrorCode.FILE_UPLOAD_FAILED.getCode(),
                    ErrorCode.FILE_UPLOAD_FAILED.getMessage(),
                    request.getRequestURI());
        } else {
            return buildViewResponse("error",
                    ErrorCode.FILE_UPLOAD_FAILED.getMessage(),
                    request.getRequestURI());
        }
    }

}
