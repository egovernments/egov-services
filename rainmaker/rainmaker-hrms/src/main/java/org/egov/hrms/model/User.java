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

package org.egov.hrms.model;

import lombok.*;
import org.egov.hrms.model.enums.Gender;
import org.egov.hrms.model.enums.UserType;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
public class User {

	private Long id;
	private String uuid;

	@Size(min=1, max=100)
	private String userName;


    @NotNull
    @Size(max=100)
    @Pattern(regexp = "^[a-zA-Z0-9 \\-'`\\.]*$", message = "Invalid name. Only alphabets and special characters -, ',`, .")
	private String name;

	@NonNull
	@Size(max=10)
	private String mobileNumber;

	@Valid
	@NotNull
	private Gender gender;


	@Size(max=128)
	private String emailId;
	
	
    private Long dob;
    
    @Size(max=300)
    private String correspondenceAddress;

	@Valid
	@NotNull
	private UserType type = UserType.EMPLOYEE;


	@Valid
	@NotNull
	private List<Role> roles = new ArrayList<Role>();

	@NonNull
	@Size(max=256)
	private String tenantId;

}