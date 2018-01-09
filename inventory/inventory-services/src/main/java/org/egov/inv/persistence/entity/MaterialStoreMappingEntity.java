package org.egov.inv.persistence.entity;

import lombok.*;
import org.egov.inv.model.*;

import static org.springframework.util.StringUtils.isEmpty;

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
                .chartofAccount(!isEmpty(chartOfAccount) ? buildChartOfAccount() : null)
                .auditDetails(buildAuditDetails())
                .build();
    }


    public MaterialStoreMappingEntity toEntity(MaterialStoreMapping materialStoreMapping) {
        AuditDetails auditDetails = materialStoreMapping.getAuditDetails();
        return MaterialStoreMappingEntity.builder()
                .id(materialStoreMapping.getId())
                .material(null != materialStoreMapping.getMaterial() ? materialStoreMapping.getMaterial().getCode() : null)
                .store(null != materialStoreMapping.getStore() ? materialStoreMapping.getStore().getCode() : null)
                .active(materialStoreMapping.getActive())
                .chartOfAccount(null != materialStoreMapping.getChartofAccount() ? materialStoreMapping.getChartofAccount().getGlcode() : null)
                .tenantId(materialStoreMapping.getTenantId())
                .build();
    }

    private Material buildMaterial() {
        return Material.builder()
                .code(material)
                .build();
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
