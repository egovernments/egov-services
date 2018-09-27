package org.egov.tlcalculator.web.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.egov.tlcalculator.web.models.AuditDetails;
import org.egov.tlcalculator.web.models.BillDetails;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

/**
 * Bill
 */
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2018-09-27T14:56:03.454+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Bill   {
        @JsonProperty("id")
        
private String id = null;

        @JsonProperty("mobileNumber")
        
private String mobileNumber = null;

        @JsonProperty("payeeAddress")
        
private String payeeAddress = null;

        @JsonProperty("payeeEmail")
        
private String payeeEmail = null;

        @JsonProperty("isActive")
        
private Boolean isActive = null;

        @JsonProperty("isCancelled")
        
private Boolean isCancelled = null;

        @JsonProperty("billDetails")
        @Valid
        private List<BillDetails> billDetails = null;

        @JsonProperty("tenantId")
        @Size(min=4,max=128) 
private String tenantId = null;

        @JsonProperty("auditDetails")
        @Valid
private AuditDetails auditDetails = null;


}

