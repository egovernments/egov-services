package org.egov.lams.common.web.contract;

import java.util.Set;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class LandTransferSearchCriteria {
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo = null;
	@NotNull
	private String tenantId;
	private String transferNumber;
	private String landAquisitionNumber;
	private String resolutionNumber;
	private String transferFrom;
	private String transferedToDate ;
	private Set<String> sort;
	@Min(1)
	@Max(500)
	private Integer pageSize;

	private Short pageNumber;
	
	public boolean isTransferNumberAbsent(){
        return    StringUtils.isBlank(transferNumber);
    }
	
	public boolean isLandAquisitionNumberAbsent(){
        return   StringUtils.isBlank(landAquisitionNumber);
    }
	
	public boolean isResolutionNumberAbsent(){
        return  StringUtils.isBlank(resolutionNumber);
    }
	
	public boolean isTransferFromAbsent(){
        return   StringUtils.isBlank(transferFrom) ;
    }
	
	
	public boolean isTransferedToDateAbsent(){
        return transferedToDate == null  || transferedToDate == "";
    }
	

}
