package org.egov.swm.persistence.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.swm.domain.model.RouteCollectionPointMap;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RouteCollectionPointMapJdbcRepository extends JdbcRepository {

    public static final String TABLE_NAME = "egswm_routecollectionpointmap";

    @Transactional
    public void delete(final String tenantId, final String route) {
        delete(TABLE_NAME, tenantId, "route", route);
    }

    public List<RouteCollectionPointMap> search(final RouteCollectionPointMap searchRequest) {

        String searchQuery = "select * from " + TABLE_NAME + " :condition ";

        final Map<String, Object> paramValues = new HashMap<>();
        final StringBuffer params = new StringBuffer();

        if (searchRequest.getTenantId() != null) {
            addAnd(params);
            params.append("tenantId =:tenantId");
            paramValues.put("tenantId", searchRequest.getTenantId());
        }

        if (searchRequest.getRoute() != null && searchRequest.getRoute() != null) {
            addAnd(params);
            params.append("route =:route");
            paramValues.put("route", searchRequest.getRoute());
        }

        if (searchRequest.getRoutes() != null) {
            addAnd(params);
            params.append("route in (:routes)");
            paramValues.put("routes", new ArrayList<>(Arrays.asList(searchRequest.getRoutes().split(","))));
        }

        if (searchRequest.getCollectionPoint() != null && searchRequest.getCollectionPoint() != null) {
            addAnd(params);
            params.append("collectionPoint =:collectionPoint");
            paramValues.put("collectionPoint", searchRequest.getCollectionPoint());
        }

        if (params.length() > 0)
            searchQuery = searchQuery.replace(":condition", " where " + params.toString());
        else

            searchQuery = searchQuery.replace(":condition", "");

        final BeanPropertyRowMapper row = new BeanPropertyRowMapper(RouteCollectionPointMap.class);

        return namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);

    }

}