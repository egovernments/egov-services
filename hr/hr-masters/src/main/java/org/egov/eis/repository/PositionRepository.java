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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.eis.model.Position;
import org.egov.eis.repository.builder.PositionQueryBuilder;
import org.egov.eis.repository.rowmapper.IdsRowMapper;
import org.egov.eis.repository.rowmapper.PositionRowMapper;
import org.egov.eis.service.DepartmentDesignationService;
import org.egov.eis.web.contract.PositionGetRequest;
import org.egov.eis.web.contract.PositionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class PositionRepository {

	public static final String INSERT_POSITION_QUERY = "INSERT INTO egeis_position"
			+ " (id, name, deptDesigId, isPostOutsourced, active, tenantId)" + " VALUES (?, ?, ?, ?, ?, ?)";

	public static final String UPDATE_POSITION_QUERY = "UPDATE egeis_position SET active = ?, isPostOutsourced = ?"
			+ " WHERE id = ? AND tenantId = ?";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private PositionRowMapper positionRowMapper;

	@Autowired
	private IdsRowMapper idsRowMapper;

	@Autowired
	private PositionQueryBuilder positionQueryBuilder;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	private DepartmentDesignationService departmentDesignationService;

	public List<Position> findForCriteria(PositionGetRequest positionGetRequest) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String queryStr = positionQueryBuilder.getQuery(positionGetRequest, preparedStatementValues);
		List<Position> positions = namedParameterJdbcTemplate.query(queryStr, preparedStatementValues,
				positionRowMapper);
		return positions;
	}

	public List<Long> generateSequences(Integer noOfPositions) {
		return jdbcTemplate.query("SELECT nextval('seq_egeis_position') AS id FROM generate_series(1, ?)",
				new Object[] { noOfPositions }, idsRowMapper);
	}

	public void create(PositionRequest positionRequest) {
		List<Object[]> batchArgs = new ArrayList<>();

		for (Position position : positionRequest.getPosition()) {
			Long deptDesigId = position.getDeptdesig().getId();
			Object[] positionRecord = { position.getId(), position.getName(), deptDesigId,
					position.getIsPostOutsourced(), position.getActive(), position.getTenantId() };
			batchArgs.add(positionRecord);
		}

		try {
			jdbcTemplate.batchUpdate(INSERT_POSITION_QUERY, batchArgs);
		} catch (DataAccessException ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex.getMessage());
		}
	}

	public void update(PositionRequest positionRequest) {
		List<Object[]> batchArgs = new ArrayList<>();

		for (Position position : positionRequest.getPosition()) {
			Object[] positionRecord = { position.getActive(), position.getIsPostOutsourced(), position.getId(),
					position.getTenantId() };
			batchArgs.add(positionRecord);
		}

		try {
			jdbcTemplate.batchUpdate(UPDATE_POSITION_QUERY, batchArgs);
		} catch (DataAccessException ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex.getMessage());
		}
	}

	public Integer getTotalDBRecords(PositionGetRequest positionGetRequest) {
		Map<String, Object> namedParamsForMatchingRecords = new HashMap<>();
		String queryStrForMatchingRecords = positionQueryBuilder.getQuery(positionGetRequest,
				namedParamsForMatchingRecords);

		String queryForTotalMatchingRecords = "SELECT count(DISTINCT p_id) FROM (" + queryStrForMatchingRecords
				+ ") AS pos";
		log.debug("queryForTotalMatchingRecords :: " + queryForTotalMatchingRecords);
		return namedParameterJdbcTemplate.queryForObject(queryForTotalMatchingRecords, namedParamsForMatchingRecords,
				Integer.class);
	}

	public String generatePositionNameWithMultiplePosition(String name, Long deptDesigId, String tenantId, int index) {
		String seqQuery = "SELECT '" + name + "'::TEXT || LPAD((count(id))::TEXT, 3, '0') FROM egeis_position"
				+ " WHERE deptDesigId = " + deptDesigId + " AND tenantId = '" + tenantId + "'";

		String result = String.valueOf(jdbcTemplate.queryForObject(seqQuery, String.class));
		Integer integer = Integer.parseInt(result.split("_")[2]) + index;

		if (integer < 10)
			result = result.split("_")[0] + "_" + result.split("_")[1] + "_00" + integer;
		else if (integer >= 10 && integer < 100)
			result = result.split("_")[0] + "_" + result.split("_")[1] + "_0" + integer;
		else
			result = result.split("_")[0] + "_" + result.split("_")[1] + "_" + integer;
		return result;
	}

}