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

import org.egov.eis.model.APRDetail;
import org.egov.eis.model.enums.EntityType;
import org.egov.eis.repository.rowmapper.APRDetailTableRowMapper;
import org.egov.eis.web.contract.EmployeeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.util.ObjectUtils.isEmpty;

@Component
public class APRDetailRepository {

    public static final String SELECT_BY_EMPLOYEEID_QUERY = "SELECT id, yearOfSubmission, detailsSubmitted,"
            + " dateOfSubmission, remarks, createdBy, createdDate, lastModifiedBy, lastModifiedDate, tenantId"
            + " FROM egeis_aprDetails WHERE employeeId = ? AND tenantId = ?";

    public static final String INSERT_APR_DETAILS_QUERY = "INSERT INTO egeis_aprDetails"
            + " (id, employeeId, yearOfSubmission, detailsSubmitted, dateOfSubmission, remarks, createdBy, createdDate,"
            + " lastModifiedBy, lastModifiedDate, tenantId) VALUES (?,?,?,?,?,?,?,?,?,?,?)";

    public static final String UPDATE_APR_DETAILS_QUERY = "UPDATE egeis_aprDetails"
            + " SET (yearOfSubmission, detailsSubmitted, dateOfSubmission, remarks, lastModifiedBy, lastModifiedDate)"
            + " = (?,?,?,?,?,?) WHERE id = ? and tenantId = ?";

    public static final String DELETE_QUERY = "DELETE FROM egeis_aprDetails"
            + " WHERE id IN (:id) AND employeeId = :employeeId AND tenantId = :tenantId";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EmployeeDocumentsRepository documentsRepository;

    @Autowired
    private APRDetailTableRowMapper aprDetailRowMapper;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /**
     * @param namedParameterJdbcTemplate
     */
    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public void save(EmployeeRequest employeeRequest) {
        List<APRDetail> aprDetails = employeeRequest.getEmployee().getAprDetails();
        jdbcTemplate.batchUpdate(INSERT_APR_DETAILS_QUERY, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                APRDetail aprDetail = aprDetails.get(i);
                ps.setLong(1, aprDetail.getId());
                ps.setLong(2, employeeRequest.getEmployee().getId());
                ps.setLong(3, aprDetail.getYearOfSubmission());
                ps.setBoolean(4, aprDetail.getDetailsSubmitted());
                ps.setDate(5, isEmpty(aprDetail.getDateOfSubmission()) ? null
                        : new Date(aprDetail.getDateOfSubmission().getTime()));
                ps.setString(6, aprDetail.getRemarks());
                ps.setLong(7, employeeRequest.getRequestInfo().getUserInfo().getId());
                ps.setTimestamp(8, new Timestamp(new java.util.Date().getTime()));
                ps.setLong(9, employeeRequest.getRequestInfo().getUserInfo().getId());
                ps.setTimestamp(10, new Timestamp(new java.util.Date().getTime()));
                ps.setString(11, aprDetail.getTenantId());

                if (aprDetail.getDocuments() != null && !aprDetail.getDocuments().isEmpty()) {
                    documentsRepository.save(employeeRequest.getEmployee().getId(), aprDetail.getDocuments(),
                            EntityType.APR_DETAILS.toString(), aprDetail.getId(), aprDetail.getTenantId());
                }
            }

            @Override
            public int getBatchSize() {
                return aprDetails.size();
            }
        });
    }

    public void update(APRDetail aprDetail) {
        Object[] obj = new Object[]{
                aprDetail.getYearOfSubmission(), aprDetail.getDetailsSubmitted(), aprDetail.getDateOfSubmission(),
                aprDetail.getRemarks(), aprDetail.getLastModifiedBy(), aprDetail.getLastModifiedDate(),
                aprDetail.getId(), aprDetail.getTenantId()};

        jdbcTemplate.update(UPDATE_APR_DETAILS_QUERY, obj);
    }

    public void insert(APRDetail aprDetail, Long empId) {
        Object[] obj = new Object[]{aprDetail.getId(), empId, aprDetail.getYearOfSubmission(),
                aprDetail.getDetailsSubmitted(), aprDetail.getDateOfSubmission(), aprDetail.getRemarks(),
                aprDetail.getCreatedBy(), aprDetail.getCreatedDate(), aprDetail.getLastModifiedBy(),
                aprDetail.getLastModifiedDate(), aprDetail.getTenantId()};

        jdbcTemplate.update(INSERT_APR_DETAILS_QUERY, obj);
    }

    public List<APRDetail> findByEmployeeId(Long id, String tenantId) {
        try {
            return jdbcTemplate.query(SELECT_BY_EMPLOYEEID_QUERY, new Object[]{id, tenantId}, aprDetailRowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void delete(List<Long> aprDetailsIdsToDelete, Long employeeId, String tenantId) {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("id", aprDetailsIdsToDelete);
        namedParameters.put("employeeId", employeeId);
        namedParameters.put("tenantId", tenantId);

        namedParameterJdbcTemplate.update(DELETE_QUERY, namedParameters);
    }
}