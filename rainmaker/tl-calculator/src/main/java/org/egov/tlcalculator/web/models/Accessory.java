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
public class Accessory   {
        @JsonProperty("tenantId")
        @NotNull@Size(min=2,max=128) 
private String tenantId = null;

        @JsonProperty("accessoryCategory")
        @Size(min=2,max=64) 
private String accessoryCategory = null;

        @JsonProperty("uom")
        @Size(min=2,max=32) 
private String uom = null;

        @JsonProperty("uomValue")
        @Size(min=2,max=32) 
private String uomValue = null;

        @JsonProperty("auditDetails")
        @Valid
private AuditDetails auditDetails = null;


}

