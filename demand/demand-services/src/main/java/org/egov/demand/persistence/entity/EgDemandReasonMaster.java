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
 * EgDemandReasonMaster entity.
 *
 * @author Ramki
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "eg_demand_reason_master")
@SequenceGenerator(name = EgDemandReasonMaster.SEQ_EGDEMANDREASONMASTER, sequenceName = EgDemandReasonMaster.SEQ_EGDEMANDREASONMASTER, allocationSize = 1)
public class EgDemandReasonMaster implements java.io.Serializable {
	public static final String SEQ_EGDEMANDREASONMASTER = "SEQ_EG_DEMAND_REASON_MASTER";
	@Id
	@GeneratedValue(generator = SEQ_EGDEMANDREASONMASTER, strategy = GenerationType.SEQUENCE)
	private Long id;
	@Column(name = "module")
	private String module;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category")
	private EgReasonCategory egReasonCategory;
	@Column(name = "reasonmaster")
	private String reasonMaster;
	@Column(name = "isdebit")
	private String isDebit;
	@Column(name = "code")
	private String code;
	@Column(name = "order")
	private Long orderId;
	@Column(name = "create_date")
	private Date createdDate;
	@Column(name = "modified_date")
	private Date modifiedDate;
	@OneToMany(mappedBy = "egDemandReason", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<EgDemandReason> egDemandReasons = new HashSet<EgDemandReason>(0);
	@Column(name = "isdemand")
	private Boolean isDemand;
	@Column(name = "tenantid")
	private String tenantId;
}