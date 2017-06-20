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
import java.util.Map;

import org.egov.pgr.model.Attribute;
import org.egov.pgr.model.ServiceType;
import org.egov.pgr.model.Value;
import org.egov.pgr.repository.builder.ServiceTypeQueryBuilder;
import org.egov.pgr.repository.rowmapper.ServiceTypeRowMapper;
import org.egov.pgr.web.contract.ServiceGetRequest;
import org.egov.pgr.web.contract.ServiceRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ServiceTypeRepository {

    public static final Logger LOGGER = LoggerFactory.getLogger(ServiceTypeRepository.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ServiceTypeQueryBuilder serviceTypeQueryBuilder;

    @Autowired
    private ServiceTypeRowMapper serviceTypeRowMapper;

	public ServiceRequest persistServiceType(final ServiceRequest serviceRequest) {
		LOGGER.info("Service Type Request::" + serviceRequest);
		final String complaintInsert = ServiceTypeQueryBuilder.insertComplaintTypeQuery();
		boolean active = true;
		final Object[] object = new Object[] { serviceRequest.getService().getServiceName(),
				serviceRequest.getService().getServiceCode(), serviceRequest.getService().getDescription(), active, serviceRequest.getService().getSlaHours(),
				serviceRequest.getService().getTenantId(), serviceRequest.getService().getType(),
				serviceRequest.getRequestInfo().getUserInfo().getId(), new Date(new java.util.Date().getTime()), serviceRequest.getService().getCategory()
				};
		jdbcTemplate.update(complaintInsert, object);

		final String serviceInsert = ServiceTypeQueryBuilder.insertServiceTypeQuery();
		final Object[] obj = new Object[] { serviceRequest.getService().getServiceCode(),
				serviceRequest.getService().getTenantId(), serviceRequest.getRequestInfo().getUserInfo().getId(),
				new Date(new java.util.Date().getTime()) };
		jdbcTemplate.update(serviceInsert, obj);
		if (serviceRequest.getService().isMetadata()) {
			persistAttributeValues(serviceRequest);
		}
		return serviceRequest;
	}
	
	private void persistAttributeValues(ServiceRequest serviceRequest){
		final String serviceInsertAttribValues = ServiceTypeQueryBuilder.insertServiceTypeQueryAttribValues();
		List<Attribute> attributeList = new ArrayList<>() ;
		if(null != serviceRequest.getService().getAttributes()){
			attributeList = serviceRequest.getService().getAttributes();
		}
		for (int i = 0; i < attributeList.size(); i++) {
			Attribute attribute = attributeList.get(i);
			final Object[] obj1 = new Object[] { attribute.getCode(), attribute.getVariable()? "Y" : "N",
					attribute.getDatatype(), attribute.getDescription(), attribute.getDatatypeDescription(), serviceRequest.getService().getServiceCode(), attribute.getRequired()? "Y" : "N",
					serviceRequest.getService().getTenantId(),
					serviceRequest.getRequestInfo().getUserInfo().getId(),
					new Date(new java.util.Date().getTime()) };
			jdbcTemplate.update(serviceInsertAttribValues, obj1);
			if(attribute.getAttributes().size() > 0){
				final String valueInsertQuery = ServiceTypeQueryBuilder.insertValueDefinitionQuery();
				List<Value> valueList = attribute.getAttributes();
				for (int j = 0; j < valueList.size() ; j++) {
					Value value = valueList.get(j);
					final Object[] obj2 = new Object[] { 
							serviceRequest.getService().getServiceCode(), attribute.getCode(), value.getKey(), value.getName(), serviceRequest.getService().getTenantId(), 
							new Date(new java.util.Date().getTime()), serviceRequest.getRequestInfo().getUserInfo().getId() };
					jdbcTemplate.update(valueInsertQuery, obj2);
				}
			}
		}
	}

    public ServiceRequest persistModifyServiceType(final ServiceRequest serviceRequest) {
        LOGGER.info("Service Type Request::" + serviceRequest);
        final String serviceTypeUpdate = ServiceTypeQueryBuilder.updateServiceTypeQuery();
        final ServiceType serviceType = serviceRequest.getService();
        final Object[] obj = new Object[] { serviceType.getServiceName(),
        		serviceType.getDescription(), serviceType.getCategory(), serviceType.isActive(), serviceRequest.getRequestInfo().getUserInfo().getId(), 
                new Date(new java.util.Date().getTime()), serviceType.getServiceCode(), serviceType.getTenantId() };
        jdbcTemplate.update(serviceTypeUpdate, obj);
        final String valueRemove = ServiceTypeQueryBuilder.removeValueQuery();
        final Object[] objValueRemove = new Object[] { serviceType.getServiceCode()};
        jdbcTemplate.update(valueRemove, objValueRemove);
        final String attributeRemove = ServiceTypeQueryBuilder.removeAttributeQuery();
        final Object[] objAttributeRemove = new Object[] { serviceType.getServiceCode()};
        jdbcTemplate.update(attributeRemove, objAttributeRemove);
        if (serviceRequest.getService().isMetadata()) {
			persistAttributeValues(serviceRequest);
		}
        return serviceRequest;

    }

	public boolean checkServiceByNameAndCode(final String code, final String name, final String tenantId) {
		final List<Object> preparedStatementValues = new ArrayList<>();
		preparedStatementValues.add(name);
		preparedStatementValues.add(tenantId);
		preparedStatementValues.add(code);
		final String query = ServiceTypeQueryBuilder.selectServiceNameAndCodeQuery();
		final List<Map<String, Object>> serviceTypes = jdbcTemplate.queryForList(query,
				preparedStatementValues.toArray());
		if (!serviceTypes.isEmpty())
			return false;
		return true;
	}

    public List<ServiceType> findForCriteria(final ServiceGetRequest serviceTypeGetRequest) {
        final List<Object> preparedStatementValues = new ArrayList<>();
        final String queryStr = serviceTypeQueryBuilder.getAllServiceTypes();
        final List<ServiceType> serviceTypes = jdbcTemplate.query(queryStr, serviceTypeRowMapper);
        return serviceTypes;
    }

}
