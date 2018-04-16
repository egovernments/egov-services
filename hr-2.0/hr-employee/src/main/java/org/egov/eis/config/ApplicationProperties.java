/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.empernments.org
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
 *  In case of any queries, you can reach eGovernments Foundation at contact@empernments.org.
 */

package org.egov.eis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource(value = { "classpath:config/application-config.properties" }, ignoreResourceNotFound = true)
@Order(0)
public class ApplicationProperties {

	private static final String EMP_SEARCH_PAGESIZE_DEFAULT = "egov.services.emp.search.pagesize.default";
	private static final String EMP_SEARCH_PAGENO_MAX = "egov.services.emp.search.pageno.max";
	private static final String EMP_SEARCH_PAGESIZE_MAX = "egov.services.emp.search.pagesize.max";

	private static final String EMP_SEQ_ASSIGNMENT = "egov.services.emp.seq.assignment";
	private static final String EMP_SEQ_DEPARTMENTALTEST = "egov.services.emp.seq.departmentaltest";
	private static final String EMP_SEQ_EDUCATIONALQUALIFICATION = "egov.services.emp.seq.educationalqualification";
	private static final String EMP_SEQ_HODDEPARTMENT = "egov.services.emp.seq.hoddepartment";
	private static final String EMP_SEQ_PROBATION = "egov.services.emp.seq.probation";
	private static final String EMP_SEQ_REGULARISATION = "egov.services.emp.seq.regularisation";
	private static final String EMP_SEQ_SERVICEHISTORY = "egov.services.emp.seq.servicehistory";
	private static final String EMP_SEQ_TECHNICALQUALIFICATION = "egov.services.emp.seq.technicalqualification";

	@Autowired
	private Environment environment;

	public String empSearchPageSizeDefault() {
		return this.environment.getProperty(EMP_SEARCH_PAGESIZE_DEFAULT);
	}

	public String empSearchPageNumberMax() {
		return this.environment.getProperty(EMP_SEARCH_PAGENO_MAX);
	}

	public String empSearchPageSizeMax() {
		return this.environment.getProperty(EMP_SEARCH_PAGESIZE_MAX);
	}

	public String empSeqAssignment() {
		return this.environment.getProperty(EMP_SEQ_ASSIGNMENT);
	}

	public String empSeqDepartmentalTest() {
		return this.environment.getProperty(EMP_SEQ_DEPARTMENTALTEST);
	}

	public String empSeqEducationalQualification() {
		return this.environment.getProperty(EMP_SEQ_EDUCATIONALQUALIFICATION);
	}

	public String empSeqHODDepartment() {
		return this.environment.getProperty(EMP_SEQ_HODDEPARTMENT);
	}

	public String empSeqProbation() {
		return this.environment.getProperty(EMP_SEQ_PROBATION);
	}

	public String empSeqRegularisation() {
		return this.environment.getProperty(EMP_SEQ_REGULARISATION);
	}

	public String empSeqServiceHistory() {
		return this.environment.getProperty(EMP_SEQ_SERVICEHISTORY);
	}

	public String empSeqTechnicalQualification() {
		return this.environment.getProperty(EMP_SEQ_TECHNICALQUALIFICATION);
	}

}