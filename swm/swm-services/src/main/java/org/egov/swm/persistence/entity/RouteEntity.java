package org.egov.swm.persistence.entity;

import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.CollectionPoint;
import org.egov.swm.domain.model.CollectionType;
import org.egov.swm.domain.model.DumpingGround;
import org.egov.swm.domain.model.Route;

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
public class RouteEntity {

    private String code = null;

    private String tenantId = null;

    private String name = null;

    private String collectionType = null;

    private String startingCollectionPoint = null;

    private String endingCollectionPoint = null;

    private String endingDumpingGroundPoint = null;

    private Double distance = null;

    private Double garbageEstimate = null;

    private String createdBy = null;

    private String lastModifiedBy = null;

    private Long createdTime = null;

    private Long lastModifiedTime = null;

    public Route toDomain() {

        final Route route = new Route();
        route.setCode(code);
        route.setTenantId(tenantId);
        route.setName(name);
        route.setCollectionType(CollectionType.builder().code(collectionType).build());
        route.setStartingCollectionPoint(CollectionPoint.builder().code(startingCollectionPoint).build());
        route.setEndingCollectionPoint(CollectionPoint.builder().code(endingCollectionPoint).build());
        route.setEndingDumpingGroundPoint(DumpingGround.builder().code(endingDumpingGroundPoint).build());
        route.setDistance(distance);
        route.setGarbageEstimate(garbageEstimate);
        route.setAuditDetails(new AuditDetails());
        route.getAuditDetails().setCreatedBy(createdBy);
        route.getAuditDetails().setCreatedTime(createdTime);
        route.getAuditDetails().setLastModifiedBy(lastModifiedBy);
        route.getAuditDetails().setLastModifiedTime(lastModifiedTime);

        return route;

    }

}
