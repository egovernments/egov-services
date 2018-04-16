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

package org.egov.eis.model.enums;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EntityType {
	EMPLOYEE_HEADER("EMPLOYEE_HEADER", "egeis_employee","employee", "Employee"),
	ASSIGNMENT("ASSIGNMENT", "egeis_assignment", "assignments", "Assignment"),
	JURISDICTION("JURISDICTION", "egeis_employeeJurisdiction", "jurisdictions", "Jurisdiction"),
	SERVICE("SERVICE", "egeis_serviceHistory", "serviceHistory", "Service History"),
	TECHNICAL("TECHNICAL", "egeis_technicalQualification", "technical", "Technical Qualification"),
	EDUCATION("EDUCATION", "egeis_educationalQualification", "education", "Educational Qualification"),
	TEST("TEST", "egeis_departmentalTest", "test", "Departmental Test"),
	REGULARISATION("REGULARISATION", "egeis_regularisation", "regularisation", "Regularisation"),
	PROBATION("PROBATION", "egeis_probation", "probation", "Probation"),
	APR_DETAILS("APR_DETAILS", "egeis_aprDetails", "aprDetails", "APRDetail"),
	NOMINEE("NOMINEE", "egeis_nominee", "nominee", "Nominee");

	private String value;
	private String dbTable;
	private String contractFieldName;
	private String entityName;

	EntityType(String value, String dbTable, String contractFieldName, String entityName) {
		this.value = value;
		this.dbTable = dbTable;
		this.contractFieldName = contractFieldName;
		this.entityName = entityName;
	}

	@Override
	@JsonValue
    public String toString() {
        return StringUtils.capitalize(name());
    }
	
	public String getDbTable() {
		return dbTable;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value){
		this.value = value;
	}
	
	public void setDbTable(String dbTable){
		this.dbTable = dbTable;
	}

	public String getContractFieldName() {
		return contractFieldName;
	}

	public String getEntityName() {
		return entityName;
	}

	@JsonCreator
	public static EntityType fromValue(String passedValue) {
		for (EntityType obj : EntityType.values()) {
			if (String.valueOf(obj.value).equals(passedValue.toUpperCase())) {
				return obj;
			}
		}
		return null;
	}
}
