package org.egov.inv.persistence.entity;

import java.math.BigDecimal;

import org.egov.inv.model.AuditDetails;
import org.egov.inv.model.ChartofAccount;
import org.egov.inv.model.Material.InventoryTypeEnum;
import org.egov.inv.model.Material;
import org.egov.inv.model.Material.MaterialClassEnum;
import org.egov.inv.model.Material.MaterialControlTypeEnum;
import org.egov.inv.model.Material.StatusEnum;
import org.egov.inv.model.MaterialType;
import org.egov.inv.model.Uom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Builder
public class MaterialEntity  {
	public static final String TABLE_NAME = "material";
	private String id;
	private String tenantId;
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
	private BigDecimal minQuantity;
	private BigDecimal maxQuantity;
	private String stockingUom;
	private String materialClass;
	private BigDecimal reorderLevel;
	private BigDecimal reorderQuantity;
	private String materialControlType;
	private String model;
	private String manufacturePartNo;
	private String techincalSpecs;
	private String termsOfDelivery;
	private Boolean overrideMaterialControlType;
	private Boolean scrapable;
	private String createdBy;
	private String lastModifiedBy;
	private Long createdTime;
	private Long lastModifiedTime; 

	public Material toDomain() {
		Material material = new Material();
		material.setId(this.id);
		material.setTenantId(this.tenantId);
		material.setCode(this.code);
		material.setName(this.name);
		material.setDescription(this.description);
		material.setOldCode(this.oldCode);
		material.setMaterialType(new MaterialType().id(materialType));
		material.setBaseUom(new Uom().id(baseUom));
		material.setInventoryType(InventoryTypeEnum.valueOf(this.inventoryType));
		material.setStatus(StatusEnum.valueOf(this.status));
		material.setPurchaseUom(new Uom().id(purchaseUom));
		material.setExpenseAccount(new ChartofAccount().id(expenseAccount));
		material.setMinQuantity(this.minQuantity);
		material.setMaxQuantity(this.maxQuantity);
		material.setStockingUom(new Uom().id(stockingUom));
		material.setMaterialClass(MaterialClassEnum.valueOf(this.materialClass));
		material.setReorderLevel(this.reorderLevel);
		material.setReorderQuantity(this.reorderQuantity);
		material.setMaterialControlType(MaterialControlTypeEnum.valueOf(this.materialControlType));
		material.setModel(this.model);
		material.setManufacturePartNo(this.manufacturePartNo);
		material.setTechincalSpecs(this.techincalSpecs);
		material.setTermsOfDelivery(this.termsOfDelivery);
		material.setOverrideMaterialControlType(this.overrideMaterialControlType);
		material.setScrapable(this.scrapable);
		AuditDetails auditDetail = new AuditDetails()
				.createdBy(createdBy)
				.lastModifiedBy(lastModifiedBy)
				.createdTime(createdTime)
				.lastModifiedTime(lastModifiedTime);
		material.setAuditDetails(auditDetail);
		return material;
	}

	public MaterialEntity toEntity(Material material) {
		this.id = material.getId();
		this.tenantId = material.getTenantId();
		this.code = material.getCode();
		this.name = material.getName();
		this.description = material.getDescription();
		this.oldCode = material.getOldCode();
		this.materialType = material.getMaterialType() != null ? material.getMaterialType().getId() : null;
		this.baseUom = material.getBaseUom() != null ? material.getBaseUom().getId() : null;
		this.inventoryType = material.getInventoryType() != null ? material.getInventoryType().toString() : null;
		this.status = material.getStatus() != null ? material.getStatus().toString() : null;
		this.purchaseUom = material.getPurchaseUom() != null ? material.getPurchaseUom().getId() : null;
		this.expenseAccount = material.getExpenseAccount() != null ? material.getExpenseAccount().getId() : null;
		this.minQuantity = material.getMinQuantity();
		this.maxQuantity = material.getMaxQuantity();
		this.stockingUom = material.getStockingUom() != null ? material.getStockingUom().getId() : null;
		this.materialClass = material.getMaterialClass() != null ? material.getMaterialClass().toString() : null;
		this.reorderLevel = material.getReorderLevel();
		this.reorderQuantity = material.getReorderQuantity();
		this.materialControlType = material.getMaterialControlType() != null
				? material.getMaterialControlType().toString() : null;
		this.model = material.getModel();
		this.manufacturePartNo = material.getManufacturePartNo();
		this.techincalSpecs = material.getTechincalSpecs();
		this.termsOfDelivery = material.getTermsOfDelivery();
		this.overrideMaterialControlType = material.getOverrideMaterialControlType();
		this.scrapable = material.getScrapable();
		this.createdBy=material.getAuditDetails().getCreatedBy();
		this.lastModifiedBy=material.getAuditDetails().getLastModifiedBy();
		this.createdTime=material.getAuditDetails().getCreatedTime();
		this.lastModifiedTime=material.getAuditDetails().getLastModifiedTime();
		return this;
	}

}
