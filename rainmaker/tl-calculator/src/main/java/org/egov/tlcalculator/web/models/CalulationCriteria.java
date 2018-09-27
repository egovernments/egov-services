package org.egov.tlcalculator.web.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.egov.tlcalculator.web.models.TradeLicense;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

/**
 * Either tradelicense object or the application number is mandatory apart from tenantid.
 */
@ApiModel(description = "Either tradelicense object or the application number is mandatory apart from tenantid.")
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2018-09-27T14:56:03.454+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CalulationCriteria   {
        @JsonProperty("tradelicense")
        @Valid
private TradeLicense tradelicense = null;

        @JsonProperty("applicationNumber")
        @Size(min=2,max=64) 
private String applicationNumber = null;

        @JsonProperty("tenantId")
        @NotNull@Size(min=2,max=256) 
private String tenantId = null;


}

