package org.egov.swm.domain.model;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
public class Route {

    @JsonProperty("code")
    private String code = null;

    @NotNull
    @Length(min = 1, max = 256, message = "Value of tenantId shall be between 1 and 256")
    @JsonProperty("tenantId")
    private String tenantId = null;

    @NotNull
    @Length(min = 1, max = 256, message = "Value of name shall be between 1 and 256")
    @JsonProperty("name")
    private String name = null;

    @NotNull
    @JsonProperty("collectionType")
    private CollectionType collectionType = null;

    @NotNull
    @Digits(fraction = 2, integer = 10, message = "totalDistance shall be with 2 decimal points")
    @DecimalMin(value = "0", message = "totalDistance shall be minimum 0 Kms")
    @DecimalMax(value = "500", message = "totalDistance Shall not exceed 500 Kms")
    @JsonProperty("totalDistance")
    private Double totalDistance = null;

    @NotNull
    @Digits(fraction = 2, integer = 10, message = "totalGarbageEstimate shall be with 2 decimal points")
    @DecimalMin(value = "0", message = "totalGarbageEstimate shall be minimum 0 Tons")
    @JsonProperty("totalGarbageEstimate")
    private Double totalGarbageEstimate = null;

    @NotNull
    @Valid
    @Size(min = 1)
    @JsonProperty("collectionPoints")
    private List<RouteCollectionPointMap> collectionPoints = null;

    @Valid
    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

    @JsonProperty("totalNoOfStops")
    private Integer totalNoOfStops;

    public boolean hasDumpingGround() {
        final Long count = collectionPoints.stream().filter(cp -> (cp.getDumpingGround() != null)).count();

        return count > 0;
    }

}
