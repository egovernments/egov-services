package org.egov.pa.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MdmsRes {
	
	@JsonProperty("common-masters")
	private CommonMasters commonMasters;

	public CommonMasters getCommonMasters() {
		return commonMasters;
	}

	public void setCommonMasters(CommonMasters commonMasters) {
		this.commonMasters = commonMasters;
	}
	
	

}
