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
package org.egov.wcms.transaction.web.contract;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyOwnerInfo {

	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("userName")
	@Size(min=1,max=64)
	private String userName = null;

	@JsonProperty("password")
	@Size(max=64)
	private String password = null;

	@JsonProperty("salutation")
	@Size(max = 5)
	private String salutation = null;

	@JsonProperty("name")
	@NotNull
	@Size(min = 3, max = 100)
	private String name = null;

	@JsonProperty("gender")
	@NotNull
	private String gender = null;

	@JsonProperty("mobileNumber")
	@NotNull
	@Size(max = 10)
	private String mobileNumber = null;

	@JsonProperty("emailId")
	@Size(max = 128)
	private String emailId = null;

	@JsonProperty("altContactNumber")
	@Size(max = 10)
	private String altContactNumber = null;

	@JsonProperty("pan")
	@Size(max = 10)
	private String pan = null;

	@JsonProperty("aadhaarNumber")
	@Pattern(regexp = "[0-9]{12}")
	@Size(max = 12)
	private String aadhaarNumber = null;

	@JsonProperty("permanentAddress")
	@Size(max = 300)
	private String permanentAddress = null;

	@JsonProperty("permanentCity")
	@Size(max = 300)
	private String permanentCity = null;

	@JsonProperty("permanentPincode")
	@Size(max = 6)
	private String permanentPincode = null;

	@JsonProperty("correspondenceCity")
	@Size(max = 50)
	private String correspondenceCity = null;

	@JsonProperty("correspondencePincode")
	@Size(max = 6)
	private String correspondencePincode = null;

	@JsonProperty("correspondenceAddress")
	@Size(max = 300)
	private String correspondenceAddress = null;

	@JsonProperty("active")
	@NotNull
	private Boolean active = null;

	@JsonProperty("dob")
	private String dob = null;

	@JsonProperty("pwdExpiryDate")
	private String pwdExpiryDate = null;

	@JsonProperty("locale")
	@NotNull
	@Size(max = 10)
	private String locale = null;

	@JsonProperty("type")
	@NotNull
	@Size(max = 20)
	private String type = null;

	@JsonProperty("tenantId")
	@NotNull
	private String tenantId = null;

	@JsonProperty("isPrimaryOwner")
	private Boolean isPrimaryOwner = null;

	
}
