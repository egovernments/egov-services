package org.egov.swm.persistence.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.domain.model.Document;
import org.egov.swm.domain.model.DocumentSearch;
import org.egov.swm.domain.model.FuelType;
import org.egov.swm.domain.model.InsuranceDetails;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.Vehicle;
import org.egov.swm.domain.model.VehicleSearch;
import org.egov.swm.domain.model.VehicleType;
import org.egov.swm.domain.model.Vendor;
import org.egov.swm.domain.model.VendorSearch;
import org.egov.swm.domain.service.FuelTypeService;
import org.egov.swm.domain.service.VehicleTypeService;
import org.egov.swm.domain.service.VendorService;
import org.egov.swm.persistence.entity.VehicleEntity;
import org.egov.swm.web.contract.Employee;
import org.egov.swm.web.contract.EmployeeResponse;
import org.egov.swm.web.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class VehicleJdbcRepository extends JdbcRepository {

    public static final String TABLE_NAME = "egswm_vehicle";

    @Autowired
    public VendorService vendorService;

    @Autowired
    private VehicleTypeService vehicleTypeService;

    @Autowired
    private FuelTypeService fuelTypeService;

    @Autowired
    private DocumentJdbcRepository documentJdbcRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public Boolean uniqueCheck(final String tenantId, final String fieldName, final String fieldValue,
            final String uniqueFieldName,
            final String uniqueFieldValue) {

        return uniqueCheck(TABLE_NAME, tenantId, fieldName, fieldValue, uniqueFieldName, uniqueFieldValue);
    }

    public Pagination<Vehicle> search(final VehicleSearch searchRequest) {

        String searchQuery = "select * from " + TABLE_NAME + " :condition  :orderby ";

        final Map<String, Object> paramValues = new HashMap<>();
        final StringBuffer params = new StringBuffer();

        if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
            validateSortByOrder(searchRequest.getSortBy());
            validateEntityFieldName(searchRequest.getSortBy(), VehicleSearch.class);
        }

        String orderBy = "order by regNumber";
        if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty())
            orderBy = "order by " + searchRequest.getSortBy();

        if (searchRequest.getTenantId() != null) {
            addAnd(params);
            params.append("tenantId =:tenantId");
            paramValues.put("tenantId", searchRequest.getTenantId());
        }

        if (searchRequest.getRegNumber() != null) {
            addAnd(params);
            params.append("regNumber =:regNumber");
            paramValues.put("regNumber", searchRequest.getRegNumber());
        }

        if (searchRequest.getRegNumbers() != null) {
            addAnd(params);
            params.append("regNumber in (:regNumbers)");
            paramValues.put("regNumbers", new ArrayList<>(Arrays.asList(searchRequest.getRegNumbers().split(","))));
        }

        if (searchRequest.getChassisSrNumber() != null) {
            addAnd(params);
            params.append("chassisSrNumber =:chassisSrNumber");
            paramValues.put("chassisSrNumber", searchRequest.getChassisSrNumber());
        }

        if (searchRequest.getEngineSrNumber() != null) {
            addAnd(params);
            params.append("engineSrNumber =:engineSrNumber");
            paramValues.put("engineSrNumber", searchRequest.getEngineSrNumber());
        }

        if (searchRequest.getInsuranceNumber() != null) {
            addAnd(params);
            params.append("insuranceNumber =:insuranceNumber");
            paramValues.put("insuranceNumber", searchRequest.getInsuranceNumber());
        }

        if (searchRequest.getModel() != null) {
            addAnd(params);
            params.append("model =:model");
            paramValues.put("model", searchRequest.getModel());
        }

        if (searchRequest.getInsuranceValidityDate() != null) {
            addAnd(params);
            params.append("insuranceValidityDate =:insuranceValidityDate");
            paramValues.put("insuranceValidityDate",
                    searchRequest.getInsuranceValidityDate());
        }

        if (searchRequest.getPurchaseDate() != null) {
            addAnd(params);
            params.append("purchaseDate =:purchaseDate");
            paramValues.put("purchaseDate", searchRequest.getPurchaseDate());
        }

        if (searchRequest.getVehicleTypeCode() != null) {
            addAnd(params);
            params.append("vehicleType =:vehicleType");
            paramValues.put("vehicleType", searchRequest.getVehicleTypeCode());
        }

        if (searchRequest.getFuelTypeCode() != null) {
            addAnd(params);
            params.append("fuelType =:fuelType");
            paramValues.put("fuelType", searchRequest.getFuelTypeCode());
        }

        if (searchRequest.getDriverCode() != null) {
            addAnd(params);
            params.append("driver =:driver");
            paramValues.put("driver", searchRequest.getDriverCode());
        }

        if (searchRequest.getPurchaseYear() != null) {
            addAnd(params);
            params.append("yearOfPurchase =:yearOfPurchase");
            paramValues.put("yearOfPurchase", searchRequest.getPurchaseYear());
        }

        if (searchRequest.getVendorNo() != null) {
            addAnd(params);
            params.append("vendor =:vendor");
            paramValues.put("vendor", searchRequest.getVendorNo());
        }

        if (searchRequest.getIsUlbOwned() != null && searchRequest.getIsUlbOwned()) {
            addAnd(params);
            params.append("isulbowned IS " + searchRequest.getIsUlbOwned());
        }

        if (searchRequest.getIsUnderWarranty() != null) {
            addAnd(params);
            params.append("isvehicleunderwarranty IS " + searchRequest.getIsUnderWarranty());
        }

        Pagination<Vehicle> page = new Pagination<>();
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

        <Vehicle>) getPagination(searchQuery, page, paramValues);
        searchQuery = searchQuery + " :pagination";

        searchQuery = searchQuery.replace(":pagination",
                "limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

        final BeanPropertyRowMapper row = new BeanPropertyRowMapper(VehicleEntity.class);

        final List<Vehicle> vehicleList = new ArrayList<>();

        final List<VehicleEntity> vehicleEntities = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues,
                row);
        final StringBuffer vehicleCodes = new StringBuffer();
        for (final VehicleEntity vehicleEntity : vehicleEntities) {

            if (vehicleCodes.length() >= 1)
                vehicleCodes.append(",");

            vehicleCodes.append(vehicleEntity.getRegNumber());
            vehicleList.add(vehicleEntity.toDomain());
        }

        if (vehicleList != null && !vehicleList.isEmpty()) {

            populateFuelTypes(vehicleList);

            populateVehicleTypes(vehicleList);

            populateVendors(vehicleList);

            populateDrivers(vehicleList);

            populateDocument(vehicleList, vehicleCodes.toString());
        }

        page.setPagedData(vehicleList);

        return page;
    }

    private void populateDocument(final List<Vehicle> vehicleList, final String vehicleCodes) {

        String tenantId = null;
        final Map<String, Document> documentMap = new HashMap<>();

        if (vehicleList != null && !vehicleList.isEmpty())
            tenantId = vehicleList.get(0).getTenantId();

        final DocumentSearch search = new DocumentSearch();
        search.setTenantId(tenantId);
        search.setRefCodes(vehicleCodes);

        final List<Document> docs = documentJdbcRepository.search(search);

        if (docs != null)
            for (final Document d : docs)
                documentMap.put(d.getRefCode(), d);

        for (final Vehicle vehicle : vehicleList) {

            if (vehicle.getInsuranceDetails() == null)
                vehicle.setInsuranceDetails(InsuranceDetails.builder().build());

            vehicle.getInsuranceDetails().setInsuranceDocument(documentMap.get(vehicle.getRegNumber()));

        }
    }

    private void populateFuelTypes(final List<Vehicle> vehicleList) {
        final Map<String, FuelType> fuelTypeMap = new HashMap<>();
        String tenantId = null;

        if (vehicleList != null && !vehicleList.isEmpty())
            tenantId = vehicleList.get(0).getTenantId();

        final List<FuelType> fuelTypes = fuelTypeService.getAll(tenantId, new RequestInfo());

        for (final FuelType ft : fuelTypes)
            fuelTypeMap.put(ft.getCode(), ft);

        for (final Vehicle vehicle : vehicleList)
            if (vehicle.getFuelType() != null && vehicle.getFuelType().getCode() != null
                    && !vehicle.getFuelType().getCode().isEmpty())
                vehicle.setFuelType(fuelTypeMap.get(vehicle.getFuelType().getCode()));
    }

    private void populateVehicleTypes(final List<Vehicle> vehicleList) {
        final Map<String, VehicleType> vehicleTypeMap = new HashMap<>();
        String tenantId = null;

        if (vehicleList != null && !vehicleList.isEmpty())
            tenantId = vehicleList.get(0).getTenantId();

        final List<VehicleType> vehicleTypes = vehicleTypeService.getAll(tenantId, new RequestInfo());

        for (final VehicleType vt : vehicleTypes)
            vehicleTypeMap.put(vt.getCode(), vt);

        for (final Vehicle vehicle : vehicleList)
            if (vehicle.getVehicleType() != null && vehicle.getVehicleType().getCode() != null
                    && !vehicle.getVehicleType().getCode().isEmpty())
                vehicle.setVehicleType(vehicleTypeMap.get(vehicle.getVehicleType().getCode()));
    }

    private void populateVendors(final List<Vehicle> vehicleList) {

        VendorSearch vendorSearch;
        Pagination<Vendor> vendors;
        final StringBuffer vendorNos = new StringBuffer();
        final Set<String> vendorNoSet = new HashSet<>();

        for (final Vehicle v : vehicleList)
            if (v.getVendor() != null && v.getVendor().getVendorNo() != null
                    && !v.getVendor().getVendorNo().isEmpty())
                vendorNoSet.add(v.getVendor().getVendorNo());

        final List<String> vendorNoList = new ArrayList(vendorNoSet);

        for (final String vendorNo : vendorNoList) {

            if (vendorNos.length() >= 1)
                vendorNos.append(",");

            vendorNos.append(vendorNo);

        }

        if (vendorNos != null && vendorNos.length() > 0) {

            String tenantId = null;
            final Map<String, Vendor> vendorMap = new HashMap<>();

            if (vehicleList != null && !vehicleList.isEmpty())
                tenantId = vehicleList.get(0).getTenantId();

            vendorSearch = new VendorSearch();
            vendorSearch.setTenantId(tenantId);
            vendorSearch.setVendorNos(vendorNos.toString());

            vendors = vendorService.search(vendorSearch);

            if (vendors != null && vendors.getPagedData() != null)
                for (final Vendor bd : vendors.getPagedData())
                    vendorMap.put(bd.getVendorNo(), bd);

            for (final Vehicle vehicle : vehicleList)
                if (vehicle.getVendor() != null && vehicle.getVendor().getVendorNo() != null
                        && !vehicle.getVendor().getVendorNo().isEmpty())
                    vehicle.setVendor(vendorMap.get(vehicle.getVendor().getVendorNo()));
        }

    }

    private void populateDrivers(final List<Vehicle> vehicleList) {

        final StringBuffer driverCodes = new StringBuffer();
        final Set<String> driverCodesSet = new HashSet<>();

        for (final Vehicle v : vehicleList)
            if (v.getDriver() != null && v.getDriver().getCode() != null
                    && !v.getDriver().getCode().isEmpty())
                driverCodesSet.add(v.getDriver().getCode());

        final List<String> driverCodeList = new ArrayList(driverCodesSet);

        for (final String code : driverCodeList) {

            if (driverCodes.length() >= 1)
                driverCodes.append(",");

            driverCodes.append(code);

        }
        if (driverCodes != null && driverCodes.length() > 0) {

            String tenantId = null;
            final Map<String, Employee> driverMap = new HashMap<>();

            if (vehicleList != null && !vehicleList.isEmpty())
                tenantId = vehicleList.get(0).getTenantId();

            final EmployeeResponse response = employeeRepository.getEmployeeByCodes(driverCodes.toString(), tenantId,
                    new RequestInfo());

            if (response != null && response.getEmployees() != null)
                for (final Employee e : response.getEmployees())
                    driverMap.put(e.getCode(), e);

            for (final Vehicle vehicle : vehicleList)
                if (vehicle.getDriver() != null && vehicle.getDriver().getCode() != null
                        && !vehicle.getDriver().getCode().isEmpty())
                    vehicle.setDriver(driverMap.get(vehicle.getDriver().getCode()));

        }

    }

}