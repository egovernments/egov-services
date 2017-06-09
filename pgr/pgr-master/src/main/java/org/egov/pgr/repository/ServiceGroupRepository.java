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
package org.egov.pgr.repository;

import java.sql.Date;

import org.egov.pgr.model.ServiceGroup;
import org.egov.pgr.repository.builder.ServiceGroupQueryBuilder;
//import org.egov.pgrrest.master.repository.rowmapper.CategoryTypeRowMapper;
import org.egov.pgr.web.contract.ServiceGroupRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ServiceGroupRepository {

    public static final Logger LOGGER = LoggerFactory.getLogger(ServiceGroupRepository.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

  /*  @Autowired
    private CategoryTypeQueryBuilder categoryQueryBuilder;

    @Autowired
    private CategoryTypeRowMapper categoryRowMapper; */

    public ServiceGroupRequest persistCreateCategory(final ServiceGroupRequest serviceGroupRequest) {
        LOGGER.info("ServiceGroupRequest::" + serviceGroupRequest);
        final String categoryInsert = ServiceGroupQueryBuilder.insertCategoryQuery();
        final ServiceGroup serviceGroup = serviceGroupRequest.getServiceGroup();
        final Object[] obj = new Object[] { serviceGroup.getName(),
        		serviceGroup.getDescription(), Long.valueOf(serviceGroupRequest.getRequestInfo().getUserInfo().getId()),
                Long.valueOf(serviceGroupRequest.getRequestInfo().getUserInfo().getId()),
                new Date(new java.util.Date().getTime()), new Date(new java.util.Date().getTime()),
                serviceGroup.getTenantId()};
        jdbcTemplate.update(categoryInsert, obj);
        return serviceGroupRequest;
    }

 /*   public CategoryTypeRequest persistModifyCategory(final CategoryTypeRequest categoryRequest) {
        LOGGER.info("ConnectionCategoryRequest::" + categoryRequest);
        final String categoryUpdate = CategoryTypeQueryBuilder.updateCategoryQuery();
        final CategoryType category = categoryRequest.getCategory();
        final Object[] obj = new Object[] { category.getName(), category.getDescription(), category.getActive(),
                Long.valueOf(categoryRequest.getRequestInfo().getUserInfo().getId()),
                new Date(new java.util.Date().getTime()), category.getCode() };
        jdbcTemplate.update(categoryUpdate, obj);
        return categoryRequest;

    }

    public boolean checkCategoryByNameAndCode(final String code, final String name, final String tenantId) {
        final List<Object> preparedStatementValues = new ArrayList<>();
        preparedStatementValues.add(name);
        // preparedStatementValues.add(id);
        preparedStatementValues.add(tenantId);
        final String query;
        if (code == null)
            query = CategoryTypeQueryBuilder.selectCategoryByNameAndCodeQuery();
        else {
            preparedStatementValues.add(code);
            query = CategoryTypeQueryBuilder.selectCategoryByNameAndCodeNotInQuery();
        }
        final List<Map<String, Object>> categories = jdbcTemplate.queryForList(query,
                preparedStatementValues.toArray());
        if (!categories.isEmpty())
            return false;

        return true;
    }

    public List<CategoryType> findForCriteria(final CategoryTypeGetRequest categoryGetRequest) {
        final List<Object> preparedStatementValues = new ArrayList<>();
        final String queryStr = categoryQueryBuilder.getQuery(categoryGetRequest, preparedStatementValues);
        final List<CategoryType> categories = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(),
                categoryRowMapper);
        return categories;
    } */

}
