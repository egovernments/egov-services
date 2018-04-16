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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

	private Long id;

	@Size(min=1, max=100)
	private String userName;

	@Size(max=64)
	private String password;

	@Size(max=5)
	private String salutation;

	@NotNull
	@Size(min=3, max=100)
	private String name;

	@Valid
	@NotNull
	private String gender;

	@Size(max=10)
	private String mobileNumber;

	@Size(max=128)
	private String emailId;

	@Size(max=10)
	private String altContactNumber;

	@Size(max=10)
	private String pan;

	@Size(max=12)
	private String aadhaarNumber;

	@Size(max=300)
	private String permanentAddress;

	@Size(max=50)
	private String permanentCity;

	@Size(max=6)
	private String permanentPinCode;

	@Size(max=50)
	private String correspondenceCity;

	@Size(max=6)
	private String correspondencePinCode;

	@Size(max=300)
	private String correspondenceAddress;

	@NotNull
	private Boolean active;

	// FIXME : User service is expecting & sending dates in multiple formats. Fix a common standard for date formats.
	@NotNull
	private String dob;

	// FIXME : User service is expecting & sending dates in multiple formats. Fix a common standard for date formats.
	private String pwdExpiryDate;

	@Size(max=5)
	private String locale;

	@Valid
	@NotNull
	private String type;

	private Boolean accountLocked;

	@Valid
	@NotNull
	private List<Role> roles = new ArrayList<Role>();

	@Size(max=100)
	private String fatherOrHusbandName;

	private String bloodGroup;

	@Size(max=36)
	private String photo;

	@Size(max=36)
	private String signature;

	@Size(max=300)
	private String identificationMark;

	private Long createdBy;

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss")
	private Date createdDate;

	private Long lastModifiedBy;

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss")
	private Date lastModifiedDate;

	@Size(max=256)
	private String tenantId;

}