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

import org.egov.eis.model.LeaveOpeningBalance;
import org.egov.eis.repository.builder.LeaveOpeningBalanceQueryBuilder;
import org.egov.eis.repository.rowmapper.LeaveOpeningBalanceRowMapper;
import org.egov.eis.service.UserService;
import org.egov.eis.web.contract.LeaveOpeningBalanceGetRequest;
import org.egov.eis.web.contract.LeaveOpeningBalanceRequest;
import org.egov.eis.web.contract.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LeaveOpeningBalanceRepository {

	public static final String INSERT_LEAVEOPENINGBALANCE_QUERY = "INSERT INTO egeis_leaveOpeningBalance"
			+ " (id, employeeId, calendarYear, leaveTypeId, noOfDays, createdBy, createdDate,"
			+ " lastModifiedBy, lastModifiedDate, tenantId)"
			+ " VALUES (nextval('seq_egeis_leaveOpeningBalance'),?,?,?,?,?,?,?,?,?)";

	public static final String UPDATE_LEAVEOPENINGBALANCE_QUERY = "UPDATE egeis_leaveOpeningBalance"
			+ " SET employeeId=?, calendarYear=?, leaveTypeId=?, noOfDays=?,"
			+ " lastModifiedBy=?, lastModifiedDate=?, tenantId=? where id=? and tenantid=? ";

	public static final String UPDATE_CARRYFORWARDLOB_QUERY = "UPDATE egeis_leaveOpeningBalance"
			+ " SET noOfDays=? where employeeId = ? and calendarYear=? and leaveTypeId=?  and tenantid=? ";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private LeaveOpeningBalanceRowMapper leaveOpeningBalanceRowMapper;

	@Autowired
	private LeaveOpeningBalanceQueryBuilder leaveOpeningBalanceQueryBuilder;

	@Autowired
	private UserService userService;

	public List<LeaveOpeningBalance> findForCriteria(LeaveOpeningBalanceGetRequest leaveOpeningBalanceGetRequest) {
		List<Object> preparedStatementValues = new ArrayList<Object>();
		String queryStr = leaveOpeningBalanceQueryBuilder.getQuery(leaveOpeningBalanceGetRequest,
				preparedStatementValues);
		List<LeaveOpeningBalance> leaveOpeningBalances = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(),
				leaveOpeningBalanceRowMapper);
		return leaveOpeningBalances;
	}

	public void create(LeaveOpeningBalanceRequest leaveOpeningBalanceRequest) {

		List<Object[]> batchArgs = new ArrayList<>();
		final UserResponse userResponse = userService
				.findUserByUserNameAndTenantId(leaveOpeningBalanceRequest.getRequestInfo());
		for (LeaveOpeningBalance leaveOpeningBalance : leaveOpeningBalanceRequest.getLeaveOpeningBalance()) {
			Object[] lobRecord = { leaveOpeningBalance.getEmployee(), leaveOpeningBalance.getCalendarYear(),
					leaveOpeningBalance.getLeaveType().getId(), leaveOpeningBalance.getNoOfDays(),
					userResponse.getUsers().get(0).getId(), new Date(System.currentTimeMillis()),
					userResponse.getUsers().get(0).getId(), new Date(System.currentTimeMillis()),
					leaveOpeningBalance.getTenantId() };
			batchArgs.add(lobRecord);
		}

		try {
			jdbcTemplate.batchUpdate(INSERT_LEAVEOPENINGBALANCE_QUERY, batchArgs);
		} catch (DataAccessException ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex.getMessage());
		}

	}

	public void update(LeaveOpeningBalanceRequest leaveOpeningBalanceRequest) {

		List<Object[]> batchArgs = new ArrayList<>();
		final UserResponse userResponse = userService
				.findUserByUserNameAndTenantId(leaveOpeningBalanceRequest.getRequestInfo());
		for (LeaveOpeningBalance leaveOpeningBalance : leaveOpeningBalanceRequest.getLeaveOpeningBalance()) {
			Object[] lobRecord = { leaveOpeningBalance.getEmployee(), leaveOpeningBalance.getCalendarYear(),
					leaveOpeningBalance.getLeaveType().getId(), leaveOpeningBalance.getNoOfDays(),
					userResponse.getUsers().get(0).getId(), new Date(System.currentTimeMillis()),
					leaveOpeningBalance.getTenantId(), leaveOpeningBalance.getId(), leaveOpeningBalance.getTenantId() };
			batchArgs.add(lobRecord);
		}

		try {
			jdbcTemplate.batchUpdate(UPDATE_LEAVEOPENINGBALANCE_QUERY, batchArgs);
		} catch (DataAccessException ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex.getMessage());
		}

	}

	public void updateLOBCarryForward(LeaveOpeningBalanceRequest leaveOpeningBalanceRequest) {

		List<Object[]> batchArgs = new ArrayList<>();
		final UserResponse userResponse = userService
				.findUserByUserNameAndTenantId(leaveOpeningBalanceRequest.getRequestInfo());
		for (LeaveOpeningBalance leaveOpeningBalance : leaveOpeningBalanceRequest.getLeaveOpeningBalance()) {
			Object[] lobRecord = { leaveOpeningBalance.getNoOfDays(), leaveOpeningBalance.getEmployee(),
					leaveOpeningBalance.getCalendarYear(), leaveOpeningBalance.getLeaveType().getId(),
					leaveOpeningBalance.getTenantId() };
			batchArgs.add(lobRecord);
		}

		try {
			jdbcTemplate.batchUpdate(UPDATE_CARRYFORWARDLOB_QUERY, batchArgs);
		} catch (DataAccessException ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex.getMessage());
		}
	}

	@SuppressWarnings("static-access")
	public boolean checkLOBExists(final Long empId, final Long leaveTypeId, final Integer year, final String tenantId) {
		final List<Object> preparedStatementValues = new ArrayList<Object>();
		preparedStatementValues.add(empId);
		preparedStatementValues.add(leaveTypeId);
		preparedStatementValues.add(year);
		preparedStatementValues.add(tenantId);

		final String query = leaveOpeningBalanceQueryBuilder.selectLeaveOpeningBalanceQuery();

		final List<Map<String, Object>> lob = jdbcTemplate.queryForList(query,
				preparedStatementValues.toArray());
		if (lob.isEmpty())
			return false;

		return true;
	}

}
