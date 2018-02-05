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

import org.egov.eis.model.LeaveType;
import org.egov.eis.repository.builder.LeaveTypeQueryBuilder;
import org.egov.eis.repository.rowmapper.LeaveTypeRowMapper;
import org.egov.eis.web.contract.LeaveTypeGetRequest;
import org.egov.eis.web.contract.LeaveTypeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class LeaveTypeRepository {

	public static final Logger LOGGER = LoggerFactory.getLogger(LeaveTypeRepository.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private LeaveTypeRowMapper leaveTypeRowMapper;

	@Autowired
	private LeaveTypeQueryBuilder leaveTypeQueryBuilder;

	public List<LeaveType> findForCriteria(LeaveTypeGetRequest leaveTypeGetRequest) {
		List<Object> preparedStatementValues = new ArrayList<Object>();
		String queryStr = leaveTypeQueryBuilder.getQuery(leaveTypeGetRequest, preparedStatementValues);
		List<LeaveType> leaveTypes = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(),
				leaveTypeRowMapper);
		return leaveTypes;
	}

	@SuppressWarnings("static-access")
	public LeaveTypeRequest createLeaveType(final LeaveTypeRequest leaveTypeRequest) {

		LOGGER.info("LeaveTypeRequest::" + leaveTypeRequest);
		final String leaveTypeInsert = leaveTypeQueryBuilder.insertLeaveTypeQuery();
		saveLeaveType(leaveTypeRequest, leaveTypeInsert);
		return leaveTypeRequest;
	}

	@SuppressWarnings("static-access")
	public LeaveTypeRequest updateLeaveType(final LeaveTypeRequest leaveTypeRequest) {

		LOGGER.info("LeaveTypeRequest::" + leaveTypeRequest);
		final String leaveTypeUpdate = leaveTypeQueryBuilder.updateLeaveTypeQuery();
		saveLeaveType(leaveTypeRequest, leaveTypeUpdate);
		return leaveTypeRequest;

	}

	public LeaveTypeRequest saveLeaveType(final LeaveTypeRequest leaveTypeRequest, final String leaveTypeQuery) {
		try {
			jdbcTemplate.batchUpdate(leaveTypeQuery, new BatchPreparedStatementSetter() {

				@Override
				public void setValues(final PreparedStatement ps, final int i) throws SQLException {
					final LeaveType leaveType = ((List<LeaveType>) leaveTypeRequest.getLeaveType()).get(i);
					ps.setString(1, leaveType.getName());
					ps.setString(2, leaveType.getDescription());
					ps.setBoolean(3, leaveType.getHalfdayAllowed());
					ps.setBoolean(4, leaveType.getPayEligible());
					ps.setBoolean(5, leaveType.getAccumulative());
					ps.setBoolean(6, leaveType.getEncashable());
					ps.setBoolean(7, leaveType.getEncloseHoliday());
					ps.setBoolean(8, leaveType.getIncludePrefixSuffix());
					ps.setInt(9, leaveType.getMaxDays());
					ps.setBoolean(10, leaveType.getActive() == null ? true : leaveType.getActive());
					ps.setLong(11, Long.valueOf(leaveTypeRequest.getRequestInfo().getMsgId()));
					ps.setLong(12, Long.valueOf(leaveTypeRequest.getRequestInfo().getMsgId()));
					if (leaveType.getId() != null) {
						ps.setDate(13, new Date(leaveType.getCreatedDate().getTime()));
						ps.setDate(14, new Date(leaveType.getLastModifiedDate().getTime()));
						ps.setString(15, leaveType.getTenantId());
						ps.setLong(16, leaveType.getId());
						ps.setString(17, leaveType.getTenantId());
					} else {
						ps.setDate(13, new Date(new java.util.Date().getTime()));
						ps.setDate(14, new Date(new java.util.Date().getTime()));
						ps.setString(15, leaveType.getTenantId());

					}
				}

				@Override
				public int getBatchSize() {
					return leaveTypeRequest.getLeaveType().size();
				}
			});
		} catch (final DataAccessException ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex.getMessage());
		}

		return leaveTypeRequest;

	}

	@SuppressWarnings("static-access")
	public boolean checkLeaveTypeByName(final Long id, final String name, final String tenantId) {
		final List<Object> preparedStatementValues = new ArrayList<Object>();
		preparedStatementValues.add(name.toUpperCase());
		preparedStatementValues.add(tenantId);
		final String query;
		if (id == null)
			query = leaveTypeQueryBuilder.selectLeaveTypeByNameQuery();
		else {
			preparedStatementValues.add(id);
			query = leaveTypeQueryBuilder.selectLeaveTypeByNameAndIdNotInQuery();
		}
		final List<Map<String, Object>> leaveTypes = jdbcTemplate.queryForList(query,
				preparedStatementValues.toArray());
		if (leaveTypes.isEmpty())
			return false;

		return true;
	}
}