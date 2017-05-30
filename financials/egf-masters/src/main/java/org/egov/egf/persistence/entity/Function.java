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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.egov.egf.persistence.queue.contract.FunctionContract;
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
@EqualsAndHashCode(exclude = { "parentId" }, callSuper = false)

@Table(name = "egf_function")
@SequenceGenerator(name = Function.SEQ, sequenceName = Function.SEQ, allocationSize = 1)
public class Function extends AbstractAuditable {

	private static final long serialVersionUID = -810383403515366449L;

	public static final String SEQ = "seq_egf_function";

	@Id
	@GeneratedValue(generator = SEQ, strategy = GenerationType.SEQUENCE)
	private Long id;

	@Length(max = 128, min = 2)
	@NotNull
	private String name;

	@Length(max = 16, min = 2)
	@NotNull
	private String code;

	@NotNull
	private Integer level;

	@NotNull
	private Boolean active;

	// is this required?
	@NotNull
	private Boolean isParent;

	private Long parentId;

	@Override
	public Long getId() {
		return this.id;
	}

	public Function(FunctionContract contract) {
		this.setId(contract.getId());
		this.setName(contract.getName());
		this.setCode(contract.getCode());
		this.setActive(contract.getActive());
		this.setLevel(contract.getLevel());
		this.setIsParent(contract.getIsParent());
		this.setParentId(contract.getParentId() != null ? contract.getParentId().getId() : null);
		this.setTenantId(contract.getTenantId());
	}
}
