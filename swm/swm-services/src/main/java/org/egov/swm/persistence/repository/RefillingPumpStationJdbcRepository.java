package org.egov.swm.persistence.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.domain.model.Boundary;
import org.egov.swm.domain.model.FuelType;
import org.egov.swm.domain.model.OilCompany;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.PumpStationFuelTypes;
import org.egov.swm.domain.model.RefillingPumpStation;
import org.egov.swm.domain.model.RefillingPumpStationSearch;
import org.egov.swm.domain.service.BoundaryService;
import org.egov.swm.domain.service.FuelTypeService;
import org.egov.swm.domain.service.OilCompanyService;
import org.egov.swm.persistence.entity.RefillingPumpStationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class RefillingPumpStationJdbcRepository extends JdbcRepository {

    public static final String TABLE_NAME = "egswm_refillingpumpstation";

    @Autowired
    private OilCompanyService oilCompanyService;

    @Autowired
    private FuelTypeService fuelTypeService;

    @Autowired
    private PumpStationFuelTypesJdbcRepository pumpStationFuelTypesJdbcRepository;

    @Autowired
    private BoundaryService boundaryService;

    public Boolean checkForUniqueRecords(final String tenantId, final String fieldName, final String fieldValue,
            final String uniqueFieldName,
            final String uniqueFieldValue) {
        return uniqueCheck(TABLE_NAME, tenantId, fieldName, fieldValue, uniqueFieldName, uniqueFieldValue);
    }

    public Pagination<RefillingPumpStation> search(final RefillingPumpStationSearch searchRequest) {

        String searchQuery = "select * from " + TABLE_NAME + " :condition  :orderby ";

        final Map<String, Object> paramValues = new HashMap<>();
        final StringBuffer params = new StringBuffer();

        if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
            validateSortByOrder(searchRequest.getSortBy());
            validateEntityFieldName(searchRequest.getSortBy(), RefillingPumpStationSearch.class);
        }

        String orderBy = "order by name";
        if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty())
            orderBy = "order by " + searchRequest.getSortBy();

        if (searchRequest.getCodes() != null) {
            addAnd(params);
            params.append("code in (:codes)");
            paramValues.put("codes", new ArrayList<>(Arrays.asList(searchRequest.getCodes().split(","))));
        }

        if (searchRequest.getTenantId() != null) {
            addAnd(params);
            params.append("tenantId =:tenantId");
            paramValues.put("tenantId", searchRequest.getTenantId());
        }

        if (searchRequest.getCode() != null) {
            addAnd(params);
            params.append("code =:code");
            paramValues.put("code", searchRequest.getCode());
        }

        if (searchRequest.getName() != null) {
            addAnd(params);
            params.append("name =:name");
            paramValues.put("name", searchRequest.getName());
        }

        if (searchRequest.getQuantity() != null) {
            addAnd(params);
            params.append("quantity =:quantity");
            paramValues.put("quantity", searchRequest.getQuantity());
        }

        if (searchRequest.getTypeOfFuelCode() != null) {

            PumpStationFuelTypes pumpStation = new PumpStationFuelTypes();
            pumpStation.setTenantId(searchRequest.getTenantId());
            pumpStation.setFuelType(searchRequest.getTypeOfFuelCode());

            List<PumpStationFuelTypes> pumpStationFuelTypes = pumpStationFuelTypesJdbcRepository.search(pumpStation);

            List<String> pumpStationCodes = pumpStationFuelTypes.stream()
                    .map(PumpStationFuelTypes::getPumpStation)
                    .collect(Collectors.toList());
            if (pumpStationCodes != null && !pumpStationCodes.isEmpty()) {
                addAnd(params);
                params.append("code in (:pumpStationCodes) ");
                paramValues.put("pumpStationCodes", pumpStationCodes);
            }

        }

        if (searchRequest.getLocationCode() != null) {
            addAnd(params);
            params.append("location =:location");
            paramValues.put("location", searchRequest.getLocationCode());
        }

        if (searchRequest.getTypeOfPumpCode() != null) {
            addAnd(params);
            params.append("typeofpump =:typeofpump");
            paramValues.put("typeofpump", searchRequest.getTypeOfPumpCode());
        }

        Pagination<RefillingPumpStation> page = new Pagination<>();
        if (searchRequest.getOffset() != null)
            page.setOffset(searchRequest.getOffset());
        if (searchRequest.getPageSize() != null)
            page.setPageSize(searchRequest.getPageSize());

        if (params.length() > 0)
            searchQuery = searchQuery.replace(":condition", " where " + params.toString());
        else

            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        page = (Pagination<RefillingPumpStation>) getPagination(searchQuery, page, paramValues);
        searchQuery = searchQuery + " :pagination";

        searchQuery = searchQuery.replace(":pagination",
                "limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

        final BeanPropertyRowMapper row = new BeanPropertyRowMapper(RefillingPumpStationEntity.class);

        final List<RefillingPumpStation> refillingPumpStationList = new ArrayList<>();

        final List<RefillingPumpStationEntity> refillingPumpStationEntities = namedParameterJdbcTemplate
                .query(searchQuery.toString(), paramValues, row);

        for (final RefillingPumpStationEntity refillingPumpStationEntity : refillingPumpStationEntities) {

            refillingPumpStationList.add(refillingPumpStationEntity.toDomain());
        }

        if (refillingPumpStationList != null && !refillingPumpStationList.isEmpty()) {

            populateBoundarys(refillingPumpStationList);

            populateFuelTypes(refillingPumpStationList);

            populateTypeOfPumps(refillingPumpStationList);

        }
        page.setPagedData(refillingPumpStationList);

        return page;
    }

    private void populateBoundarys(List<RefillingPumpStation> refillingPumpStationList) {

        String tenantId = null;
        Map<String, Boundary> boundaryMap = new HashMap<>();

        if (refillingPumpStationList != null && !refillingPumpStationList.isEmpty())
            tenantId = refillingPumpStationList.get(0).getTenantId();

        List<Boundary> boundarys = boundaryService.getAll(tenantId, new RequestInfo());

        if (boundarys != null) {

            for (Boundary bd : boundarys) {

                boundaryMap.put(bd.getCode(), bd);

            }
        }
        for (RefillingPumpStation refillingPumpStation : refillingPumpStationList) {

            if (refillingPumpStation.getLocation() != null && refillingPumpStation.getLocation().getCode() != null
                    && !refillingPumpStation.getLocation().getCode().isEmpty()) {

                refillingPumpStation.setLocation(boundaryMap.get(refillingPumpStation.getLocation().getCode()));
            }

        }

    }

    private void populateFuelTypes(List<RefillingPumpStation> refillingPumpStationList) {

        PumpStationFuelTypes pumpStation = new PumpStationFuelTypes();
        List<PumpStationFuelTypes> pumpStationFuelTypes = new ArrayList<>();
        Map<String, FuelType> fuelTypeMap = new HashMap<>();
        Map<String, List<PumpStationFuelTypes>> pumpStationFuelTypesMap = new HashMap<>();
        Map<String, List<FuelType>> fuelTypesMap = new HashMap<>();
        StringBuffer pumpStationCodes = new StringBuffer();

        String tenantId = null;

        if (refillingPumpStationList != null && !refillingPumpStationList.isEmpty())
            tenantId = refillingPumpStationList.get(0).getTenantId();

        List<FuelType> fuelTypes = fuelTypeService.getAll(tenantId, new RequestInfo());

        for (FuelType ft : fuelTypes) {
            fuelTypeMap.put(ft.getCode(), ft);
        }

        for (RefillingPumpStation refillingPumpStation : refillingPumpStationList) {
            if (pumpStationCodes.length() >= 1)
                pumpStationCodes.append(",");

            pumpStationCodes.append(refillingPumpStation.getCode());
        }

        pumpStation.setTenantId(tenantId);
        pumpStation.setPumpStations(pumpStationCodes.toString());

        pumpStationFuelTypes = pumpStationFuelTypesJdbcRepository.search(pumpStation);

        List<PumpStationFuelTypes> tempList;
        for (PumpStationFuelTypes psft : pumpStationFuelTypes) {

            if (pumpStationFuelTypesMap.get(psft.getPumpStation()) == null) {

                pumpStationFuelTypesMap.put(psft.getPumpStation(), Collections.singletonList(psft));

            } else {
                tempList = new ArrayList<>(pumpStationFuelTypesMap.get(psft.getPumpStation()));
                tempList.add(psft);
                pumpStationFuelTypesMap.put(psft.getPumpStation(), tempList);
            }
        }

        for (String pumpStationCode : pumpStationFuelTypesMap.keySet()) {

            fuelTypes = new ArrayList<>();

            for (PumpStationFuelTypes psft : pumpStationFuelTypesMap.get(pumpStationCode)) {
                fuelTypes.add(fuelTypeMap.get(psft.getFuelType()));
            }

            fuelTypesMap.put(pumpStationCode, fuelTypes);

        }

        for (RefillingPumpStation station : refillingPumpStationList) {
            station.setFuelTypes(fuelTypesMap.get(station.getCode()));
        }
    }

    private void populateTypeOfPumps(List<RefillingPumpStation> refillingPumpStationList) {
        Map<String, OilCompany> typeOfPumpMap = new HashMap<>();
        String tenantId = null;

        if (refillingPumpStationList != null && !refillingPumpStationList.isEmpty())
            tenantId = refillingPumpStationList.get(0).getTenantId();

        List<OilCompany> typeOfPumps = oilCompanyService.getAll(tenantId, new RequestInfo());

        for (OilCompany top : typeOfPumps) {
            typeOfPumpMap.put(top.getCode(), top);
        }

        for (RefillingPumpStation refillingPumpStation : refillingPumpStationList) {

            if (refillingPumpStation.getTypeOfPump() != null && refillingPumpStation.getTypeOfPump().getCode() != null
                    && !refillingPumpStation.getTypeOfPump().getCode().isEmpty()) {

                refillingPumpStation.setTypeOfPump(typeOfPumpMap.get(refillingPumpStation.getTypeOfPump().getCode()));
            }

        }
    }

}
