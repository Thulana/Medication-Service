package com.client_name.medication.exception;

public class ServiceException extends RuntimeException{
    String errorId;

    public ServiceException(String errorId, String message) {
        super(message);
        this.errorId = errorId;
    }

    public String getErrorId() {
        return errorId;
    }

}

