package com.client_name.medication.exception;

import com.client_name.medication.controller.MedicationController;
import com.client_name.medication.service.MedicationService;
import com.client_name.medication.ErrorCodes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@WebMvcTest(MedicationController.class)
class MedicationServiceExceptionHandlerTest {

    @Value("${auth.user.name}")
    String userName;

    @Value("${user.password}")
    String password;

    @MockBean
    private MedicationController controller;

    @Autowired
    private MockMvc mock;


    @Test
    void shouldFailIfServiceErrorsExist() throws Exception {

        given(controller.getMedications(any(String.class)))
                .willThrow(new RuntimeException("error"));
        ResultActions response = this.mock.perform(MockMvcRequestBuilders
                .get("/medications")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic(userName, password))
                .queryParam("search_term", "")
                .characterEncoding("utf-8"))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
        String contentAsString = response.andReturn().getResponse().getContentAsString();

        Assertions.assertEquals("{\"error\":[{\"errorId\":\"SE-01\",\"errorMessage\":\"Service error occurred\"}]}",contentAsString);

    }

    @Test
    void shouldFailIfDataAccessErrorsExist() throws Exception {

        given(controller.getMedications(any(String.class)))
                .willThrow(new DataAccessException(ErrorCodes.ServiceError.DATA_ACCESS_FAILURE.getErrorId(), ErrorCodes.ServiceError.DATA_ACCESS_FAILURE.getErrorMessage()));
        ResultActions response = this.mock.perform(MockMvcRequestBuilders
                .get("/medications")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic(userName, password))
                .queryParam("search_term", "")
                .characterEncoding("utf-8"))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
        String contentAsString = response.andReturn().getResponse().getContentAsString();

        Assertions.assertEquals("{\"error\":[{\"errorId\":\"SE-03\",\"errorMessage\":\"Error accessing data source\"}]}",contentAsString);

    }

    @Test
    void shouldFailIfDataNotFoundErrorsExist() throws Exception {

        given(controller.getMedications(any(String.class)))
                .willThrow(new DataAccessException(ErrorCodes.ServiceError.DATA_NOT_FOUND.getErrorId(), ErrorCodes.ServiceError.DATA_NOT_FOUND.getErrorMessage()));
        ResultActions response = this.mock.perform(MockMvcRequestBuilders
                .get("/medications")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic(userName, password))
                .queryParam("search_term", "")
                .characterEncoding("utf-8"))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
        String contentAsString = response.andReturn().getResponse().getContentAsString();

        Assertions.assertEquals("{\"error\":[{\"errorId\":\"SE-02\",\"errorMessage\":\"No data found\"}]}",contentAsString);

    }

}
