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

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.egov.wcms.model.SupplyType;
import org.egov.wcms.repository.builder.SupplyTypeQueryBuilder;
import org.egov.wcms.repository.rowmapper.SupplyTypeRowMapper;
import org.egov.wcms.web.contract.SupplyTypeGetRequest;
import org.egov.wcms.web.contract.SupplyTypeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SupplyTypeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SupplyTypeQueryBuilder  supplyTypeQueryBuilder;
    
    @Autowired
    private SupplyTypeRowMapper supplyTypeRowMapper;
    
    public SupplyTypeRequest persistSupplyType(final SupplyTypeRequest supplyTypeRequest) {
        final String insertQuery = SupplyTypeQueryBuilder.insertSupplyTypeQuery();
        final SupplyType supplyType = supplyTypeRequest.getSupplyType();
        final Object[] obj = new Object[] { Long.valueOf(supplyType.getCode()), supplyType.getCode(),supplyType.getName(), supplyType.getDescription(),supplyType.getActive(),
                Long.valueOf(supplyTypeRequest.getRequestInfo().getUserInfo().getId()),
                Long.valueOf(supplyTypeRequest.getRequestInfo().getUserInfo().getId()),
                new Date(new java.util.Date().getTime()), new Date(new java.util.Date().getTime()),
                supplyType.getTenantId() };
        jdbcTemplate.update(insertQuery, obj);
        return supplyTypeRequest;
    }
    
    public SupplyTypeRequest upateSupplyType(final SupplyTypeRequest supplyTypeRequest) {
        final String supplytypevalue = SupplyTypeQueryBuilder.updateSupplyTypeQuery();
        final SupplyType supply = supplyTypeRequest.getSupplyType();
        final Object[] obj = new Object[] { supply.getName(), supply.getDescription(), supply.getActive(),
                Long.valueOf(supplyTypeRequest.getRequestInfo().getUserInfo().getId()),
                new Date(new java.util.Date().getTime()), supply.getCode() };
        jdbcTemplate.update(supplytypevalue, obj);
        return supplyTypeRequest;

    }
    

    public boolean checkSupplyTypeByNameAndCode(final String code, final String name, final String tenantId) {
        final List<Object> preparedStatementValues = new ArrayList<>();
        preparedStatementValues.add(name);
        // preparedStatementValues.add(id);
        preparedStatementValues.add(tenantId);
        final String query;
        if (code == null)
            query = SupplyTypeQueryBuilder.selectSupplyTypeByNameAndCodeQuery();
        else {
            preparedStatementValues.add(code);
            query = SupplyTypeQueryBuilder.selectSupplyTypeByNameAndCodeNotInQuery();
        }
        final List<Map<String, Object>> supplytypes = jdbcTemplate.queryForList(query,
                preparedStatementValues.toArray());
        if (!supplytypes.isEmpty())
            return false;

        return true;
    }

    public List<SupplyType> findForCriteria(final SupplyTypeGetRequest supplyTypeGetRequest) {
        final List<Object> preparedStatementValues = new ArrayList<>();
        final String queryStr = supplyTypeQueryBuilder.getQuery(supplyTypeGetRequest, preparedStatementValues);
        return jdbcTemplate.query(queryStr, preparedStatementValues.toArray(),
                supplyTypeRowMapper);
    }
}
