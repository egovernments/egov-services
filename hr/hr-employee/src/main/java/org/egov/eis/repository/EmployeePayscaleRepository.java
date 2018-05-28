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


import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.eis.model.EmployeePayscale;
import org.egov.eis.repository.builder.EmployeePayscaleQueryBuilder;
import org.egov.eis.repository.rowmapper.EmployeePayscaleRowMapper;
import org.egov.eis.web.contract.EmployeePayscaleGetRequest;
import org.egov.eis.web.contract.EmployeePayscaleRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class EmployeePayscaleRepository {

    public static final Logger LOGGER = LoggerFactory.getLogger(EmployeePayscaleRepository.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EmployeePayscaleQueryBuilder employeePayscaleQueryBuilder;

    @Autowired
    private EmployeePayscaleRowMapper employeePayscaleRowMapper;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public static final String INSERT_EMPLOYEEPAYSCALE_QUERY = "INSERT INTO egeis_employeepayscale"
            + " (id, employeeid, payscaleheaderid, effectivefrom, incrementmonth, basicamount, "
            + "  reason, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantId)"
            + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

    public static final String UPDATE_EMPLOYEEPAYSCALE_QUERY = "UPDATE egeis_employeepayscale"
            + " SET (effectivefrom, incrementmonth, basicamount, reason, lastmodifiedby, lastmodifieddate)"
            + " = (?,?,?,?,?,?) WHERE id = ? AND tenantId=?";

    public static final String DELETE_EMPLOYEEPAYSCALE_QUERY = "DELETE FROM egeis_employeepayscale"
            + " WHERE id IN (:id) AND employeeid=:employeeId AND tenantId=:tenantId";

    public void saveEmployeePayscale(EmployeePayscale employeePayscale) {
        Object[] obj = new Object[]{employeePayscale.getId(), employeePayscale.getEmployee().getId(),
                employeePayscale.getPayscaleHeader().getId(), employeePayscale.getEffectiveFrom(), employeePayscale.getIncrementMonth(),
                employeePayscale.getBasicAmount(), employeePayscale.getReason(),
                employeePayscale.getCreatedBy(), new java.util.Date(), employeePayscale.getLastModifiedBy(),
                new java.util.Date(), employeePayscale.getTenantId()};

        jdbcTemplate.update(INSERT_EMPLOYEEPAYSCALE_QUERY, obj);
    }

    public List<EmployeePayscale> findForCriteria(final EmployeePayscaleGetRequest employeePayscaleGetRequest,
                                                  final RequestInfo requestInfo) {
        final List<Object> preparedStatementValues = new ArrayList<Object>();
        final String queryStr = employeePayscaleQueryBuilder.getQuery(employeePayscaleGetRequest,
                preparedStatementValues, requestInfo);
        final List<EmployeePayscale> empPayscaleList = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(),
                employeePayscaleRowMapper);
        return empPayscaleList;
    }

    public void updateEmpPayscale(EmployeePayscale employeePayscale) {

        Object[] obj = new Object[]{employeePayscale.getEffectiveFrom(), employeePayscale.getIncrementMonth(), employeePayscale.getBasicAmount(),
                employeePayscale.getReason(), employeePayscale.getLastModifiedBy(), employeePayscale.getLastModifiedDate(),
                employeePayscale.getId(), employeePayscale.getTenantId()};

        jdbcTemplate.update(UPDATE_EMPLOYEEPAYSCALE_QUERY, obj);
    }

    public void deleteEmpPayscale(List<Long> empPayscaleIdsToDelete, Long employeeId, String tenantId) {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("id", empPayscaleIdsToDelete);
        namedParameters.put("employeeId", employeeId);
        namedParameters.put("tenantId", tenantId);

        namedParameterJdbcTemplate.update(DELETE_EMPLOYEEPAYSCALE_QUERY, namedParameters);
    }
}
