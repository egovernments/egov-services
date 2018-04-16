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

package org.egov.eis.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.eis.model.EmployeeDocument;
import org.egov.eis.model.EmployeeInfo;
import org.egov.eis.repository.builder.HODEmployeeQueryBuilder;
import org.egov.eis.repository.rowmapper.EmployeeDocumentsRowMapper;
import org.egov.eis.repository.rowmapper.EmployeeIdsRowMapper;
import org.egov.eis.repository.rowmapper.EmployeeInfoRowMapper;
import org.egov.eis.web.contract.HODEmployeeCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class HODEmployeeRepository {

	public static final Logger LOGGER = LoggerFactory.getLogger(HODEmployeeRepository.class);

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	private EmployeeIdsRowMapper employeeIdsRowMapper;

	@Autowired
	private EmployeeInfoRowMapper employeeInfoRowMapper;

	@Autowired
	private EmployeeDocumentsRowMapper employeeDocumentsRowMapper;

	@Autowired
	private HODEmployeeQueryBuilder hodEmployeeQueryBuilder;

	private static final String SELECT_EMPLOYEE_DOCUMENTS_QUERY = "SELECT employeeId, document"
			+ " FROM egeis_employeeDocuments where employeeId IN (:employeeIds) AND tenantId = :tenantId";

	public List<Long> findIdsForCriteria(HODEmployeeCriteria hodEmployeeCriteria) {
		Map<String, Object> namedParametersForListOfHODEmployeeIds = new HashMap<>();
		String queryStrForListOfEmployeeIds = hodEmployeeQueryBuilder.getQueryForListOfHODEmployeeIds(hodEmployeeCriteria,
				namedParametersForListOfHODEmployeeIds);

		List<Long> listOfIds = namedParameterJdbcTemplate.query(queryStrForListOfEmployeeIds,
				namedParametersForListOfHODEmployeeIds, employeeIdsRowMapper);

		return listOfIds;
	}

	public List<EmployeeInfo> findForCriteria(HODEmployeeCriteria hodEmployeeCriteria, List<Long> listOfIds) {
		Map<String, Object> namedParameters = new HashMap<>();
		String queryStr = hodEmployeeQueryBuilder.getQuery(hodEmployeeCriteria, namedParameters, listOfIds);

		List<EmployeeInfo> employeesInfo = namedParameterJdbcTemplate.query(queryStr, namedParameters, employeeInfoRowMapper);

		return employeesInfo;
	}

	@SuppressWarnings("serial")
	public List<EmployeeDocument> getDocumentsForListOfHODEmployeeIds(List<Long> employeeIds, String tenantId) {
		Map<String, Object> namedParameters = new HashMap<String, Object>(){{
			put("employeeIds", employeeIds);
			put("tenantId", tenantId);
		}};
		List<EmployeeDocument> documents = namedParameterJdbcTemplate.query(SELECT_EMPLOYEE_DOCUMENTS_QUERY,
				namedParameters, employeeDocumentsRowMapper);
		return documents;
	}
}