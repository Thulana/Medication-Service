package com.client_name.medication.controller;

import com.client_name.medication.dal.entity.Medication;
import com.client_name.medication.model.request.PutMedicationDTO;
import com.client_name.medication.model.request.SearchMedicationDTO;
import com.client_name.medication.model.response.EnvelopedResponse;
import com.client_name.medication.model.response.MedicationResponse;
import com.client_name.medication.service.MedicationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * REST controller for defining endpoint for consuming the API
 */

@RestController
@Validated
@Api(value = "Medication API", tags = "Medication")
@RequestMapping(value = "/medications", produces = MediaType.APPLICATION_JSON_VALUE)
public class MedicationController {

    private final MedicationService medicationService;

    @Autowired
    public MedicationController(MedicationService medicationService) {
        this.medicationService = medicationService;
    }


    @ApiOperation(
            value = "Find Medications by name or disease",
            authorizations = {@Authorization(value = "basicAuth")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Request validation errors", content = @Content(
                    schema = @Schema(implementation = EnvelopedResponse.class)
            )),
            @ApiResponse(responseCode = "404", description = "Medication not found", content = @Content(
                    schema = @Schema(implementation = EnvelopedResponse.class)
            )),
            @ApiResponse(responseCode = "500", description = "Internal Server errors", content = @Content(
                    schema = @Schema(implementation = EnvelopedResponse.class)
            )),
    })
    @Parameters(value = {
            @Parameter(
                    name = "search_term",
                    in = ParameterIn.QUERY,
                    required = true,
                    schema = @Schema(implementation = String.class),
                    example = "cancer"
            )
    })
    @GetMapping(value = "")
    public EnvelopedResponse<List<Medication>> getMedications(@RequestParam(value = "search_term")
                                                                  @NotBlank(message = "Search term cannot be blank")
                                                                  @Size(max = 100, message = "Search term cannot be more than 100 characters")
                                                                          String searchQuery) {
        MedicationResponse<List<Medication>> response = new MedicationResponse<>();

        List<Medication> medications = medicationService.findMedications(new SearchMedicationDTO(searchQuery));

        response.setData(medications);
        response.setError(response.toErrorResponse(new ArrayList<>()));

        return response;
    }

    @ApiOperation(
            value = "Create/modify medications",
            authorizations = {@Authorization(value = "basicAuth")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Request validation errors", content = @Content(
                    schema = @Schema(implementation = EnvelopedResponse.class)
            )),
            @ApiResponse(responseCode = "500", description = "Internal Server errors", content = @Content(
                    schema = @Schema(implementation = EnvelopedResponse.class)
            )),
    })
    @PutMapping(value = "")
    public EnvelopedResponse<List<Medication>> putMedications(@RequestBody @Validated PutMedicationDTO dto) {
        MedicationResponse<List<Medication>> response = new MedicationResponse<>();

        List<Medication> medications = medicationService.putMedications(dto);

        response.setData(medications);
        response.setError(response.toErrorResponse(new ArrayList<>()));

        return response;
    }
}
