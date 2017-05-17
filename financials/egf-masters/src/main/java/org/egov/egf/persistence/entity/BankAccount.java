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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.egov.egf.persistence.entity.enums.BankAccountType;
import org.egov.egf.persistence.queue.contract.BankAccountContract;
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
@EqualsAndHashCode(exclude = { "bankBranch", "chartOfAccount", "fund", "type" }, callSuper = false)

@Table(name = "egf_bankaccount")
@SequenceGenerator(name = BankAccount.SEQ_BANKACCOUNT, sequenceName = BankAccount.SEQ_BANKACCOUNT, allocationSize = 1)
public class BankAccount extends AbstractAuditable implements java.io.Serializable {

	public static final String SEQ_BANKACCOUNT = "seq_egf_bankaccount";

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = SEQ_BANKACCOUNT, strategy = GenerationType.SEQUENCE)
	private Long id;

	// @ManyToOne
	@Column(name = "branchid")
	private Long bankBranch;

	// @JsonProperty(access = Access.WRITE_ONLY)
	// @ManyToOne
	// @JoinColumn(name = "glcodeid")
	@Column(name = "glcodeid")
	private Long chartOfAccount;

	// @JsonProperty(access = Access.WRITE_ONLY)
	// @ManyToOne
	@Column(name = "fundid")
	private Long fund;

	@NotNull
	@Length(max = 25)
	private String accountNumber;

	// is this required ?
	private String accountType;
	@Length(max = 256)
	private String description;

	@NotNull
	private Boolean active;

	@Length(max = 100)
	private String payTo;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "type")
	private BankAccountType type;

	@Override
	public Long getId() {
		return this.id;
	}

	public BankAccount(BankAccountContract contract) {
		this.setId(contract.getId());
		this.setBankBranch(contract.getBankBranch() != null ? contract.getBankBranch().getId() : null);
		this.setChartOfAccount(contract.getChartOfAccount() != null ? contract.getChartOfAccount().getId() : null);
		this.setFund(contract.getFund() != null ? contract.getFund().getId() : null);
		this.setAccountNumber(contract.getAccountNumber());
		this.setAccountType(contract.getAccountType());
		this.setDescription(contract.getDescription());
		this.setActive(contract.getActive());
		this.setPayTo(contract.getPayTo());
		this.setType(contract.getType());
		this.setTenantId(contract.getTenantId());
	}

}