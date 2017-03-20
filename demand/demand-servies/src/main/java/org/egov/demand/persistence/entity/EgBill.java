/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.demand.persistence.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.egov.demand.web.contract.Bill;
import org.egov.demand.web.contract.BillAddlInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * EgBill entity.
 *
 * @author Ramki
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "eg_bill")
@SequenceGenerator(name = EgBill.SEQ_EGBILL, sequenceName = EgBill.SEQ_EGBILL, allocationSize = 1)
public class EgBill implements java.io.Serializable {

	private static final long serialVersionUID = 8442662015032023843L;
	public static final String SEQ_EGBILL = "seq_eg_bill";
	@Id
	@GeneratedValue(generator = SEQ_EGBILL, strategy = GenerationType.SEQUENCE)
	private Long id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_demand")
	private EgDemand egDemand;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_bill_type")
	private EgBillType egBillType;
	@Column(name = "citizen_name")
	private String citizenName;
	@Column(name = "citizen_address")
	private String citizenAddress;
	@Column(name = "bill_no")
	private String billNo;
	@Column(name = "issue_date")
	private Date issueDate;
	@Column(name = "last_date")
	private Date lastDate;
	@Column(name = "module_id")
	private Long module;
	@Column(name = "user_id")
	private Long userId;
	@Column(name = "create_date")
	private Date createDate;
	@Column(name = "modified_date")
	private Date modifiedDate;
	@OneToMany(mappedBy = "egBill", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<EgBillDetails> egBillDetails = new HashSet<EgBillDetails>(0);
	@Column(name = "is_history")
	private String is_History;
	@Column(name = "is_cancelled")
	private String is_Cancelled;
	@Column(name = "fundcode")
	private String fundCode;
	@Column(name = "functionary_code")
	private BigDecimal functionaryCode;
	@Column(name = "fundsource_code")
	private String fundSourceCode;
	@Column(name = "department_code")
	private String departmentCode;
	@Column(name = "coll_modes_not_allowed")
	private String collModesNotAllowed;
	@Column(name = "boundary_num")
	private Integer boundaryNum;
	@Column(name = "boundary_type")
	private String boundaryType;
	@Column(name = "total_amount")
	private BigDecimal totalAmount;
	@Column(name = "id_status")
	private BigDecimal totalCollectedAmount;
	@Column(name = "service_code")
	private String serviceCode;
	@Column(name = "part_payment_allowed")
	private Boolean partPaymentAllowed;
	@Column(name = "total_collected_amount")
	private Boolean overrideAccountHeadsAllowed;
	@Column(name = "description")
	private String description;
	@Column(name = "min_amt_payable")
	private BigDecimal minAmtPayable;
	@Column(name = "consumer_id")
	private String consumerId;
	@Column(name = "consumertype")
	private String consumerType;
	@Column(name = "dspl_message")
	private String displayMessage;
	@Column(name = "callback_for_apportion")
	private Boolean callBackForApportion;
	private String transanctionReferenceNumber;
	@Column(name = "emailid")
	private String emailId;

	public EgBill (BillAddlInfo bill) {
		this.id = bill.getId();
		//this.egDemand = bill.getEgdemand();
		//this.egBillType = bill.getBillType();
		this.citizenName = bill.getCitizenName();
		this.citizenAddress = bill.getCitizenAddress();
		this.billNo = bill.getBillNumber();
		this.issueDate = bill.getIssuedDate();
		this.lastDate = bill.getLastDate();
		//this.module = bill.getModuleName();
		//this.userId = bill.getUserid();
		//this.createDate = bill.getCreateDate();
		//this.modifiedDate = bill.getModifiedDate();
		//this.egBillDetails = bill.getEgbillDetails();
		this.is_History = bill.getHistory();
		this.is_Cancelled = bill.getCancelled();
		this.fundCode = bill.getFundCode();
		this.functionaryCode = BigDecimal.valueOf(bill.getFunctionaryCode());
		this.fundSourceCode = bill.getFundSourceCode();
		this.departmentCode = bill.getDepartmentCode();
		this.collModesNotAllowed = bill.getCollModesNotAllowed();
		this.boundaryNum = bill.getBoundaryNumber();
		this.boundaryType = bill.getBoundaryType();
		this.totalAmount = BigDecimal.valueOf(bill.getTotalAmount());
		//this.totalCollectedAmount = bill.getTotalcollectedamount();
		this.serviceCode = bill.getServiceCode();
		this.partPaymentAllowed = bill.getPartPaymentAllowed();
		this.overrideAccountHeadsAllowed = bill.getOverrideAccHeadAllowed();
		this.description = bill.getDescription();
		this.minAmtPayable = BigDecimal.valueOf(bill.getMinAmountPayable());
		this.consumerId = bill.getConsumerCode();
		this.consumerType = bill.getConsumerType();
		this.displayMessage = bill.getDisplayMessage();
		this.callBackForApportion = bill.getCallbackForApportion();
		//this.transanctionReferenceNumber = bill.getTransanctionreferencenumber();
		this.emailId = bill.getEmailId();

	}
	public BillAddlInfo toDomain(EgBill bill){
		return BillAddlInfo.builder().id(id)
				.billType(egBillType.getName())
				.citizenName(citizenName)
				.citizenAddress(citizenAddress)
				.billNumber(billNo).build();
	}
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(id).append("-").append(totalAmount).append("-").append(description).append("-").append(egBillDetails);
		return sb.toString();
	}

	/**
	 * Checks if the given GL code exists in the bill details for this bill.
	 * 
	 * @param glCode
	 * @return
	 */
	public boolean containsGLCode(final String glCode) {
		boolean contains = false;
		for (final EgBillDetails bd : getEgBillDetails())
			if (bd.getGlcode().equals(glCode)) {
				contains = true;
				break;
			}
		return contains;
	}

	/**
	 * Returns the difference between the CR and DR amount for the given GL code
	 * if it exists; null otherwise.
	 */
	public BigDecimal balanceForGLCode(final String glCode) {
		BigDecimal balance = BigDecimal.ZERO;
		for (final EgBillDetails bd : getEgBillDetails())
			if (bd.getGlcode().equals(glCode)) {
				balance = bd.balance();
				break;
			}
		return balance;
	}

	public String getDisplayMessage() {
		return displayMessage;
	}

	public void setDisplayMessage(final String displayMessage) {
		this.displayMessage = displayMessage;
	}

	public BigDecimal getMinAmtPayable() {
		return minAmtPayable;
	}

	public void setMinAmtPayable(final BigDecimal minAmtPayable) {
		this.minAmtPayable = minAmtPayable;
	}

	public String getFundCode() {
		return fundCode;
	}

	public void setFundCode(final String fundCode) {
		this.fundCode = fundCode;
	}

	public String getFundSourceCode() {
		return fundSourceCode;
	}

	public void setFundSourceCode(final String fundSourceCode) {
		this.fundSourceCode = fundSourceCode;
	}

	public String getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(final String departmentCode) {
		this.departmentCode = departmentCode;
	}

	public String getCollModesNotAllowed() {
		return collModesNotAllowed;
	}

	public void setCollModesNotAllowed(final String collModesNotAllowed) {
		this.collModesNotAllowed = collModesNotAllowed;
	}

	public Integer getBoundaryNum() {
		return boundaryNum;
	}

	public void setBoundaryNum(final Integer boundaryNum) {
		this.boundaryNum = boundaryNum;
	}

	public String getBoundaryType() {
		return boundaryType;
	}

	public void setBoundaryType(final String boundaryType) {
		this.boundaryType = boundaryType;
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public EgDemand getEgDemand() {
		return egDemand;
	}

	public void setEgDemand(final EgDemand egDemand) {
		this.egDemand = egDemand;
	}

	public EgBillType getEgBillType() {
		return egBillType;
	}

	public void setEgBillType(final EgBillType egBillType) {
		this.egBillType = egBillType;
	}

	public String getCitizenName() {
		return citizenName;
	}

	public void setCitizenName(final String citizenName) {
		this.citizenName = citizenName;
	}

	public String getCitizenAddress() {
		return citizenAddress;
	}

	public void setCitizenAddress(final String citizenAddress) {
		this.citizenAddress = citizenAddress;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(final Date issueDate) {
		this.issueDate = issueDate;
	}

	public Date getLastDate() {
		return lastDate;
	}

	public void setLastDate(final Date lastDate) {
		this.lastDate = lastDate;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(final Long userId) {
		this.userId = userId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(final Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(final Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Set<EgBillDetails> getEgBillDetails() {
		return egBillDetails;
	}

	public void setEgBillDetails(final Set<EgBillDetails> egBillDetails) {
		this.egBillDetails = egBillDetails;
	}

	public String getIs_History() {
		return is_History;
	}

	public void setIs_History(final String is_History) {
		this.is_History = is_History;
	}

	public String getIs_Cancelled() {
		return is_Cancelled;
	}

	public void setIs_Cancelled(final String is_Cancelled) {
		this.is_Cancelled = is_Cancelled;
	}

	public void addEgBillDetails(final EgBillDetails egBillDetails) {
		getEgBillDetails().add(egBillDetails);
	}

	public void removeEgBillDetails(final EgBillDetails egBillDetails) {
		getEgBillDetails().remove(egBillDetails);
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(final String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public Boolean getPartPaymentAllowed() {
		return partPaymentAllowed;
	}

	public void setPartPaymentAllowed(final Boolean partPaymentAllowed) {
		this.partPaymentAllowed = partPaymentAllowed;
	}

	public Boolean getOverrideAccountHeadsAllowed() {
		return overrideAccountHeadsAllowed;
	}

	public void setOverrideAccountHeadsAllowed(final Boolean overrideAccountHeadsAllowed) {
		this.overrideAccountHeadsAllowed = overrideAccountHeadsAllowed;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(final BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getTotalCollectedAmount() {
		return totalCollectedAmount;
	}

	public void setTotalCollectedAmount(final BigDecimal totalCollectedAmount) {
		this.totalCollectedAmount = totalCollectedAmount;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(final String billNo) {
		this.billNo = billNo;
	}

	public Long getModule() {
		return module;
	}

	public void setModule(final Long module) {
		this.module = module;
	}

	public BigDecimal getFunctionaryCode() {
		return functionaryCode;
	}

	public void setFunctionaryCode(final BigDecimal functionaryCode) {
		this.functionaryCode = functionaryCode;
	}

	public String getConsumerId() {
		return consumerId;
	}

	public void setConsumerId(final String consumerId) {
		this.consumerId = consumerId;
	}

	public Boolean getCallBackForApportion() {
		return callBackForApportion;
	}

	public void setCallBackForApportion(final Boolean callBackForApportion) {
		this.callBackForApportion = callBackForApportion;
	}

	public String getTransanctionReferenceNumber() {
		return transanctionReferenceNumber;
	}

	public void setTransanctionReferenceNumber(final String transanctionReferenceNumber) {
		this.transanctionReferenceNumber = transanctionReferenceNumber;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(final String emailId) {
		this.emailId = emailId;
	}

	public String getConsumerType() {
		return consumerType;
	}

	public void setConsumerType(String consumerType) {
		this.consumerType = consumerType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (id == null ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		final EgBill other = (EgBill) obj;

		if (id != null && other != null && id.equals(other.id))
			return true;
		return false;
	}

}