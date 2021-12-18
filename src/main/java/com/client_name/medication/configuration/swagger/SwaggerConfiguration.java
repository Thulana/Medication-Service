package com.client_name.medication.configuration.swagger;

import com.client_name.medication.model.response.EnvelopedResponse;
import com.fasterxml.classmate.TypeResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Swagger configuration class
 */
@Configuration
public class SwaggerConfiguration {

    private static final String CONTROLLER_PACKAGE = "com.client_name.medication.controller";
    private static final String SERVICE_NAME = "Medication Service";
    private static final String SERVICE_DESCRIPTION = "Manage Different medications and their details";
    private static final String SERVICE_TOS = "";
    private static final String COMPANY_NAME = "Client-Name GmbH";
    private static final String COMPANY_WEB = "https://www.clientname.de/";
    private static final String COMPANY_EMAIL = "info@clientname.de";
    private static final String OSS_LICENSE = "MIT License";
    private static final String OSS_LICENSE_URL = "https://opensource.org/licenses/MIT";

    private BuildProperties buildProperties;

    @Autowired
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public Docket api(TypeResolver typeResolver) {
        return new Docket(DocumentationType.SWAGGER_2)
                .additionalModels(
                        typeResolver.resolve(EnvelopedResponse.class)
                )
                .select()
                .apis(RequestHandlerSelectors.basePackage(CONTROLLER_PACKAGE))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo()).securitySchemes(basicScheme());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                SERVICE_NAME,
                SERVICE_DESCRIPTION,
                buildProperties.getVersion(),
                SERVICE_TOS,
                new Contact(COMPANY_NAME, COMPANY_WEB, COMPANY_EMAIL),
                OSS_LICENSE,
                OSS_LICENSE_URL,
                Collections.emptyList());
    }

    private List<SecurityScheme> basicScheme() {
        List<SecurityScheme> schemeList = new ArrayList<>();
        schemeList.add(new BasicAuth("basicAuth"));
        return schemeList;
    }

}
