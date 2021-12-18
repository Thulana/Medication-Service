package com.client_name.medication.configuration.db;

import com.client_name.medication.dal.entity.Disease;
import com.client_name.medication.dal.entity.Medication;
import com.client_name.medication.dal.entity.MedicationIndex;
import com.client_name.medication.dal.repository.MedicationRepository;
import com.client_name.medication.dal.repository.MedicationSearchRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This is a helper written to populate the initial database state and the inverted index with the provided data.
 * This is just for the demonstration purpose. Eventually this cn be moved for a POST/PUT endpoint for external access.
 */
@Component
public class DataLoader implements ApplicationRunner {

    private MedicationRepository medicationRepository;
    private MedicationSearchRepository medicationSearchRepository;

    @Autowired
    public DataLoader(MedicationRepository medicationRepository, MedicationSearchRepository medicationSearchRepository) {
        this.medicationRepository = medicationRepository;
        this.medicationSearchRepository = medicationSearchRepository;
    }

    public void run(ApplicationArguments args) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String fileContent = new BufferedReader(
                    new InputStreamReader(ResourceUtils.getURL("classpath:db/dataset.json").openStream(), StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));
            List<Medication> medications = objectMapper.readValue(fileContent, new TypeReference<List<Medication>>() { });
            List<MedicationIndex> indices = new ArrayList<>();
            medications.forEach(medication -> {
                MedicationIndex medicationIndex = new MedicationIndex();
                medicationIndex.setMedicationId(medication.getId());
                String content = medication.getName() + " " + medication.getDiseases().stream().map(Disease::getName).collect(Collectors.joining(" "));
                medicationIndex.setContent(content);
                indices.add(medicationIndex);
            });
            medicationRepository.saveAllAndFlush(medications);
            medicationSearchRepository.saveAllAndFlush(indices);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}