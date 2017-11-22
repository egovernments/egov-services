package org.egov.swm.persistence.entity;

import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.Boundary;
import org.egov.swm.domain.model.CollectionPoint;

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
public class CollectionPointEntity {

    private String code = null;

    private String tenantId = null;

    private String name = null;

    private String location = null;

    private Double latitude = null;

    private Double longitude = null;

    private String createdBy = null;

    private String lastModifiedBy = null;

    private Long createdTime = null;

    private Long lastModifiedTime = null;

    public CollectionPoint toDomain() {

        final CollectionPoint collectionPoint = new CollectionPoint();
        collectionPoint.setCode(code);
        collectionPoint.setTenantId(tenantId);
        collectionPoint.setName(name);
        collectionPoint.setLocation(Boundary.builder().code(location).build());
        collectionPoint.setAuditDetails(new AuditDetails());
        collectionPoint.getAuditDetails().setCreatedBy(createdBy);
        collectionPoint.getAuditDetails().setCreatedTime(createdTime);
        collectionPoint.getAuditDetails().setLastModifiedBy(lastModifiedBy);
        collectionPoint.getAuditDetails().setLastModifiedTime(lastModifiedTime);

        return collectionPoint;

    }

}
