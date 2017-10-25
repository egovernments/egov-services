package org.egov.lams.common.web.contract;

 import javax.validation.constraints.NotNull;

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
	private String landAquisitionNumber;
	private String landOwnerName;
	private String surveyNo;
	private String ctsNumber ;
	private String advocateName;
	private String landType;
	private String organizationName;
	
	public boolean isLandAcquisitionIdAbsent(){
        return   landAcquisitionId == null || landAcquisitionId == "";
    }
	
	public boolean isLandAquisitionNumberAbsent(){
        return  landAquisitionNumber == "" ||landAquisitionNumber == null;
    }
	
	public boolean isLandOwnerNameAbsent(){
        return landOwnerName == null || landOwnerName == "";
    }
	
	public boolean isSurveyNoAbsent(){
        return  surveyNo == null || surveyNo == "";
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
