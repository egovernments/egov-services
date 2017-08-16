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
package org.egov.egf.persistence.queue.contract;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.OrderBy;
import javax.validation.constraints.NotNull;

import org.egov.egf.persistence.entity.FiscalPeriod;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@JsonPropertyOrder({ "id", "finYearRange", "startingDate", "endingDate", "active", "isActiveForPosting", "isClosed",
		"transferClosingBalance", "fiscalPeriodList" })
public class FinancialYearContract extends AuditableContract {

	private Long id;

	@Length(min = 1, max = 25)
	@NotBlank
	private String finYearRange;

	@NotNull
	private Date startingDate;
	
	private Date asOnDate;

	@NotNull
	private Date endingDate;
	@NotNull
	private Boolean active;
	@NotNull
	private Boolean isActiveForPosting;

	private Boolean isClosed;

	private Boolean transferClosingBalance;

	@OrderBy("id DESC ")
	private List<FiscalPeriod> fiscalPeriodList = new ArrayList<FiscalPeriod>(0);

	public void setFiscalPeriod(final List<FiscalPeriod> fiscalPeriod) {
		this.fiscalPeriodList.clear();
		if (fiscalPeriod != null)
			this.fiscalPeriodList.addAll(fiscalPeriod);
	}

	public void addFiscalPeriod(final FiscalPeriod fiscalPeriod) {
		this.fiscalPeriodList.add(fiscalPeriod);
	}

	public Long getId() {
		return this.id;
	}

	public FinancialYearContract(final String id) {
		super();
		this.id = Long.valueOf(id);
	}
}