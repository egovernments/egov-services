package org.egov.swm.persistence.entity;

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

    public CollectionPointDetails toDomain() {

        final CollectionPointDetails collectionPointDetails = new CollectionPointDetails();
        collectionPointDetails.setId(id);
        collectionPointDetails.setTenantId(tenantId);
        collectionPointDetails.setCollectionType(CollectionType.builder().code(collectionType).build());
        collectionPointDetails.setGarbageEstimate(garbageEstimate);
        collectionPointDetails.setDescription(description);
        collectionPointDetails.setCollectionPoint(collectionPoint);

        return collectionPointDetails;

    }

}
