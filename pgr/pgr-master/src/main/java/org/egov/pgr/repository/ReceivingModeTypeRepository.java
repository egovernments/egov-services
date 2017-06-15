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

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.egov.pgr.model.ReceivingModeType;
import org.egov.pgr.repository.builder.ReceivingModeTypeQueryBuilder;
import org.egov.pgr.repository.rowmapper.ReceivingModeTypeRowMapper;
import org.egov.pgr.web.contract.ReceivingModeTypeGetReq;
import org.egov.pgr.web.contract.ReceivingModeTypeReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ReceivingModeTypeRepository {

	public static final Logger LOGGER = LoggerFactory.getLogger(ReceivingModeTypeRepository.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private ReceivingModeTypeQueryBuilder receivingModeTypeQueryBuilder;

	@Autowired
	private ReceivingModeTypeRowMapper receivingModeRowMapper;

	public ReceivingModeTypeReq persistReceivingModeType(final ReceivingModeTypeReq modeTypeRequest) {
		LOGGER.info("ReceivingModeType Create Request::" + modeTypeRequest);
		final String receivingModeTypeInsert = ReceivingModeTypeQueryBuilder.insertReceivingModeTypeQuery();
		final ReceivingModeType modeType = modeTypeRequest.getModeType();
		final Object[] obj = new Object[] { modeType.getCode(), modeType.getName(), modeType.getDescription(),
				modeType.getVisible(),modeType.getActive(),
				Long.valueOf(modeTypeRequest.getRequestInfo().getUserInfo().getId()),
				Long.valueOf(modeTypeRequest.getRequestInfo().getUserInfo().getId()),
				new Date(new java.util.Date().getTime()), new Date(new java.util.Date().getTime()),
				modeType.getTenantId() };
		jdbcTemplate.update(receivingModeTypeInsert, obj);
		return modeTypeRequest;
	}

	public ReceivingModeTypeReq persistModifyReceivingModeType(final ReceivingModeTypeReq modeTypeRequest) {
		LOGGER.info("ReceivingModeType Update Request::" + modeTypeRequest);
		final String receivingCenterTypeUpdate = ReceivingModeTypeQueryBuilder.updateReceivingModeTypeQuery();
		final ReceivingModeType modeType = modeTypeRequest.getModeType();
		final Object[] obj = new Object[] {modeType.getName(), modeType.getDescription(),modeType.getVisible(),modeType.getActive(),
				Long.valueOf(modeTypeRequest.getRequestInfo().getUserInfo().getId()), new Date(new java.util.Date().getTime()), modeType.getCode() };
		jdbcTemplate.update(receivingCenterTypeUpdate, obj);
		return modeTypeRequest;

	}

	public List<ReceivingModeType> getAllReceivingModeTypes(final ReceivingModeTypeGetReq modeTypeGetRequest) {
		LOGGER.info("ReceivingModeType search Request::" + modeTypeGetRequest);
		final List<Object> preparedStatementValues = new ArrayList<>();
		final String queryStr = receivingModeTypeQueryBuilder.getQuery(modeTypeGetRequest, preparedStatementValues);
		final List<ReceivingModeType> receivingModeTypes = jdbcTemplate.query(queryStr,
				preparedStatementValues.toArray(), receivingModeRowMapper);
		return receivingModeTypes;
	}
	
	
	
    public boolean checkReceivingModeTypeByNameAndCode(final String code,final String name, final String tenantId) {
        final List<Object> preparedStatementValues = new ArrayList<>();
      
        // preparedStatementValues.add(id);
        preparedStatementValues.add(tenantId);
        String query="";
        if(code!=null && code!=""){
        	
        	preparedStatementValues.add(code);
        	query = ReceivingModeTypeQueryBuilder.checkReceivinModeTypeByNameAndCode();
        	
        } 
        
        final List<Map<String, Object>> ceneterTypes = jdbcTemplate.queryForList(query,
                preparedStatementValues.toArray());
        if (!ceneterTypes.isEmpty())
            return false;

        return true;
    }

}
