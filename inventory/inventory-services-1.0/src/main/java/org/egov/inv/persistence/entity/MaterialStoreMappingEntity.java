package org.egov.inv.persistence.entity;

import org.egov.inv.model.AuditDetails;
import org.egov.inv.model.ChartofAccount;
import org.egov.inv.model.Material;
import org.egov.inv.model.MaterialStoreMapping;
import org.egov.inv.model.Store;

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
public class MaterialStoreMappingEntity   {
	public static final String TABLE_NAME = "materialstoremapping";
	private String id;
	private String material;
	private String store;
	private String chartofAccount;
	private Boolean active;
	private String createdBy;
	private String lastModifiedBy;
	private Long createdTime;
	private Long lastModifiedTime;


	public MaterialStoreMapping toDomain() {
		MaterialStoreMapping materialStoreMapping = new MaterialStoreMapping();
		materialStoreMapping.setId(this.id);
		materialStoreMapping.setMaterial(new Material().id(material));
		materialStoreMapping.setStore(new Store().id(store));
		materialStoreMapping.setChartofAccount(new ChartofAccount().id(chartofAccount));
		materialStoreMapping.setActive(this.active);
		AuditDetails auditDetail = new AuditDetails()
				.createdBy(createdBy)
				.lastModifiedBy(lastModifiedBy)
				.createdTime(createdTime)
				.lastModifiedTime(lastModifiedTime);
		materialStoreMapping.setAuditDetails(auditDetail);
		return materialStoreMapping;
	}

	public MaterialStoreMappingEntity toEntity(MaterialStoreMapping materialStoreMapping) {
		this.id = materialStoreMapping.getId();
		this.material = materialStoreMapping.getMaterial() != null ? materialStoreMapping.getMaterial().getId() : null;
		this.store = materialStoreMapping.getStore() != null ? materialStoreMapping.getStore().getId() : null;
		this.chartofAccount = materialStoreMapping.getChartofAccount() != null
				? materialStoreMapping.getChartofAccount().getId() : null;
		this.active = materialStoreMapping.getActive();
		this.createdBy=materialStoreMapping.getAuditDetails().getCreatedBy();
		this.lastModifiedBy=materialStoreMapping.getAuditDetails().getLastModifiedBy();
		this.createdTime=materialStoreMapping.getAuditDetails().getCreatedTime();
		this.lastModifiedTime=materialStoreMapping.getAuditDetails().getLastModifiedTime(); 
		return this;
	}

}
