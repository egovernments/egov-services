package org.egov.inv.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@javax.annotation.Generated(value = "org.egov.inv.codegen.languages.SpringCodegen", date = "2017-11-08T13:51:07.770Z")

@Configuration
public class SwaggerDocumentationConfig {

    ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("Inventory Service")
            .description("APIs available for Inventory:    - Create, Search, Update for Store Master.   - Create, Search, Update for Opening Balance Master.   - Create, Search, Update for Supplier Master.   - Create, Search, Update for Material -  Store Mapping Master.   - Create, Search, Update for Indent Note.   - Create, Search, Update for Purchase Order (Indent and Non Indent).   - Create, Search, Update for Material Receipt (Indent and Non Indent).   - Create, Search, Update for Material Issue (Indent and Non Indent).   - Create, Search, Update for Transfer Indent.   - Create, Search, Update for Transfer Outward.   - Create, Search, Update for Transfer Inward.   - Create, Search, Update for Scrap.   - Create, Search, Update for Disposal. ")
            .license("")
            .licenseUrl("http://unlicense.org")
            .termsOfServiceUrl("")
            .version("1.0.0")
            .contact(new Contact("","", "info@egovernments.org"))
            .build();
    }

    @Bean
    public Docket customImplementation(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                    .apis(RequestHandlerSelectors.basePackage("org.egov.inv.api"))
                    .build()
                .directModelSubstitute(org.joda.time.LocalDate.class, java.sql.Date.class)
                .directModelSubstitute(org.joda.time.DateTime.class, java.util.Date.class)
                .apiInfo(apiInfo());
    }

}
