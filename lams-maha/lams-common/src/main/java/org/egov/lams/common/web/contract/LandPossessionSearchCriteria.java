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
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LandPossessionSearchCriteria {
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo = null;
	@NotNull
	private String tenantId;
	private String landPossessionId;
	private String possessionNumber;
	private String landAquisitionNumber;
	private String proposalNumber;
	private String landType ;
	private String possessionDate;
	private Set<String> sort;
	@Min(1)
	@Max(500)
	private Integer pageSize;

	private Short pageNumber;
	
	public boolean isLandPossessionIdAbsent(){
        return   landPossessionId == null || landPossessionId == "";
    }
	
	public boolean isPossessionNumberAbsent(){
        return   StringUtils.isBlank(possessionNumber);
    }
	
	public boolean isLandAquisitionNumberAbsent(){
        return  StringUtils.isBlank(landAquisitionNumber);
    }
	
	public boolean isProposalNumberAbsent(){
        return   StringUtils.isBlank(proposalNumber) ;
    }
	
	
	public boolean isLandTypeAbsent(){
        return landType == null  || landType == "";
    }
	
	public boolean isPossessionDateAbsent(){
        return   possessionDate == null || possessionDate == "";
    }
	

}
