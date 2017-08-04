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

package org.egov.wcms.transaction.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class EstimationNotice {
	
	private String ulbLogo;
	private String ulbName;
	private String mahLogo;
	private String dateOfLetter; 
	private String letterNumber;
	private String letterTo;
	private String letterIntimationSubject;
	private String applicationNumber;
	private String applicationDate;
	private String applicantName;
	private String serviceName;
	private String waterNo;
	private Long slaDays;
	private List<String> chargeDescription;
	
	public String getUlbLogo() {
		return ulbLogo;
	}

	public void setUlbLogo(String ulbLogo) {
		this.ulbLogo = ulbLogo;
	}

	public String getUlbName() {
		return ulbName;
	}

	public void setUlbName(String ulbName) {
		this.ulbName = ulbName;
	}

	public String getMahLogo() {
		return mahLogo;
	}

	public void setMahLogo(String mahLogo) {
		this.mahLogo = mahLogo;
	}

	public String getDateOfLetter() {
		return dateOfLetter;
	}

	public void setDateOfLetter(String dateOfLetter) {
		this.dateOfLetter = dateOfLetter;
	}

	public String getLetterNumber() {
		return letterNumber;
	}

	public void setLetterNumber(String letterNumber) {
		this.letterNumber = letterNumber;
	}

	public String getLetterTo() {
		return letterTo;
	}

	public void setLetterTo(String letterTo) {
		this.letterTo = letterTo;
	}

	public String getLetterIntimationSubject() {
		return letterIntimationSubject;
	}

	public void setLetterIntimationSubject(String letterIntimationSubject) {
		this.letterIntimationSubject = letterIntimationSubject;
	}

	public String getApplicationNumber() {
		return applicationNumber;
	}

	public void setApplicationNumber(String applicationNumber) {
		this.applicationNumber = applicationNumber;
	}

	public String getApplicationDate() {
		return applicationDate;
	}

	public void setApplicationDate(String applicationDate) {
		this.applicationDate = applicationDate;
	}

	public String getApplicantName() {
		return applicantName;
	}

	public void setApplicantName(String applicantName) {
		this.applicantName = applicantName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getWaterNo() {
		return waterNo;
	}

	public void setWaterNo(String waterNo) {
		this.waterNo = waterNo;
	}

	public Long getSlaDays() {
		return slaDays;
	}

	public void setSlaDays(Long slaDays) {
		this.slaDays = slaDays;
	}

	public List<String> getChargeDescription() {
		return chargeDescription;
	}

	public void setChargeDescription(List<String> chargeDescription) {
		this.chargeDescription = chargeDescription;
	}

	

	

}

