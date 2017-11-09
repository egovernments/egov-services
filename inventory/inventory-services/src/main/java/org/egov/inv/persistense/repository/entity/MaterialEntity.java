package org.egov.inv.persistense.repository.entity;

import io.swagger.model.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MaterialEntity {

    protected String createdBy;
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
    private Long minQuantity;
    private Long maxQuantity;
    private String staockingUom;
    private String materialClass;
    private Long reorderLevel;
    private Long reorderQuantity;
    private String materialControlType;
    private String model;
    private String manufacturePartNo;
    private String techincalSpecs;
    private String termsOfDelivery;
    private boolean overrideMaterialControlType;
    private String tenantId;
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
                .baseUom(mapUom(baseUom))
                .inventoryType(Material.InventoryTypeEnum.valueOf(inventoryType.toUpperCase()))
                .status(Material.StatusEnum.valueOf(status.toUpperCase()))
                .purchaseUom(mapUom(purchaseUom))
                .expenseAccount(mapChartOfAccounts(expenseAccount))
                .minQuantity(BigDecimal.valueOf(minQuantity))
                .maxQuantity(BigDecimal.valueOf(maxQuantity))
                .stockingUom(mapUom(staockingUom))
                .materialClass(Material.MaterialClassEnum.valueOf((materialClass.toUpperCase())))
                .reorderLevel(BigDecimal.valueOf(reorderLevel))
                .reorderQuantity(BigDecimal.valueOf(reorderQuantity))
                .materialControlType(Material.MaterialControlTypeEnum.valueOf((materialControlType.toUpperCase())))
                .model(model)
                .manufacturePartNo(manufacturePartNo)
                .techincalSpecs(techincalSpecs)
                .termsOfDelivery(termsOfDelivery)
                .overrideMaterialControlType(overrideMaterialControlType)
                .auditDetails(mapAuditDetails(tenantId, createdBy, createdTime, lastModifiedBy, lastModifiedTime))
                .build();
    }

    private Uom mapUom(String code) {
        return Uom.builder()
                .code(code)
                .build();
    }

    private ChartofAccount mapChartOfAccounts(String glCode) {
        return ChartofAccount.builder()
                .glCode(glCode)
                .build();
    }

    private MaterialType mapMaterialType(String code) {
        return MaterialType.builder()
                .code(code)
                .build();
    }

    private AuditDetails mapAuditDetails(String tenantId, String createdBy, Long createdTime, String lastModifiedBy, Long lastModifiedTime) {
        return AuditDetails.builder()
                .tenantId(tenantId)
                .createdBy(createdBy)
                .createdTime(createdTime)
                .lastModifiedBy(lastModifiedBy)
                .lastModifiedTime(lastModifiedTime)
                .build();
    }
}
