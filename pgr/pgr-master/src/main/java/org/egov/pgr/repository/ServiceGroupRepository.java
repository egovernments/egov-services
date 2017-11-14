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

import org.egov.pgr.domain.model.ServiceGroup;
import org.egov.pgr.domain.model.ServiceType;
import org.egov.pgr.domain.model.ServiceTypeSearchCriteria;
import org.egov.pgr.domain.service.ServiceTypeService;
import org.egov.pgr.repository.builder.ServiceGroupQueryBuilder;
import org.egov.pgr.repository.rowmapper.ServiceGroupRowMapper;
import org.egov.pgr.web.contract.ServiceGroupGetRequest;
import org.egov.pgr.web.contract.ServiceGroupRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//import org.egov.pgrrest.master.repository.rowmapper.CategoryTypeRowMapper;

@Repository
public class ServiceGroupRepository {

    public static final Logger LOGGER = LoggerFactory.getLogger(ServiceGroupRepository.class);

    private static final String[] taskAction = {"create", "update"};

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ServiceGroupRowMapper serviceGroupMapper;

    @Autowired
    private ServiceGroupQueryBuilder serviceGroupQueryBuilder;

    @Autowired
    private ServiceTypeService serviceTypeService;

    public ServiceGroupRequest persistCreateServiceGroup(final ServiceGroupRequest serviceGroupRequest) {
        LOGGER.info("ServiceGroupRequest::" + serviceGroupRequest);
        final String serviceGroupInsert = serviceGroupQueryBuilder.insertServiceGroupQuery();
        final ServiceGroup serviceGroup = serviceGroupRequest.getServiceGroup();
        final Object[] obj = new Object[]{serviceGroup.getCode(), serviceGroup.getName(),
                serviceGroup.getDescription(), serviceGroup.getActive(),
                Long.valueOf(serviceGroupRequest.getRequestInfo().getUserInfo().getId()),
                Long.valueOf(serviceGroupRequest.getRequestInfo().getUserInfo().getId()),
                new Date(new java.util.Date().getTime()), new Date(new java.util.Date().getTime()),
                serviceGroup.getTenantId(), serviceGroup.getKeyword(), serviceGroup.getLocalName()};
        jdbcTemplate.update(serviceGroupInsert, obj);
        return serviceGroupRequest;
    }

    public ServiceGroupRequest persistUpdateServiceGroup(final ServiceGroupRequest serviceGroupRequest) {
        LOGGER.info("ServiceGroupRequest::" + serviceGroupRequest);

        if (serviceGroupRequest != null && serviceGroupRequest.getServiceGroup() != null
                && serviceGroupRequest.getServiceGroup().getActive() != null
                && !serviceGroupRequest.getServiceGroup().getActive())
            disableServiceTypes(serviceGroupRequest.getServiceGroup().getKeyword(),
                    serviceGroupRequest.getServiceGroup().getId(),
                    serviceGroupRequest.getServiceGroup().getTenantId());

        final String serviceGroupUpdate = serviceGroupQueryBuilder.updateServiceGroupQuery();
        final ServiceGroup serviceGroup = serviceGroupRequest.getServiceGroup();
        final Object[] obj = new Object[]{serviceGroup.getName(), serviceGroup.getDescription(),
                serviceGroup.getActive(), Long.valueOf(serviceGroupRequest.getRequestInfo().getUserInfo().getId()),
                Long.valueOf(serviceGroupRequest.getRequestInfo().getUserInfo().getId()),
                new Date(new java.util.Date().getTime()), new Date(new java.util.Date().getTime()),
                serviceGroup.getTenantId(), serviceGroup.getKeyword(), serviceGroup.getLocalName(), serviceGroup.getCode()};
        jdbcTemplate.update(serviceGroupUpdate, obj);
        return serviceGroupRequest;
    }

    private void disableServiceTypes(String keyword, Long id, String tenantId) {

        List<ServiceType> serviceTyes = serviceTypeService
                .search(ServiceTypeSearchCriteria.builder().category(id.intValue()).tenantId(tenantId).build());
        if (serviceTyes != null)
            for (ServiceType type : serviceTyes) {
                type.setActive(false);
                serviceTypeService.persistForUpdate(type);
            }
    }

    public List<ServiceGroup> getAllServiceGroup(ServiceGroupGetRequest serviceGroupGetRequest) {
        LOGGER.info("Requesting the Service Group List for the tenant ID :" + serviceGroupGetRequest.getTenantId());
        final List<Object> preparedStatementValues = new ArrayList<>();
        String getQuery = serviceGroupQueryBuilder.getQuery(serviceGroupGetRequest, preparedStatementValues);
        final List<ServiceGroup> serviceGroupTypes = jdbcTemplate.query(getQuery, preparedStatementValues.toArray(),
                serviceGroupMapper);
        return serviceGroupTypes;
    }

    public boolean verifyRequestUniqueness(ServiceGroupRequest serviceGroupRequest, String action) {
        final List<Object> preparedStatementValues = new ArrayList<>();

        String tenantId = serviceGroupRequest.getServiceGroup().getTenantId().trim();
        String code = serviceGroupRequest.getServiceGroup().getCode().toUpperCase().trim();
        String name = serviceGroupRequest.getServiceGroup().getName().toUpperCase().trim();
        preparedStatementValues.add(tenantId);
        String query = "";
        if (code != null && code != "" && name != null && name != "") {

            preparedStatementValues.add(code);
            preparedStatementValues.add(name);
            query = serviceGroupQueryBuilder.checkIfAvailable();

        }
        final List<Map<String, Object>> centerTypes = jdbcTemplate.queryForList(query,
                preparedStatementValues.toArray());
        if (!centerTypes.isEmpty() && "update".equalsIgnoreCase(action)) {
            String codeFromDB = (String) centerTypes.get(0).get("code");
            if (!codeFromDB.equalsIgnoreCase(code))
                return true;
        }
        if (!centerTypes.isEmpty() && "create".equalsIgnoreCase(action)) {
            return true;
        }
        return false;
    }

    public boolean verifyIfNameAlreadyExists(ServiceGroupRequest serviceGroupRequest, String action) {
        List<Object> preparedStatementValues = new ArrayList<>();
        preparedStatementValues.add(serviceGroupRequest.getServiceGroup().getName().toUpperCase().trim());
        preparedStatementValues.add(serviceGroupRequest.getServiceGroup().getTenantId());
        String checkQuery = "";
        if (action.equals(taskAction[0])) {
            checkQuery = serviceGroupQueryBuilder.checkIfNameTenantIdAvailable();
        } else {
            preparedStatementValues.add(serviceGroupRequest.getServiceGroup().getId());
            checkQuery = serviceGroupQueryBuilder.checkIfNameTenantIdAvailableUpdate();
        }
        final List<Integer> count = jdbcTemplate.queryForList(checkQuery, preparedStatementValues.toArray(),
                Integer.class);
        if (count.size() > 0) {
            if (count.get(0) > 0) {
                return true;
            }
        }
        return false;
    }

    public boolean verifyIfCodeAlreadyExists(ServiceGroupRequest serviceGroupRequest, String action) {
        List<Object> preparedStatementValues = new ArrayList<>();
        preparedStatementValues.add(serviceGroupRequest.getServiceGroup().getCode().toUpperCase().trim());
        preparedStatementValues.add(serviceGroupRequest.getServiceGroup().getTenantId());
        String checkQuery = "";
        if (action.equals(taskAction[0])) {
            checkQuery = serviceGroupQueryBuilder.checkIfCodeTenantIdAvailable();
        } else {
            preparedStatementValues.add(serviceGroupRequest.getServiceGroup().getId());
            checkQuery = serviceGroupQueryBuilder.checkIfCodeTenantIdAvailableUpdate();
        }
        final List<Integer> count = jdbcTemplate.queryForList(checkQuery, preparedStatementValues.toArray(),
                Integer.class);
        if (count.size() > 0) {
            if (count.get(0) > 0) {
                return true;
            }
        }
        return false;
    }

}
