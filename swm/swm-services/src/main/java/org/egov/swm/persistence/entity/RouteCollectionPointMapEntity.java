package org.egov.swm.persistence.entity;

import org.egov.swm.domain.model.CollectionPoint;
import org.egov.swm.domain.model.DumpingGround;
import org.egov.swm.domain.model.RouteCollectionPointMap;

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
public class RouteCollectionPointMapEntity {

    private String id = null;

    private String tenantId = null;

    private String route = null;

    private String collectionPoint = null;

    private Double distance = null;

    private Double garbageEstimate = null;

    private Boolean isStartingCollectionPoint = null;

    private Boolean isEndingCollectionPoint = null;

    private String dumpingGround = null;

    public RouteCollectionPointMap toDomain() {

        final RouteCollectionPointMap routeCollectionPointMap = new RouteCollectionPointMap();
        routeCollectionPointMap.setId(id);
        routeCollectionPointMap.setTenantId(tenantId);
        routeCollectionPointMap.setRoute(route);
        routeCollectionPointMap.setCollectionPoint(CollectionPoint.builder().code(collectionPoint).build());
        routeCollectionPointMap.setDistance(distance);
        routeCollectionPointMap.setGarbageEstimate(garbageEstimate);
        routeCollectionPointMap.setIsEndingCollectionPoint(isEndingCollectionPoint);
        routeCollectionPointMap.setIsStartingCollectionPoint(isStartingCollectionPoint);
        routeCollectionPointMap
                .setDumpingGround(dumpingGround == null ? null : DumpingGround.builder().code(dumpingGround).build());

        return routeCollectionPointMap;

    }

}
