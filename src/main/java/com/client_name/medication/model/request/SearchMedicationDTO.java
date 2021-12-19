package com.client_name.medication.model.request;

import java.util.Arrays;
import java.util.function.Predicate;
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
        return Arrays.stream(searchTerm.split(" "))
                .filter(Predicate.not(String::isBlank))
                .map(s -> s.concat("*"))
                .collect(Collectors.joining(" "));
    }

}
