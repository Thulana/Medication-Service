package com.client_name.medication.controller;

import com.client_name.medication.dal.entity.Disease;
import com.client_name.medication.dal.entity.Medication;
import com.client_name.medication.model.request.PutMedicationDTO;
import com.client_name.medication.model.request.SearchMedicationDTO;
import com.client_name.medication.service.impl.MedicationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@WebMvcTest(MedicationController.class)
class MedicationControllerTest {

    @Value("${auth.user.name}")
    String userName;

    @MockBean
    MedicationServiceImpl medicationService;

    @Autowired
    private MockMvc mock;

    @Value("${user.password}")
    String password;

    private String samplePayload = "{\n" +
            "  \"medications\": [\n" +
            "    {\n" +
            "      \"id\": \"b52d7619-da1f-4d63-805d-1d124fe53df4\",\n" +
            "      \"diseases\": [\n" +
            "        \"bladder disease\",\n" +
            "        \"cystitis\",\n" +
            "        \"acute cystitis\"\n" +
            "      ],\n" +
            "      \"description\": \"Test description\",\n" +
            "      \"name\": \"Folic Acid\",\n" +
            "      \"released\": \"1973-12-29\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    @BeforeEach
    public void setup() throws ParseException {

        Medication medication = new Medication();
        medication.setName("Folic Acid");
        medication.setId("b52d7619-da1f-4d63-805d-1d124fe53df4");
        medication.setDescription("Test description");
        medication.setReleased(new SimpleDateFormat("yyyy-MM-dd").parse("2021-12-19")  );
        medication.setDiseases(Collections.singleton(new Disease("bladder disease")));


        given(medicationService.findMedications(any(SearchMedicationDTO.class)))
                .willReturn(Collections.singletonList(medication));
        given(medicationService.putMedications(any(PutMedicationDTO.class)))
                .willReturn(Collections.singletonList(medication));

    }


    @Test
    void shouldFailWithoutAuthentication() throws Exception {
        this.mock.perform(MockMvcRequestBuilders.get("/medications")).andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void shouldFailWhenUserNameAndPasswordAreInCorrect() throws Exception {

        this.mock.perform(MockMvcRequestBuilders
                .get("/medications")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic(userName, "wrong password"))
                .queryParam("search_term", "test")
                .characterEncoding("utf-8"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void shouldSuccessfullySearchMedicationWhenUserNameAndPasswordAreCorrect() throws Exception {

        this.mock.perform(MockMvcRequestBuilders
                .get("/medications")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic(userName, password))
                .queryParam("search_term", "Acid")
                .characterEncoding("utf-8"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        "{\"data\":[{\"id\":\"b52d7619-da1f-4d63-805d-1d124fe53df4\",\"name\":\"Folic Acid\"," +
                                "\"description\":\"Test description\",\"released\":\"2021-12-19\",\"diseases\":[\"bladder disease\"]}]," +
                                "\"error\":[]}"));
    }

    @Test
    void shouldFailToCreateMedicationWhenUserNameAndPasswordAreInCorrect() throws Exception {

        this.mock.perform(MockMvcRequestBuilders
                .put("/medications")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic(userName, "wrong password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(samplePayload)
                .characterEncoding("utf-8"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void shouldSuccessfullyCreateMedicationWhenUserNameAndPasswordAreCorrect() throws Exception {

        this.mock.perform(MockMvcRequestBuilders
                .put("/medications")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic(userName, password))
                .contentType(MediaType.APPLICATION_JSON)
                .content(samplePayload)
                .characterEncoding("utf-8"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        "{\"data\":[{\"id\":\"b52d7619-da1f-4d63-805d-1d124fe53df4\",\"name\":\"Folic Acid\"," +
                                "\"description\":\"Test description\",\"released\":\"2021-12-19\",\"diseases\":[\"bladder disease\"]}]," +
                                "\"error\":[]}"));
    }
}
