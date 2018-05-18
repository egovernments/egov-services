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
import org.egov.eis.model.Assignment;
import org.egov.eis.model.Employee;
import org.egov.eis.model.PayscaleDetails;
import org.egov.eis.model.PayscaleHeader;
import org.egov.eis.model.enums.EntityType;
import org.egov.eis.repository.builder.PayscaleQueryBuilder;
import org.egov.eis.repository.rowmapper.PayscaleDetailsRowMapper;
import org.egov.eis.repository.rowmapper.PayscaleHeaderRowMapper;
import org.egov.eis.web.contract.EmployeeRequest;
import org.egov.eis.web.contract.PayscaleGetRequest;
import org.egov.eis.web.contract.PayscaleRequest;
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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class PayscaleRepository {

    public static final Logger LOGGER = LoggerFactory.getLogger(PayscaleRepository.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PayscaleQueryBuilder payscaleQueryBuilder;

    @Autowired
    private PayscaleDetailsRowMapper payscaleDetailsRowMapper;

    @Autowired
    private PayscaleHeaderRowMapper payscaleHeaderRowMapper;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public static final String INSERT_PAYSCALEHEADER_QUERY = "INSERT INTO egeis_payscaleheader"
            + " (id, paycommission, payscale, amountfrom, amountto, "
            + " createdby, createddate, lastmodifiedby, lastmodifieddate, tenantId)"
            + " VALUES (?,?,?,?,?,?,?,?,?,?)";

    public static final String INSERT_PAYSCALEDETAILS_QUERY = "INSERT INTO egeis_payscaledetails"
            + " (id, payscaleheaderid, basicfrom, basicto, increment, tenantId)"
            + " VALUES (?,?,?,?,?,?)";

    public static final String UPDATE_PAYSCALEHEADER_QUERY = "UPDATE egeis_payscaleheader"
            + " SET (paycommission, payscale, amountfrom, amountto, lastmodifiedby, lastmodifieddate)"
            + " = (?,?,?,?,?,?) WHERE id = ? AND tenantId=?";

    public static final String UPDATE_PAYSCALEDETAILS_QUERY = "UPDATE egeis_payscaledetails"
            + " SET (basicfrom, basicto, increment )"
            + " = (?,?,?) WHERE id = ? AND tenantId=?";

    public static final String DELETE_PAYSCALEDEAILS_QUERY = "DELETE FROM egeis_payscaledetails"
            + " WHERE id IN (:id) AND payscaleheaderid=:payscaleHeaderId AND tenantId=:tenantId";

    public static final String SELECT_BY_PAYSCALEHEADERID_QUERY = "SELECT"
            + " id, payscaleHeaderId, basicFrom, basicTo, increment, "
            + " tenantId FROM egeis_payscaledetails WHERE payscaleheaderId = ? AND tenantId = ? ";

    public Long generateSequence(String sequenceName) {
        return jdbcTemplate.queryForObject("SELECT nextval('" + sequenceName + "')", Long.class);
    }

    public void savePayscaleHeader(PayscaleHeader payscaleHeader) {
        Object[] obj = new Object[]{payscaleHeader.getId(), payscaleHeader.getPaycommission(),
                payscaleHeader.getPayscale(), payscaleHeader.getAmountFrom(), payscaleHeader.getAmountTo(),
                payscaleHeader.getCreatedBy(), new java.util.Date(), payscaleHeader.getLastModifiedBy(),
                new java.util.Date(), payscaleHeader.getTenantId()};

        jdbcTemplate.update(INSERT_PAYSCALEHEADER_QUERY, obj);
    }

    public List<PayscaleDetails> findByPayscaleHeaderId(Long id, String tenantId) {
        try {
            return jdbcTemplate.query(SELECT_BY_PAYSCALEHEADERID_QUERY, new Object[]{id, tenantId},
                    payscaleDetailsRowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<PayscaleHeader> findForCriteria(final PayscaleGetRequest payscaleGetRequest,
                                                final RequestInfo requestInfo) {
        final List<Object> preparedStatementValues = new ArrayList<Object>();
        final String queryStr = payscaleQueryBuilder.getQuery(payscaleGetRequest,
                preparedStatementValues, requestInfo);
        final List<PayscaleHeader> payscaleHeaders = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(),
                payscaleHeaderRowMapper);
        return payscaleHeaders;
    }

    public void savePayscaleDetails(PayscaleHeader payscaleHeader) {
        List<PayscaleDetails> payscaleDetails = payscaleHeader.getPayscaleDetails();

        jdbcTemplate.batchUpdate(INSERT_PAYSCALEDETAILS_QUERY, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                PayscaleDetails payscaleDet = payscaleDetails.get(i);
                ps.setLong(1, payscaleDet.getId());
                ps.setLong(2, payscaleHeader.getId());
                ps.setLong(3, payscaleDet.getBasicFrom());
                ps.setLong(4, payscaleDet.getBasicTo());
                ps.setLong(5, payscaleDet.getIncrement());
                ps.setString(6, payscaleDet.getTenantId());
            }

            @Override
            public int getBatchSize() {
                return payscaleDetails.size();
            }
        });
    }

    public void insertPayscaleDetail(PayscaleDetails payscaleDetails) {

        Object[] obj = new Object[]{payscaleDetails.getId(), payscaleDetails.getPayscaleHeaderId(), payscaleDetails.getBasicFrom(),
                payscaleDetails.getBasicTo(), payscaleDetails.getIncrement(), payscaleDetails.getTenantId()};

        jdbcTemplate.update(INSERT_PAYSCALEDETAILS_QUERY, obj);
    }

    public void updatePayscaleHeader(PayscaleHeader payscaleHeader) {

        Object[] obj = new Object[]{payscaleHeader.getPaycommission(), payscaleHeader.getPayscale(), payscaleHeader.getAmountFrom(),
                payscaleHeader.getAmountTo(), payscaleHeader.getLastModifiedBy(), payscaleHeader.getLastModifiedDate(), payscaleHeader.getId(),
                payscaleHeader.getTenantId()};

        jdbcTemplate.update(UPDATE_PAYSCALEHEADER_QUERY, obj);
    }

    public void updatePayscaleDetails(PayscaleDetails payscaleDetails) {

        Object[] obj = new Object[]{payscaleDetails.getBasicFrom(), payscaleDetails.getBasicTo(), payscaleDetails.getIncrement(),
                payscaleDetails.getId(), payscaleDetails.getTenantId()};

        jdbcTemplate.update(UPDATE_PAYSCALEDETAILS_QUERY, obj);
    }

    public boolean checkIfPayscaleExists(final Long id, final String name, final String tenantId) {
        final List<Object> preparedStatementValues = new ArrayList<Object>();
        preparedStatementValues.add(name.toUpperCase());
        preparedStatementValues.add(tenantId);
        final String query;
        if (id == null) {
            query = payscaleQueryBuilder.selectPayscaleHeaderByNameQuery();
        } else {
            preparedStatementValues.add(id);
            query = payscaleQueryBuilder.selectPayscaleHeaderByNameAndIdNotInQuery();
        }

        final List<Map<String, Object>> payscaleHeaderList = jdbcTemplate.queryForList(query,
                preparedStatementValues.toArray());
        if (payscaleHeaderList.isEmpty())
            return false;

        return true;
    }

    public void deletePayscaleDetails(List<Long> payscaleDetIdsToDelete, Long payscaleHeaderId, String tenantId) {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("id", payscaleDetIdsToDelete);
        namedParameters.put("payscaleHeaderId", payscaleHeaderId);
        namedParameters.put("tenantId", tenantId);

        namedParameterJdbcTemplate.update(DELETE_PAYSCALEDEAILS_QUERY, namedParameters);
    }
}
