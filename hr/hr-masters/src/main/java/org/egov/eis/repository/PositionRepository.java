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
import java.util.List;

import org.egov.eis.model.DepartmentDesignation;
import org.egov.eis.model.Position;
import org.egov.eis.repository.builder.PositionQueryBuilder;
import org.egov.eis.repository.rowmapper.PositionRowMapper;
import org.egov.eis.service.DepartmentDesignationService;
import org.egov.eis.web.contract.PositionGetRequest;
import org.egov.eis.web.contract.PositionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PositionRepository {

	public static final String INSERT_POSITION_QUERY = "INSERT INTO egeis_position"
			+ " (id, name, deptdesigId, isPostOutsourced, active,tenantId)"
			+ " VALUES (nextval('seq_egeis_position'),?,?,?,?,?)";

	public static final String UPDATE_POSITION_QUERY = "UPDATE egeis_position"
			+ " SET  name=?, deptdesigId=?, isPostOutsourced=?, active=? where id=? and tenantid=? ";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private PositionRowMapper positionRowMapper;

	@Autowired
	private PositionQueryBuilder positionQueryBuilder;

	@Autowired
	private DepartmentDesignationService departmentDesignationService;

	public List<Position> findForCriteria(PositionGetRequest positionGetRequest) {
		List<Object> preparedStatementValues = new ArrayList<Object>();
		String queryStr = positionQueryBuilder.getQuery(positionGetRequest, preparedStatementValues);
		List<Position> positions = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), positionRowMapper);
		return positions;
	}

	public void create(PositionRequest positionRequest) {

		List<Object[]> batchArgs = new ArrayList<>();

		for (Position position : positionRequest.getPosition()) {
			DepartmentDesignation deptDesg = null;
			if (position.getDeptdesig() != null && position.getDeptdesig().getDepartmentId() != null
					&& position.getDeptdesig().getDesignation() != null
					&& position.getDeptdesig().getDesignation().getId() != null) {
				deptDesg = departmentDesignationService.getByDepartmentAndDesignation(
						position.getDeptdesig().getDepartmentId(), position.getDeptdesig().getDesignation().getId());
			}
			if (deptDesg == null) {
				position.getDeptdesig().setTenantId(position.getTenantId());
				departmentDesignationService.create(position.getDeptdesig());
				deptDesg = departmentDesignationService.getByDepartmentAndDesignation(
						position.getDeptdesig().getDepartmentId(), position.getDeptdesig().getDesignation().getId());
			}
			Object[] positionRecord = { position.getName(), deptDesg.getId(), position.getIsPostOutsourced(),
					position.getActive(), position.getTenantId() };
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
			DepartmentDesignation deptDesg = null;
			if (position.getDeptdesig() != null && position.getDeptdesig().getDepartmentId() != null
					&& position.getDeptdesig().getDesignation() != null
					&& position.getDeptdesig().getDesignation().getId() != null) {
				deptDesg = departmentDesignationService.getByDepartmentAndDesignation(
						position.getDeptdesig().getDepartmentId(), position.getDeptdesig().getDesignation().getId());
			}
			if (deptDesg == null) {
				departmentDesignationService.create(position.getDeptdesig());
				deptDesg = departmentDesignationService.getByDepartmentAndDesignation(
						position.getDeptdesig().getDepartmentId(), position.getDeptdesig().getDesignation().getId());
			}
			Object[] positionRecord = { position.getName(), deptDesg.getId(), position.getIsPostOutsourced(),
					position.getActive(), position.getId(), position.getTenantId() };
			batchArgs.add(positionRecord);
		}

		try {
			jdbcTemplate.batchUpdate(UPDATE_POSITION_QUERY, batchArgs);
		} catch (DataAccessException ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex.getMessage());
		}
	}
}