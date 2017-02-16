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

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "egf_chartofaccounts")
@SequenceGenerator(name = ChartOfAccounts.SEQ_CHARTOFACCOUNTS, sequenceName = ChartOfAccounts.SEQ_CHARTOFACCOUNTS, allocationSize = 1)

public class ChartOfAccounts extends AbstractAuditable {

	private static final long serialVersionUID = 61219209022946300L;

	public static final String SEQ_CHARTOFACCOUNTS = "seq_chartofaccounts";

	@Id
	@GeneratedValue(generator = ChartOfAccounts.SEQ_CHARTOFACCOUNTS, strategy = GenerationType.SEQUENCE)
	private Long id;

	@NotNull
	@Length(max=16)
	private String glcode;
	@NotNull
	@Length(max=128)
	private String name;

	private Long purposeId;

	@Length(max=256) 
	private String desciption;

	private Boolean isActiveForPosting;

	private Long parentId;

	@Column(name = "SCHEDULEID")
	private Long schedule;

	private Character operation;

	private Character type;

	private Long classification;

	private Boolean functionRequired;

	private Boolean budgetCheckRequired;

	@NotNull
	@Length(max=16)
	private String majorCode;

	/*@Column(name = "CLASS")
	private Long myClass;
*/
	@Transient
	private Boolean isSubLedger;

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "chartOfAccounts", targetEntity = ChartOfAccountDetail.class)
	private Set<ChartOfAccountDetail> chartOfAccountDetails = new HashSet<>();
	 @Override
	    public Long getId()
	    {
	    	return this.id;
	    }
}
