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

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull; 

import org.egov.egf.web.contract.BankContract;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "egf_bank")
@SequenceGenerator(name = Bank.SEQ_BANK, sequenceName = Bank.SEQ_BANK, allocationSize = 1)
public class  Bank extends AbstractAuditable {

	private static final long serialVersionUID = -2839424467289504649L;

	public static final String SEQ_BANK = "seq_bank";

	@Id
	@GeneratedValue(generator = SEQ_BANK, strategy = GenerationType.SEQUENCE)
	private Long id;

	@NotNull
	@Length(max = 50)
	private String code;

	@NotNull
	@Length(max = 100)
	private String name;

	@Length(max = 250)
	private String description;

	@NotNull
	private Boolean active;

	@Length(max = 50)
	private String type;

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "bank", targetEntity = BankBranch.class)
	private Set<BankBranch> bankBranches = new HashSet<>(0);

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Set<BankBranch> getBankBranches() {
		return bankBranches;
	}

	public void setBankBranches(Set<BankBranch> bankBranches) {
		this.bankBranches = bankBranches;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static String getSeqBank() {
		return SEQ_BANK;
	}
	
	public void map(BankContract bank)
	{
		if(bank!=null)
		{
		if(bank.getActive()!=null)
		this.setActive(bank.getActive());
		if(bank.getName()!=null)
		this.setName(bank.getName());
		if(bank.getCode()!=null)
		this.setCode(bank.getCode());
		if(bank.getDescription()!=null)
		this.setDescription(bank.getDescription());
		
		}
	 
		
	}

	public BankContract mapContract(BankContract bank) {
		 bank.setId(id);
		 bank.setCode(code);
		 bank.setName(name);
		 bank.setActive(active);
		 bank.setDescription(description);
		return bank;
	}
	 
}
