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
import org.egov.workflow.persistence.repository.builder.RouterQueryBuilder;
import org.egov.workflow.web.contract.RouterRequest;
import org.egov.workflow.web.contract.RouterSearch;
import org.egov.workflow.web.contract.RouterSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
        Object[] obj;
        if (serviceCodeExists) {
            routerInsert = RouterQueryBuilder.insertRouter();
            final PersistRouter persistRouter = routerReq.getRouterType();
            obj = new Object[]{persistRouter.getService(), persistRouter.getPosition(), persistRouter.getBoundary(),
                0, routerReq.getRequestInfo().getUserInfo().getId(), new Date(),
                routerReq.getRequestInfo().getUserInfo().getId(), new Date(),
                persistRouter.getTenantId()};

        } else {
            routerInsert = RouterQueryBuilder.insertRouterWithoutService();
            final PersistRouter persistRouter = routerReq.getRouterType();
            obj = new Object[]{persistRouter.getPosition(), persistRouter.getBoundary(), 0,
                routerReq.getRequestInfo().getUserInfo().getId(), new Date(),
                routerReq.getRequestInfo().getUserInfo().getId(), new Date(),
                persistRouter.getTenantId()};

        }
        LOGGER.info("Router Insert Query::" + routerInsert);
        jdbcTemplate.update(routerInsert, obj);

        return routerReq;
    }

    public PersistRouterReq updateRouter(final PersistRouterReq routerReq, boolean serviceCodeExists) {

        LOGGER.info("Update Router Request::" + routerReq);
        String routerUpdate;
        PersistRouter persistRouter = routerReq.getRouterType();
        Object[] obj;
        if (serviceCodeExists) {
            if (null != routerReq.getRouterType().getService()) {
                routerUpdate = RouterQueryBuilder.updateRouter();
                obj = new Object[]{persistRouter.getPosition(), 0,
                    routerReq.getRequestInfo().getUserInfo().getId(), new Date(),
                    routerReq.getRequestInfo().getUserInfo().getId(), new Date(), persistRouter.getActive(),
                    persistRouter.getBoundary(), persistRouter.getService(), persistRouter.getTenantId()};
            } else {
                routerUpdate = RouterQueryBuilder.updateRouterWithoutService();
                obj = new Object[]{persistRouter.getPosition(), 0,
                    routerReq.getRequestInfo().getUserInfo().getId(), new Date(),
                    routerReq.getRequestInfo().getUserInfo().getId(), new Date(), persistRouter.getActive(),
                    persistRouter.getBoundary(), persistRouter.getTenantId()};
            }

        } else {
            routerUpdate = RouterQueryBuilder.updateRouterWithoutService();
            obj = new Object[]{persistRouter.getPosition(), 0,
                routerReq.getRequestInfo().getUserInfo().getId(), new Date(),
                routerReq.getRequestInfo().getUserInfo().getId(), new Date(), persistRouter.getActive(),
                persistRouter.getBoundary(), persistRouter.getTenantId()};
        }
        jdbcTemplate.update(routerUpdate, obj);
        return routerReq;
    }

    public PersistRouter ValidateRouter(final PersistRouterReq routerReq, boolean status) {
        String validateQuery;
        PersistRouter persistRouter;
        if (status) {
            LOGGER.info("with service validation");
            if (null != routerReq.getRouterType().getService()) {
                validateQuery = RouterQueryBuilder.validateRouter();
            } else {
                validateQuery = RouterQueryBuilder.validateRouterWithoutService();
            }
            try {
                if (null != routerReq.getRouterType().getService()) {
                    persistRouter = jdbcTemplate.queryForObject(
                        validateQuery, new Object[]{routerReq.getRouterType().getService(),
                            routerReq.getRouterType().getBoundary(), routerReq.getRouterType().getTenantId()},
                        new BeanPropertyRowMapper<>(PersistRouter.class));
                } else {
                    persistRouter = jdbcTemplate.queryForObject(
                        validateQuery, new Object[]{routerReq.getRouterType().getBoundary(), routerReq.getRouterType().getTenantId()},
                        new BeanPropertyRowMapper<>(PersistRouter.class));
                }
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
                    new BeanPropertyRowMapper<>(PersistRouter.class));
                LOGGER.info("Value coming from validate query boundary::" + persistRouter.getBoundary());
            } catch (EmptyResultDataAccessException e) {
                return null;
            }
        }
        return persistRouter;
    }

    public List<RouterSearch> findForCriteria(final RouterSearchRequest routerSearchRequest) {
        final List<Object> preparedStatementValues = new ArrayList<>();
        final String queryStr = routerQueryBuilder.getQuery(routerSearchRequest, preparedStatementValues);
        List<RouterSearch> routerTypes = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), new BeanPropertyRowMapper<>(RouterSearch.class));
        return routerTypes;
    }

    public List<RouterSearch> findForHierarchyType(final RouterSearchRequest routerSearchRequest) {
        final List<Object> preparedStatementValues = new ArrayList<>();
        preparedStatementValues.add(routerSearchRequest.getHierarchyType());
        preparedStatementValues.add(routerSearchRequest.getTenantId());
        final String queryStr = routerQueryBuilder.getHierarchyTypeQuery();
        List<RouterSearch> routerTypes = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), new BeanPropertyRowMapper<>(RouterSearch.class));

        return routerTypes;
    }

    public boolean checkCombinationExists(RouterRequest routerRequest) {
        final List<Object> preparedStatementValues = new ArrayList<>();

        String finalQuery = routerQueryBuilder.checkCombinationExistsQuery(routerRequest);
        LOGGER.info("Verification Query : " + finalQuery);
        List<Integer> count = jdbcTemplate.queryForList(finalQuery, Integer.class);
        LOGGER.info("Count: " + count.toString());

        String finalIdQuery = routerQueryBuilder.checkIDQuery(routerRequest);
        List<Map<String, Object>> id = jdbcTemplate.queryForList(finalIdQuery, preparedStatementValues.toArray());
        if (count.size() > 0 && id.size() != 0) {
            Long idFromDb = (Long) id.get(0).get("id");
            return count.get(0) > 0 && idFromDb != routerRequest.getRouter().getId();
        }
        return false;
    }

}
