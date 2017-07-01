
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
package org.egov.wcms.repository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.egov.wcms.model.PropertyTypePipeSize;
import org.egov.wcms.repository.builder.PropertyPipeSizeQueryBuilder;
import org.egov.wcms.repository.rowmapper.PropertyPipeSizeRowMapper;
import org.egov.wcms.web.contract.PropertyTypePipeSizeGetRequest;
import org.egov.wcms.web.contract.PropertyTypePipeSizeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PropertyPipeSizeRepository {

    public static final Logger LOGGER = LoggerFactory.getLogger(PropertyPipeSizeRepository.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PropertyPipeSizeRowMapper propertyPipeSizeRowMapper;

    @Autowired
    private PropertyPipeSizeQueryBuilder propertyPipeSizeQueryBuilder;

    public PropertyTypePipeSizeRequest persistCreatePropertyPipeSize(
            final PropertyTypePipeSizeRequest propertyPipeSizeRequest) {
        LOGGER.info("PropertyPipeSizeRequest::" + propertyPipeSizeRequest);
        final String propertyPipeSizeInsert = PropertyPipeSizeQueryBuilder.insertPropertyPipeSizeQuery();
        final PropertyTypePipeSize propertyPipeSize = propertyPipeSizeRequest.getPropertyPipeSize();
        final String pipesizeQuery = PropertyPipeSizeQueryBuilder.getPipeSizeIdQuery();
        Long pipesizeId = 0L;
        try {
            pipesizeId = jdbcTemplate.queryForObject(pipesizeQuery,
                    new Object[] { propertyPipeSize.getPipeSize(), propertyPipeSize.getTenantId() },
                    Long.class);
        } catch (final EmptyResultDataAccessException e) {
            LOGGER.info("EmptyResultDataAccessException: Query returned empty result set");
        }
        if (pipesizeId == null)
            LOGGER.info("Invalid input.");

        final Object[] obj = new Object[] { pipesizeId, propertyPipeSizeRequest.getPropertyPipeSize().getPropertyTypeId(),
                propertyPipeSize.getActive(),
                Long.valueOf(propertyPipeSizeRequest.getRequestInfo().getUserInfo().getId()),
                Long.valueOf(propertyPipeSizeRequest.getRequestInfo().getUserInfo().getId()),
                new Date(new java.util.Date().getTime()), new Date(new java.util.Date().getTime()),
                propertyPipeSize.getTenantId() };
        jdbcTemplate.update(propertyPipeSizeInsert, obj);
        return propertyPipeSizeRequest;
    }

    public PropertyTypePipeSizeRequest persistUpdatePropertyPipeSize(
            final PropertyTypePipeSizeRequest propertyPipeSizeRequest) {
        LOGGER.info("PropertyPipeSizeRequest::" + propertyPipeSizeRequest);
        final String propertyPipeSizeUpdate = PropertyPipeSizeQueryBuilder.updatePropertyPipeSizeQuery();
        final PropertyTypePipeSize propertyPipeSize = propertyPipeSizeRequest.getPropertyPipeSize();
        final String pipesizeQuery = PropertyPipeSizeQueryBuilder.getPipeSizeIdQuery();
        Long pipesizeId = 0L;
        try {
            pipesizeId = jdbcTemplate.queryForObject(pipesizeQuery,
                    new Object[] { propertyPipeSize.getPipeSize(), propertyPipeSize.getTenantId() },
                    Long.class);
        } catch (final EmptyResultDataAccessException e) {
            LOGGER.info("EmptyResultDataAccessException: Query returned empty result set while update");
        }
        if (pipesizeId == null)
            LOGGER.info("Invalid input.");

        final Object[] obj = new Object[] { pipesizeId, propertyPipeSize.getPropertyTypeId(),
                propertyPipeSize.getActive(),
                Long.valueOf(propertyPipeSizeRequest.getRequestInfo().getUserInfo().getId()),
                new Date(new java.util.Date().getTime()), propertyPipeSize.getId() };
        jdbcTemplate.update(propertyPipeSizeUpdate, obj);
        return propertyPipeSizeRequest;
    }

    public boolean checkPropertyByPipeSize(final Long id, final String propertyTypeId, final Long pipeSizeId,
            final String tenantId) {
        final List<Object> preparedStatementValues = new ArrayList<>();
        preparedStatementValues.add(propertyTypeId);
        preparedStatementValues.add(pipeSizeId);
        preparedStatementValues.add(tenantId);
        final String query;
        if (id == null)
            query = propertyPipeSizeQueryBuilder.selectPropertyByPipeSizeQuery();
        else {
            preparedStatementValues.add(id);
            query = propertyPipeSizeQueryBuilder.selectPropertyByPipeSizeNotInQuery();
        }
        final List<Map<String, Object>> propertyPipeSizes = jdbcTemplate.queryForList(query,
                preparedStatementValues.toArray());
        if (!propertyPipeSizes.isEmpty())
            return false;

        return true;
    }
    
    

    public List<PropertyTypePipeSize> findForCriteria(final PropertyTypePipeSizeGetRequest propertyPipeSizeGetRequest) {
        final List<Object> preparedStatementValues = new ArrayList<>();
        try {
            if (propertyPipeSizeGetRequest.getPipeSize() != null)
                propertyPipeSizeGetRequest
                        .setPipeSizeId(jdbcTemplate.queryForObject(PropertyPipeSizeQueryBuilder.getPipeSizeIdQuery(),
                                new Object[] { propertyPipeSizeGetRequest.getPipeSize(),
                                        propertyPipeSizeGetRequest.getTenantId() },
                                Long.class));
        } catch (final EmptyResultDataAccessException e) {
            LOGGER.error("EmptyResultDataAccessException: Query returned empty RS.");

        }

        final String queryStr = propertyPipeSizeQueryBuilder.getQuery(propertyPipeSizeGetRequest, preparedStatementValues);
        final String pipeSizeInmmQuery = PropertyPipeSizeQueryBuilder.getPipeSizeInmm();
        final List<PropertyTypePipeSize> propertyPipeSizes = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(),
                propertyPipeSizeRowMapper);
        for (final PropertyTypePipeSize propertyPipeSize : propertyPipeSizes)
            propertyPipeSize.setPipeSize(jdbcTemplate.queryForObject(pipeSizeInmmQuery,
                    new Object[] { propertyPipeSize.getPipeSizeId(), propertyPipeSize.getTenantId() }, Double.class));
        return propertyPipeSizes;
    }

    public boolean checkPipeSizeExists(final Double pipeSize, final String tenantId) {
        final List<Object> preparedStatementValues = new ArrayList<>();
        preparedStatementValues.add(pipeSize);
        preparedStatementValues.add(tenantId);
        final String query = PropertyPipeSizeQueryBuilder.getPipeSizeIdQuery();
        final List<Map<String, Object>> pipeSizes = jdbcTemplate.queryForList(query,
                preparedStatementValues.toArray());
        if (!pipeSizes.isEmpty())
            return false;

        return true;
    }
}
