package com.client_name.medication.model.response;

import com.client_name.medication.exception.ServiceException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sun.istack.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Base response object - make API responses unified across all endpoints
 *
 * @param <T> Response object type
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class EnvelopedResponse<T>{

    private T data;

    private List<?> error;

    public void setData(T data) {
        this.data = data;
    }

    public void setError(List<?> error) {
        this.error = error;
    }

    public List<?> getError() {
        return error;
    }

    public T getData() {
        return data;
    }

    public @NotNull List<Error> toErrorResponse(@NotNull List<ServiceException> exceptions){
        List<Error> errors = new ArrayList<>();
        exceptions.forEach(exception -> {
            errors.add(new Error(exception.getErrorId(),exception.getMessage()));
        });

        return errors;

    }
}
