package org.egov.swm.domain.model;

import java.util.List;

import javax.validation.Valid;
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
public class Route {

    @JsonProperty("code")
    private String code = null;

    @NotNull
    @Length(min = 1, max = 128)
    @JsonProperty("tenantId")
    private String tenantId = null;

    @NotNull
    @Length(min = 1, max = 128)
    @JsonProperty("name")
    private String name = null;

    @NotNull
    @JsonProperty("collectionType")
    private CollectionType collectionType = null;

    @NotNull
    @JsonProperty("startingCollectionPoint")
    private CollectionPoint startingCollectionPoint = null;

    @JsonProperty("endingCollectionPoint")
    private CollectionPoint endingCollectionPoint = null;

    @JsonProperty("endingDumpingGroundPoint")
    private DumpingGround endingDumpingGroundPoint = null;

    @NotNull
    @JsonProperty("collectionPoints")
    private List<CollectionPoint> collectionPoints = null;

    @NotNull
    @JsonProperty("distance")
    private Double distance = null;

    @NotNull
    @JsonProperty("garbageEstimate")
    private Double garbageEstimate = null;

    @Valid
    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

}
