package com.client_name.medication.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 *
 * Error response object
 * Used for returning API native and user friendly error
 *
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class Error {
    private String errorId;
    private String errorMessage;

    public Error(String errorId, String errorMessage) {
        this.errorId = errorId;
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getErrorId() {
        return errorId;
    }
}
