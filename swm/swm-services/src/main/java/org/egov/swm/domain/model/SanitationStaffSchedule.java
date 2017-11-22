package org.egov.swm.domain.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
    @Size(min = 1, max = 128)
    @JsonProperty("tenantId")
    private String tenantId = null;

    @Size(min = 0, max = 256)
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
