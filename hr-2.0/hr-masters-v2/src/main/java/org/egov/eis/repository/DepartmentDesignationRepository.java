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

import org.egov.eis.model.DepartmentDesignation;
import org.egov.eis.repository.rowmapper.DepartmentDesignationRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class DepartmentDesignationRepository {

	public static final String INSERT_DEPARTMENTDESIGNATION_QUERY = "INSERT INTO egeis_departmentDesignation"
			+ " (id, departmentId, designationId,tenantId)"
			+ " VALUES (nextval('seq_egeis_departmentDesignation'),?,?,?)";

	private static final String BASE_QUERY = "SELECT id, departmentId, designationId, tenantId"
			+ " FROM egeis_departmentDesignation WHERE id = ?";

	private static final String GET_BY_DEPT_AND_DESG_QUERY = "SELECT id, departmentId, designationId, tenantId"
			+ " FROM egeis_departmentDesignation WHERE departmentId = ? AND designationId = ? AND tenantId = ?";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	private DepartmentDesignationRowMapper deptDesigRowMapper;

	public DepartmentDesignation findForId(Long id) {
		return jdbcTemplate.queryForObject(BASE_QUERY, deptDesigRowMapper, id);
	}

	public DepartmentDesignation findByDepartmentAndDesignation(Long department, Long designation, String tenantId) {
		List<Object> preparedStatementValues = new ArrayList<>();
		preparedStatementValues.add(department);
		preparedStatementValues.add(designation);
		preparedStatementValues.add(tenantId);
		List<DepartmentDesignation> departmentDesignations = jdbcTemplate.query(GET_BY_DEPT_AND_DESG_QUERY,
				preparedStatementValues.toArray(), deptDesigRowMapper);
		return departmentDesignations.isEmpty() ? null : departmentDesignations.get(0);
	}

	public void create(DepartmentDesignation departmentDesignation) {
		List<Object[]> batchArgs = new ArrayList<>();
		Object[] deptDesgRecord = { departmentDesignation.getDepartmentId(),
				departmentDesignation.getDesignation().getId(), departmentDesignation.getTenantId() };
		batchArgs.add(deptDesgRecord);

		try {
			jdbcTemplate.batchUpdate(INSERT_DEPARTMENTDESIGNATION_QUERY, batchArgs);
		} catch (DataAccessException ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex.getMessage());
		}

	}

}