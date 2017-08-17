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

package org.egov.pgr.repository;

import org.egov.pgr.domain.model.ReceivingCenterType;
import org.egov.pgr.repository.builder.ReceivingCenterTypeQueryBuilder;
import org.egov.pgr.repository.rowmapper.ReceivingCenterTypeRowMapper;
import org.egov.pgr.web.contract.ReceivingCenterTypeGetReq;
import org.egov.pgr.web.contract.ReceivingCenterTypeReq;
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
public class ReceivingCenterTypeRepository {

    public static final Logger LOGGER = LoggerFactory.getLogger(ReceivingCenterTypeRepository.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReceivingCenterTypeQueryBuilder receivingCenterQueryBuilder;

    @Autowired
    private ReceivingCenterTypeRowMapper receivingCenterRowMapper;

    public ReceivingCenterTypeReq persistReceivingCenterType(final ReceivingCenterTypeReq centerTypeRequest) {
        LOGGER.info("ReceivingCenterType Create Request::" + centerTypeRequest);
        final String receivingCenterTypeInsert = ReceivingCenterTypeQueryBuilder.insertReceivingCenterTypeQuery();
        final ReceivingCenterType centerType = centerTypeRequest.getCenterType();

        final Object[] obj = new Object[]{centerType.getCode(), centerType.getName(), centerType.getDescription(),
                centerType.getIscrnrequired(), centerType.getOrderno(), centerType.getActive(),
                Long.valueOf(centerTypeRequest.getRequestInfo().getUserInfo().getId()),
                Long.valueOf(centerTypeRequest.getRequestInfo().getUserInfo().getId()),
                new Date(new java.util.Date().getTime()), new Date(new java.util.Date().getTime()),
                centerType.getTenantId()};

        jdbcTemplate.update(receivingCenterTypeInsert, obj);
        return centerTypeRequest;
    }

    public ReceivingCenterTypeReq persistModifyReceivingCenterType(final ReceivingCenterTypeReq centerTypeRequest) {
        LOGGER.info("ReceivingCenterType Update Request::" + centerTypeRequest);
        final String receivingCenterTypeUpdate = ReceivingCenterTypeQueryBuilder.updateReceivingCenterTypeQuery();
        final ReceivingCenterType centerType = centerTypeRequest.getCenterType();
        final Object[] obj = new Object[]{centerType.getName(), centerType.getDescription(),
                centerType.getIscrnrequired(), centerType.getOrderno(), centerType.getActive(),
                Long.valueOf(centerTypeRequest.getRequestInfo().getUserInfo().getId()),
                new Date(new java.util.Date().getTime()), centerType.getCode()};
        jdbcTemplate.update(receivingCenterTypeUpdate, obj);
        return centerTypeRequest;

    }

    public List<ReceivingCenterType> getAllReceivingCenterTypes(final ReceivingCenterTypeGetReq centerTypeGetRequest) {
        LOGGER.info("ReceivingCenterType search Request::" + centerTypeGetRequest);
        final List<Object> preparedStatementValues = new ArrayList<>();
        final String queryStr = receivingCenterQueryBuilder.getQuery(centerTypeGetRequest, preparedStatementValues);
        final List<ReceivingCenterType> receivingCenterTypes = jdbcTemplate.query(queryStr,
                preparedStatementValues.toArray(), receivingCenterRowMapper);
        return receivingCenterTypes;
    }

    public boolean checkReceivingCenterTypeByCode(final String code, final String tenantId) {
        final List<Object> preparedStatementValues = new ArrayList<>();

        // preparedStatementValues.add(id);
        preparedStatementValues.add(tenantId);
        String query = "";
        if ((code != null && code != "")) {

            preparedStatementValues.add(code);
            query = ReceivingCenterTypeQueryBuilder.checkReceivingCenterTypeByCode();

        }
        /*
         * if(name!=null && tenantId!=null){ preparedStatementValues.add(name);
		 * query =
		 * ReceivingCenterTypeQueryBuilder.checkReceivingCenterTypeByName(); }
		 */
        final List<Map<String, Object>> ceneterTypes = jdbcTemplate.queryForList(query,
                preparedStatementValues.toArray());
        if (!ceneterTypes.isEmpty())
            return false;

        return true;
    }

    public boolean checkReceivingCenterTypeByCodeAndName(String code, String name, String tenantId, String mode) {
        final List<Object> preparedStatementValues = new ArrayList<>();

        // preparedStatementValues.add(id);
        preparedStatementValues.add(tenantId.trim());
        String query = "";
        if (code != null && code != "" && name != null & name != "") {

            preparedStatementValues.add(code.toUpperCase().trim());
            preparedStatementValues.add(name.toUpperCase().toUpperCase());
            query = ReceivingCenterTypeQueryBuilder.checkReceivingCenterTypeByCodeName();

        }
        final List<Map<String, Object>> centerTypes = jdbcTemplate.queryForList(query, preparedStatementValues.toArray());
        if (!centerTypes.isEmpty() && "update".equalsIgnoreCase(mode)) {
            String codeFromDB = (String) centerTypes.get(0).get("code");
            if (!codeFromDB.equalsIgnoreCase(code))
                return true;
        }
        if (!centerTypes.isEmpty() && "create".equalsIgnoreCase(mode)) {
            return true;
        }

        return false;
    }

    public boolean checkReceivingCenterNameExists(ReceivingCenterType receivingCenter, String mode) {
        final List<Object> preparedStatementValues = new ArrayList<>();

        // preparedStatementValues.add(id);
        preparedStatementValues.add(receivingCenter.getTenantId());
        String query = "";
        if (null != receivingCenter.getName() && receivingCenter.getName() != "") {
            preparedStatementValues.add(receivingCenter.getName().toUpperCase().trim());
            query = ReceivingCenterTypeQueryBuilder.checkReceivingCenterTypeByName();
        }

        final List<Map<String, Object>> centerTypes = jdbcTemplate.queryForList(query,
                preparedStatementValues.toArray());
        if (!centerTypes.isEmpty() && "update".equalsIgnoreCase(mode)) {
            String codeFromDB = (String) centerTypes.get(0).get("code");
            if (!codeFromDB.equalsIgnoreCase(receivingCenter.getCode()))
                return true;
        }
        if (!centerTypes.isEmpty() && "create".equalsIgnoreCase(mode)) {
            return true;
        }
        return false;
    }

}
