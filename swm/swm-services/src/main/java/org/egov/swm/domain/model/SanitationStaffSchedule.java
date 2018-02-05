package org.egov.swm.domain.model;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SanitationStaffSchedule {

    @NotNull
    @Length(min = 1, max = 256, message = "Value of tenantId shall be between 1 and 256")
    @JsonProperty("tenantId")
    private String tenantId = null;

    @Length(min = 0, max = 256, message = "Value of transactionNo shall be between 0 and 256")
    @JsonProperty("transactionNo")
    private String transactionNo = null;

    @NotNull
    @JsonProperty("sanitationStaffTarget")
    private SanitationStaffTarget sanitationStaffTarget = null;

    @NotNull
    @JsonProperty("shift")
    private Shift shift = null;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

}
