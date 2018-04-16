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

package org.egov.eis.web.contract;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.egov.eis.web.contract.enums.MaritalStatus;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
public class Employee {

    private Long id;

    @NotNull
    @Size(min = 1, max = 256)
    private String code;

    @NotNull
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dateOfAppointment;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dateOfJoining;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dateOfRetirement;

    @NotNull
    private Long employeeStatus;

    private Long recruitmentMode;

    private Long recruitmentType;

    private Long recruitmentQuota;

    private Boolean transferredEmployee = false;

    @Max(100)
    private Short retirementAge;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dateOfResignation;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dateOfTermination;

    @NotNull
    private Long employeeType;

    @Valid
    @NotNull
    @Size(min = 1)
    private List<Assignment> assignments = new ArrayList<Assignment>();

    @NotNull
    @Size(min = 1)
    private List<Long> jurisdictions = new ArrayList<Long>();

    private Long motherTongue;

    private Long religion;

    private Long community;

    private Long category;

    private Boolean physicallyDisabled;

    private Boolean medicalReportProduced;

    private List<Long> languagesKnown = new ArrayList<Long>();

    @Valid
    @NotNull
    private MaritalStatus maritalStatus;

    private String passportNo;

    private String gpfNo;

    private Long bank;

    private Long bankBranch;

    @Size(max = 20)
    private String bankAccount;

    private Long group;

    private String dob;

    @Size(max = 200)
    private String placeOfBirth;

    @Valid
    private List<ServiceHistory> serviceHistory = new ArrayList<ServiceHistory>();

    @Valid
    private List<Probation> probation = new ArrayList<Probation>();

    @Valid
    private List<Regularisation> regularisation = new ArrayList<Regularisation>();

    @Valid
    private List<TechnicalQualification> technical = new ArrayList<TechnicalQualification>();

    @Valid
    private List<EducationalQualification> education = new ArrayList<EducationalQualification>();

    @Valid
    private List<DepartmentalTest> test = new ArrayList<DepartmentalTest>();

    private List<String> documents = new ArrayList<String>();

    private Long createdBy;
    
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date createdDate;

    private Long lastModifiedBy;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date lastModifiedDate;

    @Valid
    @NotNull
    private User user;

    @NotNull
    @Size(max = 256)
    private String tenantId;

}