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

	private String id = null;

	private String tenantId = null;

	private String name = null;

	private String ward = null;

	private String zoneCode = null;

	private String street = null;

	private String colony = null;

	private String createdBy = null;

	private String lastModifiedBy = null;

	private Long createdTime = null;

	private Long lastModifiedTime = null;

	public CollectionPoint toDomain() {

		CollectionPoint collectionPoint = new CollectionPoint();
		collectionPoint.setId(id);
		collectionPoint.setTenantId(tenantId);
		collectionPoint.setName(name);
		collectionPoint.setWard(Boundary.builder().code(ward).build());
		collectionPoint.setZoneCode(Boundary.builder().code(zoneCode).build());
		collectionPoint.setStreet(Boundary.builder().code(street).build());
		collectionPoint.setColony(Boundary.builder().code(colony).build());
		collectionPoint.setAuditDetails(new AuditDetails());
		collectionPoint.getAuditDetails().setCreatedBy(createdBy);
		collectionPoint.getAuditDetails().setCreatedTime(createdTime);
		collectionPoint.getAuditDetails().setLastModifiedBy(lastModifiedBy);
		collectionPoint.getAuditDetails().setLastModifiedTime(lastModifiedTime);

		return collectionPoint;

	}

}
