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

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
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
import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@EqualsAndHashCode(exclude = { "chartOfAccount", "voucherHeader", "function", "ledgerDetails" }, callSuper = false)

@Table(name = "egf_generalledger")
@SequenceGenerator(name = GeneralLedger.SEQ, sequenceName = GeneralLedger.SEQ, allocationSize = 1)
public class GeneralLedger extends AbstractPersistable<Long> {

	private static final long serialVersionUID = 8461730283251609382L;
	public static final String SEQ = "seq_egf_generalledger";
	@Id
	@GeneratedValue(generator = GeneralLedger.SEQ, strategy = GenerationType.SEQUENCE)
	private Long id = null;
	private Integer orderId;
	private ChartOfAccount chartOfAccount;
	@NotNull
	@Length(max = 16)
	private String glcode;
	@NotNull
	@Min(value = 0)
	@Max(value = 999999999)
	private BigDecimal debitAmount;
	@NotNull
	@Min(value = 0)
	@Max(value = 999999999)
	private BigDecimal creditAmount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "voucherheaderid", nullable = true)
	private VoucherHeader voucherHeader;

	private Function function;

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "generalLedger", targetEntity = GeneralLedgerDetail.class)
	private Set<GeneralLedgerDetail> ledgerDetails = new HashSet<GeneralLedgerDetail>();

	@Transient
	private Boolean isSubLedger;

	public Long getId() {
		return this.id;
	}

}
