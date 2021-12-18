package com.client_name.medication.exception;

public class DataNotFoundException extends ServiceException{
    public DataNotFoundException(String errorId, String message) {
        super(errorId, message);
    }

}
