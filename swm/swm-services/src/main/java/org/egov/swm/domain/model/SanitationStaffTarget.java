package org.egov.swm.domain.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import org.egov.swm.web.contract.Employee;
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
public class SanitationStaffTarget {

    @NotNull
    @Length(min = 1, max = 128, message = "Value of tenantId shall be between 1 and 128")
    @JsonProperty("tenantId")
    private String tenantId = null;

    @Length(min = 0, max = 256, message = "Value of targetNo shall be between 1 and 256")
    @JsonProperty("targetNo")
    private String targetNo = null;

    @NotNull
    @JsonProperty("targetFrom")
    private Long targetFrom = null;

    @NotNull
    @JsonProperty("targetTo")
    private Long targetTo = null;

    @NotNull
    @JsonProperty("swmProcess")
    private SwmProcess swmProcess = null;

    @JsonProperty("route")
    private Route route = null;

    @NotNull
    @JsonProperty("employee")
    private Employee employee = null;

    @JsonProperty("collectionPoints")
    private List<CollectionPoint> collectionPoints = new ArrayList<>();

    @NotNull
    @Digits(fraction = 2, integer = 3, message = "targeted Garbage shall be with 2 decimal points")
    @DecimalMax(value = "500000", message = "Targeted garbage shall be greater then 0 and less then or equel to 500000 Tons")
    @JsonProperty("targetedGarbage")
    private Double targetedGarbage = null;

    @JsonProperty("wetWaste")
    private Double wetWaste = null;

    @JsonProperty("dryWaste")
    private Double dryWaste = null;

    @JsonProperty("dumpingGround")
    private DumpingGround dumpingGround = null;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

}
