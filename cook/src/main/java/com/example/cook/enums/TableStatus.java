package com.example.cook.enums;

public enum TableStatus {
    AVAILABLE("available"),
    NOT_AVAILABLE("not available");

    private final String dbValue;

    TableStatus(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }

    public static TableStatus fromDbValue(String value) {
        for (TableStatus status : TableStatus.values()) {
            if (status.dbValue.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + value);
    }
}
