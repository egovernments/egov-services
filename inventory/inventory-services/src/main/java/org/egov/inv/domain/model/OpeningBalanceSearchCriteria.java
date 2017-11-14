package org.egov.inv.domain.model;

import java.util.List;
import java.util.Set;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.apache.commons.lang3.StringUtils;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class OpeningBalanceSearchCriteria {
	private List<String> id;

	private List<String> mrnNumber;

	private String receiptNumber;
	
	private String materialName;
	
	private String 	supplierCode;
	
	private String financialYear;
	@Min(1)
	@Max(500)
	private Integer pageSize;

	private Integer offset;
	
	private Integer pageNumber;

	private String sortBy;

	private String tenantId;
	
	private Set<String> sort;
	
	public boolean isreceiptNumberAbsent(){
        return   StringUtils.isBlank(receiptNumber);
    }
	
	public boolean ismaterialNameAbsent(){
        return   StringUtils.isBlank(materialName);
    }
	
	public boolean isSupplierCodeAbsent(){
        return  StringUtils.isBlank(supplierCode);
    }
	
	public boolean isFinancialYearAbsent(){
        return   StringUtils.isBlank(financialYear) ;
    }
	
	public boolean isMrnNumberAbsent(){
		if(mrnNumber.size() == 0)
		{
			return true;
		}
        return  false;
    }
}
