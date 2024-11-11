package com.example.hospital_backend.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        logger.error("리소스를 찾을 수 없습니다: {}", ex.getMessage());

        ErrorDetails errorDetails = new ErrorDetails(new Date(), "리소스를 찾을 수 없습니다.", request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateFavoriteException.class)
    public ResponseEntity<ErrorDetails> handleDuplicateFavoriteException(DuplicateFavoriteException ex, WebRequest request) {
        logger.error("즐겨찾기가 중복되었습니다: {}", ex.getMessage());

        ErrorDetails errorDetails = new ErrorDetails(new Date(), "즐겨찾기가 중복되었습니다.", request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DuplicateHospitalException.class)
    public ResponseEntity<ErrorDetails> handleDuplicateHospitalException(DuplicateHospitalException ex, WebRequest request) {
        logger.error("병원이 중복되었습니다: {}", ex.getMessage());

        ErrorDetails errorDetails = new ErrorDetails(new Date(), "병원이 중복되었습니다.", request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidReviewException.class)
    public ResponseEntity<ErrorDetails> handleInvalidReviewException(InvalidReviewException ex, WebRequest request) {
        logger.error("잘못된 리뷰입니다: {}", ex.getMessage());

        ErrorDetails errorDetails = new ErrorDetails(new Date(), "잘못된 리뷰입니다.", request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorDetails> handleBadRequestException(BadRequestException ex, WebRequest request) {
        logger.error("잘못된 요청입니다: {}", ex.getMessage());

        ErrorDetails errorDetails = new ErrorDetails(new Date(), "잘못된 요청입니다.", request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        logger.error("유효성 검사 실패: {}", ex.getMessage());

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(Exception ex, WebRequest request) {
        logger.error("예기치 않은 오류가 발생했습니다: {}", ex.getMessage(), ex);

        ErrorDetails errorDetails = new ErrorDetails(new Date(), "예기치 않은 오류가 발생했습니다. 다시 시도해주세요.", request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDetails> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        logger.error("잘못된 입력입니다: {}", ex.getMessage());

        ErrorDetails errorDetails = new ErrorDetails(new Date(), "유효하지 않은 파일 형식입니다. 이미지 파일만 업로드할 수 있습니다.", request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
