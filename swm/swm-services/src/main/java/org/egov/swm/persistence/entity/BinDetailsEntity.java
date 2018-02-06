package org.egov.swm.persistence.entity;

import org.egov.swm.domain.model.Asset;
import org.egov.swm.domain.model.BinDetails;

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
public class BinDetailsEntity {

    private String id = null;

    private String collectionPoint = null;

    private String tenantId = null;

    private String asset = null;

    public BinDetails toDomain() {

        final BinDetails binDetails = new BinDetails();
        binDetails.setTenantId(tenantId);
        binDetails.setId(id);
        binDetails.setCollectionPoint(collectionPoint);
        binDetails.setAsset(Asset.builder().code(asset).build());
        return binDetails;

    }

}
