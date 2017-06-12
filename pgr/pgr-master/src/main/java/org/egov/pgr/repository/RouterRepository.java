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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.egov.pgr.model.Attribute;
import org.egov.pgr.model.ServiceType;
import org.egov.pgr.model.Value;
import org.egov.pgr.repository.builder.RouterQueryBuilder;
import org.egov.pgr.repository.rowmapper.RouterRowMapper;
import org.egov.pgr.web.contract.RouterType;
import org.egov.pgr.web.contract.RouterTypeGetReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RouterRepository {

	public static final Logger LOGGER = LoggerFactory.getLogger(RouterRepository.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired private RouterQueryBuilder routerQueryBuilder;
	
	@Autowired private RouterRowMapper routerRowMapper;
	
	/*public ServiceGroupRequest createRouter(final ServiceGroupRequest serviceGroupRequest) {
		LOGGER.info("ServiceGroupRequest::" + serviceGroupRequest);
		final String serviceGroupInsert = ServiceGroupQueryBuilder.insertServiceGroupQuery();
		final ServiceGroup serviceGroup = serviceGroupRequest.getServiceGroup();
		final Object[] obj = new Object[] { serviceGroup.getId(), serviceGroup.getName(), serviceGroup.getDescription(),
				Long.valueOf(serviceGroupRequest.getRequestInfo().getUserInfo().getId()),
				Long.valueOf(serviceGroupRequest.getRequestInfo().getUserInfo().getId()),
				new Date(new java.util.Date().getTime()), new Date(new java.util.Date().getTime()),
				serviceGroup.getTenantId() };
		jdbcTemplate.update(serviceGroupInsert, obj);
		return serviceGroupRequest;
	}*/
	
	public List<RouterType> findForCriteria(final RouterTypeGetReq routerTypeGetRequest) {
        final String queryStr = routerQueryBuilder.getRouterDetail();
        jdbcTemplate.query(queryStr, routerRowMapper);
        return prepareRouterTypeList(routerRowMapper);
    }
	
	private List<RouterType> prepareRouterTypeList(RouterRowMapper rowMapper){
		Map<String, List<Value>> attribValue = rowMapper.attribValue;
		Map<String, Map<String, Attribute>> serviceAttrib = rowMapper.serviceAttrib;
		Map<Long, Map<String, List<ServiceType>>> serviceMap = rowMapper.serviceMap;
		Map<Long, RouterType> routerMap = rowMapper.routerMap;
		RouterType routerType = new RouterType();
		List<RouterType> routerTypes = new ArrayList<>();
		List<ServiceType> serviceTypeList = new ArrayList<>();
		
		Iterator<Entry<Long, RouterType>> itr = routerMap.entrySet().iterator();
		while (itr.hasNext()) {
			Entry<Long, RouterType> routerEntry = itr.next();
			routerType = routerEntry.getValue();
			Long routerId = routerEntry.getKey();
			Map<String, List<ServiceType>> innerServiceMap = serviceMap.get(routerId);
			Iterator<Entry<String, List<ServiceType>>> innerItr = innerServiceMap.entrySet().iterator();
			while (innerItr.hasNext()) {
				Entry<String, List<ServiceType>> innerEntry = innerItr.next();
				serviceTypeList = innerEntry.getValue();
				List<ServiceType> finalServiceList = new ArrayList<>();
				Iterator<ServiceType> serviceItr = serviceTypeList.iterator();
				while (serviceItr.hasNext()) {
					ServiceType serviceType = new ServiceType();
					serviceType = serviceItr.next();
					Map<String, Attribute> innerAttrMap = serviceAttrib.get(serviceType.getServiceCode());
					Iterator<Entry<String, Attribute>> innerAttrItr = innerAttrMap.entrySet().iterator();
					List<Attribute> finalAttributeList = new ArrayList<>();
					while (innerAttrItr.hasNext()) {
						Entry<String,Attribute> attrEntry = innerAttrItr.next();
						List<Value> valueList = attribValue.get(attrEntry.getValue().getCode());
						attrEntry.getValue().setAttributes(valueList);
						finalAttributeList.add(attrEntry.getValue());
					}
					serviceType.setAttributes(finalAttributeList);
					finalServiceList.add(serviceType);
				}
				routerType.setServices(finalServiceList);
				routerTypes.add(routerType);
			}
		}
		return routerTypes;
	}
	
	
	

}
