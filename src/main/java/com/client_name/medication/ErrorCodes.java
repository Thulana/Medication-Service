package com.client_name.medication;

/**
 * For defining errors
 * This helps to handle or manage error definitions uniformly
 */
public interface ErrorCodes {

    // Errors as a result of consuming user
    enum UserError {
        INVALID_DATA("DVE-01", "Validation error");

        private String errorId;
        private String errorMessage;

        UserError(String errorId, String errorMessage){
            this.errorId = errorId;
            this.errorMessage = errorMessage;
        }

        public String getErrorId() {
            return errorId;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }

    // Error as a result of internal service behaviour
    enum ServiceError{
        SERVICE_FAILURE("SE-01", "Service error occurred"),
        DATA_NOT_FOUND("SE-02", "No data found"),
        DATA_ACCESS_FAILURE("SE-03", "Error accessing data source");

        private String errorId;
        private String errorMessage;

        ServiceError(String errorId, String errorMessage){
            this.errorId = errorId;
            this.errorMessage = errorMessage;
        }

        public String getErrorId() {
            return errorId;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }
}
