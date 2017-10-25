package org.egov.inv.persistance.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.egov.inv.domain.model.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MaterialEntity {

    private String id;

    private String code;

    private String name;

    private String description;

    private String oldCode;

    private String materialType;

    private String baseUom;

    private String inventoryType;

    private String status;

    private String purchaseUom;

    private String expenseAccount;

    private Long minQuality;

    private Long maxQuality;

    private String stockingUom;

    private String materialClass;

    private Long reorderLevel;

    private Long reorderQuantity;

    private String materialControlType;

    private String model;

    private String manufacturePartNo;

    private String technicalSpecs;

    private String termsOfDelivery;

    private boolean overrideMaterialControlType;

    private String tenantId;

    protected String createdBy;

    private String lastModifiedBy;

    private Long createdTime;

    private Long lastModifiedTime;

    public Material toDomain() {
        return Material.builder()
                .id(id)
                .code(code)
                .name(name)
                .description(description)
                .oldCode(oldCode)
                .materialType(mapMaterialType(materialType))
                .baseUom(buildUom(baseUom))
                .inventoryType(InventoryType.valueOf(inventoryType.toUpperCase()))
                .status(Status.valueOf(status.toUpperCase()))
                .purchaseUom(buildUom(purchaseUom))
                .expenseAccount(buildCoa(expenseAccount))
                .minQuality(minQuality)
                .minQuality(maxQuality)
                .stockingUom(buildUom(stockingUom))
                .materialClass(MaterialClass.valueOf(materialClass.toUpperCase()))
                .reorderLevel(reorderLevel)
                .reorderQuantity(reorderQuantity)
                .materialControlType(MaterialControlType.valueOf(materialControlType.toUpperCase()))
                .model(model)
                .manufacturePartNo(manufacturePartNo)
                .technicalSpecs(technicalSpecs)
                .termsOfDelivery(termsOfDelivery)
                .overrideMaterialControlType(overrideMaterialControlType)
                .auditDetails(buildAuditDetails(tenantId, createdBy, createdTime, lastModifiedBy, lastModifiedTime))
                .build();
    }

    private Uom buildUom(String code) {
        return Uom.builder()
                .code(code)
                .build();
    }

    private ChartOfAccount buildCoa(String glCode) {
        return ChartOfAccount.builder()
                .glCode(glCode)
                .build();
    }

    private MaterialType mapMaterialType(String code) {
        return MaterialType.builder()
                .code(code)
                .build();
    }

    private AuditDetails buildAuditDetails(String tenantId, String createdBy, Long createdTime, String lastModifiedBy, Long lastModifiedTime) {
        return AuditDetails.builder()
                .tenantId(tenantId)
                .createdBy(createdBy)
                .createdTime(createdTime)
                .lastModifiedBy(lastModifiedBy)
                .lastModifiedTime(lastModifiedTime)
                .build();
    }
}
