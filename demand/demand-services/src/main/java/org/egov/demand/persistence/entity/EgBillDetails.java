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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.egov.demand.web.contract.BillDetailInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * EgBillDetails entity.
 * 
 * @author Ramki
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "eg_bill_details")
@SequenceGenerator(name = EgBillDetails.SEQ_EGBILLDETAIL, sequenceName = EgBillDetails.SEQ_EGBILLDETAIL, allocationSize = 1)
public class EgBillDetails implements java.io.Serializable, Comparable<EgBillDetails> {

	private static final long serialVersionUID = -6153056227929260310L;
	public static final String SEQ_EGBILLDETAIL = "seq_eg_bill_details";
	@Id
	@GeneratedValue(generator = SEQ_EGBILLDETAIL, strategy = GenerationType.SEQUENCE)
	private Long id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_bill")
	private EgBill egBill;
	@Column(name = "create_date")
	private Date createDate;
	@Column(name = "modified_date")
	private Date modifiedDate;
	@Column(name = "glcode")
	private String glcode;
	@Column(name = "collected_amount")
	private BigDecimal collectedAmount;
	@Column(name = "order_no")
	private Integer orderNo;
	@Column(name = "function_code")
	private String functionCode;
	@Column(name = "cr_amount")
	private BigDecimal crAmount;
	@Column(name = "dr_amount")
	private BigDecimal drAmount;
	@Column(name = "description")
	private String description;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_installment")
	private Installment egInstallmentMaster;
	@Column(name = "additional_flag")
	private Integer additionalFlag;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_demand_reason")
	private EgDemandReason egDemandReason;
	@Column(name = "purpose")
	private String purpose;
	@Column(name = "groupid")
	private Integer groupId;
	@Column(name = "tenantid")
	private String tenantId;
	
	public EgBillDetails(BillDetailInfo billDetail) {
		this.createDate = new Date();
		this.modifiedDate = new Date();
		this.glcode = billDetail.getGlCode();
		this.crAmount = billDetail.getCreditAmount();
		this.drAmount = billDetail.getDebitAmount();
		this.orderNo = billDetail.getOrderNo();
		this.description = billDetail.getDescription();
		this.functionCode = billDetail.getFunctionCode();
		this.purpose = billDetail.getPurpose();
		this.groupId = billDetail.getGroupId();
		this.additionalFlag = billDetail.getIsActualDemand();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("EgBillDetails [glcode=").append(glcode).append(", collectedAmount=").append(collectedAmount)
				.append(", orderNo=").append(orderNo).append(", groupId=").append(groupId).append(", functionCode=")
				.append(functionCode).append(", crAmount=").append(crAmount).append(", drAmount=").append(drAmount)
				.append(", description=").append(description).append(", additionalFlag=").append(additionalFlag)
				.append(", egDemandReason=").append(egDemandReason).append(purpose).append("]");
		return builder.toString();
	}

	public BillDetailInfo toDomain(){
		
		//private Installment egInstallmentMaster;
		//private Integer additionalFlag;
		//private EgDemandReason egDemandReason;
		//private String tenantId;
		return BillDetailInfo.builder().billId(id).billId(egBill.getId()).creditAmount(crAmount).glCode(glcode)
		.amountCollected(collectedAmount).debitAmount(drAmount).functionCode(functionCode).orderNo(orderNo)
		.description(description).purpose(purpose).build();
	}

	/**
	 * The "orderNo" field is used as the key to sort bill details.
	 */
	@Override
	public int compareTo(EgBillDetails other) {
		return this.orderNo.compareTo(other.orderNo);
	}

	/**
	 * Returns the difference between the CR and DR amount.
	 */
	public BigDecimal balance() {
		return crAmount.subtract(drAmount);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EgBill getEgBill() {
		return this.egBill;
	}

	public void setEgBill(EgBill egBill) {
		this.egBill = egBill;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getGlcode() {
		return glcode;
	}

	public void setGlcode(String glcode) {
		this.glcode = glcode;
	}

	public String getFunctionCode() {
		return functionCode;
	}

	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}

	public BigDecimal getCrAmount() {
		return crAmount;
	}

	public void setCrAmount(BigDecimal crAmount) {
		this.crAmount = crAmount;
	}

	public BigDecimal getDrAmount() {
		return drAmount;
	}

	public void setDrAmount(BigDecimal drAmount) {
		this.drAmount = drAmount;
	}

	public BigDecimal getCollectedAmount() {
		return collectedAmount;
	}

	public void setCollectedAmount(BigDecimal collectedAmount) {
		this.collectedAmount = collectedAmount;
	}

	public Installment getEgInstallmentMaster() {
		return egInstallmentMaster;
	}

	public void setEgInstallmentMaster(Installment egInstallmentMaster) {
		this.egInstallmentMaster = egInstallmentMaster;
	}

	public Integer getAdditionalFlag() {
		return additionalFlag;
	}

	public void setAdditionalFlag(Integer additionalFlag) {
		this.additionalFlag = additionalFlag;
	}

	public EgDemandReason getEgDemandReason() {
		return egDemandReason;
	}

	public void setEgDemandReason(EgDemandReason egDemandReason) {
		this.egDemandReason = egDemandReason;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

}