package org.egov.swm.domain.model;

import javax.validation.constraints.NotNull;

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
public class RouteCollectionPointMap {

    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("route")
    private String route = null;

    @JsonProperty("collectionPoint")
    private CollectionPoint collectionPoint = null;

    @NotNull
    @JsonProperty("distance")
    private Double distance = null;

    @NotNull
    @JsonProperty("garbageEstimate")
    private Double garbageEstimate = null;

    @JsonProperty("isStartingCollectionPoint")
    private Boolean isStartingCollectionPoint = null;

    @JsonProperty("isEndingCollectionPoint")
    private Boolean isEndingCollectionPoint = null;

    @JsonProperty("dumpingGround")
    private DumpingGround dumpingGround = null;

}
