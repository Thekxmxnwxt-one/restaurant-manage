package com.example.cook.report.exception;

public class CsvGenerationException extends RuntimeException {
    public CsvGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
