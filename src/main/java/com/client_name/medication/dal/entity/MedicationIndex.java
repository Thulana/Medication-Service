package com.client_name.medication.dal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "medication_search_index")
public class MedicationIndex {

    @Id
    @Column(name = "medication_id", unique=true, nullable=false)
    private String medicationId;

    @Column(name = "content", unique=false, nullable=false)
    private String content;

    public MedicationIndex(String medicationId, String content) {
        this.medicationId = medicationId;
        this.content = content;
    }

    public MedicationIndex() {

    }

    public String getMedicationId() {
        return medicationId;
    }

    public void setMedicationId(String medication_id) {
        this.medicationId = medication_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
