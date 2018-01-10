package org.egov.inv.persistence.entity;

import lombok.*;
import org.egov.inv.model.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MaterialTypeStoreMappingEntity {

    public static final String TABLE_NAME = "materialtypestoremapping";
    public static final String SEQUENCE_NAME = "seq_materialtypestoremapping";
    public static final String ALIAS = "materialtypestoremapping";

    private String id;

    private String materialType;

    private String store;

    private Boolean active;

    private String chartOfAccount;

    private String tenantId;

    private String createdBy;

    private Long createdTime;

    private String lastModifiedBy;

    private Long lastModifiedTime;

    private Boolean deleted;

    public MaterialTypeStoreMapping toDomain() {
        MaterialTypeStoreMapping materialTypeStoreMapping = new MaterialTypeStoreMapping();

        return materialTypeStoreMapping
                .id(id)
                .materialType(buildMaterialType())
                .store(buildStore())
                .active(active)
                .chartofAccount(buildChartOfAccount())
                .tenantId(tenantId)
                .delete(deleted)
                .auditDetails(buildAuditDetails());
    }


    public MaterialTypeStoreMappingEntity toEntity(MaterialTypeStoreMapping materialTypeStoreMapping) {
        return MaterialTypeStoreMappingEntity.builder()
                .id(materialTypeStoreMapping.getId())
                .materialType(null != materialTypeStoreMapping.getMaterialType() ? materialTypeStoreMapping.getMaterialType().getCode() : null)
                .store(null != materialTypeStoreMapping.getStore() ? materialTypeStoreMapping.getStore().getCode() : null)
                .active(materialTypeStoreMapping.getActive())
                .chartOfAccount(null != materialTypeStoreMapping.getChartofAccount() ? materialTypeStoreMapping.getChartofAccount().getGlcode() : null)
                .tenantId(materialTypeStoreMapping.getTenantId())
                .build();
    }

    private MaterialType buildMaterialType() {
    	MaterialType materialType = new MaterialType();
        return materialType.code(this.materialType);
    }

    private Store buildStore() {
        Store store = Store.builder()
                .code(this.store)
                .build();
        return store;
    }

    private ChartOfAccount buildChartOfAccount() {
        ChartOfAccount chartOfAccount = new ChartOfAccount();
                return chartOfAccount.
                        glcode(this.chartOfAccount);

    }

    private AuditDetails buildAuditDetails() {
        return AuditDetails.builder()
                .createdBy(createdBy)
                .createdTime(createdTime)
                .lastModifiedBy(lastModifiedBy)
                .lastModifiedTime(lastModifiedTime)
                .build();
    }
}
