package org.egov.pt.calculator.web.models.property;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This object holds list of documents attached during the transaciton for a property
 */
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2018-05-11T14:12:44.497+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Document   {
        @JsonProperty("id")
        private Long id;

        @JsonProperty("documentType")
        private String documentType;

        @JsonProperty("fileStore")
        private String fileStore;


}

