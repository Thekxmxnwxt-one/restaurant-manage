package com.example.cook.report.exception;

import com.example.cook.model.ResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalReportExceptionHandler {

    @ExceptionHandler(PdfGenerationException.class)
    public ResponseEntity<ResponseModel<Void>> handlePdfException(PdfGenerationException ex) {
        log.error("PDF Generation Error", ex);
        ResponseModel<Void> error = new ResponseModel<>();
        error.setStatus(500);
        error.setDescription("PDF Generation Error: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(CsvGenerationException.class)
    public ResponseEntity<ResponseModel<Void>> handleCsvException(CsvGenerationException ex) {
        log.error("CSV Generation Error", ex);
        ResponseModel<Void> error = new ResponseModel<>();
        error.setStatus(500);
        error.setDescription("CSV Generation Error: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseModel<Void>> handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("Bad Request: {}", ex.getMessage());
        ResponseModel<Void> error = new ResponseModel<>();
        error.setStatus(400);
        error.setDescription("Bad Request: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseModel<Void>> handleGeneralException(Exception ex) {
        log.error("Unexpected Error", ex);
        ResponseModel<Void> error = new ResponseModel<>();
        error.setStatus(500);
        error.setDescription("Unexpected Error: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}


