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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.egov.wcms.model.PropertyTypeCategoryType;
import org.egov.wcms.repository.builder.PropertyTypeCategoryTypeQueryBuilder;
import org.egov.wcms.repository.rowmapper.PropertyCategoryRowMapper;
import org.egov.wcms.service.RestWaterExternalMasterService;
import org.egov.wcms.web.contract.PropertyCategoryGetRequest;
import org.egov.wcms.web.contract.PropertyTypeCategoryTypeReq;
import org.egov.wcms.web.contract.PropertyTypeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class PropertyTypeCategoryTypeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PropertyTypeCategoryTypeQueryBuilder propertyCategoryueryBuilder;

    @Autowired
    private PropertyCategoryRowMapper propertyCategoryRowMapper;

    @Autowired
    private RestWaterExternalMasterService restExternalMasterService;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public PropertyTypeCategoryTypeReq persistCreatePropertyCategory(
            final PropertyTypeCategoryTypeReq propertyCategoryRequest) {
        log.info("PropertyCategoryRequest::" + propertyCategoryRequest);
        final String propertyCategoryInsert = PropertyTypeCategoryTypeQueryBuilder.insertPropertyCategoryQuery();
        final List<PropertyTypeCategoryType> propertyCategoryList = propertyCategoryRequest.getPropertyTypeCategoryType();
        final List<Map<String, Object>> batchValues = new ArrayList<>(propertyCategoryList.size());
        for (final PropertyTypeCategoryType propertyCategory : propertyCategoryList) {

            final String categoryQuery = PropertyTypeCategoryTypeQueryBuilder.getCategoryId();
            Long categoryId = 0L;
            try {
                categoryId = jdbcTemplate.queryForObject(categoryQuery,
                        new Object[] { propertyCategory.getCategoryTypeName(), propertyCategory.getTenantId() },
                        Long.class);
                log.info("Category Id: " + categoryId);
            } catch (final EmptyResultDataAccessException e) {
                log.info("EmptyResultDataAccessException: Query returned empty result set");
            }
            if (categoryId == null)
                log.info("Invalid input.");
            batchValues.add(
                    new MapSqlParameterSource("id", Long.valueOf(propertyCategory.getCode()))
                            .addValue("code", propertyCategory.getCode())
                            .addValue("propertytypeid", propertyCategory.getPropertyTypeId())
                            .addValue("categorytypeid", categoryId)
                            .addValue("active", propertyCategory.getActive())
                            .addValue("createdby", Long.valueOf(propertyCategoryRequest.getRequestInfo().getUserInfo().getId()))
                            .addValue("lastmodifiedby",
                                    Long.valueOf(propertyCategoryRequest.getRequestInfo().getUserInfo().getId()))
                            .addValue("createddate", new Date(new java.util.Date().getTime()))
                            .addValue("lastmodifieddate", new Date(new java.util.Date().getTime()))
                            .addValue("tenantid", propertyCategory.getTenantId())
                            .getValues());
                           
        }
        namedParameterJdbcTemplate.batchUpdate(propertyCategoryInsert, batchValues.toArray(new Map[propertyCategoryList.size()]));

        return propertyCategoryRequest;
    }

    public PropertyTypeCategoryTypeReq persistUpdatePropertyCategory(
            final PropertyTypeCategoryTypeReq propertyCategoryRequest) {
        log.info("PropertyCategoryRequest::" + propertyCategoryRequest);
        final String propertyCategoryUpdate = PropertyTypeCategoryTypeQueryBuilder.updatePropertyCategoryQuery();
        final List<PropertyTypeCategoryType> propertyCategoryList = propertyCategoryRequest.getPropertyTypeCategoryType();
        final List<Map<String, Object>> batchValues = new ArrayList<>(propertyCategoryList.size());
        for (final PropertyTypeCategoryType propertyCategory : propertyCategoryRequest.getPropertyTypeCategoryType()) {
            final String categoryQuery = PropertyTypeCategoryTypeQueryBuilder.getCategoryId();
            Long categoryId = 0L;
            try {
                categoryId = jdbcTemplate.queryForObject(categoryQuery,
                        new Object[] { propertyCategory.getCategoryTypeName(), propertyCategory.getTenantId() },
                        Long.class);
                log.info("Category Id: " + categoryId);
            } catch (final EmptyResultDataAccessException e) {
                log.info("EmptyResultDataAccessException: Query returned empty result set");
            }
            if (categoryId == null)
                log.info("Invalid input.");
            batchValues.add(
                    new MapSqlParameterSource("propertytypeid", propertyCategory.getPropertyTypeId())
                            .addValue("categorytypeid", categoryId)
                            .addValue("active", propertyCategory.getActive())

                            .addValue("lastmodifiedby",
                                    Long.valueOf(propertyCategoryRequest.getRequestInfo().getUserInfo().getId()))

                            .addValue("lastmodifieddate", new Date(new java.util.Date().getTime()))
                            .addValue("code", propertyCategory.getCode())
                            .getValues());
        }
        namedParameterJdbcTemplate.batchUpdate(propertyCategoryUpdate, batchValues.toArray(new Map[propertyCategoryList.size()]));
        return propertyCategoryRequest;
    }

    public List<PropertyTypeCategoryType> findForCriteria(final PropertyCategoryGetRequest propertyCategoryRequest) {
        final List<Object> preparedStatementValues = new ArrayList<>();
        final List<Integer> propertyTypeIdsList = new ArrayList<>();
        final String queryStr = propertyCategoryueryBuilder.getQuery(propertyCategoryRequest, preparedStatementValues);
        final List<PropertyTypeCategoryType> propertyCategories = jdbcTemplate.query(queryStr,
                preparedStatementValues.toArray(), propertyCategoryRowMapper);
        // fetch property type Id and set the property type name here
        for (final PropertyTypeCategoryType propertyCategory : propertyCategories)
            propertyTypeIdsList.add(Integer.valueOf(propertyCategory.getPropertyTypeId()));
        final Integer[] propertypeIds = propertyTypeIdsList.stream().distinct().toArray(Integer[]::new);
        final PropertyTypeResponse propertyTypes = restExternalMasterService.getPropertyNameFromPTModule(
                propertypeIds, propertyCategoryRequest.getTenantId());

        propertyCategories.forEach(int1 -> {
            propertyTypes.getPropertyTypes().forEach(int2 -> {
                if (int2.getId().equals(int1.getPropertyTypeId()))
                    int1.setPropertyTypeName(int2.getName());
            });
        });
        log.info("PropertyCategoryList: " + propertyCategories.toString());
        return propertyCategories;
    }

    public boolean checkIfMappingExists(final String code, final String propertyTypeId, final String categoryType,
            final String tenantId) {
        final List<Object> preparedStatementValues = new ArrayList<>();
        final String categoryQuery = PropertyTypeCategoryTypeQueryBuilder.getCategoryId();
        Long categoryId = 0L;
        try {
            categoryId = jdbcTemplate.queryForObject(categoryQuery,
                    new Object[] { categoryType, tenantId },
                    Long.class);
        } catch (final EmptyResultDataAccessException e) {
            log.info("EmptyResultDataAccessException: Query returned empty result set while update");
        }
        if (categoryId == null)
            log.info("Invalid input.");
        preparedStatementValues.add(propertyTypeId);
        preparedStatementValues.add(categoryId);
        preparedStatementValues.add(tenantId);
        final String query;
        if (code == null)
            query = PropertyTypeCategoryTypeQueryBuilder.selectPropertyByCategoryQuery();
        else {
            preparedStatementValues.add(code);
            query = PropertyTypeCategoryTypeQueryBuilder.selectPropertyByCategoryNotInQuery();
        }
        final List<Map<String, Object>> propertyCategory = jdbcTemplate.queryForList(query,
                preparedStatementValues.toArray());
        if (!propertyCategory.isEmpty())
            return false;

        return true;
    }

}
