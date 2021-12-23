package com.client_name.medication.model.request;

import com.client_name.medication.dal.entity.Medication;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;


public class PutMedicationDTO {

    @NotEmpty
    @Valid
    private List<Medication> medications;

    public PutMedicationDTO(){}

    public PutMedicationDTO(List<Medication> medications) {
        this.medications = medications;
    }

    public List<Medication> getMedications() {
        return medications;
    }

    public void setMedications(List<Medication> medications) {
        this.medications = medications;
    }
}
