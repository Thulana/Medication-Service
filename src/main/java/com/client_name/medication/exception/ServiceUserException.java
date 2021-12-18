package com.client_name.medication.exception;

public class ServiceUserException extends ServiceException{

    public ServiceUserException(String errorId, String message) {
        super(errorId, message);
    }

}

