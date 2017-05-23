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
 *         3) This license does not grant any rights to any Long of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.egf.persistence.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.egov.egf.persistence.queue.contract.FiscalPeriodContract;
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
@EqualsAndHashCode(exclude = { "financialYear" }, callSuper = false)
@Table(name = "egf_fiscalperiod")
@SequenceGenerator(name = FiscalPeriod.SEQ_FISCALPERIOD, sequenceName = FiscalPeriod.SEQ_FISCALPERIOD, allocationSize = 1)
public class FiscalPeriod extends AbstractAuditable {

	private static final long serialVersionUID = -5166451072153556422L;

	public static final String SEQ_FISCALPERIOD = "seq_egf_fiscalperiod";

	@Id
	@GeneratedValue(generator = SEQ_FISCALPERIOD, strategy = GenerationType.SEQUENCE)
	private Long id;

	@Length(min = 1, max = 25)
	@NotNull
	private String name = "";

	@NotNull
	@Column(name = "FinancialYearid")
	private Long financialYear;

	@NotNull
	private Date startingDate;

	@NotNull
	private Date endingDate;
	@NotNull
	private Boolean active;
	@NotNull
	private Boolean isActiveForPosting;

	private Boolean isClosed;

	@Override
	public Long getId() {
		return this.id;
	}

	public FiscalPeriod(FiscalPeriodContract contract) {
		this.setId(contract.getId());
		this.setName(contract.getName());
		this.setFinancialYear(contract.getFinancialYear() != null ? contract.getFinancialYear().getId() : null);
		this.setStartingDate(contract.getStartingDate());
		this.setEndingDate(contract.getEndingDate());
		this.setActive(contract.getActive());
		this.setIsActiveForPosting(contract.getIsActiveForPosting());
		this.setIsClosed(contract.getIsClosed());
		this.setTenantId(contract.getTenantId());
	}

}
