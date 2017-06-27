package org.egov.commons.model;




import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;


import org.egov.commons.web.contract.BusinessDetailsRequestInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class BusinessDetails  {

	@NotNull
    private Long id;
    
    @NotNull
	private String name;

	private Boolean isEnabled;
	
    @NotNull
    private String code;
	
    @NotNull
	private String businessType;
	
	private String businessUrl;
	
	private Date voucherCutoffDate;
	
	private Integer ordernumber;
	
    private Boolean voucherCreation;

	private Boolean isVoucherApproved;

	@NotNull
	private String fund;
	
	private String department;
	
	private String fundSource;
	
	private String functionary;
	
	@NotNull
	private BusinessCategory businessCategory;

	@NotNull
	private String function;
	
	@NotNull
	private String tenantId;
	
	@NotNull
	private Long createdBy;
	
	private Date createdDate;
	
	private List<BusinessAccountDetails> accountDetails;
	
	@NotNull
	private Long lastModifiedBy;
	
	private Date lastModifiedDate;
	
	
	
	public BusinessDetails(BusinessDetailsRequestInfo detailsInfo, BusinessCategory modelCategory) {
	    id=detailsInfo.getId();
		name=detailsInfo.getName();
		isEnabled=detailsInfo.getActive();
		code=detailsInfo.getCode();
		businessType=detailsInfo.getBusinessType();
		businessUrl=detailsInfo.getBusinessUrl();
		voucherCutoffDate=detailsInfo.getVoucherCutoffDate();
		ordernumber=detailsInfo.getOrdernumber();
		voucherCreation=detailsInfo.getVoucherCreation();
		isVoucherApproved=detailsInfo.getIsVoucherApproved();
		fund=detailsInfo.getFund();
		department=detailsInfo.getDepartment();
		fundSource=detailsInfo.getFundSource();
		functionary=detailsInfo.getFunctionary();
		businessCategory=modelCategory;
		function=detailsInfo.getFunction();
		tenantId=detailsInfo.getTenantId();
	}
    
	
	
	public BusinessDetails toDomainModel(){
		BusinessCategory category=	BusinessCategory.builder().id(businessCategory.getId()).build();
	return	BusinessDetails.builder().id(id).name(name).isEnabled(isEnabled).code(code).businessType(businessType)
		.businessUrl(businessUrl).voucherCreation(voucherCreation).isVoucherApproved(isVoucherApproved).
		voucherCutoffDate(voucherCutoffDate).ordernumber(ordernumber).function(function).fund(fund).functionary(functionary)
		.fundSource(fundSource).tenantId(tenantId).department(department).businessCategory(category).build();
	}
	
	
	
}
