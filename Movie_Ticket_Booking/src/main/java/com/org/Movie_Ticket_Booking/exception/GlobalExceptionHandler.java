package com.org.Movie_Ticket_Booking.exception;

import com.org.Movie_Ticket_Booking.dto.respone.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Map<ErrorCode, HttpStatus> ERROR_CODE_STATUS_MAP = new HashMap<>();

    static {
        ERROR_CODE_STATUS_MAP.put(ErrorCode.USER_NOT_FOUND, HttpStatus.UNAUTHORIZED);
        ERROR_CODE_STATUS_MAP.put(ErrorCode.INVALID_CREDENTIALS, HttpStatus.UNAUTHORIZED);
        ERROR_CODE_STATUS_MAP.put(ErrorCode.REQUEST_INVALID, HttpStatus.BAD_REQUEST);
        ERROR_CODE_STATUS_MAP.put(ErrorCode.WRONG_DATATYPE, HttpStatus.BAD_REQUEST);
        ERROR_CODE_STATUS_MAP.put(ErrorCode.METHOD_NOTSUPPORT, HttpStatus.METHOD_NOT_ALLOWED);
        ERROR_CODE_STATUS_MAP.put(ErrorCode.ACCESS_DENIED, HttpStatus.FORBIDDEN);
        ERROR_CODE_STATUS_MAP.put(ErrorCode.AUTH_FAILED, HttpStatus.UNAUTHORIZED);
        ERROR_CODE_STATUS_MAP.put(ErrorCode.UNIDENTIFIED_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        ERROR_CODE_STATUS_MAP.put(ErrorCode.EMAIL_ALREADY_EXISTS, HttpStatus.CONFLICT);
        ERROR_CODE_STATUS_MAP.put(ErrorCode.PASSWORD_TOO_WEAK, HttpStatus.BAD_REQUEST);
        ERROR_CODE_STATUS_MAP.put(ErrorCode.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND);
        ERROR_CODE_STATUS_MAP.put(ErrorCode.TOKEN_EXPIRED, HttpStatus.UNAUTHORIZED);
        ERROR_CODE_STATUS_MAP.put(ErrorCode.TOKEN_INVALID, HttpStatus.UNAUTHORIZED);
        ERROR_CODE_STATUS_MAP.put(ErrorCode.FILE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        ERROR_CODE_STATUS_MAP.put(ErrorCode.FILE_UPLOAD_FAILED, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private boolean isApiRequest(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/api/");
    }

    private String nowIsoTimestamp() {
        return OffsetDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    private ResponseEntity<ApiResponse> buildApiResponse(ErrorCode errorCode, String message, String path) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(message);
        apiResponse.setPath(path);
        apiResponse.setTimestamp(nowIsoTimestamp());

        HttpStatus status = ERROR_CODE_STATUS_MAP.getOrDefault(errorCode, HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(status).body(apiResponse);
    }

    private ModelAndView buildViewResponse(String viewName, String message, String path) {
        ModelAndView mav = new ModelAndView(viewName);
        mav.addObject("message", message);
        mav.addObject("path", path);
        return mav;
    }

    @ExceptionHandler(AppException.class)
    public Object handleAppException(AppException e, HttpServletRequest request) {
        ErrorCode errorCode = e.getErrorCode();
        if (isApiRequest(request)) {
            return buildApiResponse(errorCode, errorCode.getMessage(), request.getRequestURI());
        } else {
            return buildViewResponse("error", errorCode.getMessage(), request.getRequestURI());
        }
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Object handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        if (isApiRequest(request)) {
            return buildApiResponse(ErrorCode.WRONG_DATATYPE, ErrorCode.WRONG_DATATYPE.getMessage(), request.getRequestURI());
        } else {
            return buildViewResponse("error", ErrorCode.WRONG_DATATYPE.getMessage(), request.getRequestURI());
        }
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Object handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        if (isApiRequest(request)) {
            return buildApiResponse(ErrorCode.METHOD_NOTSUPPORT, ErrorCode.METHOD_NOTSUPPORT.getMessage(), request.getRequestURI());
        } else {
            return buildViewResponse("error", ErrorCode.METHOD_NOTSUPPORT.getMessage(), request.getRequestURI());
        }
    }


    @ExceptionHandler(AuthenticationException.class)
    public Object handleAuthenticationException(AuthenticationException ex, HttpServletRequest request) {
        if (isApiRequest(request)) {
            return buildApiResponse(ErrorCode.AUTH_FAILED, ErrorCode.AUTH_FAILED.getMessage(), request.getRequestURI());
        } else {
            return buildViewResponse("error", ErrorCode.AUTH_FAILED.getMessage(), request.getRequestURI());
        }
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Object handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        if (isApiRequest(request)) {
            return buildApiResponse(ErrorCode.ACCESS_DENIED, ErrorCode.ACCESS_DENIED.getMessage(), request.getRequestURI());
        } else {
            return buildViewResponse("error", ErrorCode.ACCESS_DENIED.getMessage(), request.getRequestURI());
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String errorMessage = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("; "));

        if (errorMessage.isEmpty()) {
            errorMessage = ErrorCode.REQUEST_INVALID.getMessage();
        }

        if (isApiRequest(request)) {
            return buildApiResponse(ErrorCode.REQUEST_INVALID, errorMessage, request.getRequestURI());
        } else {
            return buildViewResponse("error", errorMessage, request.getRequestURI());
        }
    }

    @ExceptionHandler(IOException.class)
    public Object handleIOException(IOException ex, HttpServletRequest request) {
        if (isApiRequest(request)) {
            return buildApiResponse(ErrorCode.FILE_ERROR, ErrorCode.FILE_ERROR.getMessage(), request.getRequestURI());
        } else {
            return buildViewResponse("error", ErrorCode.FILE_ERROR.getMessage(), request.getRequestURI());
        }
    }

    @ExceptionHandler(org.springframework.web.multipart.MultipartException.class)
    public Object handleMultipartException(org.springframework.web.multipart.MultipartException ex, HttpServletRequest request) {
        if (isApiRequest(request)) {
            return buildApiResponse(ErrorCode.FILE_UPLOAD_FAILED, ErrorCode.FILE_UPLOAD_FAILED.getMessage(), request.getRequestURI());
        } else {
            return buildViewResponse("error", ErrorCode.FILE_UPLOAD_FAILED.getMessage(), request.getRequestURI());
        }
    }

    @ExceptionHandler(Exception.class)
    public Object fallback(Exception e, HttpServletRequest request) {
        if (isApiRequest(request)) {
            return buildApiResponse(ErrorCode.UNIDENTIFIED_ERROR, ErrorCode.UNIDENTIFIED_ERROR.getMessage(), request.getRequestURI());
        } else {
            return buildViewResponse("error", ErrorCode.UNIDENTIFIED_ERROR.getMessage(), request.getRequestURI());
        }
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public Object handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        if (isApiRequest(request)) {
            return buildApiResponse(ErrorCode.INVALID_STATUS,
                    ErrorCode.INVALID_STATUS.getMessage(),
                    request.getRequestURI());
        } else {
            return buildViewResponse("error",
                    ErrorCode.INVALID_STATUS.getMessage(),
                    request.getRequestURI());
        }
    }

    @ExceptionHandler(org.springframework.dao.DataAccessException.class)
    public Object handleDatabaseError(org.springframework.dao.DataAccessException ex, HttpServletRequest request) {
        if (isApiRequest(request)) {
            return buildApiResponse(ErrorCode.DATABASE_ERROR,
                    ErrorCode.DATABASE_ERROR.getMessage(),
                    request.getRequestURI());
        } else {
            return buildViewResponse("error",
                    ErrorCode.DATABASE_ERROR.getMessage(),
                    request.getRequestURI());
        }
    }
}
