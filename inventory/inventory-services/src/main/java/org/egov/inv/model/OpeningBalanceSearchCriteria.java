package org.egov.inv.model;

import java.util.List;
import java.util.Set;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OpeningBalanceSearchCriteria {
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo = null;
	private List<String> id;

	
	private String materialTypeName;

	private String materialName;
	
	private String 	storeName;
	
	private String financialYear;
	@Min(1)
	@Max(500)
	private Integer pageSize;

	private Integer offset;
	
	private Integer pageNumber;

	private String sortBy;

	private String tenantId;
	
	private Set<String> sort;
	
	public boolean isMaterialTypeNameAbsent(){
        return   StringUtils.isBlank(materialTypeName);
    }
	
	public boolean ismaterialNameAbsent(){
        return   StringUtils.isBlank(materialName);
    }
	
	public boolean isStoreNameAbsent(){
        return  StringUtils.isBlank(storeName);
    }
	
	public boolean isFinancialYearAbsent(){
        return   StringUtils.isBlank(financialYear) ;
    }
	
	
}
