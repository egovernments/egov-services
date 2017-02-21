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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.ja.annotation.Ignore;

@Entity
@Table(name = "egf_bankbranch")
@SequenceGenerator(name = BankBranch.SEQ_BANKBRANCH, sequenceName = BankBranch.SEQ_BANKBRANCH, allocationSize = 1)
public class BankBranch extends AbstractAuditable {

    private static final long serialVersionUID = -1445070413847273114L;

    public static final String SEQ_BANKBRANCH = "seq_egf_bankbranch";

    @Id
    @GeneratedValue(generator = SEQ_BANKBRANCH, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bankid")
    @NotNull
    private Bank bank;

    @NotNull
    @Length(max = 50,min=1)
    private String code;

    @NotNull
    @Length(max = 50,min=1)
    @Pattern(regexp="^[a-zA-Z0-9_]*$")
    private String name;

    @NotNull
    @Length(max = 50,min=1)
    private String address;

    @Length(max = 50)
    private String address2;

    @Length(max = 50)
    private String city;

    @Length(max = 50)
    private String state;

    @Length(max = 50)
    private String pincode;

    @Length(max = 15)
    private String phone;

    @Length(max = 15)
    private String fax;

    @Length(max = 50)
    private String contactPerson;

    @NotNull
    private Boolean active;

    @Length(max = 256)
    private String description;

    @Length(max = 50)
    @Column(name = "micr")
    private String micr;

    @Ignore
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "bankBranch", targetEntity = BankAccount.class)
    private Set<BankAccount> bankAccounts = new HashSet<>(0);
    public boolean isAccountsExist() {
        return bankAccounts != null && !bankAccounts.isEmpty();
    }
    @Override
    public Long getId()
    {
    	return this.id;
    }
  

}