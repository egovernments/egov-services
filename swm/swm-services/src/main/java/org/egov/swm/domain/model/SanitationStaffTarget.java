package org.egov.swm.domain.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.egov.swm.web.contract.Employee;

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
    @Size(min = 1, max = 128)
    @JsonProperty("tenantId")
    private String tenantId = null;

    @Size(min = 0, max = 256)
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

    @JsonProperty("location")
    private Boundary location = null;

    @JsonProperty("route")
    private Route route = null;

    @NotNull
    @JsonProperty("employee")
    private Employee employee = null;

    @JsonProperty("collectionPoints")
    private List<CollectionPoint> collectionPoints = new ArrayList<>();

    @NotNull
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
