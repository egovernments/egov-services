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

import java.io.Serializable;
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
@Table(name = "egdm_collected_receipts")
@SequenceGenerator(name = EgdmCollectedReceipt.SEQ_COLLECTEDRECEIPTS, sequenceName = EgdmCollectedReceipt.SEQ_COLLECTEDRECEIPTS, allocationSize = 1)
public class EgdmCollectedReceipt implements Cloneable, Serializable {
	private static final long serialVersionUID = 1L;
	public static final Character RCPT_CANCEL_STATUS = 'C';
	public static final String SEQ_COLLECTEDRECEIPTS = "seq_egdm_collected_receipts";
	@Id
	@GeneratedValue(generator = SEQ_COLLECTEDRECEIPTS, strategy = GenerationType.SEQUENCE)
	private Long id;
	@Column(name = "receipt_number")
	private String receiptNumber;
	@Column(name = "receipt_date")
	private Date receiptDate;
	@Column(name = "receipt_amount")
	private BigDecimal amount;
	@Column(name = "reason_amount")
	private BigDecimal reasonAmount;
	@Column(name = "status")
	private Character status;
	@Column(name = "updated_time")
	private Date updatedTime;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_demand_detail")
	private EgDemandDetails egdemandDetail;
	@Column(name = "tenantid")
	private String tenantId;

	public String toString() {
		return receiptNumber;
	}

	/**
	 * Returns a copy that can be associated with another EgDemandDetails. The
	 * copy has the same receipt number, date, amount, status and time stamp.
	 * (Note: making it public instead of protected to allow any class to use
	 * it.)
	 */
	@Override
	public Object clone() {
		EgdmCollectedReceipt clone = null;
		try {
			clone = (EgdmCollectedReceipt) super.clone();
		} catch (CloneNotSupportedException e) {
			// this should never happen
			throw new InternalError(e.toString());
		}
		clone.setId(null);
		clone.setEgdemandDetail(null);
		return clone;
	}

	public EgDemandDetails getEgdemandDetail() {
		return egdemandDetail;
	}

	public void setEgdemandDetail(EgDemandDetails egdemandDetail) {
		this.egdemandDetail = egdemandDetail;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReceiptNumber() {
		return receiptNumber;
	}

	public void setReceiptNumber(String receiptNumber) {
		this.receiptNumber = receiptNumber;
	}

	public Date getReceiptDate() {
		return receiptDate;
	}

	public void setReceiptDate(Date receiptDate) {
		this.receiptDate = receiptDate;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public BigDecimal getReasonAmount() {
		return reasonAmount;
	}

	public void setReasonAmount(BigDecimal reasonAmount) {
		this.reasonAmount = reasonAmount;
	}

	public Boolean isCancelled() {
		Boolean cancelStatus = Boolean.FALSE;
		if (getStatus().equals(RCPT_CANCEL_STATUS)) {
			cancelStatus = Boolean.TRUE;
		}
		return cancelStatus;
	}

}
