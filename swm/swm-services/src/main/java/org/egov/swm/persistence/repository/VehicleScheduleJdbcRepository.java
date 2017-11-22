package org.egov.swm.persistence.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.Route;
import org.egov.swm.domain.model.RouteSearch;
import org.egov.swm.domain.model.Vehicle;
import org.egov.swm.domain.model.VehicleSchedule;
import org.egov.swm.domain.model.VehicleScheduleSearch;
import org.egov.swm.domain.model.VehicleSearch;
import org.egov.swm.domain.service.RouteService;
import org.egov.swm.domain.service.VehicleService;
import org.egov.swm.persistence.entity.VehicleScheduleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class VehicleScheduleJdbcRepository extends JdbcRepository {

    public static final String TABLE_NAME = "egswm_vehicleschedule";

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private RouteService routeService;

    public Boolean uniqueCheck(final String tenantId, final String fieldName, final String fieldValue,
            final String uniqueFieldName,
            final String uniqueFieldValue) {

        return uniqueCheck(TABLE_NAME, tenantId, fieldName, fieldValue, uniqueFieldName, uniqueFieldValue);
    }

    public Pagination<VehicleSchedule> search(final VehicleScheduleSearch searchRequest) {

        String searchQuery = "select * from " + TABLE_NAME + " :condition  :orderby ";

        final Map<String, Object> paramValues = new HashMap<>();
        final StringBuffer params = new StringBuffer();

        if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
            validateSortByOrder(searchRequest.getSortBy());
            validateEntityFieldName(searchRequest.getSortBy(), VehicleScheduleSearch.class);
        }

        String orderBy = "order by transactionNo";
        if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty())
            orderBy = "order by " + searchRequest.getSortBy();

        if (searchRequest.getTransactionNo() != null) {
            addAnd(params);
            params.append("transactionNo in (:transactionNo)");
            paramValues.put("transactionNo", searchRequest.getTransactionNo());
        }

        if (searchRequest.getTransactionNos() != null) {
            addAnd(params);
            params.append("transactionNo in (:transactionNos)");
            paramValues.put("transactionNos",
                    new ArrayList<>(Arrays.asList(searchRequest.getTransactionNos().split(","))));
        }
        if (searchRequest.getTenantId() != null) {
            addAnd(params);
            params.append("tenantId =:tenantId");
            paramValues.put("tenantId", searchRequest.getTenantId());
        }

        if (searchRequest.getRouteCode() != null) {
            addAnd(params);
            params.append("route =:route");
            paramValues.put("route", searchRequest.getRouteCode());
        }

        if (searchRequest.getRegNumber() != null) {
            addAnd(params);
            params.append("vehicle =:vehicle");
            paramValues.put("vehicle", searchRequest.getRegNumber());
        }

        if (searchRequest.getScheduledFrom() != null) {
            addAnd(params);
            params.append("scheduledFrom =:scheduledFrom");
            paramValues.put("scheduledFrom", searchRequest.getScheduledFrom());
        }

        if (searchRequest.getScheduledTo() != null) {
            addAnd(params);
            params.append("scheduledTo =:scheduledTo");
            paramValues.put("scheduledTo", searchRequest.getScheduledTo());
        }

        Pagination<VehicleSchedule> page = new Pagination<>();
        if (searchRequest.getOffset() != null)
            page.setOffset(searchRequest.getOffset());
        if (searchRequest.getPageSize() != null)
            page.setPageSize(searchRequest.getPageSize());

        if (params.length() > 0)
            searchQuery = searchQuery.replace(":condition", " where " + params.toString());
        else

            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        page = (Pagination<VehicleSchedule>) getPagination(searchQuery, page, paramValues);
        searchQuery = searchQuery + " :pagination";

        searchQuery = searchQuery.replace(":pagination",
                "limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

        final BeanPropertyRowMapper row = new BeanPropertyRowMapper(VehicleScheduleEntity.class);

        final List<VehicleSchedule> vehicleScheduleList = new ArrayList<>();

        final List<VehicleScheduleEntity> vehicleScheduleEntities = namedParameterJdbcTemplate.query(searchQuery.toString(),
                paramValues, row);

        VehicleSchedule vehicleSchedule;
        RouteSearch routeSearch;
        Pagination<Route> routes;
        VehicleSearch vehicleSearch;
        Pagination<Vehicle> vehicleList;

        for (final VehicleScheduleEntity vehicleScheduleEntity : vehicleScheduleEntities) {

            vehicleSchedule = vehicleScheduleEntity.toDomain();

            if (vehicleSchedule.getVehicle() != null && vehicleSchedule.getVehicle().getRegNumber() != null
                    && !vehicleSchedule.getVehicle().getRegNumber().isEmpty()) {

                vehicleSearch = new VehicleSearch();
                vehicleSearch.setTenantId(vehicleSchedule.getTenantId());
                vehicleSearch.setRegNumber(vehicleSchedule.getVehicle().getRegNumber());
                vehicleList = vehicleService.search(vehicleSearch);

                if (vehicleList != null && vehicleList.getPagedData() != null && !vehicleList.getPagedData().isEmpty())
                    vehicleSchedule.setVehicle(vehicleList.getPagedData().get(0));

            }

            if (vehicleSchedule.getRoute() != null && vehicleSchedule.getRoute().getCode() != null
                    && !vehicleSchedule.getRoute().getCode().isEmpty()) {

                routeSearch = new RouteSearch();
                routeSearch.setTenantId(vehicleSchedule.getTenantId());
                routeSearch.setCode(vehicleSchedule.getRoute().getCode());
                routes = routeService.search(routeSearch);

                if (routes != null && routes.getPagedData() != null && !routes.getPagedData().isEmpty())
                    vehicleSchedule.setRoute(routes.getPagedData().get(0));

            }

            vehicleScheduleList.add(vehicleSchedule);

        }

        page.setTotalResults(vehicleScheduleList.size());

        page.setPagedData(vehicleScheduleList);

        return page;
    }

}