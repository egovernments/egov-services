package org.egov.swm.domain.model;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.*;

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
    @Length(min = 1, max = 128, message = "Value of tenantId shall be between 1 and 128")
    @JsonProperty("tenantId")
    private String tenantId = null;

    @NotNull
    @Length(min = 1, max = 128, message = "Value of name shall be between 1 and 128")
    @JsonProperty("name")
    private String name = null;

    @NotNull
    @JsonProperty("collectionType")
    private CollectionType collectionType = null;

    @NotNull
    @Digits(fraction = 2, integer = 10, message = "totalDistance shall be with 2 decimal points")
    @DecimalMin(value = "1", message = "totalDistance shall be minimum 1 Kms")
    @JsonProperty("totalDistance")
    private Double totalDistance = null;

    @NotNull
    @Digits(fraction = 2, integer = 10, message = "totalGarbageEstimate shall be with 2 decimal points")
    @DecimalMin(value = "1", message = "totalGarbageEstimate shall be minimum 1 Kms")
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

    public boolean hasDumpingGround(){
        Long count = collectionPoints.stream().filter(cp -> (cp.getDumpingGround()!= null)).count();

        return count > 0;
    }

}
