/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any Long of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.egf.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.egov.egf.persistence.queue.contract.AccountDetailKeyContract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "egf_accountdetailkey")
@SequenceGenerator(name = AccountDetailKey.SEQ_ACCOUNTDETAILKEY, sequenceName = AccountDetailKey.SEQ_ACCOUNTDETAILKEY, allocationSize = 1)

public class AccountDetailKey extends AbstractAuditable {

	private static final long serialVersionUID = 1L;
	public static final String SEQ_ACCOUNTDETAILKEY = "seq_egf_accountdetailkey";

	@Id
	@GeneratedValue(generator = SEQ_ACCOUNTDETAILKEY, strategy = GenerationType.SEQUENCE)
	private Long id;
	@NotNull
	private Long key;

	@NotNull
	@Column(name = "detailtypeid")
	private Long accountDetailType;

	@Override
	public Long getId() {
		return this.id;
	}

	public AccountDetailKey(AccountDetailKeyContract contract) {
		this.setId(contract.getId());
		this.setKey(contract.getKey());
		this.setAccountDetailType(
				contract.getAccountDetailType() != null ? contract.getAccountDetailType().getId() : null);
		this.setTenantId(contract.getTenantId());
	}

}
