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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * EgDemandReason entity.
 *
 * @author Ramki
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "eg_demand_reason")
@SequenceGenerator(name = EgDemandReason.SEQ_EGDEMANDREASON, sequenceName = EgDemandReason.SEQ_EGDEMANDREASON, allocationSize = 1)
public class EgDemandReason implements java.io.Serializable {

	private static final long serialVersionUID = -4522737060288133513L;
	public static final String SEQ_EGDEMANDREASON = "SEQ_EG_DEMAND_REASON";
	@Id
	@GeneratedValue(generator = SEQ_EGDEMANDREASON, strategy = GenerationType.SEQUENCE)
	private Long id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_base_reason")
	private EgDemandReason egDemandReason;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_demand_reason_master")
	private EgDemandReasonMaster egDemandReasonMaster;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_installment")
	private Installment egInstallmentMaster;
	@Column(name = "percentage_basis")
	private BigDecimal percentageBasis;
	@Column(name = "create_date")
	private Date createDate;
	@Column(name = "modified_date")
	private Date modifiedDate;
	@OneToMany(mappedBy = "egDemandReason", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<EgDemandReasonDetails> egDemandReasonDetails = new HashSet<EgDemandReasonDetails>(0);
	@Column(name = "glcode")
	private String glcode;
	@Column(name = "tenantid")
	private String tenantId;
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(egDemandReasonMaster).append("-").append(egInstallmentMaster);
		return sb.toString();
	}


	/**
	 * @return Returns if the given Object is equal to PropertyImpl
	 */
	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (this == obj)
			return true;

		if (!(obj instanceof EgDemandReason))
			return false;

		final EgDemandReason other = (EgDemandReason) obj;

		if (getId() != null || other.getId() != null) {
			if (getId().equals(other.getId())) {
				return true;
			}
		}
		if ((getEgDemandReasonMaster() != null || other.getEgDemandReasonMaster() != null)
				&& (getEgInstallmentMaster() != null || other.getEgInstallmentMaster() != null)) {
			if (getEgDemandReasonMaster().equals(other.getEgDemandReasonMaster())
					&& getEgInstallmentMaster().equals(other.getEgInstallmentMaster())) {
				return true;
			} else
				return false;
		} else
			return false;
	}

	/**
	 * @return Returns the hashCode
	 */
	public int hashCode() {
		int hashCode = 0;

		if (getId() != null) {
			hashCode = hashCode + this.getId().hashCode();
		}
		if (getEgDemandReasonMaster() != null) {
			hashCode = hashCode + this.getEgDemandReasonMaster().hashCode();
		}
		if (getEgInstallmentMaster() != null) {
			hashCode = hashCode + this.getEgInstallmentMaster().hashCode();
		}
		return hashCode;
	}
}
