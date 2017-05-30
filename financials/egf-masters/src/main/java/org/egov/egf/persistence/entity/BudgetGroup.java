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

package org.egov.egf.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.egov.egf.persistence.entity.enums.BudgetAccountType;
import org.egov.egf.persistence.entity.enums.BudgetingType;
import org.egov.egf.persistence.queue.contract.BudgetGroupContract;
import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = { "majorCode", "maxCode", "minCode", "accountType", "budgetingType" }, callSuper = false)

@Table(name = "egf_budgetgroup")
@SequenceGenerator(name = BudgetGroup.SEQ_BUDGETGROUP, sequenceName = BudgetGroup.SEQ_BUDGETGROUP, allocationSize = 1)
public class BudgetGroup extends AbstractAuditable {

	public static final String SEQ_BUDGETGROUP = "seq_egf_budgetgroup";
	private static final long serialVersionUID = 8907540544512153346L;
	@Id
	@GeneratedValue(generator = SEQ_BUDGETGROUP, strategy = GenerationType.SEQUENCE)
	private Long id;

	@Length(max = 250, min = 1)
	private String name;

	@Length(max = 250, message = "Max 250 characters are allowed for description")
	private String description;

	private Long majorCode;

	private Long maxCode;

	private Long minCode;

	@Enumerated(value = EnumType.STRING)
	private BudgetAccountType accountType;

	@Enumerated(value = EnumType.STRING)
	private BudgetingType budgetingType;

	private Boolean active;

	@Override
	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public BudgetGroup(BudgetGroupContract contract) {
		this.setId(contract.getId());
		this.setName(contract.getName());
		this.setDescription(contract.getDescription());
		this.setMajorCode(contract.getMajorCode() != null ? contract.getMajorCode().getId() : null);
		this.setMaxCode(contract.getMaxCode() != null ? contract.getMaxCode().getId() : null);
		this.setMinCode(contract.getMinCode() != null ? contract.getMinCode().getId() : null);
		this.setAccountType(contract.getAccountType());
		this.setBudgetingType(contract.getBudgetingType());
		this.setActive(contract.getIsActive());
		this.setTenantId(contract.getTenantId());
	}

}