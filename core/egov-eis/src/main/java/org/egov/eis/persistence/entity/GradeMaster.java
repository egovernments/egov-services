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
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.eis.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "EGEIS_GRADE_MSTR")
@SequenceGenerator(name = GradeMaster.SEQ_EGPIMS_GRADE_MSTR_SEQ, sequenceName = GradeMaster.SEQ_EGPIMS_GRADE_MSTR_SEQ, allocationSize = 1)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(value = { "handler", "hibernateLazyInitializer" })
public class GradeMaster implements GenericMaster {

	private static final long serialVersionUID = -5019661346624936153L;
	public static final String SEQ_EGPIMS_GRADE_MSTR_SEQ = "EGPIMS_GRADE_MSTR_SEQ";
	@Id
	@GeneratedValue(generator = SEQ_EGPIMS_GRADE_MSTR_SEQ, strategy = GenerationType.SEQUENCE)
	@Column(name="GRADE_ID")
	public Integer id;

	public Integer age;

	@Column(name = "GRADE_VALUE")
	public String name;

	@Column(name = "START_DATE")
	public java.util.Date fromDate;

	@Column(name = "END_DATE")
	public java.util.Date toDate;

	@Column(name = "ORDER_NO")
	public Integer orderNo;

}
