package org.egov.pa.web.contract;

import org.egov.pa.model.TenantList;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MdmsRes {
	
	@JsonProperty("common-masters")
	private CommonMasters commonMasters;
	
	@JsonProperty("tenant")
	private TenantList tenantList;
	
	@JsonProperty("PM")
	private KpiCategoryList kpiCategoryList; 

	public CommonMasters getCommonMasters() {
		return commonMasters;
	}

	public void setCommonMasters(CommonMasters commonMasters) {
		this.commonMasters = commonMasters;
	}

	public TenantList getTenantList() {
		return tenantList;
	}

	public void setTenantList(TenantList tenantList) {
		this.tenantList = tenantList;
	}

	public KpiCategoryList getKpiCategoryList() {
		return kpiCategoryList;
	}

	public void setKpiCategoryList(KpiCategoryList kpiCategoryList) {
		this.kpiCategoryList = kpiCategoryList;
	}
	
	
	
	
	
	

}
