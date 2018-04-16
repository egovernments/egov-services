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

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.egov.eis.model.LeaveAllotment;
import org.egov.eis.repository.builder.LeaveAllotmentQueryBuilder;
import org.egov.eis.repository.rowmapper.LeaveAllotmentRowMapper;
import org.egov.eis.service.UserService;
import org.egov.eis.web.contract.LeaveAllotmentGetRequest;
import org.egov.eis.web.contract.LeaveAllotmentRequest;
import org.egov.eis.web.contract.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LeaveAllotmentRepository {

	public static final String INSERT_LEAVEALLOTMENT_QUERY = "INSERT INTO egeis_leaveAllotment"
			+ " (id, designationId, leaveTypeId, noOfDays, createdBy, createdDate,"
			+ " lastModifiedBy, lastModifiedDate, tenantId)"
			+ " VALUES (nextval('seq_egeis_leaveAllotment'),?,?,?,?,?,?,?,?)";

	public static final String UPDATE_LEAVEALLOTMENT_QUERY = "UPDATE egeis_leaveAllotment"
			+ " SET designationId=?,  leaveTypeId=?, noOfDays=?,"
			+ " lastModifiedBy=?, lastModifiedDate=?, tenantId=? where id=? and tenantid=? ";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private LeaveAllotmentRowMapper leaveAllotmentRowMapper;

	@Autowired
	private LeaveAllotmentQueryBuilder leaveAllotmentQueryBuilder;

	@Autowired
	private UserService userService;

	public List<LeaveAllotment> findForCriteria(LeaveAllotmentGetRequest leaveAllotmentGetRequest) {
		List<Object> preparedStatementValues = new ArrayList<Object>();
		String queryStr = leaveAllotmentQueryBuilder.getQuery(leaveAllotmentGetRequest, preparedStatementValues);
		List<LeaveAllotment> leaveAllotments = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(),
				leaveAllotmentRowMapper);
		return leaveAllotments;
	}

	public void create(LeaveAllotmentRequest leaveAllotmentRequest) {
		List<Object[]> batchArgs = new ArrayList<>();
		final UserResponse userResponse = userService
				.findUserByUserNameAndTenantId(leaveAllotmentRequest.getRequestInfo());
		for (LeaveAllotment la : leaveAllotmentRequest.getLeaveAllotment()) {
			Object[] laRecord = { la.getDesignation(), la.getLeaveType().getId(), la.getNoOfDays(),
					userResponse.getUsers().get(0).getId(), new Date(System.currentTimeMillis()),
					userResponse.getUsers().get(0).getId(), new Date(System.currentTimeMillis()), la.getTenantId() };
			batchArgs.add(laRecord);
		}

		try {
			jdbcTemplate.batchUpdate(INSERT_LEAVEALLOTMENT_QUERY, batchArgs);
		} catch (DataAccessException ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex.getMessage());
		}

	}

	public void update(LeaveAllotmentRequest leaveAllotmentRequest) {
		List<Object[]> batchArgs = new ArrayList<>();
		final UserResponse userResponse = userService
				.findUserByUserNameAndTenantId(leaveAllotmentRequest.getRequestInfo());
		for (LeaveAllotment la : leaveAllotmentRequest.getLeaveAllotment()) {
			Object[] laRecord = { la.getDesignation(), la.getLeaveType().getId(), la.getNoOfDays(),
					userResponse.getUsers().get(0).getId(), new Date(System.currentTimeMillis()), la.getTenantId(),
					la.getId(), la.getTenantId() };
			batchArgs.add(laRecord);
		}

		try {
			jdbcTemplate.batchUpdate(UPDATE_LEAVEALLOTMENT_QUERY, batchArgs);
		} catch (DataAccessException ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex.getMessage());
		}

	}

	public List<Map<String, Object>> getLeaveAllotmentByDesignation(final Long leavetypeid, final Long designationid,
			final String tenantId) {
		final List<Object> preparedStatementValues = new ArrayList<Object>();
		preparedStatementValues.add(leavetypeid);
		preparedStatementValues.add(tenantId);

		String query;
		if (designationid != null) {
			preparedStatementValues.add(designationid);
			query = leaveAllotmentQueryBuilder.selectLeaveAllotmentByDesignationQuery();
		} else {
			query = leaveAllotmentQueryBuilder.selectLeaveAllotmentByLeavetypeQuery();
		}

		List<Map<String, Object>> maps = jdbcTemplate.queryForList(query, preparedStatementValues.toArray());
		return maps;

	}

	public List<Map<String, Object>> getLeaveAllotmentByLeaveType(final Long id, final Long leavetypeid,
			final Long designationid, final String tenantId) {
		final List<Object> preparedStatementValues = new ArrayList<Object>();
		preparedStatementValues.add(leavetypeid);
		preparedStatementValues.add(tenantId);

		String query;
		if (designationid != null) {
			preparedStatementValues.add(designationid);
			preparedStatementValues.add(id);
			query = leaveAllotmentQueryBuilder.selectLeaveAllotmentByDesignationAndIdNotInQuery();
		} else {
			preparedStatementValues.add(id);
			query = leaveAllotmentQueryBuilder.selectLeaveAllotmentByLeavetypeAndIdNotInQuery();
		}

		List<Map<String, Object>> maps = jdbcTemplate.queryForList(query, preparedStatementValues.toArray());
		return maps;

	}
}