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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.egov.eis.model.enums.EmployeeStatus;
import org.egov.eis.model.enums.MaritalStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
public class Employee {

	@NotNull
	private Long id;

	@NotNull
	@Size(min=1, max=256)
	private String code;

	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date dateOfAppointment;

	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date dateOfJoining;

	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date dateOfRetirement;

	private EmployeeStatus employeeStatus = EmployeeStatus.EMPLOYED;

	private Long recruitmentMode;

	private Long recruitmentType;

	private Long recruitmentQuota;

	@Max(100)
	private Short retirementAge;

	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date dateOfResignation;

	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date dateOfTermination;

	private Long employeeType;

	private List<Assignment> assignments = new ArrayList<Assignment>();

	private List<Long> jurisdictions = new ArrayList<Long>();

	private Long motherTongue;

	private Long religion;

	private Long community;

	private Long category;

	private Boolean physicallyDisabled;

	private Boolean medicalReportProduced;

	private List<Long> languagesKnown = new ArrayList<Long>();

	private MaritalStatus maritalStatus;

	private String passportNo;

	private String gpfNo;

	private Long bank;

	private Long bankBranch;

	@Size(max=20)
	private String bankAccount;

	private Long group;

	@Size(max=200)
	private String placeOfBirth;

	private List<ServiceHistory> serviceHistory = new ArrayList<ServiceHistory>();

	private List<Probation> probation = new ArrayList<Probation>();

	private List<Regularisation> regularisation = new ArrayList<Regularisation>();

	private List<TechnicalQualification> technical = new ArrayList<TechnicalQualification>();

	private List<EducationalQualification> education = new ArrayList<EducationalQualification>();

	private List<DepartmentalTest> test = new ArrayList<DepartmentalTest>();

	private UserInfo userInfo;

	@Getter(AccessLevel.NONE)
	@NotNull
	private String tenantId;

}