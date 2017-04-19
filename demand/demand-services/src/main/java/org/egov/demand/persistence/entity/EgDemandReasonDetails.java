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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * EgDemandReasonDetails entity.
 * 
 * @author Ramki
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "eg_demand_reason_details")
@SequenceGenerator(name = EgDemandReasonDetails.SEQ_DEMANDREASONDETAILS, sequenceName = EgDemandReasonDetails.SEQ_DEMANDREASONDETAILS, allocationSize = 1)
public class EgDemandReasonDetails implements java.io.Serializable {
	public static final String SEQ_DEMANDREASONDETAILS = "EG_DEMAND_REASON_DETAILS";
	@Id
	@GeneratedValue(generator = SEQ_DEMANDREASONDETAILS, strategy = GenerationType.SEQUENCE)
	private Long id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_demand_reason")
	private EgDemandReason egDemandReason;
	@Column(name = "percentage")
	private BigDecimal percentage;
	@Column(name = "from_date")
	private Date fromDate;
	@Column(name = "to_date")
	private Date toDate;
	@Column(name = "low_limit")
	private BigDecimal lowLimit;
	@Column(name = "high_limit")
	private BigDecimal highLimit;
	@Column(name = "create_date")
	private Date createDate;
	@Column(name = "modified_date")
	private Date modifiedDate;
	@Column(name = "flat_amount")
	private BigDecimal flatAmount;
	@Column(name = "is_flatamnt_max")
	private Integer isFlatAmntMax;
	@Column(name = "tenantid")
	private String tenantId;
	// Property accessors

	@Override
	public Object clone() {
		EgDemandReasonDetails clone = null;
		try {
			clone = (EgDemandReasonDetails) super.clone();
		} catch (CloneNotSupportedException e) {
			// this should never happen
			throw new InternalError(e.toString());
		}
		clone.setId(null);
		return clone;
	}

	public BigDecimal getFlatAmount() {
		return flatAmount;
	}

	public void setFlatAmount(BigDecimal flatAmount) {
		this.flatAmount = flatAmount;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EgDemandReason getEgDemandReason() {
		return this.egDemandReason;
	}

	public void setEgDemandReason(EgDemandReason egDemandReason) {
		this.egDemandReason = egDemandReason;
	}

	public BigDecimal getPercentage() {
		return this.percentage;
	}

	public void setPercentage(BigDecimal percentage) {
		this.percentage = percentage;
	}

	public Date getFromDate() {
		return this.fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return this.toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public BigDecimal getLowLimit() {
		return this.lowLimit;
	}

	public void setLowLimit(BigDecimal lowLimit) {
		this.lowLimit = lowLimit;
	}

	public BigDecimal getHighLimit() {
		return this.highLimit;
	}

	public void setHighLimit(BigDecimal highLimit) {
		this.highLimit = highLimit;
	}

	public Integer getIsFlatAmntMax() {
		return isFlatAmntMax;
	}

	public void setIsFlatAmntMax(Integer isFlatAmntMax) {
		this.isFlatAmntMax = isFlatAmntMax;
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

}