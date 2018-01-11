package org.egov.swm.persistence.entity;

import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.Boundary;
import org.egov.swm.domain.model.OilCompany;
import org.egov.swm.domain.model.RefillingPumpStation;

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
public class RefillingPumpStationEntity {

    private String code = null;

    private String tenantId = null;

    private String location = null;

    private String name = null;

    private String typeOfPump = null;

    private String remarks = null;

    private Double quantity = null;

    private String createdBy;

    private String lastModifiedBy;

    private Long createdTime;

    private Long lastModifiedTime;

    public RefillingPumpStation toDomain() {

        final RefillingPumpStation refillingPumpStation = new RefillingPumpStation();
        refillingPumpStation.setCode(code);
        refillingPumpStation.setTenantId(tenantId);
        refillingPumpStation.setLocation(Boundary.builder().code(location).build());
        refillingPumpStation.setName(name);
        refillingPumpStation.setRemarks(remarks);
        refillingPumpStation.setTypeOfPump(OilCompany.builder().code(typeOfPump).build());
        refillingPumpStation.setQuantity(quantity);
        refillingPumpStation.setAuditDetails(AuditDetails.builder().createdBy(createdBy).createdTime(createdTime)
                .lastModifiedBy(lastModifiedBy).lastModifiedTime(lastModifiedTime).build());

        return refillingPumpStation;

    }

}
