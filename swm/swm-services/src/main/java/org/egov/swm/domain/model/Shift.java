package org.egov.swm.domain.model;

import javax.validation.constraints.NotNull;

import org.egov.swm.web.contract.Department;
import org.egov.swm.web.contract.Designation;
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
public class Shift {

    @Length(min = 0, max = 256, message = "Value of code shall be between 0 and 256")
    @JsonProperty("code")
    private String code = null;

    @NotNull
    @Length(min = 1, max = 256, message = "Value of tenantId shall be between 1 and 256")
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

    @Length(min = 0, max = 300, message = "Value of remarks shall be between 0 and 300")
    @JsonProperty("remarks")
    private String remarks = null;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

}
