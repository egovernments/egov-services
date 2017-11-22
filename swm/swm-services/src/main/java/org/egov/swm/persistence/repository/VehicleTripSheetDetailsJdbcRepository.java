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
import org.egov.swm.domain.model.VehicleSearch;
import org.egov.swm.domain.model.VehicleTripSheetDetails;
import org.egov.swm.domain.model.VehicleTripSheetDetailsSearch;
import org.egov.swm.domain.model.Vendor;
import org.egov.swm.domain.model.VendorSearch;
import org.egov.swm.domain.service.RouteService;
import org.egov.swm.domain.service.VehicleService;
import org.egov.swm.domain.service.VendorService;
import org.egov.swm.persistence.entity.VehicleTripSheetDetailsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class VehicleTripSheetDetailsJdbcRepository extends JdbcRepository {

    public static final String TABLE_NAME = "egswm_vehicletripsheetdetails";

    @Autowired
    private RouteService routeService;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private VendorService vendorService;

    public Boolean uniqueCheck(final String tenantId, final String fieldName, final String fieldValue,
            final String uniqueFieldName,
            final String uniqueFieldValue) {

        return uniqueCheck(TABLE_NAME, tenantId, fieldName, fieldValue, uniqueFieldName, uniqueFieldValue);
    }

    public Pagination<VehicleTripSheetDetails> search(final VehicleTripSheetDetailsSearch searchRequest) {

        String searchQuery = "select * from " + TABLE_NAME + " :condition  :orderby ";

        final Map<String, Object> paramValues = new HashMap<>();
        final StringBuffer params = new StringBuffer();

        if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
            validateSortByOrder(searchRequest.getSortBy());
            validateEntityFieldName(searchRequest.getSortBy(), VehicleTripSheetDetailsSearch.class);
        }

        String orderBy = "order by tripNo";
        if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty())
            orderBy = "order by " + searchRequest.getSortBy();

        if (searchRequest.getTripNos() != null) {
            addAnd(params);
            params.append("tripNo in(:tripNo) ");
            paramValues.put("tripNo", new ArrayList<>(Arrays.asList(searchRequest.getTripNos().split(","))));
        }

        if (searchRequest.getTenantId() != null) {
            addAnd(params);
            params.append("tenantId =:tenantId");
            paramValues.put("tenantId", searchRequest.getTenantId());
        }
        if (searchRequest.getRegNumber() != null) {
            addAnd(params);
            params.append("vehicle =:vehicle");
            paramValues.put("vehicle", searchRequest.getRegNumber());
        }

        if (searchRequest.getRouteCode() != null) {

            addAnd(params);
            params.append("route =:route");
            paramValues.put("route", searchRequest.getRouteCode());
        }

        if (searchRequest.getVendorNo() != null) {

            addAnd(params);
            params.append("vendor =:vendor");
            paramValues.put("vendor", searchRequest.getVendorNo());

        }

        if (searchRequest.getTripStartDate() != null) {
            addAnd(params);
            params.append("tripStartDate =:tripStartDate");
            paramValues.put("tripStartDate", searchRequest.getTripStartDate());
        }

        if (searchRequest.getTripEndDate() != null) {
            addAnd(params);
            params.append("tripEndDate =:tripEndDate");
            paramValues.put("tripEndDate", searchRequest.getTripEndDate());
        }

        Pagination<VehicleTripSheetDetails> page = new Pagination<>();
        if (searchRequest.getOffset() != null)
            page.setOffset(searchRequest.getOffset());
        if (searchRequest.getPageSize() != null)
            page.setPageSize(searchRequest.getPageSize());

        if (params.length() > 0)
            searchQuery = searchQuery.replace(":condition", " where " + params.toString());
        else

            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        page = (Pagination

        <VehicleTripSheetDetails>) getPagination(searchQuery, page, paramValues);
        searchQuery = searchQuery + " :pagination";

        searchQuery = searchQuery.replace(":pagination",
                "limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

        final BeanPropertyRowMapper row = new BeanPropertyRowMapper(VehicleTripSheetDetailsEntity.class);

        final List<VehicleTripSheetDetails> vehicleTripSheetDetailsList = new ArrayList<>();

        final List<VehicleTripSheetDetailsEntity> vehicleTripSheetDetailsEntities = namedParameterJdbcTemplate
                .query(searchQuery.toString(), paramValues, row);

        VehicleTripSheetDetails vehicleTripSheetDetails;
        final RouteSearch routeSearch = new RouteSearch();
        Pagination<Route> routes;
        VehicleSearch vehicleSearch;
        Pagination<Vehicle> vehicleList;
        VendorSearch vendorSearch;
        Pagination<Vendor> vendors;

        for (final VehicleTripSheetDetailsEntity vehicleTripSheetDetailsEntity : vehicleTripSheetDetailsEntities) {

            vehicleTripSheetDetails = vehicleTripSheetDetailsEntity.toDomain();

            if (vehicleTripSheetDetails.getRoute() != null && vehicleTripSheetDetails.getRoute().getCode() != null
                    && !vehicleTripSheetDetails.getRoute().getCode().isEmpty()) {

                routeSearch.setTenantId(vehicleTripSheetDetails.getTenantId());
                routeSearch.setCode(vehicleTripSheetDetails.getRoute().getCode());
                routes = routeService.search(routeSearch);

                if (routes != null && routes.getPagedData() != null && !routes.getPagedData().isEmpty())
                    vehicleTripSheetDetails.setRoute(routes.getPagedData().get(0));

            }

            if (vehicleTripSheetDetails.getVehicle() != null
                    && vehicleTripSheetDetails.getVehicle().getRegNumber() != null
                    && !vehicleTripSheetDetails.getVehicle().getRegNumber().isEmpty()) {

                vehicleSearch = new VehicleSearch();
                vehicleSearch.setTenantId(vehicleTripSheetDetails.getTenantId());
                vehicleSearch.setRegNumber(vehicleTripSheetDetails.getVehicle().getRegNumber());
                vehicleList = vehicleService.search(vehicleSearch);

                if (vehicleList != null && vehicleList.getPagedData() != null
                        && !vehicleList.getPagedData().isEmpty())
                    vehicleTripSheetDetails.setVehicle(vehicleList.getPagedData().get(0));

            }

            if (vehicleTripSheetDetails.getVendor() != null && vehicleTripSheetDetails.getVendor().getVendorNo() != null
                    && !vehicleTripSheetDetails.getVendor().getVendorNo().isEmpty()) {

                vendorSearch = new VendorSearch();
                vendorSearch.setTenantId(vehicleTripSheetDetails.getTenantId());
                vendorSearch.setVendorNo(vehicleTripSheetDetails.getVendor().getVendorNo());
                vendors = vendorService.search(vendorSearch);
                if (vendors != null && vendors.getPagedData() != null && !vendors.getPagedData().isEmpty())
                    vehicleTripSheetDetails.setVendor(vendors.getPagedData().get(0));
            }

            vehicleTripSheetDetailsList.add(vehicleTripSheetDetails);
        }

        page.setTotalResults(vehicleTripSheetDetailsList.size());

        page.setPagedData(vehicleTripSheetDetailsList);

        return page;
    }

}