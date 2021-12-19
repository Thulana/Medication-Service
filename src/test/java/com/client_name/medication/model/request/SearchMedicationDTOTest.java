package com.client_name.medication.model.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SearchMedicationDTOTest {
    private SearchMedicationDTO dto;

    @BeforeEach
    public void setup(){
        dto = new SearchMedicationDTO("Viral infection");
    }

    @Test
    void shouldReturnTokenizedSearchTermWithSeveralTerms(){
        Assertions.assertEquals("Viral* infection*", dto.getTokenizedSearchTerm());
    }

    @Test
    void shouldReturnTokenizedSearchTermWithSingleTerm(){
        dto = new SearchMedicationDTO("Viral");
        Assertions.assertEquals("Viral*", dto.getTokenizedSearchTerm());
    }

    @Test
    void shouldReturnTokenizedSearchTermWithMultipleSpaces(){
        dto = new SearchMedicationDTO("Viral  infection");
        Assertions.assertEquals("Viral* infection*", dto.getTokenizedSearchTerm());
    }

}
