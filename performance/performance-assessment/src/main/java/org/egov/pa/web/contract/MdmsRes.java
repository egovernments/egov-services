package org.egov.pa.web.contract;

import org.egov.pa.model.TenantList;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MdmsRes {
	
	@JsonProperty("common-masters")
	private CommonMasters commonMasters;
	
	@JsonProperty("tenant")
	private TenantList tenantList; 

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
	
	
	
	

}
