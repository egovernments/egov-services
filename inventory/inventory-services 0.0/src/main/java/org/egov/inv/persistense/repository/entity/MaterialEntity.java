package org.egov.inv.persistense.repository.entity;

import io.swagger.model.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MaterialEntity {

    public static final String TABLE_NAME = "material";
    public static final String SEQUENCE_NAME = "seq_material";
    public static final String ALIAS = "material";

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
    private String stockingUom;
    private String materialClass;
    private Long reorderLevel;
    private Long reorderQuantity;
    private Boolean lotcontrol;
    private Boolean shelfLifeControl;
    private Boolean serialNumber;
    private Boolean scrapable;
    private String assetcategory;
    private String model;
    private String manufacturePartNo;
    private String techincalSpecs;
    private String termsOfDelivery;
    private String tenantId;
    private String lastModifiedBy;
    private Long createdTime;
    private String createdBy;
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
                .inventoryType(null != inventoryType ? Material.InventoryTypeEnum.valueOf(inventoryType.toUpperCase()) : null)
                .status(Material.StatusEnum.valueOf(status.toUpperCase()))
                .purchaseUom(mapUom(purchaseUom))
                .expenseAccount(mapChartOfAccounts(expenseAccount))
                .minQuantity(BigDecimal.valueOf(minQuantity))
                .maxQuantity(BigDecimal.valueOf(maxQuantity))
                .stockingUom(mapUom(stockingUom))
                .materialClass(null != materialClass ? Material.MaterialClassEnum.valueOf((materialClass.toUpperCase())) : null)
                .reorderLevel(BigDecimal.valueOf(reorderLevel))
                .reorderQuantity(BigDecimal.valueOf(reorderQuantity))
                .lotControl(lotcontrol)
                .shelfLifeControl(shelfLifeControl)
                .serialNumber(serialNumber)
                .scrapable(scrapable)
                .assetCategory(buildAssetCategory())
                .model(model)
                .manufacturePartNo(manufacturePartNo)
                .techincalSpecs(techincalSpecs)
                .termsOfDelivery(termsOfDelivery)
                .auditDetails(mapAuditDetails(tenantId, createdBy, createdTime, lastModifiedBy, lastModifiedTime))
                .build();
    }

    public MaterialEntity toEntity(Material material) {
        return MaterialEntity.builder()
                .id(material.getId())
                .assetcategory(null != material.getAssetCategory() ? material.getAssetCategory().getCode() : null)
                .baseUom(material.getBaseUom().getCode())
                .code(material.getCode())
                .createdBy(material.getAuditDetails().getCreatedBy())
                .createdTime(material.getAuditDetails().getCreatedTime())
                .description(material.getDescription())
                .expenseAccount(null != material.getExpenseAccount() ? material.getExpenseAccount().getGlCode() : null)
                .inventoryType(null != material.getInventoryType() ? material.getInventoryType().toString() : null)
                .lastModifiedBy(material.getAuditDetails().getLastModifiedBy())
                .lastModifiedTime(material.getAuditDetails().getLastModifiedTime())
                .lotcontrol(material.getLotControl())
                .manufacturePartNo(material.getManufacturePartNo())
                .materialClass(null != material.getMaterialClass() ? material.getMaterialClass().toString() : null)
                .materialType(null != material.getMaterialType() ? material.getMaterialType().getCode() : null)
                .maxQuantity(null != material.getMaxQuantity() ? material.getMaxQuantity().longValue() : null)
                .minQuantity(null != material.getMinQuantity() ? material.getMinQuantity().longValue() : null)
                .model(material.getModel())
                .name(material.getName())
                .oldCode(material.getOldCode())
                .purchaseUom(null != material.getPurchaseUom() ? material.getPurchaseUom().getCode() : null)
                .reorderLevel(null != material.getReorderLevel() ? material.getReorderLevel().longValue() : null)
                .reorderQuantity(null != material.getReorderQuantity() ? material.getReorderQuantity().longValue() : null)
                .scrapable(material.getScrapable())
                .serialNumber(material.getSerialNumber())
                .shelfLifeControl(material.getShelfLifeControl())
                .status(null != material.getStatus() ? material.getStatus().toString() : null)
                .stockingUom(null != material.getStockingUom() ? material.getStockingUom().getCode() : null)
                .techincalSpecs(material.getTechincalSpecs())
                .tenantId(material.getAuditDetails().getTenantId())
                .termsOfDelivery(material.getTermsOfDelivery())
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

    private AssetCategory buildAssetCategory() {
        return AssetCategory.builder()
                .code(assetcategory)
                .build();
    }
}
