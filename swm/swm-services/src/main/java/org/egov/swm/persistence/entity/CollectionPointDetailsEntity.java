package org.egov.swm.persistence.entity;

import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.CollectionPointDetails;
import org.egov.swm.domain.model.CollectionType;

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
public class CollectionPointDetailsEntity {

	private String id = null;

	private String tenantId = null;

	private String collectionType = null;

	private String collectionPoint = null;

	private Double garbageEstimate = null;

	private String description = null;

	private String createdBy = null;

	private String lastModifiedBy = null;

	private Long createdTime = null;

	private Long lastModifiedTime = null;

	public CollectionPointDetails toDomain() {

		CollectionPointDetails collectionPointDetails = new CollectionPointDetails();
		collectionPointDetails.setId(id);
		collectionPointDetails.setTenantId(tenantId);
		collectionPointDetails.setCollectionType(CollectionType.builder().code(collectionType).build());
		collectionPointDetails.setGarbageEstimate(garbageEstimate);
		collectionPointDetails.setDescription(description);
		collectionPointDetails.setAuditDetails(new AuditDetails());
		collectionPointDetails.getAuditDetails().setCreatedBy(createdBy);
		collectionPointDetails.getAuditDetails().setCreatedTime(createdTime);
		collectionPointDetails.getAuditDetails().setLastModifiedBy(lastModifiedBy);
		collectionPointDetails.getAuditDetails().setLastModifiedTime(lastModifiedTime);

		return collectionPointDetails;

	}

}
