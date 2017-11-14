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

import org.egov.pgr.domain.model.Attribute;
import org.egov.pgr.domain.model.GrievanceType;
import org.egov.pgr.domain.model.Value;
import org.egov.pgr.repository.builder.GrievanceTypeQueryBuilder;
import org.egov.pgr.web.contract.ServiceGetRequest;
import org.egov.pgr.web.contract.ServiceRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Repository
public class GrievanceTypeRepository {

    public static final Logger LOGGER = LoggerFactory.getLogger(GrievanceTypeRepository.class);
    public static final String COMPLAINT = "complaint";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private GrievanceTypeQueryBuilder grievanceTypeQueryBuilder;

    public ServiceRequest persistServiceType(final ServiceRequest serviceRequest) {
        LOGGER.info("Service Type Request::" + serviceRequest);
        final String complaintInsert = grievanceTypeQueryBuilder.insertComplaintTypeQuery();
        boolean active = (null != serviceRequest.getService().getActive() ? serviceRequest.getService().getActive() : true);
        boolean days = (null != serviceRequest.getService().getDays() ? serviceRequest.getService().getDays() : false);
        final Object[] object = new Object[]{serviceRequest.getService().getServiceName(),
                serviceRequest.getService().getServiceCode(), serviceRequest.getService().getDescription(), active, serviceRequest.getService().getSlaHours(),
                serviceRequest.getService().getTenantId(), serviceRequest.getService().getType(),
                serviceRequest.getRequestInfo().getUserInfo().getId(), new Date(new java.util.Date().getTime()), serviceRequest.getService().getCategory(), days, serviceRequest.getService().getLocalName()
        };
        jdbcTemplate.update(complaintInsert, object);

        final String serviceInsert = grievanceTypeQueryBuilder.insertServiceTypeQuery();
        final Object[] obj = new Object[]{serviceRequest.getService().getServiceCode(),
                serviceRequest.getService().getTenantId(), serviceRequest.getRequestInfo().getUserInfo().getId(),
                new Date(new java.util.Date().getTime())};
        jdbcTemplate.update(serviceInsert, obj);
        if (serviceRequest.getService().isMetadata()) {
            persistAttributeValues(serviceRequest);
        }
        if (null != serviceRequest.getService().getKeywords()) {
            if (serviceRequest.getService().getKeywords().size() > 0) {
                persistKeywordServiceCodeMapping(serviceRequest);
            }
        }
        return serviceRequest;
    }

    private void persistAttributeValues(ServiceRequest serviceRequest) {
        final String serviceInsertAttribValues = GrievanceTypeQueryBuilder.insertServiceTypeQueryAttribValues();
        List<Attribute> attributeList = new ArrayList<>();
        if (null != serviceRequest.getService().getAttributes()) {
            attributeList = serviceRequest.getService().getAttributes();
        }
        for (int i = 0; i < attributeList.size(); i++) {
            Attribute attribute = attributeList.get(i);
            final Object[] obj1 = new Object[]{attribute.getCode(), attribute.getVariable() ? "Y" : "N",
                    attribute.getDatatype(), attribute.getDescription(), attribute.getDatatypeDescription(),
                    serviceRequest.getService().getServiceCode(), attribute.getRequired() ? "Y" : "N",
                    attribute.getGroupCode(), serviceRequest.getService().getTenantId(),
                    serviceRequest.getRequestInfo().getUserInfo().getId(), new Date(new java.util.Date().getTime())};
            jdbcTemplate.update(serviceInsertAttribValues, obj1);
            if (null != attribute.getAttributes()) {
                if (attribute.getAttributes().size() > 0) {
                    final String valueInsertQuery = GrievanceTypeQueryBuilder.insertValueDefinitionQuery();
                    List<Value> valueList = attribute.getAttributes();
                    for (int j = 0; j < valueList.size(); j++) {
                        Value value = valueList.get(j);
                        final Object[] obj2 = new Object[]{serviceRequest.getService().getServiceCode(),
                                attribute.getCode(), value.getKey(), value.getName(),
                                serviceRequest.getService().getTenantId(), new Date(new java.util.Date().getTime()),
                                serviceRequest.getRequestInfo().getUserInfo().getId()};
                        jdbcTemplate.update(valueInsertQuery, obj2);
                    }
                }
            }
        }
    }

    private void persistKeywordServiceCodeMapping(ServiceRequest serviceRequest) {
        GrievanceType grievanceType = serviceRequest.getService();
        final String serviceKeywordMappingQuery = GrievanceTypeQueryBuilder.insertServiceKeyworkMappingQuery();
        for (int i = 0; i < grievanceType.getKeywords().size(); i++) {
            final Object[] obj1 = new Object[]{grievanceType.getServiceCode(), grievanceType.getKeywords().get(i), grievanceType.getTenantId(),
                    serviceRequest.getRequestInfo().getUserInfo().getId(),
                    new Date(new java.util.Date().getTime())};
            jdbcTemplate.update(serviceKeywordMappingQuery, obj1);
        }
    }

    public ServiceRequest persistModifyServiceType(final ServiceRequest serviceRequest) {
        LOGGER.info("Service Type Request::" + serviceRequest);
        final String serviceTypeUpdate = GrievanceTypeQueryBuilder.updateServiceTypeQuery();
        final GrievanceType grievanceType = serviceRequest.getService();
        final Object[] obj = new Object[]{grievanceType.getServiceName(),
                grievanceType.getDescription(), grievanceType.getCategory(), grievanceType.getSlaHours(), grievanceType.getActive(), grievanceType.isHasFinancialImpact(), grievanceType.getDays(), serviceRequest.getRequestInfo().getUserInfo().getId(),
                new Date(new java.util.Date().getTime()), grievanceType.getLocalName(), grievanceType.getServiceCode(), grievanceType.getTenantId()};
        jdbcTemplate.update(serviceTypeUpdate, obj);
        final String valueRemove = GrievanceTypeQueryBuilder.removeValueQuery();
        final Object[] objValueRemove = new Object[]{grievanceType.getServiceCode(), grievanceType.getTenantId()};
        jdbcTemplate.update(valueRemove, objValueRemove);
        final String attributeRemove = GrievanceTypeQueryBuilder.removeAttributeQuery();
        final Object[] objAttributeRemove = new Object[]{grievanceType.getServiceCode(), grievanceType.getTenantId()};
        jdbcTemplate.update(attributeRemove, objAttributeRemove);
        final String serviceKeywordRemove = GrievanceTypeQueryBuilder.removeServiceKeywordMapping();
        final Object[] objserviceKeywordRemove = new Object[]{grievanceType.getServiceCode(), grievanceType.getTenantId()};
        jdbcTemplate.update(serviceKeywordRemove, objserviceKeywordRemove);
        if (serviceRequest.getService().isMetadata()) {
            persistAttributeValues(serviceRequest);
        }
        if (null != serviceRequest.getService().getKeywords()) {
            if (serviceRequest.getService().getKeywords().size() > 0) {
                persistKeywordServiceCodeMapping(serviceRequest);
            }
        }
        return serviceRequest;

    }

    public boolean checkServiceByNameAndCode(final String code, final String name, final String tenantId) {
        final List<Object> preparedStatementValues = new ArrayList<>();
        preparedStatementValues.add(name);
        preparedStatementValues.add(tenantId);
        preparedStatementValues.add(code);
        final String query = GrievanceTypeQueryBuilder.selectServiceNameAndCodeQuery();
        final List<Map<String, Object>> serviceTypes = jdbcTemplate.queryForList(query,
                preparedStatementValues.toArray());
        if (!serviceTypes.isEmpty())
            return false;
        return true;
    }

    public boolean checkServiceCodeIfExists(final String serviceCode, final String tenantId) {
        final List<Object> preparedStatementValues = new ArrayList<>();
        preparedStatementValues.add(serviceCode);
        preparedStatementValues.add(tenantId);
        final String query = GrievanceTypeQueryBuilder.checkServiceCodeIfExists();
        final List<Map<String, Object>> serviceTypes = jdbcTemplate.queryForList(query,
                preparedStatementValues.toArray());
        final String query2 = GrievanceTypeQueryBuilder.checkComplaintCodeIfExists();
        final List<Map<String, Object>> serviceTypes2 = jdbcTemplate.queryForList(query2,
                preparedStatementValues.toArray());

        if (!serviceTypes.isEmpty() && !serviceTypes2.isEmpty())
            return true;
        return false;
    }

    public boolean checkComplaintNameIfExists(final String serviceName, final String tenantId, final String serviceCode, String mode) {
        final List<Object> preparedStatementValues = new ArrayList<>();
        preparedStatementValues.add(serviceName.toUpperCase().trim());
        preparedStatementValues.add(tenantId);

        final String query = GrievanceTypeQueryBuilder.checkServiceNameIfExists();
        final List<Map<String, Object>> serviceTypes = jdbcTemplate.queryForList(query,
                preparedStatementValues.toArray());
        if (!serviceTypes.isEmpty() && "update".equalsIgnoreCase(mode)) {
            String codeFromDB = (String) serviceTypes.get(0).get("code");
            if (!codeFromDB.equalsIgnoreCase(serviceCode))
                return true;
        }
        if (!serviceTypes.isEmpty() && "create".equalsIgnoreCase(mode)) {
            return true;
        }
        return false;
    }

    public boolean checkComplaintCodeNameIfExists(final String serviceName, final String tenantId, final String serviceCode, String mode) {
        final List<Object> preparedStatementValues = new ArrayList<>();
        preparedStatementValues.add(serviceName.toUpperCase().trim());
        preparedStatementValues.add(serviceCode.toUpperCase().trim());
        preparedStatementValues.add(tenantId);

        final String query = GrievanceTypeQueryBuilder.checkServiceCodeNameIfExists();
        final List<Map<String, Object>> serviceTypes = jdbcTemplate.queryForList(query,
                preparedStatementValues.toArray());
        if (!serviceTypes.isEmpty() && "create".equalsIgnoreCase(mode)) {
            return true;
        }
        return false;
    }

    //search only complaint types
    public List<GrievanceType> findForCriteria(final ServiceGetRequest serviceTypeGetRequest) {
        final List<Object> preparedStatementValues = new ArrayList<>();

        String queryStr = grievanceTypeQueryBuilder.getQuery(serviceTypeGetRequest, preparedStatementValues);
        List<GrievanceType> grievanceTypes = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), new BeanPropertyRowMapper<>(GrievanceType.class));

        grievanceTypes.stream().forEach(grievanceType -> grievanceType.setKeywords(Arrays.asList(COMPLAINT)));

        return grievanceTypes;
    }

}


