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

import org.egov.eis.model.Designation;
import org.egov.eis.repository.builder.DesignationQueryBuilder;
import org.egov.eis.repository.rowmapper.DesignationRowMapper;
import org.egov.eis.web.contract.DesignationGetRequest;
import org.egov.eis.web.contract.DesignationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class DesignationRepository {

	public static final String INSERT_DESIGNATION_QUERY = "INSERT INTO egeis_designation"
			+ " (id, code, name, description, chartOfAccounts, active,tenantId)"
			+ " VALUES (?,?,?,?,?,?,?)";

	public static final String UPDATE_DESIGNATION_QUERY = "UPDATE egeis_designation"
			+ " SET code=?, name=?, description=?, chartOfAccounts=?, active=? where id=? and tenantid=? ";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private DesignationRowMapper designationRowMapper;

	@Autowired
	private DesignationQueryBuilder designationQueryBuilder;

	public List<Designation> findForCriteria(DesignationGetRequest designationGetRequest) {
		List<Object> preparedStatementValues = new ArrayList<Object>();
		if(designationGetRequest.getDestinationTenant()!=null && !designationGetRequest.getDestinationTenant().equals(""))
			designationGetRequest.setTenantId(designationGetRequest.getDestinationTenant());

		String queryStr = designationQueryBuilder.getQuery(designationGetRequest, preparedStatementValues);
		List<Designation> designations = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(),
				designationRowMapper);
		return designations;
	}

	public void create(DesignationRequest designationRequest) {

		List<Object[]> batchArgs = new ArrayList<>();
		Object[] lobRecord = { designationRequest.getDesignation().getId(),designationRequest.getDesignation().getCode(),
				designationRequest.getDesignation().getName(), designationRequest.getDesignation().getDescription(),
				designationRequest.getDesignation().getChartOfAccounts(),
				designationRequest.getDesignation().getActive(), designationRequest.getDesignation().getTenantId() };
		batchArgs.add(lobRecord);

		try {
			jdbcTemplate.batchUpdate(INSERT_DESIGNATION_QUERY, batchArgs);
		} catch (DataAccessException ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex.getMessage());
		}

	}

	public void update(DesignationRequest designationRequest) {

		List<Object[]> batchArgs = new ArrayList<>();
		Object[] lobRecord = { designationRequest.getDesignation().getCode(),
				designationRequest.getDesignation().getName(), designationRequest.getDesignation().getDescription(),
				designationRequest.getDesignation().getChartOfAccounts(),
				designationRequest.getDesignation().getActive(), designationRequest.getDesignation().getId(),
				designationRequest.getDesignation().getTenantId() };
		batchArgs.add(lobRecord);

		try {
			jdbcTemplate.batchUpdate(UPDATE_DESIGNATION_QUERY, batchArgs);
		} catch (DataAccessException ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex.getMessage());
		}

	}
}