package com.client_name.medication.exception;

public class DataAccessException extends ServiceException{
    public DataAccessException(String errorId, String message) {
        super(errorId, message);
    }
}
