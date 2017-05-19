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


import org.egov.wcms.model.ConnectionCategory;
import org.egov.wcms.model.PipeSize;
import org.egov.wcms.repository.builder.ConnectionCategoryQueryBuilder;
import org.egov.wcms.repository.builder.PipeSizeQueryBuilder;
import org.egov.wcms.repository.rowmapper.ConnectionCategoryRowMapper;
import org.egov.wcms.repository.rowmapper.PipeSizeRowMapper;
import org.egov.wcms.web.contract.CategoryGetRequest;
import org.egov.wcms.web.contract.ConnectionCategoryRequest;
import org.egov.wcms.web.contract.PipeSizeGetRequest;
import org.egov.wcms.web.contract.PipeSizeRequest;
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
public class PipeSizeRepository {

    public static final Logger LOGGER = LoggerFactory.getLogger(PipeSizeRepository.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PipeSizeQueryBuilder pipeSizeQueryBuilder;

    @Autowired
    private PipeSizeRowMapper pipeSizeRowMapper;

    public PipeSizeRequest persistCreatePipeSize(final PipeSizeRequest pipeSizeRequest) {
        LOGGER.info("PipeSizeRequest::" + pipeSizeRequest);
        final String pipeSizeInsert = pipeSizeQueryBuilder.insertPipeSizeQuery();
        final PipeSize pipeSize = pipeSizeRequest.getPipeSize();
        Object[] obj = new Object[] {Long.valueOf(pipeSize.getCode()),pipeSize.getCode(),pipeSize.getSizeInMilimeter(),pipeSize.getSizeInInch(),pipeSize.getActive(),Long.valueOf(pipeSizeRequest.getRequestInfo().getUserInfo().getId()),Long.valueOf(pipeSizeRequest.getRequestInfo().getUserInfo().getId()),new Date(new java.util.Date().getTime()),
                new Date(new java.util.Date().getTime()),pipeSize.getTenantId() };
        jdbcTemplate.update(pipeSizeInsert, obj);
        return pipeSizeRequest;
    }


    public PipeSizeRequest persistModifyPipeSize(final PipeSizeRequest pipeSizeRequest) {
        LOGGER.info("PipeSizeRequest::" + pipeSizeRequest);
        final String pipeSizeUpdate = pipeSizeQueryBuilder.updatePipeSizeQuery();
        final PipeSize pipeSize = pipeSizeRequest.getPipeSize();
        Object[] obj = new Object[] {pipeSize.getSizeInMilimeter(),pipeSize.getSizeInInch(),pipeSize.getActive(),
                Long.valueOf(pipeSizeRequest.getRequestInfo().getUserInfo().getId()),new Date(new java.util.Date().getTime()), pipeSize.getCode() };
        jdbcTemplate.update(pipeSizeUpdate, obj);
        return pipeSizeRequest;

    }

    public boolean checkPipeSizeInmmAndCode(final String code,final Double sizeInMilimeter,final String tenantId) {
        final List<Object> preparedStatementValues = new ArrayList<Object>();
        preparedStatementValues.add(sizeInMilimeter);
        preparedStatementValues.add(tenantId);
        final String query;
        if (code == null)
            query = pipeSizeQueryBuilder.selectPipeSizeInmmAndCodeQuery();
        else {
            preparedStatementValues.add(code);
            query = pipeSizeQueryBuilder.selectPipeSizeInmmAndCodeNotInQuery();
        }
        final List<Map<String, Object>> pipeSizes = jdbcTemplate.queryForList(query,
                preparedStatementValues.toArray());
        if (!pipeSizes.isEmpty())
            return false;

        return true;
    }

    public List<PipeSize> findForCriteria(final PipeSizeGetRequest pipeSizeGetRequest) {
        List<Object> preparedStatementValues = new ArrayList<Object>();
        String queryStr = pipeSizeQueryBuilder.getQuery(pipeSizeGetRequest, preparedStatementValues);
        List<PipeSize> pipeSizes = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), pipeSizeRowMapper);
        return pipeSizes;
    }

}
