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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.eis.model.ServiceHistory;
import org.egov.eis.model.enums.EntityType;
import org.egov.eis.repository.rowmapper.ServiceHistoryTableRowMapper;
import org.egov.eis.web.contract.EmployeeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Component
public class ServiceHistoryRepository {

	public static final Logger LOGGER = LoggerFactory.getLogger(ServiceHistoryRepository.class);

	public static final String INSERT_SERVICE_HISTORY_QUERY = "INSERT INTO egeis_serviceHistory"
			+ " (id, employeeId, serviceInfo, serviceFrom, serviceTo, remarks, orderNo, city, department, designation, positionId,"
			+ " assignmentId, isAssignmentBased, createdBy, createdDate, lastModifiedBy, lastModifiedDate, tenantId)"
			+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String UPDATE_SERVICE_HISTORY_QUERY = "UPDATE egeis_serviceHistory"
			+ " SET (serviceInfo, serviceFrom, serviceTo, remarks, orderNo, city, department, designation, positionId, assignmentId, isAssignmentBased, lastModifiedBy, lastModifiedDate)"
			+ " = (?,?,?,?,?,?,?,?,?,?,?,?,?) where id = ? and tenantId=?";

	public static final String SELECT_BY_EMPLOYEEID_QUERY = "SELECT"
			+ " id, serviceinfo, servicefrom, serviceto, remarks, orderno, city, department, designation, positionId, assignmentId, isAssignmentBased, createdby, createddate,"
			+ " lastmodifiedby, lastmodifieddate, tenantid" + " FROM egeis_servicehistory"
			+ " WHERE employeeId = ? AND tenantId = ? ";

	public static final String DELETE_QUERY = "DELETE FROM egeis_servicehistory"
			+ " WHERE id IN (:id) AND employeeId = :employeeId AND tenantId = :tenantId";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private EmployeeDocumentsRepository documentsRepository;

	@Autowired
	private ServiceHistoryTableRowMapper serviceHistoryTableRowMapper;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	/**
	 * @param namedParameterJdbcTemplate
	 *            the namedParameterJdbcTemplate to set
	 */
	public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	public void save(EmployeeRequest employeeRequest) {
		List<ServiceHistory> serviceHistories = employeeRequest.getEmployee().getServiceHistory();

		jdbcTemplate.batchUpdate(INSERT_SERVICE_HISTORY_QUERY, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ServiceHistory serviceHistory = serviceHistories.get(i);
				ps.setLong(1, serviceHistory.getId());
				ps.setLong(2, employeeRequest.getEmployee().getId());
				ps.setString(3, serviceHistory.getServiceInfo());
				ps.setDate(4, new Date(serviceHistory.getServiceFrom().getTime()));
				ps.setDate(5, new Date(serviceHistory.getServiceTo().getTime()));
				ps.setString(6, serviceHistory.getRemarks());
				ps.setString(7, serviceHistory.getOrderNo());
				ps.setString(8, serviceHistory.getCity());
				ps.setString(9, serviceHistory.getDepartment());
				ps.setString(10, serviceHistory.getDesignation());
				ps.setLong(11,serviceHistory.getPosition() == null ? 0L : serviceHistory.getPosition());
				ps.setLong(12,serviceHistory.getAssignmentId() == null ? 0L : serviceHistory.getAssignmentId());
				ps.setBoolean(13, serviceHistory.getIsAssignmentBased());
				ps.setLong(14, employeeRequest.getRequestInfo().getUserInfo().getId());
				ps.setTimestamp(15, new Timestamp(new java.util.Date().getTime()));
				ps.setLong(16, employeeRequest.getRequestInfo().getUserInfo().getId());
				ps.setTimestamp(17, new Timestamp(new java.util.Date().getTime()));
				ps.setString(18, serviceHistory.getTenantId());

				if (serviceHistory.getDocuments() != null && !serviceHistory.getDocuments().isEmpty()) {
					documentsRepository.save(employeeRequest.getEmployee().getId(), serviceHistory.getDocuments(),
							EntityType.SERVICE.toString(), serviceHistory.getId(),
							serviceHistory.getTenantId());
				}
			}

			@Override
			public int getBatchSize() {
				return serviceHistories.size();
			}
		});
	}

	public void update(ServiceHistory service) {
		Object[] obj = new Object[] { service.getServiceInfo(), service.getServiceFrom(), service.getServiceTo(), service.getRemarks(),
				service.getOrderNo(), service.getCity(), service.getDepartment(), service.getDesignation(), service.getPosition(),
				service.getAssignmentId(), service.getIsAssignmentBased(),
				service.getLastModifiedBy(), service.getLastModifiedDate(), service.getId(), service.getTenantId() };

		jdbcTemplate.update(UPDATE_SERVICE_HISTORY_QUERY, obj);

	}

	public void insert(ServiceHistory service, Long empId) {
		Object[] obj = new Object[] {

				service.getId(), empId, service.getServiceInfo(), service.getServiceFrom(), service.getServiceTo(), service.getRemarks(),
				service.getOrderNo(), service.getCity(), service.getDepartment(), service.getDesignation(), service.getPosition(),
				service.getAssignmentId(), service.getIsAssignmentBased(),service.getCreatedBy(), service.getCreatedDate(),
				service.getLastModifiedBy(), service.getLastModifiedDate(), service.getTenantId() };

		jdbcTemplate.update(INSERT_SERVICE_HISTORY_QUERY, obj);
	}

	public List<ServiceHistory> findByEmployeeId(Long id, String tenantId) {
		try {
			return jdbcTemplate.query(SELECT_BY_EMPLOYEEID_QUERY, new Object[] { id, tenantId },
					serviceHistoryTableRowMapper);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public void delete(List<Long> servicesIdsToDelete, Long employeeId, String tenantId) {
		Map<String, Object> namedParameters = new HashMap<>();
		namedParameters.put("id", servicesIdsToDelete);
		namedParameters.put("employeeId", employeeId);
		namedParameters.put("tenantId", tenantId);

		namedParameterJdbcTemplate.update(DELETE_QUERY, namedParameters);
	}

}