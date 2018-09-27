package org.egov.tlcalculator.web.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

/**
 * Role
 */
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2018-09-27T14:56:03.454+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role   {
        @JsonProperty("id")
        
private Long id = null;

        @JsonProperty("name")
        @NotNull@Size(min=2,max=100) 
private String name = null;

        @JsonProperty("code")
        @Size(min=2,max=50) 
private String code = null;

        @JsonProperty("description")
        @Size(max=256) 
private String description = null;

        @JsonProperty("createdBy")
        
private Long createdBy = null;

        @JsonProperty("createdDate")
        @Valid
private LocalDate createdDate = null;

        @JsonProperty("lastModifiedBy")
        
private Long lastModifiedBy = null;

        @JsonProperty("lastModifiedDate")
        @Valid
private LocalDate lastModifiedDate = null;

        @JsonProperty("tenantId")
        @NotNull
private String tenantId = null;


}

