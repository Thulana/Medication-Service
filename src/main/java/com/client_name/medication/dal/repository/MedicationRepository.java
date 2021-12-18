package com.client_name.medication.dal.repository;

import com.client_name.medication.dal.entity.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Medication repository for interactions with Medication table
 *
 */
public interface MedicationRepository extends JpaRepository<Medication,String> {

    /**
     * Query to find medications which contains the search term
     * Utilize JPA native query support for FTS functionality
     *
     * @param searchTerm search term to use
     * @return All medications containing the search term
     *
     */
    @Query(value = "SELECT * FROM Medication m WHERE m.id in (SELECT medication_id from medication_search_index msi WHERE msi.content MATCH ?1)", nativeQuery = true)
    List<Medication> searchForMedications(String searchTerm);

}
