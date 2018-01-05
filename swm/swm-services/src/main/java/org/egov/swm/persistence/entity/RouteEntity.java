package org.egov.swm.persistence.entity;

import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.CollectionType;
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
        route.setAuditDetails(new AuditDetails());
        route.getAuditDetails().setCreatedBy(createdBy);
        route.getAuditDetails().setCreatedTime(createdTime);
        route.getAuditDetails().setLastModifiedBy(lastModifiedBy);
        route.getAuditDetails().setLastModifiedTime(lastModifiedTime);

        return route;

    }

}
