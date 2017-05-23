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

import org.egov.egf.persistence.queue.contract.AccountDetailTypeContract;
import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "egf_accountdetailtype")
@SequenceGenerator(name = AccountDetailType.SEQ_ACCOUNTDETAILTYPE, sequenceName = AccountDetailType.SEQ_ACCOUNTDETAILTYPE, allocationSize = 1)

public class AccountDetailType extends AbstractAuditable implements java.io.Serializable {

	private static final long serialVersionUID = 3499589983886551123L;

	public static final String SEQ_ACCOUNTDETAILTYPE = "seq_egf_accountdetailtype";

	@Id
	@GeneratedValue(generator = SEQ_ACCOUNTDETAILTYPE, strategy = GenerationType.SEQUENCE)

	private Long id;

	@NotNull
	@Length(max = 50, min = 1)
	private String name;

	@NotNull
	@Length(max = 50, min = 1)
	private String description;

	@Length(max = 25)
	private String tableName;

	@NotNull
	private Boolean active;

	@Column(name = "FULLY_QUALIFIED_NAME")
	@Length(max = 250, min = 1)
	private String fullyQualifiedName;

	@Override
	public Long getId() {
		return this.id;
	}

	public AccountDetailType(AccountDetailTypeContract contract) {
		this.setId(contract.getId());
		this.setName(contract.getName());
		this.setDescription(contract.getDescription());
		this.setTableName(contract.getTableName());
		this.setActive(contract.getActive());
		this.setFullyQualifiedName(contract.getFullyQualifiedName());
		this.setTenantId(contract.getTenantId());
	}

}