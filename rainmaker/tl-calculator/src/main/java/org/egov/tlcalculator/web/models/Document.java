package org.egov.tlcalculator.web.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.egov.tlcalculator.web.models.AuditDetails;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

/**
 * A Object holds the basic data for a Trade License
 */
@ApiModel(description = "A Object holds the basic data for a Trade License")
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2018-09-27T14:56:03.454+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Document   {
        @JsonProperty("tenantId")
        @NotNull@Size(min=2,max=128) 
private String tenantId = null;

        @JsonProperty("documentType")
        @NotNull@Size(min=2,max=64) 
private String documentType = null;

        @JsonProperty("fileStoreId")
        @NotNull@Size(min=2,max=64) 
private String fileStoreId = null;

        @JsonProperty("documentUid")
        @Size(min=2,max=64) 
private String documentUid = null;

        @JsonProperty("auditDetails")
        @Valid
private AuditDetails auditDetails = null;


}

