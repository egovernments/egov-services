package org.egov.swm.domain.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.egov.swm.web.contract.Department;
import org.egov.swm.web.contract.Designation;

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
public class Shift {

    @Size(min = 0, max = 256)
    @JsonProperty("code")
    private String code = null;

    @NotNull
    @Size(min = 1, max = 128)
    @JsonProperty("tenantId")
    private String tenantId = null;

    @NotNull
    @JsonProperty("shiftType")
    private ShiftType shiftType = null;

    @NotNull
    @JsonProperty("department")
    private Department department = null;

    @NotNull
    @JsonProperty("designation")
    private Designation designation = null;

    @NotNull
    @JsonProperty("shiftStartTime")
    private Long shiftStartTime = null;

    @NotNull
    @JsonProperty("shiftEndTime")
    private Long shiftEndTime = null;

    @NotNull
    @JsonProperty("lunchStartTime")
    private Long lunchStartTime = null;

    @NotNull
    @JsonProperty("lunchEndTime")
    private Long lunchEndTime = null;

    @NotNull
    @JsonProperty("graceTimeFrom")
    private Long graceTimeFrom = null;

    @NotNull
    @JsonProperty("graceTimeTo")
    private Long graceTimeTo = null;

    @Size(min = 0, max = 300)
    @JsonProperty("remarks")
    private String remarks = null;

    @JsonProperty("auditD0etails")
    private AuditDetails auditDetails = null;

}
