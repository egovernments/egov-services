/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.wcms.repository;


import org.egov.wcms.model.ConnectionCategory;
import org.egov.wcms.repository.builder.ConnectionCategoryQueryBuilder;
import org.egov.wcms.repository.rowmapper.ConnectionCategoryRowMapper;
import org.egov.wcms.web.contract.CategoryGetRequest;
import org.egov.wcms.web.contract.ConnectionCategoryRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ConnectionCategoryRepository {

    public static final Logger LOGGER = LoggerFactory.getLogger(ConnectionCategoryRepository.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ConnectionCategoryQueryBuilder categoryQueryBuilder;

    @Autowired
    private ConnectionCategoryRowMapper categoryRowMapper;

    public ConnectionCategoryRequest persistCreateCategory(final ConnectionCategoryRequest categoryRequest) {
        LOGGER.info("ConnectionCategoryRequest::" + categoryRequest);
        final String categoryInsert = categoryQueryBuilder.insertCategoryQuery();
        final ConnectionCategory category = categoryRequest.getCategory();
        Object[] obj = new Object[] {Long.valueOf(category.getCode()),category.getCode(),category.getName(),category.getDescription(),category.getActive(),Long.valueOf(categoryRequest.getRequestInfo().getUserInfo().getId()),Long.valueOf(categoryRequest.getRequestInfo().getUserInfo().getId()),new Date(new java.util.Date().getTime()),
                new Date(new java.util.Date().getTime()),category.getTenantId() };
        jdbcTemplate.update(categoryInsert, obj);
        return categoryRequest;
    }


   public ConnectionCategoryRequest persistModifyCategory(final ConnectionCategoryRequest categoryRequest) {
        LOGGER.info("ConnectionCategoryRequest::" + categoryRequest);
        final String categoryUpdate = categoryQueryBuilder.updateCategoryQuery();
        final ConnectionCategory category = categoryRequest.getCategory();
        Object[] obj = new Object[] {category.getName(),category.getDescription(),category.getActive(),
                Long.valueOf(categoryRequest.getRequestInfo().getUserInfo().getId()),new Date(new java.util.Date().getTime()), category.getCode() };
        jdbcTemplate.update(categoryUpdate, obj);
        return categoryRequest;

    }

    public boolean checkCategoryByNameAndCode(final String code,final String name,final String tenantId) {
        final List<Object> preparedStatementValues = new ArrayList<Object>();
        preparedStatementValues.add(name);
        // preparedStatementValues.add(id);
        preparedStatementValues.add(tenantId);
        final String query;
        if (code == null)
            query = categoryQueryBuilder.selectCategoryByNameAndCodeQuery();
        else {
            preparedStatementValues.add(code);
            query = categoryQueryBuilder.selectCategoryByNameAndCodeNotInQuery();
        }
        final List<Map<String, Object>> categories = jdbcTemplate.queryForList(query,
                preparedStatementValues.toArray());
        if (!categories.isEmpty())
            return false;

        return true;
    }

    public List<ConnectionCategory> findForCriteria(CategoryGetRequest categoryGetRequest) {
        List<Object> preparedStatementValues = new ArrayList<Object>();
        String queryStr = categoryQueryBuilder.getQuery(categoryGetRequest, preparedStatementValues);
        List<ConnectionCategory> categories = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), categoryRowMapper);
        return categories;
    }

}
