package com.client_name.medication.service;

import com.client_name.medication.dal.entity.Medication;
import com.client_name.medication.model.request.SearchMedicationDTO;

import java.util.List;

public interface MedicationService {

    /**
     * This method fetch medications for the given search term.
     * Use an inverted index fused with tokenizing and proximity searching
     *
     * @param dto - Search Query dto object
     * @return {@link List< Medication >} response object with medications
     */
    List<Medication> findMedications(SearchMedicationDTO dto);
}
