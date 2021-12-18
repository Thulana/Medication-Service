package com.client_name.medication.exception;

import com.client_name.medication.ErrorCodes;
import com.client_name.medication.model.response.EnvelopedResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.web.firewall.RequestRejectedException;
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

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                               HttpStatus status, WebRequest request) {
        BindingResult bindingResult = ex.getBindingResult();

        EnvelopedResponse envelopedResponse = new EnvelopedResponse();

        List<ServiceUserException> errors = bindingResult.getFieldErrors().stream().map(this::fieldErrors)
                .collect(Collectors.toList());

        envelopedResponse.setError(envelopedResponse.toErrorResponse(errors));

        return new ResponseEntity<>(envelopedResponse, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        EnvelopedResponse envelopedResponse = new EnvelopedResponse();

        ServiceUserException exception = new ServiceUserException(ErrorCodes.UserError.INVALID_DATA.getErrorId(), "Invalid request body");

        envelopedResponse.setError(envelopedResponse.toErrorResponse(Collections.singletonList(exception)));
        LOG.error("bad request", ex);
        return new ResponseEntity<>(envelopedResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(RequestRejectedException.class)
    public ResponseEntity<EnvelopedResponse> handleRequestRejectedException(RequestRejectedException exception) {
        EnvelopedResponse envelopedResponse = new EnvelopedResponse();
        InvalidDataException invalidDataException = new InvalidDataException(ErrorCodes.UserError.INVALID_DATA.getErrorId(), exception.getMessage());
        envelopedResponse.setError(envelopedResponse.toErrorResponse(Collections.singletonList(invalidDataException)));
        LOG.error("bad request", exception);
        return new ResponseEntity<>(envelopedResponse, HttpStatus.BAD_REQUEST);
    }

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

    private ServiceUserException fieldErrors(FieldError fieldError) {
        return new ServiceUserException(ErrorCodes.UserError.INVALID_DATA.getErrorId(), fieldError.getCode().equalsIgnoreCase("typeMismatch")
                ? "Type of the request parameter '" + fieldError.getField() + "' is invalid"
                : fieldError.getField() + " " + fieldError.getDefaultMessage());

    }

}
