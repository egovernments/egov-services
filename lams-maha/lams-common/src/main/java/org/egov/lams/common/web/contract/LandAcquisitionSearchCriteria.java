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
public class LandAcquisitionSearchCriteria {
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo = null;
	@NotNull
	private String tenantId;
	private String landAcquisitionId;
	private String landAcquisitionNumber;
	private String landOwnerName;
	private String surveyNo;
	private String ctsNumber ;
	private String advocateName;
	private String landType;
	private String organizationName;
	private Set<String> sort;


	@Min(1)
	@Max(500)
	private Integer pageSize;

	private Short pageNumber;
	
	public boolean isLandAcquisitionIdAbsent(){
        return   landAcquisitionId == null || landAcquisitionId == "";
    }
	
	public boolean isLandAquisitionNumberAbsent(){
        return   StringUtils.isBlank(landAcquisitionNumber);
    }
	
	public boolean isLandOwnerNameAbsent(){
        return  StringUtils.isBlank(landOwnerName);
    }
	
	public boolean isSurveyNoAbsent(){
        return   StringUtils.isBlank(surveyNo) ;
    }
	
	public boolean isCtsNumberAbsent(){
        return   ctsNumber == null || ctsNumber == "";
    }
	
	public boolean isAdvocateNameAbsent(){
        return   advocateName == null || advocateName== "";
    }
	
	public boolean isLandTypeAbsent(){
        return landType == null  || landType == "";
    }
	
	public boolean isOrganizationNameAbsent(){
        return   organizationName == null || organizationName == "";
    }
	

}
