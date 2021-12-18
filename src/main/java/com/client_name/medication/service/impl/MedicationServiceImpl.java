package com.client_name.medication.service.impl;

import com.client_name.medication.ErrorCodes;
import com.client_name.medication.dal.entity.Medication;
import com.client_name.medication.dal.repository.MedicationRepository;
import com.client_name.medication.exception.DataNotFoundException;
import com.client_name.medication.model.request.SearchMedicationDTO;
import com.client_name.medication.service.MedicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicationServiceImpl implements MedicationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MedicationServiceImpl.class);

    private final MedicationRepository medicationRepository;

    @Autowired
    public MedicationServiceImpl(MedicationRepository  medicationRepository) {
        this.medicationRepository = medicationRepository;
    }


    @Override
    public List<Medication> findMedications(SearchMedicationDTO dto) {

        LOGGER.info(String.format("Fetching medications for search term : %s", dto.getSearchTerm()));

        List<Medication> medications;
        try{
            medications = medicationRepository.searchForMedications(dto.getTokenizedSearchTerm());
        }catch (DataAccessException ex){
            throw new com.client_name.medication.exception.DataAccessException(ErrorCodes.ServiceError.DATA_ACCESS_FAILURE.getErrorId(), ex.getMessage());
        }

        if (medications.isEmpty()) {
            throw new DataNotFoundException(ErrorCodes.ServiceError.DATA_NOT_FOUND.getErrorId(), ErrorCodes.ServiceError.DATA_NOT_FOUND.getErrorMessage());
        }

        return medications;

    }
}
