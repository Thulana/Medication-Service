package com.client_name.medication.service;

import com.client_name.medication.ErrorCodes;
import com.client_name.medication.dal.entity.Disease;
import com.client_name.medication.dal.entity.Medication;
import com.client_name.medication.dal.repository.MedicationRepository;
import com.client_name.medication.dal.repository.MedicationSearchRepository;
import com.client_name.medication.exception.DataAccessException;
import com.client_name.medication.model.request.PutMedicationDTO;
import com.client_name.medication.model.request.SearchMedicationDTO;
import com.client_name.medication.service.impl.MedicationServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.UncategorizedSQLException;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class MedicationServiceTest {

    private SearchMedicationDTO dto;
    private PutMedicationDTO putMedicationDTO;

    @Autowired
    private MedicationServiceImpl medicationService;

    @MockBean
    private MedicationRepository medicationRepository;

    @MockBean
    private MedicationSearchRepository medicationSearchRepository;

    @BeforeEach
    void setup() {
        dto = new SearchMedicationDTO("Viral infection");

        Medication medication = new Medication();
        medication.setName("Folic Acid");
        medication.setId("b52d7619-da1f-4d63-805d-1d124fe53df4");
        medication.setDescription("Test description");
        medication.setReleased(new Date());
        medication.setDiseases(Collections.singleton(new Disease("bladder disease")));

        putMedicationDTO = new PutMedicationDTO(Collections.singletonList(medication));

        given(medicationRepository.searchForMedications(any())).willReturn(Collections.singletonList(medication));
        given(medicationRepository.saveAll(any())).willReturn(Collections.singletonList(medication));

    }

    @Test
    void shouldGetMedicationsSuccessfully() {

        List<Medication> medications = medicationService.findMedications(dto);

        Assertions.assertEquals(1, medications.size());
    }

    @Test
    void shouldFailToGetMedicationsOnDatabaseFailure() {

        given(medicationRepository.searchForMedications(dto.getTokenizedSearchTerm())).willThrow(UncategorizedSQLException.class);

        try {
            medicationService.findMedications(dto);
            Assertions.fail("Should throw data access exception");
        } catch (DataAccessException expectedException) {
            Assertions.assertEquals(ErrorCodes.ServiceError.DATA_ACCESS_FAILURE.getErrorId(), expectedException.getErrorId());
        }

    }

    @Test
    void shouldCreateMedicationsSuccessfully() {

        List<Medication> medications = medicationService.putMedications(putMedicationDTO);

        Assertions.assertEquals(1, medications.size());
    }

    @Test
    void shouldFailToCreateMedicationsOnDatabaseFailure() {

        given(medicationSearchRepository.saveAll(any())).willThrow(UncategorizedSQLException.class);

        try {
            medicationService.putMedications(putMedicationDTO);
            Assertions.fail("Should throw data access exception");
        } catch (DataAccessException expectedException) {
            Assertions.assertEquals(ErrorCodes.ServiceError.DATA_ACCESS_FAILURE.getErrorId(), expectedException.getErrorId());
        }

    }

}
