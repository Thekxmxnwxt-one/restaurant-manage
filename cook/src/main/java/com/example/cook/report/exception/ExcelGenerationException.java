package com.example.cook.report.exception;

public class ExcelGenerationException extends RuntimeException {
    public ExcelGenerationException(String message) {
        super(message);
    }

    public ExcelGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
