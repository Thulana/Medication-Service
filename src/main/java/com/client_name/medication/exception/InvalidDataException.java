package com.client_name.medication.exception;

public class InvalidDataException extends ServiceUserException{

    public InvalidDataException(String errorId, String message) {
        super(errorId, message);
    }

}

