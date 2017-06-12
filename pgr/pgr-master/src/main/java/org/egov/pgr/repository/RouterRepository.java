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

import org.egov.pgr.model.PersistRouter;
import org.egov.pgr.model.PersistRouterReq;
import org.egov.pgr.model.ServiceGroup;
import org.egov.pgr.repository.builder.RouterQueryBuilder;
import org.egov.pgr.repository.builder.ServiceGroupQueryBuilder;
import org.egov.pgr.repository.rowmapper.PersistRouteRowMapper;
//import org.egov.pgrrest.master.repository.rowmapper.CategoryTypeRowMapper;
import org.egov.pgr.web.contract.ServiceGroupRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RouterRepository {

	public static final Logger LOGGER = LoggerFactory.getLogger(RouterRepository.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	

	public PersistRouterReq createRouter(final PersistRouterReq routerReq) {
		
		LOGGER.info("Router Request::" + routerReq);
		final String routerInsert = RouterQueryBuilder.insertRouter();
		final PersistRouter persistRouter = routerReq.getRouterType();
		final Object[] obj = new Object[] {persistRouter.getService(),persistRouter.getPosition(), persistRouter.getBoundary(),0,
				Long.valueOf(routerReq.getRequestInfo().getUserInfo().getId()),new Date(new java.util.Date().getTime()),
				Long.valueOf(routerReq.getRequestInfo().getUserInfo().getId()),new Date(new java.util.Date().getTime()),
				persistRouter.getTenantId() };
		
		jdbcTemplate.update(routerInsert, obj);
		return routerReq;
	}
public PersistRouter ValidateRouter(final PersistRouterReq routerReq) {
		
		
		final String validateQuery = RouterQueryBuilder.validateRouter();
		PersistRouter persistRouter = new PersistRouter();
		try{
		persistRouter = (PersistRouter)jdbcTemplate.queryForObject(
				validateQuery, new Object[] { routerReq.getRouterType().getService(),routerReq.getRouterType().getBoundary()}, new PersistRouteRowMapper());
		LOGGER.info("Value coming from validate query boundary::" + persistRouter.getBoundary());
		}
		catch (EmptyResultDataAccessException e) {
			return null;
		}
		return persistRouter;
	}
	
	
	
	

}
