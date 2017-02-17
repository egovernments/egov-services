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
package org.egov.eis.persistence.entity;

import static org.egov.eis.persistence.entity.EmployeeType.SEQ_EMPLOYEETYPE;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Table(name = "egeis_employeetype")
@SequenceGenerator(name = SEQ_EMPLOYEETYPE, sequenceName = SEQ_EMPLOYEETYPE, allocationSize = 1)
public class EmployeeType extends AbstractAuditable {

	private static final long serialVersionUID = 1962562283324722151L;

	public static final String SEQ_EMPLOYEETYPE = "SEQ_EGEIS_EMPLOYEETYPE";

	public static final String ALPHABETS = "[A-Za-z]+";

	@SafeHtml
	@Column(name = "name", unique = true)
	@Pattern(regexp = ALPHABETS)
	public String name;
	@Id
	@GeneratedValue(generator = SEQ_EMPLOYEETYPE, strategy = GenerationType.SEQUENCE)
	private Long id;
	@JoinColumn(name = "chartofaccounts")
	private Long chartOfAccounts;

	public Long getChartOfAccounts() {
		return chartOfAccounts;
	}

	public void setChartOfAccounts(final Long chartOfAccounts) {
		this.chartOfAccounts = chartOfAccounts;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}
}
