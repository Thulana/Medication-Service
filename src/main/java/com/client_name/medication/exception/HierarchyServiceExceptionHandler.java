package com.client_name.medication.exception;

import com.client_name.medication.ErrorCodes;
import com.client_name.medication.model.response.EnvelopedResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@ControllerAdvice
public class HierarchyServiceExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(HierarchyServiceExceptionHandler.class);

    @ExceptionHandler(Throwable.class)
    public static ResponseEntity<EnvelopedResponse> handleServiceException(Throwable e) {
        EnvelopedResponse envelopedResponse = new EnvelopedResponse();

        ServiceUserException exception = new ServiceUserException(ErrorCodes.ServiceError.SERVICE_FAILURE.getErrorId(), ErrorCodes.ServiceError.SERVICE_FAILURE.getErrorMessage());

        envelopedResponse.setError(envelopedResponse.toErrorResponse(Collections.singletonList(exception)));
        LOG.error("Service error occurred", e);
        return new ResponseEntity<>(envelopedResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataNotFoundException.class)
    public static ResponseEntity<EnvelopedResponse> handleDataNotFoundException(DataNotFoundException e) {
        EnvelopedResponse envelopedResponse = new EnvelopedResponse();
        envelopedResponse.setError(envelopedResponse.toErrorResponse(Collections.singletonList(e)));
        LOG.error("No Data found", e);
        return new ResponseEntity<>(envelopedResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataAccessException.class)
    public static ResponseEntity<EnvelopedResponse> handleDataAccessException(DataAccessException e) {
        EnvelopedResponse envelopedResponse = new EnvelopedResponse();
        envelopedResponse.setError(envelopedResponse.toErrorResponse(Collections.singletonList(e)));
        LOG.error("Error accessing database", e);
        return new ResponseEntity<>(envelopedResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(javax.validation.ValidationException.class)
    public static ResponseEntity<EnvelopedResponse> handleJavaXValidationException(javax.validation.ValidationException e) {
        EnvelopedResponse envelopedResponse = new EnvelopedResponse();

        envelopedResponse.setError(envelopedResponse.toErrorResponse(Collections.singletonList( new InvalidDataException(ErrorCodes.UserError.INVALID_DATA.getErrorId(), e.getMessage()))));

        LOG.error("bad request", e);
        return new ResponseEntity<>(envelopedResponse, HttpStatus.BAD_REQUEST);
    }

}
