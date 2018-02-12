package org.egov.swm.persistence.entity;

import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.DumpingGround;
import org.egov.swm.domain.model.SourceSegregation;
import org.egov.swm.domain.model.Tenant;

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
public class SourceSegregationEntity {

    private String code = null;

    private String tenantId = null;

    private String dumpingGround = null;

    private String ulb = null;

    private Long sourceSegregationDate = null;

    private String createdBy = null;

    private String lastModifiedBy = null;

    private Long createdTime = null;

    private Long lastModifiedTime = null;

    public SourceSegregation toDomain() {

        final SourceSegregation sourceSegregation = new SourceSegregation();
        sourceSegregation.setCode(code);
        sourceSegregation.setTenantId(tenantId);
        sourceSegregation.setDumpingGround(DumpingGround.builder().code(dumpingGround).build());
        sourceSegregation.setUlb(Tenant.builder().code(ulb).build());
        sourceSegregation.setSourceSegregationDate(sourceSegregationDate);
        sourceSegregation.setAuditDetails(new AuditDetails());
        sourceSegregation.getAuditDetails().setCreatedBy(createdBy);
        sourceSegregation.getAuditDetails().setCreatedTime(createdTime);
        sourceSegregation.getAuditDetails().setLastModifiedBy(lastModifiedBy);
        sourceSegregation.getAuditDetails().setLastModifiedTime(lastModifiedTime);

        return sourceSegregation;

    }

}
