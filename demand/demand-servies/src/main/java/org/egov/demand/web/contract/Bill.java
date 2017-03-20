package org.egov.demand.web.contract;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Bill {
	private Long id = null;
	private Long demandId = null;
	private String citizenName = null;
	private String citizenAddress = null;
	private String billNumber = null;
	private String billType = null;
	private Date issuedDate = null;
	private Date lastDate = null;
	private String moduleName = null;
	private String createdBy = null;
	private String history = null;
	private String cancelled = null;
	private String fundCode = null;
	private Long functionaryCode = null;
	private String fundSourceCode = null;
	private String departmentCode = null;
	private String collModesNotAllowed = null;
	private Integer boundaryNumber = null;
	private String boundaryType = null;
	private Double billAmount = null;
	private Double billAmountCollected = null;
	private String serviceCode = null;
	private Boolean partPaymentAllowed = null;
	private Boolean overrideAccHeadAllowed = null;
	private String description = null;
	private Double minAmountPayable = null;
	private String consumerCode = null;
	private String displayMessage = null;
	private Boolean callbackForApportion = null;
	private String emailId = null;
	private String consumerType = null;
	private List<BillDetail> billDetails = new ArrayList<BillDetail>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDemandId() {
		return demandId;
	}

	public void setDemandId(Long demandId) {
		this.demandId = demandId;
	}

	public String getCitizenName() {
		return citizenName;
	}

	public void setCitizenName(String citizenName) {
		this.citizenName = citizenName;
	}

	public String getCitizenAddress() {
		return citizenAddress;
	}

	public void setCitizenAddress(String citizenAddress) {
		this.citizenAddress = citizenAddress;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	public Date getIssuedDate() {
		return issuedDate;
	}

	public void setIssuedDate(Date issuedDate) {
		this.issuedDate = issuedDate;
	}

	public Date getLastDate() {
		return lastDate;
	}

	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getHistory() {
		return history;
	}

	public void setHistory(String history) {
		this.history = history;
	}

	public String getCancelled() {
		return cancelled;
	}

	public void setCancelled(String cancelled) {
		this.cancelled = cancelled;
	}

	public String getFundCode() {
		return fundCode;
	}

	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}

	public Long getFunctionaryCode() {
		return functionaryCode;
	}

	public void setFunctionaryCode(Long functionaryCode) {
		this.functionaryCode = functionaryCode;
	}

	public String getFundSourceCode() {
		return fundSourceCode;
	}

	public void setFundSourceCode(String fundSourceCode) {
		this.fundSourceCode = fundSourceCode;
	}

	public String getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	public String getCollModesNotAllowed() {
		return collModesNotAllowed;
	}

	public void setCollModesNotAllowed(String collModesNotAllowed) {
		this.collModesNotAllowed = collModesNotAllowed;
	}

	public Integer getBoundaryNumber() {
		return boundaryNumber;
	}

	public void setBoundaryNumber(Integer boundaryNumber) {
		this.boundaryNumber = boundaryNumber;
	}

	public String getBoundaryType() {
		return boundaryType;
	}

	public void setBoundaryType(String boundaryType) {
		this.boundaryType = boundaryType;
	}

	public Double getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(Double billAmount) {
		this.billAmount = billAmount;
	}

	public Double getBillAmountCollected() {
		return billAmountCollected;
	}

	public void setBillAmountCollected(Double billAmountCollected) {
		this.billAmountCollected = billAmountCollected;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public Boolean getPartPaymentAllowed() {
		return partPaymentAllowed;
	}

	public void setPartPaymentAllowed(Boolean partPaymentAllowed) {
		this.partPaymentAllowed = partPaymentAllowed;
	}

	public Boolean getOverrideAccHeadAllowed() {
		return overrideAccHeadAllowed;
	}

	public void setOverrideAccHeadAllowed(Boolean overrideAccHeadAllowed) {
		this.overrideAccHeadAllowed = overrideAccHeadAllowed;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getMinAmountPayable() {
		return minAmountPayable;
	}

	public void setMinAmountPayable(Double minAmountPayable) {
		this.minAmountPayable = minAmountPayable;
	}

	public String getConsumerCode() {
		return consumerCode;
	}

	public void setConsumerCode(String consumerCode) {
		this.consumerCode = consumerCode;
	}

	public String getDisplayMessage() {
		return displayMessage;
	}

	public void setDisplayMessage(String displayMessage) {
		this.displayMessage = displayMessage;
	}

	public Boolean getCallbackForApportion() {
		return callbackForApportion;
	}

	public void setCallbackForApportion(Boolean callbackForApportion) {
		this.callbackForApportion = callbackForApportion;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getConsumerType() {
		return consumerType;
	}

	public void setConsumerType(String consumerType) {
		this.consumerType = consumerType;
	}

	public List<BillDetail> getBillDetails() {
		return billDetails;
	}

	public void setBillDetails(List<BillDetail> billDetails) {
		this.billDetails = billDetails;
	}

}
