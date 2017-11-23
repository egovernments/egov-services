package org.egov.swm.persistence.entity;

import org.egov.swm.domain.model.CollectionDetails;
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
public class CollectionDetailsEntity {

    private String tenantId = null;

    private String id = null;

    private String collectionType = null;

    private Double wetWasteCollected = null;

    private Double dryWasteCollected = null;

    private String sourceSegregation = null;

    public CollectionDetails toDomain() {

        final CollectionDetails collectionDetails = new CollectionDetails();
        collectionDetails.setTenantId(tenantId);
        collectionDetails.setId(id);
        collectionDetails.setCollectionType(CollectionType.builder().code(collectionType).build());
        collectionDetails.setWetWasteCollected(wetWasteCollected);
        collectionDetails.setDryWasteCollected(dryWasteCollected);
        collectionDetails.setSourceSegregation(sourceSegregation);

        return collectionDetails;

    }

}
