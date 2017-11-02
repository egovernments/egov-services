package org.egov.inv.persistence.entity;

import io.swagger.model.AuditDetails;
import io.swagger.model.MaterialStoreMapping;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MaterialStoreMappingEntity {


    public static final String TABLE_NAME = "materialstoremapping";
    public static final String SEQUENCE_NAME = "seq_materialstoremapping";
    public static final String ALIAS = "materialstoremapping";

    private String id;

    private String material;

    private String store;

    private Boolean active;

    private String tenantId;

    private String createdBy;

    private Long createdTime;

    private String lastmodifiedBy;

    private Long lastmodifiedTime;


    public MaterialStoreMapping toDomain() {
        return MaterialStoreMapping.builder()
                .id(id)
                .material(material)
                .store(store)
                .active(active)
                .auditDetails(buildAuditDetails())
                .build();
    }

    public MaterialStoreMappingEntity toEntity(MaterialStoreMapping materialStoreMapping) {
        AuditDetails auditDetails = materialStoreMapping.getAuditDetails();
        return MaterialStoreMappingEntity.builder()
                .id(materialStoreMapping.getId())
                .material(materialStoreMapping.getMaterial())
                .store(materialStoreMapping.getStore())
                .active(materialStoreMapping.isActive())
                .createdBy(auditDetails.getCreatedBy())
                .createdTime(auditDetails.getCreatedTime())
                .lastmodifiedBy(auditDetails.getLastModifiedBy())
                .lastmodifiedTime(auditDetails.getLastModifiedTime())
                .tenantId(auditDetails.getTenantId())
                .build();
    }

    private AuditDetails buildAuditDetails() {
        return AuditDetails.builder()
                .createdBy(createdBy)
                .createdTime(createdTime)
                .lastModifiedBy(lastmodifiedBy)
                .lastModifiedTime(lastmodifiedTime)
                .tenantId(tenantId)
                .build();
    }
}
