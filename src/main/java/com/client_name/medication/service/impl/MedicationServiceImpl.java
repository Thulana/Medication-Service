package com.client_name.medication.service.impl;

import com.client_name.medication.ErrorCodes;
import com.client_name.medication.dal.entity.Disease;
import com.client_name.medication.dal.entity.Medication;
import com.client_name.medication.dal.entity.MedicationIndex;
import com.client_name.medication.dal.repository.MedicationRepository;
import com.client_name.medication.dal.repository.MedicationSearchRepository;
import com.client_name.medication.exception.DataNotFoundException;
import com.client_name.medication.model.request.PutMedicationDTO;
import com.client_name.medication.model.request.SearchMedicationDTO;
import com.client_name.medication.service.MedicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicationServiceImpl implements MedicationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MedicationServiceImpl.class);

    private final MedicationRepository medicationRepository;
    private final MedicationSearchRepository medicationSearchRepository;

    @Autowired
    public MedicationServiceImpl(MedicationRepository  medicationRepository, MedicationSearchRepository medicationSearchRepository) {
        this.medicationRepository = medicationRepository;
        this.medicationSearchRepository = medicationSearchRepository;
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

    @Override
    @Transactional
    public List<Medication> putMedications(PutMedicationDTO dto) {

        LOGGER.info(String.format("Updating medications : %s", dto.getMedications()));

        List<Medication> medications;
        try{
            medications = medicationRepository.saveAll(dto.getMedications());
            updateSearchIndex(medications);
        }catch (DataAccessException ex){
            throw new com.client_name.medication.exception.DataAccessException(ErrorCodes.ServiceError.DATA_ACCESS_FAILURE.getErrorId(), ex.getMessage());
        }

        return medications;
    }

    private void updateSearchIndex(List<Medication> medications){
        List<MedicationIndex> indices = new ArrayList<>();
        medications.forEach(medication -> {
            MedicationIndex medicationIndex = new MedicationIndex();
            medicationIndex.setMedicationId(medication.getId());
            String content = medication.getName() + " " + medication.getDescription() + " " + medication.getDiseases().stream().map(Disease::getName).collect(Collectors.joining(" "));
            medicationIndex.setContent(content);
            indices.add(medicationIndex);
        });
        medicationSearchRepository.saveAll(indices);
    }
}
