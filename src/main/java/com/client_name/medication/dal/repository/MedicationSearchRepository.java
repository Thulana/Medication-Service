package com.client_name.medication.dal.repository;

import com.client_name.medication.dal.entity.MedicationIndex;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * MedicationSearch repository for interactions with full text search index
 *
 */
public interface MedicationSearchRepository extends JpaRepository<MedicationIndex,String> {

}
