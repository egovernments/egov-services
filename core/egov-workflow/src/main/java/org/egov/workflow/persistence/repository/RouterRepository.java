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
package org.egov.workflow.persistence.repository;

import org.egov.workflow.domain.model.PersistRouter;
import org.egov.workflow.domain.model.PersistRouterReq;
import org.egov.workflow.domain.model.ServiceType;
import org.egov.workflow.persistence.repository.builder.RouterQueryBuilder;
import org.egov.workflow.persistence.repository.rowmapper.PersistRouteRowMapper;
import org.egov.workflow.persistence.repository.rowmapper.RouterRowMapper;
import org.egov.workflow.web.contract.RouterType;
import org.egov.workflow.web.contract.RouterTypeGetReq;
import org.egov.workflow.web.contract.RouterTypeReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.Map.Entry;

@Repository
public class RouterRepository {

    public static final Logger LOGGER = LoggerFactory.getLogger(RouterRepository.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RouterQueryBuilder routerQueryBuilder;


    public PersistRouterReq createRouter(final PersistRouterReq routerReq, boolean serviceCodeExists) {

        LOGGER.info("Router Request::" + routerReq);
        String routerInsert = "";
        Object[] obj = new Object[]{};
        if (serviceCodeExists) {

            routerInsert = RouterQueryBuilder.insertRouter();
            final PersistRouter persistRouter = routerReq.getRouterType();
            obj = new Object[]{persistRouter.getService(), persistRouter.getPosition(), persistRouter.getBoundary(),
                0, Long.valueOf(routerReq.getRequestInfo().getUserInfo().getId()), new Date(),
                Long.valueOf(routerReq.getRequestInfo().getUserInfo().getId()), new Date(),
                persistRouter.getTenantId()};

        } else {
            routerInsert = RouterQueryBuilder.insertRouterWithoutService();
            final PersistRouter persistRouter = routerReq.getRouterType();
            obj = new Object[]{persistRouter.getPosition(), persistRouter.getBoundary(), 0,
                Long.valueOf(routerReq.getRequestInfo().getUserInfo().getId()), new Date(),
                Long.valueOf(routerReq.getRequestInfo().getUserInfo().getId()), new Date(),
                persistRouter.getTenantId()};

        }
        LOGGER.info("Router Insert Query::" + routerInsert);
        jdbcTemplate.update(routerInsert, obj);

        return routerReq;
    }

    public PersistRouterReq updateRouter(final PersistRouterReq routerReq, boolean serviceCodeExists) {

        LOGGER.info("Update Router Request::" + routerReq);
        String routerUpdate = "";
        PersistRouter persistRouter = routerReq.getRouterType();
        Object[] obj = new Object[]{};
        if (serviceCodeExists) {
            routerUpdate = RouterQueryBuilder.updateRouter();
            obj = new Object[]{persistRouter.getPosition(), 0,
                Long.valueOf(routerReq.getRequestInfo().getUserInfo().getId()), new Date(),
                Long.valueOf(routerReq.getRequestInfo().getUserInfo().getId()), new Date(), persistRouter.getActive(),
                persistRouter.getBoundary(), persistRouter.getService(), persistRouter.getTenantId()};
        } else {
            routerUpdate = RouterQueryBuilder.updateRouterWithoutService();
            obj = new Object[]{persistRouter.getPosition(), 0,
                Long.valueOf(routerReq.getRequestInfo().getUserInfo().getId()), new Date(),
                Long.valueOf(routerReq.getRequestInfo().getUserInfo().getId()), new Date(), persistRouter.getActive(),
                persistRouter.getBoundary(), persistRouter.getTenantId()};
        }
        jdbcTemplate.update(routerUpdate, obj);
        return routerReq;
    }

    public PersistRouter ValidateRouter(final PersistRouterReq routerReq, boolean status) {
        String validateQuery = "";
        PersistRouter persistRouter = new PersistRouter();
        if (status) {
            LOGGER.info("with service validation");
            validateQuery = RouterQueryBuilder.validateRouter();
            try {
                persistRouter = jdbcTemplate.queryForObject(
                    validateQuery, new Object[]{routerReq.getRouterType().getService(),
                        routerReq.getRouterType().getBoundary(), routerReq.getRouterType().getTenantId()},
                    new PersistRouteRowMapper());
                LOGGER.info("Value coming from validate query boundary::" + persistRouter.getBoundary());
            } catch (EmptyResultDataAccessException e) {
                return null;
            }
        } else {
            LOGGER.info("without service validation");
            validateQuery = RouterQueryBuilder.validateRouterWithoutService();
            try {
                persistRouter = jdbcTemplate.queryForObject(
                    validateQuery, new Object[]{routerReq.getRouterType().getBoundary(), routerReq.getRouterType().getTenantId()},
                    new PersistRouteRowMapper());
                LOGGER.info("Value coming from validate query boundary::" + persistRouter.getBoundary());
            } catch (EmptyResultDataAccessException e) {
                return null;
            }
        }
        return persistRouter;
    }

    public List<RouterType> findForCriteria(final RouterTypeGetReq routerTypeGetRequest) {
        final List<Object> preparedStatementValues = new ArrayList<>();
        final String queryStr = routerQueryBuilder.getQuery(routerTypeGetRequest, preparedStatementValues);
        RouterRowMapper routerRowMapper = new RouterRowMapper();
        jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), routerRowMapper);
        return prepareRouterTypeList(routerRowMapper);
    }

    public List<RouterType> findForHierarchyType(final RouterTypeGetReq routerTypeGetRequest) {
        final List<Object> preparedStatementValues = new ArrayList<>();
        preparedStatementValues.add(routerTypeGetRequest.getHierarchyType());
        preparedStatementValues.add(routerTypeGetRequest.getTenantId());
        final String queryStr = routerQueryBuilder.getHierarchyTypeQuery();
        List<RouterType> routerTypes = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), new BeanPropertyRowMapper<>(RouterType.class));

        return routerTypes;
    }

    private List<RouterType> prepareRouterTypeList(RouterRowMapper rowMapper) {
        /*Map<String, List<Value>> attribValue = rowMapper.attribValue;
        Map<String, Map<String, Attribute>> serviceAttrib = rowMapper.serviceAttrib;*/
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
                Iterator<ServiceType> serviceItr = serviceTypeList.iterator();
                ServiceType serviceType = new ServiceType();
                while (serviceItr.hasNext()) {
                    serviceType = serviceItr.next();
                    // Removed as the Attributes and Values are not being considered in the
                    // router search API.
                    // If Attributes and Values are required in future, enable these commented code
                    /*Map<String, Attribute> innerAttrMap = serviceAttrib.get(serviceType.getServiceCode());
                    Iterator<Entry<String, Attribute>> innerAttrItr = innerAttrMap.entrySet().iterator();
					List<Attribute> finalAttributeList = new ArrayList<>();
					while (innerAttrItr.hasNext()) {
						Entry<String,Attribute> attrEntry = innerAttrItr.next();
						List<Value> valueList = attribValue.get(attrEntry.getValue().getCode());
						attrEntry.getValue().setAttributes(valueList);
						finalAttributeList.add(attrEntry.getValue());
					}
					serviceType.setAttributes(finalAttributeList);*/
                }
                routerType.setService(serviceType);
                routerTypes.add(routerType);
            }
        }
        return routerTypes;
    }

    public boolean verifyUniquenessOfRequest(RouterTypeReq routerTypeReq) {
        String finalQuery = routerQueryBuilder.getVerificationQuery(routerTypeReq);
        LOGGER.info("Verification Query : " + finalQuery);
        List<Integer> count = jdbcTemplate.queryForList(finalQuery, Integer.class);
        LOGGER.info("Count: " + count.toString());
        if (count.size() > 0) {
            if (count.get(0) > 0) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public boolean checkCombinationExists(RouterTypeReq routerTypeReq) {
        final List<Object> preparedStatementValues = new ArrayList<>();

        String finalQuery = routerQueryBuilder.checkCombinationExistsQuery(routerTypeReq);
        LOGGER.info("Verification Query : " + finalQuery);
        List<Integer> count = jdbcTemplate.queryForList(finalQuery, Integer.class);
        LOGGER.info("Count: " + count.toString());

        String finalIdQuery = routerQueryBuilder.checkIDQuery(routerTypeReq);
        List<Map<String, Object>> id = jdbcTemplate.queryForList(finalIdQuery, preparedStatementValues.toArray());
        if (count.size() > 0 && id.size() != 0) {
            Long idFromDb = (Long) id.get(0).get("id");
            if (count.get(0) > 0 && idFromDb != routerTypeReq.getRouterType().getId()) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }


}
