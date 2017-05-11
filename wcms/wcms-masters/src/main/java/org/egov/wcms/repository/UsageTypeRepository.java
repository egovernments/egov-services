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

import org.egov.wcms.model.UsageType;
import org.egov.wcms.repository.builder.UsageTypeQueryBuilder;
import org.egov.wcms.repository.rowmapper.UsageTypeRowMapper;
import org.egov.wcms.web.contract.UsageTypeGetRequest;
import org.egov.wcms.web.contract.UsageTypeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class UsageTypeRepository {

    public static final Logger LOGGER = LoggerFactory.getLogger(UsageTypeRepository.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UsageTypeQueryBuilder usageTypeQueryBuilder;

    @Autowired
    private UsageTypeRowMapper usageTypeRowMapper;

    public UsageTypeRequest persistCreateUsageType(final UsageTypeRequest usageTypeRequest) {
        LOGGER.info("UsageTypeRequest::" + usageTypeRequest);
        final String usageTypeInsert = usageTypeQueryBuilder.insertUsageTypeQuery();
        final UsageType usageType = usageTypeRequest.getUsageType();
        Object[] obj = new Object[] {Long.valueOf(usageType.getCode()),usageType.getCode(),usageType.getName(),usageType.getDescription(),usageType.getActive(),Long.valueOf(usageTypeRequest.getRequestInfo().getUserInfo().getId()),Long.valueOf(usageTypeRequest.getRequestInfo().getUserInfo().getId()),new Date(new java.util.Date().getTime()),
                new Date(new java.util.Date().getTime()),usageType.getTenantId() };

        jdbcTemplate.update(usageTypeInsert, obj);
        return usageTypeRequest;
    }


    public UsageTypeRequest persistModifyUsageType(final UsageTypeRequest usageTypeRequest) {
        LOGGER.info("UsageTypeRequest::" + usageTypeRequest);
        final String usageTypeUpdate = usageTypeQueryBuilder.updateUsageTypeQuery();
        final UsageType usageType = usageTypeRequest.getUsageType();
        Object[] obj = new Object[] {usageType.getName(),usageType.getDescription(),usageType.getActive(),
                Long.valueOf(usageTypeRequest.getRequestInfo().getUserInfo().getId()),new Date(new java.util.Date().getTime()), usageType.getCode() };
        jdbcTemplate.update(usageTypeUpdate, obj);
        return usageTypeRequest;

    }

    public boolean checkUsageTypeByNameAndCode(final String code,final String name,final String tenantId) {
        final List<Object> preparedStatementValues = new ArrayList<Object>();
        preparedStatementValues.add(name);
       // preparedStatementValues.add(id);
        preparedStatementValues.add(tenantId);
        final String query;
        if (code == null)
            query = usageTypeQueryBuilder.selectUsageTypeByNameAndCodeQuery();
        else {
            preparedStatementValues.add(code);
            query = usageTypeQueryBuilder.selectUsageTypeByNameAndCodeNotInQuery();
        }
        final List<Map<String, Object>> usageTypes = jdbcTemplate.queryForList(query,
                preparedStatementValues.toArray());
        if (!usageTypes.isEmpty())
            return false;

        return true;
    }

    public List<UsageType> findForCriteria(UsageTypeGetRequest usageTypeGetRequest) {
        List<Object> preparedStatementValues = new ArrayList<Object>();
        String queryStr = usageTypeQueryBuilder.getQuery(usageTypeGetRequest, preparedStatementValues);
        List<UsageType> usageTypes = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), usageTypeRowMapper);
        return usageTypes;
    }


}