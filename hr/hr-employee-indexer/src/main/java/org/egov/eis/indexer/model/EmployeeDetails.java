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

package org.egov.eis.indexer.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.commons.model.Category;
import org.egov.commons.model.Community;
import org.egov.commons.model.Language;
import org.egov.commons.model.Religion;
import org.egov.egf.persistence.queue.contract.Bank;
import org.egov.egf.persistence.queue.contract.BankBranch;
import org.egov.eis.model.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDetails {

	private Long id;

	private String code;

	private String userName;

	private String salutation;

	private String name;

	private String gender;

	private String mobileNumber;

	private String altContactNumber;

	private String emailId;

	private String pan;

	private String aadhaarNumber;

	private String permanentAddress;

	private String permanentCity;

	private String permanentPinCode;

	private String correspondenceAddress;

	private String correspondenceCity;

	private String correspondencePinCode;

	private Boolean active;

	private String dob;

	private String pwdExpiryDate;

	private String locale;

	private String type;

	private Boolean accountLocked;

	private String bloodGroup;

	private String fatherOrHusbandName;

	private String identificationMark;

	private String photo;

	private String signature;

	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date dateOfAppointment;

	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date dateOfJoining;

	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date dateOfRetirement;

	private HRStatus employeeStatus;

	private RecruitmentMode recruitmentMode;

	private RecruitmentType recruitmentType;

	private RecruitmentQuota recruitmentQuota;

	private Short retirementAge;

	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date dateOfResignation;

	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date dateOfTermination;

	private EmployeeType employeeType;

	private List<EmployeeAssignment> assignments = new ArrayList<>();

	private List<EmployeeJurisdiction> jurisdictions = new ArrayList<>();

	private Language motherTongue;

	private Religion religion;

	private Community community;

	private Category category;

	private Boolean physicallyDisabled;

	private Boolean medicalReportProduced;

	private List<Language> languagesKnown = new ArrayList<>();

	private String maritalStatus;

	private String passportNo;

	private String gpfNo;

	private Bank bank;

	private BankBranch bankBranch;

	private String bankAccount;

	private Group group;

	private String placeOfBirth;

	private List<EmployeeServiceHistory> serviceHistory = new ArrayList<>();

	private List<EmployeeProbation> probation = new ArrayList<>();

	private List<EmployeeRegularisation> regularisation = new ArrayList<>();

	private List<EmployeeTechnical> technical = new ArrayList<>();

	private List<EmployeeEducation> education = new ArrayList<>();

	private List<EmployeeTest> test = new ArrayList<>();

	@JsonProperty("APRDetails")
	private List<EmployeeAPRDetail> aprDetails = new ArrayList<>();

	private List<String> documents = new ArrayList<>();

	private AuditDetails auditDetails;

	private TenantDetails tenantDetails;

}