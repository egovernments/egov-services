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

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.egov.eis.model.enums.Gender;
import org.egov.eis.model.enums.MaritalStatus;
import org.egov.eis.model.enums.UserType;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Builder
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
public class EmployeeInfo {

    private Long id;

    @Size(min = 1, max = 256)
    private String code;
    
    private String uuid;

    @Size(max = 5)
    private String salutation;

    @Size(min = 3, max = 100)
    private String name;

    @Size(min = 1, max = 64)
    private String userName;

    private Gender gender;

    private MaritalStatus maritalStatus;

    private String bloodGroup;

    private String permanentAddress;

    private String permanentCity;

    private String permanentPinCode;

    private String correspondenceAddress;

    private String correspondenceCity;

    private String correspondencePinCode;

    private String guardian;

    @Size(max = 10)
    private String mobileNumber;

    @Size(max = 10)
    private String altContactNumber;

    @Size(min = 5, max = 128)
    private String emailId;

    @Size(max = 10)
    private String pan;

    @Size(max = 12)
    private String aadhaarNumber;

    @Size(max = 200)
    private String placeOfBirth;

    private Boolean active;

    @Valid
    private UserType type = UserType.EMPLOYEE;

    private List<Long> languagesKnown = new ArrayList<>();

    private Long employeeStatus;

    private Long employeeType;

    private Long group;

    private Long motherTongue;

    private String identificationMark;

    private String passportNo;

    private String gpfNo;

    private Long recruitmentMode;

    private Long recruitmentType;

    private Long recruitmentQuota;

    @Valid
    private List<Assignment> assignments = new ArrayList<Assignment>();

    private List<Long> jurisdictions = new ArrayList<Long>();

    private Long bank;

    private Long bankBranch;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dateOfRetirement;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dateOfAppointment;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dateOfJoining;

    private String dob;

    @Max(100)
    private Short retirementAge;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dateOfResignation;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dateOfTermination;

    @Size(max = 20)
    private String bankAccount;

    @Size(max = 20)
    private String ifscCode;

    private List<String> documents = new ArrayList<String>();
	
    private List<Role> roles = new ArrayList<Role>();

    private String tenantId;
    
    private String photo;

}