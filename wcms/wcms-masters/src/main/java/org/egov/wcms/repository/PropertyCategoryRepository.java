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

import org.egov.wcms.model.PropertyTypeCategoryType;
import org.egov.wcms.repository.builder.PropertyCategoryQueryBuilder;
import org.egov.wcms.repository.rowmapper.PropertyCategoryRowMapper;
import org.egov.wcms.web.contract.PropertyCategoryGetRequest;
import org.egov.wcms.web.contract.PropertyTypeCategoryTypeReq;
import org.egov.wcms.web.contract.PropertyTypeCategoryTypesRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PropertyCategoryRepository {

    public static final Logger LOGGER = LoggerFactory.getLogger(PropertyCategoryRepository.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PropertyCategoryQueryBuilder propertyCategoryueryBuilder;

    @Autowired
    private PropertyCategoryRowMapper propertyCategoryRowMapper;

    public PropertyTypeCategoryTypeReq persistCreatePropertyCategory(final PropertyTypeCategoryTypeReq propertyCategoryRequest) {
        LOGGER.info("PropertyCategoryRequest::" + propertyCategoryRequest);
        final String propertyCategoryInsert = propertyCategoryueryBuilder.insertPropertyCategoryQuery();

        // hit an API of Property Tax alongwith propertyType to fetch propertyId.
        // right now for functionality sake it has been hard coded to value 10.

        String categoryQuery = propertyCategoryueryBuilder.getCategoryId();
        Long categoryId = 0L;
        try {
            categoryId = jdbcTemplate.queryForObject(categoryQuery,
                    new Object[] { propertyCategoryRequest.getPropertyTypeCategoryType().getCategoryTypeName() }, Long.class);
            LOGGER.info("Category Id: " + categoryId);
        } catch (EmptyResultDataAccessException e) {
            LOGGER.info("EmptyResultDataAccessException: Query returned empty result set");
        }
        if (categoryId == null) {
            LOGGER.info("Invalid input.");
        }
        Object[] obj = new Object[] { 10, categoryId, propertyCategoryRequest.getPropertyTypeCategoryType().getActive(),
                propertyCategoryRequest.getPropertyTypeCategoryType().getTenantId(), new Date(new java.util.Date().getTime()),
                propertyCategoryRequest.getRequestInfo().getUserInfo().getId(), new Date(new java.util.Date().getTime()),
                propertyCategoryRequest.getRequestInfo().getUserInfo().getId() };
        try {
            jdbcTemplate.update(propertyCategoryInsert, obj);
        } catch (Exception e) {
            LOGGER.info("PSQLException: Foreign key constraint voilated.");
        }
        return propertyCategoryRequest;
    }

    public PropertyTypeCategoryTypeReq persistUpdatePropertyCategory(final PropertyTypeCategoryTypeReq propertyCategoryRequest) {
        LOGGER.info("PropertyCategoryRequest::" + propertyCategoryRequest);
        final String propertyCategoryUpdate = propertyCategoryueryBuilder.updatePropertyCategoryQuery();

        // hit an API of Property Tax alongwith propertyType to fetch propertyId.
        // right now for functionality sake it has been hard coded to value 10.

        String categoryQuery = propertyCategoryueryBuilder.getCategoryId();
        Long categoryId = 0L;
        try {
            categoryId = jdbcTemplate.queryForObject(categoryQuery,
                    new Object[] { propertyCategoryRequest.getPropertyTypeCategoryType().getCategoryTypeName() }, Long.class);
            LOGGER.info("Category Id: " + categoryId);
        } catch (EmptyResultDataAccessException e) {
            LOGGER.info("EmptyResultDataAccessException: Query returned empty result set");
        }
        if (categoryId == null) {
            LOGGER.info("Invalid input.");
        }
        Object[] obj = new Object[] { 10, categoryId, propertyCategoryRequest.getPropertyTypeCategoryType().getActive(),

                propertyCategoryRequest.getRequestInfo().getUserInfo().getId(), new Date(new java.util.Date().getTime()),
                propertyCategoryRequest.getPropertyTypeCategoryType().getId() };
        try {
            jdbcTemplate.update(propertyCategoryUpdate, obj);
        } catch (Exception e) {
            LOGGER.info("PSQLException: Foreign key constraint voilated.");
        }
        return propertyCategoryRequest;
    }

    public PropertyTypeCategoryTypesRes findForCriteria(PropertyCategoryGetRequest propertyCategoryRequest) {
        List<Object> preparedStatementValues = new ArrayList<Object>();
        try {
            if (propertyCategoryRequest.getCategoryType() != null) {
                propertyCategoryRequest.setCategoryTypeId(jdbcTemplate.queryForObject(propertyCategoryueryBuilder.getCategoryId(),
                        new Object[] { propertyCategoryRequest.getCategoryType() }, Long.class));
            }
        } catch (EmptyResultDataAccessException e) {
            LOGGER.info("EmptyResultDataAccessException: Query returned empty RS.");

        }

        // Similar api call for fetching property id to enable search based on property name.

        String queryStr = propertyCategoryueryBuilder.getQuery(propertyCategoryRequest, preparedStatementValues);
        String categoryNameQuery = propertyCategoryueryBuilder.getCategoryTypeName();
        List<PropertyTypeCategoryType> propertyCategories = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(),
                propertyCategoryRowMapper);
        for (PropertyTypeCategoryType propertyTypeCategoryType : propertyCategories) {
            propertyTypeCategoryType.setCategoryTypeName(jdbcTemplate.queryForObject(categoryNameQuery,
                    new Object[] { propertyTypeCategoryType.getCategoryTypeId() }, String.class));
        }
        LOGGER.info("PropertyCategoryList: " + propertyCategories.toString());
        PropertyTypeCategoryTypesRes propertyCategoryResponse = new PropertyTypeCategoryTypesRes();
        propertyCategoryResponse.setPropertyTypeCategoryTypes(propertyCategories);
        return propertyCategoryResponse;
    }

}
