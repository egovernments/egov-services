package org.egov.inv.persistence.entity;

import lombok.*;
import org.egov.inv.model.AuditDetails;
import org.egov.inv.model.Material;

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
                .minQuantity(BigDecimal.valueOf(minQuantity))
                .maxQuantity(BigDecimal.valueOf(maxQuantity))
                .reorderLevel(BigDecimal.valueOf(reorderLevel))
                .reorderQuantity(BigDecimal.valueOf(reorderQuantity))
                .auditDetails(mapAuditDetails())
                .build();
    }

    public MaterialEntity toEntity(Material material) {
        return MaterialEntity.builder()
                .id(material.getId())
                .assetcategory(null != material.getAssetCategory() ? material.getAssetCategory().getCode() : null)
                .baseUom(material.getBaseUom().getCode())
                .code(material.getCode())
                .description(material.getDescription())
                .expenseAccount(null != material.getExpenseAccount() ? material.getExpenseAccount().getGlcode() : null)
                .inventoryType(null != material.getInventoryType() ? material.getInventoryType().toString() : null)
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
                .tenantId(material.getTenantId())
                .termsOfDelivery(material.getTermsOfDelivery())
                .build();
    }

    private AuditDetails mapAuditDetails() {
        return AuditDetails.builder()
                .createdBy(createdBy)
                .createdTime(createdTime)
                .lastModifiedBy(lastModifiedBy)
                .lastModifiedTime(lastModifiedTime)
                .build();
    }
}
