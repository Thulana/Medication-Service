package com.client_name.medication.model.request;

import java.util.Arrays;
import java.util.stream.Collectors;

public class SearchMedicationDTO {
    private String searchTerm;

    public SearchMedicationDTO(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public String getTokenizedSearchTerm() {
        return Arrays.stream(searchTerm.split(" ")).map(s -> {
            return s.concat("*");
        }).collect(Collectors.joining(" "));
    }

}
