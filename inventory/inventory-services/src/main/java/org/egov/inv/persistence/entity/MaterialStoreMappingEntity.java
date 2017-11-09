package org.egov.inv.persistence.entity;

import io.swagger.model.*;
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

    private String chartOfAccount;

    private String tenantId;

    private String createdBy;

    private Long createdTime;

    private String lastModifiedBy;

    private Long lastModifiedTime;


    public MaterialStoreMapping toDomain() {

        return MaterialStoreMapping.builder()
                .id(id)
                .material(buildMaterial())
                .store(buildStore())
                .active(active)
                .chartofAccount(buildChartOfAccount())
                .auditDetails(buildAuditDetails())
                .build();
    }


    public MaterialStoreMappingEntity toEntity(MaterialStoreMapping materialStoreMapping) {
        AuditDetails auditDetails = materialStoreMapping.getAuditDetails();
        return MaterialStoreMappingEntity.builder()
                .id(materialStoreMapping.getId())
                .material(materialStoreMapping.getMaterial().getCode())
                .store(materialStoreMapping.getStore().getCode())
                .active(materialStoreMapping.getActive())
                .chartOfAccount(null != materialStoreMapping.getChartofAccount() ? materialStoreMapping.getChartofAccount().getGlCode() : null)
                .createdBy(auditDetails.getCreatedBy())
                .createdTime(auditDetails.getCreatedTime())
                .lastModifiedBy(auditDetails.getLastModifiedBy())
                .lastModifiedTime(auditDetails.getLastModifiedTime())
                .tenantId(auditDetails.getTenantId())
                .build();
    }

    private Material buildMaterial() {
        return Material.builder()
                .code(material)
                .build();
    }

    private Store buildStore() {
        Store store = new Store();
        store.setCode(this.store);
        return store;
    }

    private ChartofAccount buildChartOfAccount() {
        return ChartofAccount.builder()
                .glCode(chartOfAccount)
                .build();
    }

    private AuditDetails buildAuditDetails() {
        return AuditDetails.builder()
                .createdBy(createdBy)
                .createdTime(createdTime)
                .lastModifiedBy(lastModifiedBy)
                .lastModifiedTime(lastModifiedTime)
                .tenantId(tenantId)
                .build();
    }
}
