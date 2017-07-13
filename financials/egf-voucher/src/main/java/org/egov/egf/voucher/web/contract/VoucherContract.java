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
package org.egov.egf.voucher.web.contract;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.egov.common.web.contract.AuditableContract;
import org.egov.egf.master.web.contract.EgfStatusContract;
import org.egov.egf.master.web.contract.FundContract;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

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

@JsonPropertyOrder({ "id","type","name","description","voucherNumber","voucherDate","fund","status","originalVoucherNumber","refVoucherNumber","moduleName","ledgers","vouchermis"})
public class VoucherContract extends AuditableContract {

	@Length(max = 32)
	private String id;

	@NotEmpty
	@NotNull
	@Length(max = 16)
	private String type;

	@NotEmpty
	@NotNull
	@Length(max = 16)
	private String name;

	@Length(max = 256)
	private String description;
	@Length(max = 32)
	private String voucherNumber;

	@NotNull
	private Date voucherDate;

	private FundContract fund;
	private EgfStatusContract status;
	private String originalVoucherContractNumber;
	private String refVoucherContractNumber;
	private String moduleName;
	//@DrillDownTable
	private Set<LedgerContract> ledgers;    
	//@DrillDown
	private VouchermisContract vouchermis;   

	public BigDecimal getTotalAmount() {
		BigDecimal amount = BigDecimal.ZERO;
		for (final LedgerContract detail : ledgers)
			amount = amount.add(detail.getDebitAmount());
		return amount;
	}

}