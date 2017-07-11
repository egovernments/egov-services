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
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.eis.model;

import static org.egov.eis.utils.DateUtils.areDatesEqualWithoutTimePart;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
@ToString
public class Assignment {

	private Long id;

	@NotNull
	private Long position;

	private Long fund;

	private Long functionary;

	private Long function;

	@NotNull
	private Long department;

	@NotNull
	private Long designation;

	@Valid
	private List<HODDepartment> hod = new ArrayList<HODDepartment>();

	@NotNull
	private Boolean isPrimary;

	@NotNull
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date fromDate;

	@NotNull
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date toDate;

	private Long grade;

	private String govtOrderNumber;

	private List<String> documents = new ArrayList<String>();

	private Long createdBy;

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date createdDate;

	private Long lastModifiedBy;

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date lastModifiedDate;

	private String tenantId;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Assignment other = (Assignment) obj;
		if (department == null) {
			if (other.department != null)
				return false;
		} else if (!department.equals(other.department))
			return false;
		if (designation == null) {
			if (other.designation != null)
				return false;
		} else if (!designation.equals(other.designation))
			return false;
		if (documents == null) {
			if (other.documents != null)
				return false;
		} else if (!documents.equals(other.documents))
			return false;
		if (fromDate == null) {
			if (other.fromDate != null)
				return false;
		} 
		else if (!areDatesEqualWithoutTimePart(fromDate, other.fromDate))
			return false;
		if (function == null) {
			if (other.function != null)
				return false;
		} else if (!function.equals(other.function))
			return false;
		if (functionary == null) {
			if (other.functionary != null)
				return false;
		} else if (!functionary.equals(other.functionary))
			return false;
		if (fund == null) {
			if (other.fund != null)
				return false;
		} else if (!fund.equals(other.fund))
			return false;
		if (govtOrderNumber == null) {
			if (other.govtOrderNumber != null)
				return false;
		} else if (!govtOrderNumber.equals(other.govtOrderNumber))
			return false;
		if (grade == null) {
			if (other.grade != null)
				return false;
		} else if (!grade.equals(other.grade))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (isPrimary == null) {
			if (other.isPrimary != null)
				return false;
		} else if (!isPrimary.equals(other.isPrimary))
			return false;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		if (toDate == null) {
			if (other.toDate != null)
				return false;
		} else if (!areDatesEqualWithoutTimePart(toDate, other.fromDate))
			{
			return false;
			}
		return true;
	}
	
  /*@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Assignment other = (Assignment) obj;
		if (department == null) {
			if (other.department != null)
				return false;
		} else if (!department.equals(other.department))
			return false;
		if (designation == null) {
			if (other.designation != null)
				return false;
		} else if (!designation.equals(other.designation))
			return false;
		if (documents == null) {
			if (other.documents != null)
				return false;
		} else if (!documents.equals(other.documents))
			return false;
		if (fromDate == null) {
			if (other.fromDate != null)
				return false;
		} else if (!areDatesEqualWithoutTimePart(fromDate, other.fromDate))
			return false;
		if (function == null) {
			if (other.function != null)
				return false;
		} else if (!function.equals(other.function))
			return false;
		if (functionary == null) {
			if (other.functionary != null)
				return false;
		} else if (!functionary.equals(other.functionary))
			return false;
		if (fund == null) {
			if (other.fund != null)
				return false;
		} else if (!fund.equals(other.fund))
			return false;
		if (govtOrderNumber == null) {
			if (other.govtOrderNumber != null)
				return false;
		} else if (!govtOrderNumber.equals(other.govtOrderNumber))
			return false;
		if (grade == null) {
			if (other.grade != null)
				return false;
		} else if (!grade.equals(other.grade))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (isPrimary == null) {
			if (other.isPrimary != null)
				return false;
		} else if (!isPrimary.equals(other.isPrimary))
			return false;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		if (toDate == null) {
			if (other.toDate != null)
				return false;
		} else if (!areDatesEqualWithoutTimePart(toDate, other.toDate))
			return false;
		return true;
	}*/
	


}