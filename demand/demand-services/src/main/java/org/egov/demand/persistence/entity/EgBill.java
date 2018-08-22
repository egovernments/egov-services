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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.egov.demand.web.contract.BillDetailInfo;
import org.egov.demand.web.contract.BillInfo;

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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	// @ManyToOne(fetch = FetchType.LAZY)
	// @JoinColumn(name = "id_demand")
	@Column(name = "id_demand")
	private Long egDemand;
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
	@Column(name = "module")
	private String module;
	@Column(name = "user_id")
	private Long userId;
	@Column(name = "create_date")
	private Date createDate;
	@Column(name = "modified_date")
	private Date modifiedDate;
	@OneToMany(mappedBy = "egBill", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<EgBillDetails> egBillDetails = new ArrayList<>();
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
	@Column(name = "total_collected_amount")
	private BigDecimal totalCollectedAmount;
	@Column(name = "service_code")
	private String serviceCode;
	@Column(name = "part_payment_allowed")
	private Character partPaymentAllowed;
	@Column(name = "override_accountheads_allowed")
	private Character overrideAccountHeadsAllowed;
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
	private Character callBackForApportion;
	@Column(name = "emailid")
	private String emailId;
	@Column(name = "tenantid")
	private String tenantId;

	public EgBill(BillInfo bill) {
		this.egDemand = bill.getDemandId();
		// this.egBillType = bill.getBillType();
		this.citizenName = bill.getCitizenName();
		this.citizenAddress = bill.getCitizenAddress();
		this.billNo = bill.getBillNumber();
		this.issueDate = bill.getIssuedDate();
		this.lastDate = bill.getLastDate();
		this.module = bill.getModuleName();
		// this.userId = bill.getUserid();
		this.createDate = new Date();
		this.modifiedDate = new Date();
		// this.egBillDetails = bill.getEgbillDetails();
		this.is_History = "N";
		this.is_Cancelled = "N";
		this.fundCode = bill.getFundCode();
		this.functionaryCode = BigDecimal.valueOf(bill.getFunctionaryCode());
		this.fundSourceCode = bill.getFundSourceCode();
		this.departmentCode = bill.getDepartmentCode();
		this.collModesNotAllowed = bill.getCollModesNotAllowed();
		this.boundaryNum = bill.getBoundaryNumber();
		this.boundaryType = bill.getBoundaryType();
		this.totalAmount = BigDecimal.valueOf(bill.getTotalAmount());
		this.serviceCode = bill.getServiceCode();
		this.partPaymentAllowed = bill.getPartPaymentAllowed();
		this.overrideAccountHeadsAllowed = bill.getOverrideAccHeadAllowed();
		this.description = bill.getDescription();
		this.minAmtPayable = BigDecimal.valueOf(bill.getMinAmountPayable()==null?0d:bill.getMinAmountPayable());
		this.consumerId = bill.getConsumerCode();
		this.consumerType = bill.getConsumerType();
		this.displayMessage = bill.getDisplayMessage();
		this.callBackForApportion = bill.getCallbackForApportion();
		this.billNo = bill.getBillNumber();
		this.emailId = bill.getEmailId();
	}

	public BillInfo toDomain() {

		List<BillDetailInfo> billDetailInfos = new ArrayList<>();
		for (EgBillDetails egBillDetailInfo : egBillDetails) {
			billDetailInfos.add(egBillDetailInfo.toDomain());
		}
		return BillInfo.builder().id(id).demandId(egDemand).citizenName(citizenName).citizenAddress(citizenAddress).billNumber(billNo).billDetailInfos(billDetailInfos)
				.cancelled(is_Cancelled).consumerCode(consumerId).tenantId(tenantId).build();
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