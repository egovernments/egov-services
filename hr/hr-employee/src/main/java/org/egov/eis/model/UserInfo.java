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

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.egov.eis.model.enums.Gender;
import org.egov.eis.model.enums.UserType;

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
public class UserInfo {

	@NotNull
	private Long id;

	private String userName;

	private String salutation;

	@Size(min=2, max=100)
	private String name;

	private Gender gender;

	private String mobileNumber;

	private String emailId;

	private String altContactNumber;

	private String pan;

	private Long aadhaarNumber;

	private String permanentAddress;

	private String permanentCity;

	private String permanentPincode;

	private String correspondenceCity;

	private String correspondencePincode;

	private String correspondenceAddress;

	private Boolean active;

	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date dob;

	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date pwdExpiryDate;

	private String locale;

	@Setter(AccessLevel.NONE)
	private UserType type = UserType.EMPLOYEE;

	private String signature;

	private Boolean accountLocked;

	private List<Long> roles = new ArrayList<Long>();

	private String fatherOrHusbandName;

	private String bloodGroup;

	private String identificationMark;

	private String photo;

	private Long createdBy;

	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date createdDate;

	private Long lastModifiedBy;

	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date lastModifiedDate;

	@NotNull
	private String tenantId;

}