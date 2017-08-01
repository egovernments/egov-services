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
import java.util.ArrayList;
import java.util.List;

import org.egov.pgr.domain.model.EscalationTimeType;
import org.egov.pgr.repository.builder.EscalationTimeTypeQueryBuilder;
import org.egov.pgr.repository.rowmapper.EscalationTimeTypeRowMapper;
import org.egov.pgr.web.contract.EscalationTimeTypeGetReq;
import org.egov.pgr.web.contract.EscalationTimeTypeReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EscalationTimeTypeRepository {
	

	public static final Logger LOGGER = LoggerFactory.getLogger(EscalationTimeTypeRepository.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private EscalationTimeTypeQueryBuilder escalationTimeTypeQueryBuilder;

	@Autowired
	private EscalationTimeTypeRowMapper escalationRowMapper;


	public EscalationTimeTypeReq persistCreateEscalationTimeType(final EscalationTimeTypeReq escalationTimeTypeRequest) {
		LOGGER.info("EscalationTimeTypeRequest::" + escalationTimeTypeRequest);
		final String escalationTimeTypeInsert = escalationTimeTypeQueryBuilder.insertEscalationTimeType();
		final EscalationTimeType ecalationTimeType = escalationTimeTypeRequest.getEscalationTimeType();
		final Object[] obj = new Object[] { ecalationTimeType.getGrievanceType().getId(), ecalationTimeType.getNoOfHours(),
				ecalationTimeType.getDesignation(), ecalationTimeType.getTenantId(),
				Long.valueOf(escalationTimeTypeRequest.getRequestInfo().getUserInfo().getId()),
				Long.valueOf(escalationTimeTypeRequest.getRequestInfo().getUserInfo().getId()),
				new Date(new java.util.Date().getTime()), new Date(new java.util.Date().getTime()) };
		jdbcTemplate.update(escalationTimeTypeInsert, obj);
		return escalationTimeTypeRequest;
	}
	
	public List<EscalationTimeType> getAllEscalationTimeTypes(final EscalationTimeTypeGetReq escalationGetRequest) {
		LOGGER.info("EscalationTimeType search Request::" + escalationGetRequest);
		final List<Object> preparedStatementValues = new ArrayList<>();
		final String queryStr = escalationTimeTypeQueryBuilder.getQuery(escalationGetRequest, preparedStatementValues);
		final List<EscalationTimeType> escalationTypes = jdbcTemplate.query(queryStr,
				preparedStatementValues.toArray(), escalationRowMapper);
		return escalationTypes;
	}

	public EscalationTimeTypeReq persistUpdateEscalationTimeType(final EscalationTimeTypeReq escalationTimeTypeRequest) {
		LOGGER.info("EscalationTimeTypeRequest::" + escalationTimeTypeRequest);
		final String escalationTimeTypeInsert = escalationTimeTypeQueryBuilder.updateEscalationTimeType();
		final EscalationTimeType ecalationTimeType = escalationTimeTypeRequest.getEscalationTimeType();
		final Object[] obj = new Object[] { ecalationTimeType.getGrievanceType().getId(), ecalationTimeType.getNoOfHours(),
				ecalationTimeType.getDesignation(), ecalationTimeType.getTenantId(),
				Long.valueOf(escalationTimeTypeRequest.getRequestInfo().getUserInfo().getId()),
				Long.valueOf(escalationTimeTypeRequest.getRequestInfo().getUserInfo().getId()),
				new Date(new java.util.Date().getTime()), new Date(new java.util.Date().getTime()), ecalationTimeType.getId()};
		jdbcTemplate.update(escalationTimeTypeInsert, obj);
		return escalationTimeTypeRequest;
	}
}
