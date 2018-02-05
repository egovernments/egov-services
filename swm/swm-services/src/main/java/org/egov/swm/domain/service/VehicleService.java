package org.egov.swm.domain.service;

import static org.springframework.util.StringUtils.isEmpty;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.egov.swm.constants.Constants;
import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.Document;
import org.egov.swm.domain.model.InsuranceDetails;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.Vehicle;
import org.egov.swm.domain.model.VehicleMaintenance;
import org.egov.swm.domain.model.VehicleMaintenanceSearch;
import org.egov.swm.domain.model.VehicleSearch;
import org.egov.swm.domain.model.Vendor;
import org.egov.swm.domain.model.VendorSearch;
import org.egov.swm.domain.repository.VehicleRepository;
import org.egov.swm.utils.Utils;
import org.egov.swm.web.contract.DesignationResponse;
import org.egov.swm.web.contract.EmployeeResponse;
import org.egov.swm.web.repository.DesignationRepository;
import org.egov.swm.web.repository.EmployeeRepository;
import org.egov.swm.web.requests.VehicleRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private VehicleTypeService vehicleTypeService;

    @Autowired
    private FuelTypeService fuelTypeService;

    @Autowired
    private VendorService vendorService;

    @Autowired
    private DesignationRepository designationRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private Utils utils;

    @Autowired
    private VehicleMaintenanceService vehicleMaintenanceService;

    @Transactional
    public VehicleRequest create(final VehicleRequest vehicleRequest) {

        validate(Constants.ACTION_CREATE, vehicleRequest);
        Long userId = null;

        if (vehicleRequest.getRequestInfo() != null && vehicleRequest.getRequestInfo().getUserInfo() != null
                && null != vehicleRequest.getRequestInfo().getUserInfo().getId())
            userId = vehicleRequest.getRequestInfo().getUserInfo().getId();

        for (final Vehicle v : vehicleRequest.getVehicles()) {

            setAuditDetails(v, userId);

            if (v.getInsuranceDetails() == null) {
                v.setInsuranceDetails(new InsuranceDetails());
            }

            prepareInsuranceDocument(v);
        }

        return vehicleRepository.save(vehicleRequest);

    }

    private void prepareInsuranceDocument(final Vehicle v) {

        if (v.getInsuranceDetails().getInsuranceDocument() != null
                && v.getInsuranceDetails().getInsuranceDocument().getFileStoreId() != null) {
            v.getInsuranceDetails().getInsuranceDocument().setId(UUID.randomUUID().toString().replace("-", ""));
            v.getInsuranceDetails().getInsuranceDocument().setTenantId(v.getTenantId());
            v.getInsuranceDetails().getInsuranceDocument().setRefCode(v.getRegNumber());
            v.getInsuranceDetails().getInsuranceDocument().setAuditDetails(v.getAuditDetails());
        } else {
            v.getInsuranceDetails().setInsuranceDocument(new Document());
        }
    }

    @Transactional
    public VehicleRequest update(final VehicleRequest vehicleRequest) {

        Long userId = null;
        for (final Vehicle v : vehicleRequest.getVehicles()) {

            if (vehicleRequest.getRequestInfo() != null && vehicleRequest.getRequestInfo().getUserInfo() != null
                    && null != vehicleRequest.getRequestInfo().getUserInfo().getId())
                userId = vehicleRequest.getRequestInfo().getUserInfo().getId();

            setAuditDetails(v, userId);

            prepareInsuranceDocument(v);

        }

        validate(Constants.ACTION_UPDATE, vehicleRequest);

        return vehicleRepository.update(vehicleRequest);

    }

    public Pagination<Vehicle> search(final VehicleSearch vehicleSearch) {

        if (vehicleSearch.getIsScheduled() != null && vehicleSearch.getIsScheduled())
            setVehicleCodesFromVehicleMaintenance(vehicleSearch);

        return vehicleRepository.search(vehicleSearch);
    }

    private void setVehicleCodesFromVehicleMaintenance(VehicleSearch vehicleSearch) {
        VehicleMaintenanceSearch vehicleMaintenanceSearch = new VehicleMaintenanceSearch();
        vehicleMaintenanceSearch.setTenantId(vehicleSearch.getTenantId());

        Pagination<VehicleMaintenance> vehicleMaintenances = vehicleMaintenanceService.search(vehicleMaintenanceSearch);

        if (!vehicleMaintenances.getPagedData().isEmpty()) {
            vehicleSearch.setRegNumbers(vehicleMaintenances.getPagedData().stream()
                    .map(v -> (v.getVehicle() != null) ? v.getVehicle().getRegNumber() : StringUtils.EMPTY)
                    .distinct()
                    .collect(Collectors.joining(",")));
        }
    }

    private void validate(final String action, final VehicleRequest vehicleRequest) {

        VendorSearch vendorSearch;
        Pagination<Vendor> vendors;
        DesignationResponse designationResponse = null;
        String designationId = null;
        EmployeeResponse employeeResponse = null;

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        findDuplicatesInUniqueFields(vehicleRequest);

        for (final Vehicle vehicle : vehicleRequest.getVehicles()) {

            if (vehicle.getVehicleType() != null
                    && (vehicle.getVehicleType().getCode() == null || vehicle.getVehicleType().getCode().isEmpty()))
                throw new CustomException("VehicleType",
                        "The field VehicleType Code is Mandatory . It cannot be not be null or empty.Please provide correct value ");

            // Validate vehicle Type
            if (vehicle.getVehicleType() != null && vehicle.getVehicleType().getCode() != null)
                vehicle.setVehicleType(vehicleTypeService.getVehicleType(vehicle.getTenantId(),
                        vehicle.getVehicleType().getCode(), vehicleRequest.getRequestInfo()));

            if (vehicle.getVendor() != null && vehicle.getVendor().getVendorNo() == null)
                throw new CustomException("Vendor",
                        "The field Vendor number is Mandatory . It cannot be not be null or empty.Please provide correct value ");

            // Validate vendor

            if (vehicle.getVendor() != null && vehicle.getVendor().getVendorNo() != null
                    && !vehicle.getVendor().getVendorNo().isEmpty()) {
                vendorSearch = new VendorSearch();
                vendorSearch.setTenantId(vehicle.getTenantId());
                vendorSearch.setVendorNo(vehicle.getVendor().getVendorNo());
                vendors = vendorService.search(vendorSearch);
                if (vendors != null && vendors.getPagedData() != null && !vendors.getPagedData().isEmpty())
                    vehicle.setVendor(vendors.getPagedData().get(0));
                else
                    throw new CustomException("Vendor",
                            "Given Vendor is invalid: " + vehicle.getVendor().getVendorNo());
            }

            if (vehicle.getFuelType() != null
                    && (vehicle.getFuelType().getCode() == null || vehicle.getFuelType().getCode().isEmpty()))
                throw new CustomException("FuelType",
                        "The field FuelType Code is Mandatory . It cannot be not be null or empty.Please provide correct value ");

            // Validate Fuel Type
            if (vehicle.getFuelType() != null)
                vehicle.setFuelType(fuelTypeService.getFuelType(vehicle.getTenantId(), vehicle.getFuelType().getCode(),
                        vehicleRequest.getRequestInfo()));

            if (vehicle.getInsuranceDetails() != null
                    && vehicle.getInsuranceDetails().getInsuranceValidityDate() != null)
                if (!utils.isFutureDate(new Date(vehicle.getInsuranceDetails().getInsuranceValidityDate())))
                    throw new CustomException("InsuranceValidityDate",
                            "Given InsuranceValidityDate is invalid: "
                                    + dateFormat.format(new Date(vehicle.getInsuranceDetails().getInsuranceValidityDate()))
                                    + " It is not a future date");

            if (vehicle.getPurchaseInfo() != null && vehicle.getPurchaseInfo().getPurchaseDate() != null)
                if (utils.isFutureDate(new Date(vehicle.getPurchaseInfo().getPurchaseDate())))
                    throw new CustomException("PurchaseDate",
                            "Given PurchaseDate is invalid: "
                                    + dateFormat.format(new Date(vehicle.getInsuranceDetails().getInsuranceValidityDate()))
                                    + " It should not be a future date");

            // Validate Driver
            if (vehicle.getDriver() != null && vehicle.getDriver().getCode() != null
                    && !vehicle.getDriver().getCode().isEmpty()) {

                designationResponse = designationRepository.getDesignationByName(Constants.DRIVER_DESIGNATION_NAME,
                        vehicle.getTenantId(),
                        vehicleRequest.getRequestInfo());
                if (designationResponse != null && designationResponse.getDesignation() != null
                        && !designationResponse.getDesignation().isEmpty()) {
                    designationId = designationResponse.getDesignation().get(0).getId().toString();
                } else {
                    throw new CustomException("Driver", "Given Driver is invalid: " + vehicle.getDriver().getCode());
                }

                if (designationId != null) {
                    employeeResponse = employeeRepository.getEmployeeByDesgIdAndCode(designationId,
                            vehicle.getDriver().getCode(), vehicle.getTenantId(), vehicleRequest.getRequestInfo());
                } else {
                    throw new CustomException("Driver", "Given Driver is invalid: " + vehicle.getDriver().getCode());
                }

                if (employeeResponse == null || employeeResponse.getEmployees() == null
                        || employeeResponse.getEmployees().isEmpty()) {
                    throw new CustomException("Driver", "Given Driver is invalid: " + vehicle.getDriver().getCode());
                } else {
                    vehicle.setDriver(employeeResponse.getEmployees().get(0));
                }

            } else {
                throw new CustomException("Driver",
                        "The field Driver is Mandatory . It cannot be not be null or empty.Please provide correct value ");
            }

            // validate for is vehicle under warranty
            if (vehicle.getIsVehicleUnderWarranty() != null && vehicle.getIsVehicleUnderWarranty()) {
                if (vehicle.getKilometers() == null || vehicle.getEndOfWarranty() == null)
                    throw new CustomException("isVehicleUnderWarranty",
                            "Value should be present for both kilometer and endOfWarranty");
            }

            // validate for is ulb owned
            if (vehicle.getIsUlbOwned() != null && vehicle.getIsUlbOwned()) {
                if (vehicle.getVendor() == null || isEmpty(vehicle.getVendor()))
                    throw new CustomException("isUlbOwned",
                            "Value should be present for vendor");
            }

            validateUniqueFields(action, vehicle);
        }

    }

    private void findDuplicatesInUniqueFields(final VehicleRequest vehicleRequest) {

        final Map<String, String> regNumberMap = new HashMap<>();
        final Map<String, String> engineSrNumberMap = new HashMap<>();
        final Map<String, String> chassisSrNumberMap = new HashMap<>();
        final Map<String, String> insuranceNumberMap = new HashMap<>();

        for (final Vehicle vehicle : vehicleRequest.getVehicles()) {

            if (vehicle.getRegNumber() != null) {
                if (regNumberMap.get(vehicle.getRegNumber()) != null)
                    throw new CustomException("Name",
                            "Duplicate registration numbers in given Vehicles : " + vehicle.getRegNumber());

                regNumberMap.put(vehicle.getRegNumber(), vehicle.getRegNumber());
            }

            if (vehicle.getManufacturingDetails() != null) {

                if (vehicle.getManufacturingDetails().getEngineSrNumber() != null
                        && !vehicle.getManufacturingDetails().getEngineSrNumber().isEmpty()) {

                    if (engineSrNumberMap.get(vehicle.getManufacturingDetails().getEngineSrNumber()) != null)
                        throw new CustomException("engineSrNumber", "Duplicate engineSrNumbers in given Vehicles : "
                                + vehicle.getManufacturingDetails().getEngineSrNumber());

                    engineSrNumberMap.put(vehicle.getManufacturingDetails().getEngineSrNumber(),
                            vehicle.getManufacturingDetails().getEngineSrNumber());
                }

                if (vehicle.getManufacturingDetails().getChassisSrNumber() != null
                        && !vehicle.getManufacturingDetails().getChassisSrNumber().isEmpty()) {

                    if (chassisSrNumberMap.get(vehicle.getManufacturingDetails().getChassisSrNumber()) != null)
                        throw new CustomException("chassisSrNumber", "Duplicate chassisSrNumbers in given Vehicles : "
                                + vehicle.getManufacturingDetails().getChassisSrNumber());

                    chassisSrNumberMap.put(vehicle.getManufacturingDetails().getChassisSrNumber(),
                            vehicle.getManufacturingDetails().getChassisSrNumber());
                }
            }

            if (vehicle.getInsuranceDetails() != null)
                if (vehicle.getInsuranceDetails().getInsuranceNumber() != null
                        && !vehicle.getInsuranceDetails().getInsuranceNumber().isEmpty()) {

                    if (insuranceNumberMap.get(vehicle.getInsuranceDetails().getInsuranceNumber()) != null)
                        throw new CustomException("insuranceNumber", "Duplicate insuranceNumbers in given Vehicles : "
                                + vehicle.getInsuranceDetails().getInsuranceNumber());

                    insuranceNumberMap.put(vehicle.getInsuranceDetails().getInsuranceNumber(),
                            vehicle.getInsuranceDetails().getInsuranceNumber());
                }

        }

    }

    private void validateUniqueFields(final String action, final Vehicle vehicle) {

        String regNumber;

        if (action.equalsIgnoreCase(Constants.ACTION_CREATE))
            regNumber = null;
        else
            regNumber = vehicle.getRegNumber();

        if (vehicle.getRegNumber() != null)
            if (!vehicleRepository.uniqueCheck(vehicle.getTenantId(), "regNumber", vehicle.getRegNumber(), "regNumber",
                    regNumber))
                throw new CustomException("regNumber", "The field regNumber must be unique in the system The  value "
                        + vehicle.getRegNumber()
                        + " for the field regNumber already exists in the system. Please provide different value ");

        if (vehicle.getManufacturingDetails() != null) {

            if (vehicle.getManufacturingDetails().getEngineSrNumber() != null
                    && !vehicle.getManufacturingDetails().getEngineSrNumber().isEmpty())
                if (!vehicleRepository.uniqueCheck(vehicle.getTenantId(), "engineSrNumber",
                        vehicle.getManufacturingDetails().getEngineSrNumber(), "regNumber", regNumber))
                    throw new CustomException("engineSrNumber",
                            "The field engineSrNumber must be unique in the system The  value "
                                    + vehicle.getManufacturingDetails().getEngineSrNumber()
                                    + " for the field engineSrNumber already exists in the system. Please provide different value ");

            if (vehicle.getManufacturingDetails().getChassisSrNumber() != null
                    && !vehicle.getManufacturingDetails().getChassisSrNumber().isEmpty())
                if (!vehicleRepository.uniqueCheck(vehicle.getTenantId(), "chassisSrNumber",
                        vehicle.getManufacturingDetails().getChassisSrNumber(), "regNumber", regNumber))
                    throw new CustomException("chassisSrNumber",
                            "The field chassisSrNumber must be unique in the system The  value "
                                    + vehicle.getManufacturingDetails().getChassisSrNumber()
                                    + " for the field chassisSrNumber already exists in the system. Please provide different value ");
        }

        if (vehicle.getInsuranceDetails() != null)
            if (vehicle.getInsuranceDetails().getInsuranceNumber() != null
                    && !vehicle.getInsuranceDetails().getInsuranceNumber().isEmpty())
                if (!vehicleRepository.uniqueCheck(vehicle.getTenantId(), "insuranceNumber",
                        vehicle.getInsuranceDetails().getInsuranceNumber(), "regNumber", regNumber))
                    throw new CustomException("insuranceNumber",
                            "The field insuranceNumber must be unique in the system The  value "
                                    + vehicle.getInsuranceDetails().getInsuranceNumber()
                                    + " for the field insuranceNumber already exists in the system. Please provide different value ");
    }

    private void setAuditDetails(final Vehicle contract, final Long userId) {

        if (contract.getAuditDetails() == null)
            contract.setAuditDetails(new AuditDetails());

        if (null == contract.getRegNumber() || contract.getRegNumber().isEmpty()) {
            contract.getAuditDetails().setCreatedBy(null != userId ? userId.toString() : null);
            contract.getAuditDetails().setCreatedTime(new Date().getTime());
        }

        contract.getAuditDetails().setLastModifiedBy(null != userId ? userId.toString() : null);
        contract.getAuditDetails().setLastModifiedTime(new Date().getTime());
    }

}