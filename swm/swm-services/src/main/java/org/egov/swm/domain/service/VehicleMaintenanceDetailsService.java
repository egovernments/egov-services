package org.egov.swm.domain.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang3.time.DateUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.domain.model.*;
import org.egov.swm.domain.repository.VehicleMaintenanceDetailsRepository;
import org.egov.swm.domain.repository.VehicleRepository;
import org.egov.swm.persistence.repository.RouteCollectionPointMapJdbcRepository;
import org.egov.swm.web.repository.IdgenRepository;
import org.egov.swm.web.requests.VehicleMaintenanceDetailsRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@Service
public class VehicleMaintenanceDetailsService {

    private final VehicleRepository vehicleRepository;

    private final VehicleMaintenanceDetailsRepository vehicleMaintenanceDetailsRepository;

    private final VehicleMaintenanceService vehicleMaintenanceService;

    private final VehicleScheduleService vehicleScheduleService;

    private final RouteService routeService;

    private final RouteCollectionPointMapJdbcRepository routeCollectionPointMapJdbcRepository;

    private IdgenRepository idgenRepository;

    private String idGenNameForTrnNumPath;

    public VehicleMaintenanceDetailsService(final VehicleRepository vehicleRepository,
                                            final VehicleMaintenanceDetailsRepository vehicleMaintenanceDetailsRepository,
                                            final VehicleMaintenanceService vehicleMaintenanceService, final VehicleScheduleService vehicleScheduleService,
                                            final RouteService routeService, RouteCollectionPointMapJdbcRepository routeCollectionPointMapJdbcRepository,
                                            final IdgenRepository idgenRepository,
                                            final @Value("${egov.swm.vehiclemaintenancedetails.transaction.num.idgen.name}") String idGenNameForTrnNumPath) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleMaintenanceDetailsRepository = vehicleMaintenanceDetailsRepository;
        this.vehicleMaintenanceService = vehicleMaintenanceService;
        this.vehicleScheduleService = vehicleScheduleService;
        this.routeService = routeService;
        this.routeCollectionPointMapJdbcRepository = routeCollectionPointMapJdbcRepository;
        this.idgenRepository = idgenRepository;
        this.idGenNameForTrnNumPath = idGenNameForTrnNumPath;
    }

    public VehicleMaintenanceDetailsRequest create(final VehicleMaintenanceDetailsRequest vehicleMaintenanceDetailsRequest) {

        validate(vehicleMaintenanceDetailsRequest);
        Long userId = null;
        if (vehicleMaintenanceDetailsRequest.getRequestInfo() != null
                && vehicleMaintenanceDetailsRequest.getRequestInfo().getUserInfo() != null
                && null != vehicleMaintenanceDetailsRequest.getRequestInfo().getUserInfo().getId())
            userId = vehicleMaintenanceDetailsRequest.getRequestInfo().getUserInfo().getId();

        for (final VehicleMaintenanceDetails vehicleMaintenanceDetail : vehicleMaintenanceDetailsRequest
                .getVehicleMaintenanceDetails()) {
            setAuditDetails(vehicleMaintenanceDetail, userId);
            vehicleMaintenanceDetail.setTransactionNo(
                    generateTransactionNumber(vehicleMaintenanceDetail.getTenantId(), vehicleMaintenanceDetailsRequest.getRequestInfo()));
        }

        return vehicleMaintenanceDetailsRepository.create(vehicleMaintenanceDetailsRequest);
    }

    public VehicleMaintenanceDetailsRequest update(final VehicleMaintenanceDetailsRequest vehicleMaintenanceDetailsRequest) {
        Long userId = null;
        if (vehicleMaintenanceDetailsRequest.getRequestInfo() != null
                && vehicleMaintenanceDetailsRequest.getRequestInfo().getUserInfo() != null
                && null != vehicleMaintenanceDetailsRequest.getRequestInfo().getUserInfo().getId())
            userId = vehicleMaintenanceDetailsRequest.getRequestInfo().getUserInfo().getId();

        for (final VehicleMaintenanceDetails vehicleMaintenanceDetail : vehicleMaintenanceDetailsRequest
                .getVehicleMaintenanceDetails())
            setAuditDetails(vehicleMaintenanceDetail, userId);

        validateForUniqueCodesInRequest(vehicleMaintenanceDetailsRequest);
        validate(vehicleMaintenanceDetailsRequest);

        return vehicleMaintenanceDetailsRepository.update(vehicleMaintenanceDetailsRequest);

    }

    public Pagination<VehicleMaintenanceDetails> search(
            final VehicleMaintenanceDetailsSearch vehicleMaintenanceDetailsSearch) {

        return vehicleMaintenanceDetailsRepository.search(vehicleMaintenanceDetailsSearch);
    }

    public long calaculateNextSceduledMaintenanceDate(final String tenantId, final String vehicleRegNumber) {
        // Fetch vehicle maintenance detail using code and tenantid
        final VehicleMaintenanceDetailsSearch vehicleMaintenanceDetailsSearch = new VehicleMaintenanceDetailsSearch();
        vehicleMaintenanceDetailsSearch.setTenantId(tenantId);
        vehicleMaintenanceDetailsSearch.setRegNumber(vehicleRegNumber);
        vehicleMaintenanceDetailsSearch.setSortBy("lastModifiedTime desc");

        final Pagination<VehicleMaintenanceDetails> vehicleMaintenanceDetailsPage = vehicleMaintenanceDetailsRepository
                .search(vehicleMaintenanceDetailsSearch);

        // Check if vehicleMaintenance detail is present
        if (!vehicleMaintenanceDetailsPage.getPagedData().isEmpty()) {
            final Long lastServiceDate = vehicleMaintenanceDetailsPage.getPagedData().get(0).getActualMaintenanceDate();
            final int noOfDays = fetchMaintenanceAfterFromVehicleMaintenance(
                    vehicleMaintenanceDetailsPage.getPagedData().get(0));

            return DateUtils.addDays(new Date(lastServiceDate), noOfDays).getTime();
        } else
            throw new CustomException("VehicleMaintenanceDetail", "Next Scheduled Date is not applicable for vehicle :" +
                    vehicleRegNumber);
    }

    private void validateForUniqueCodesInRequest(final VehicleMaintenanceDetailsRequest vehicleMaintenanceDetailsRequest) {

        final List<String> codesList = vehicleMaintenanceDetailsRequest.getVehicleMaintenanceDetails().stream()
                .map(VehicleMaintenanceDetails::getTransactionNo).collect(Collectors.toList());

        if (codesList.size() != codesList.stream().distinct().count())
            throw new CustomException("Transaction Number", "Duplicate Transaction Numbers in given Vehicle Maintenance Details:");
    }

    private void validate(final VehicleMaintenanceDetailsRequest vehicleMaintenanceDetailsRequest) {

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        for (final VehicleMaintenanceDetails vehicleMaintenanceDetails : vehicleMaintenanceDetailsRequest
                .getVehicleMaintenanceDetails()) {

            // Validate Vehicle
            if (vehicleMaintenanceDetails.getVehicle() != null
                    && (vehicleMaintenanceDetails.getVehicle().getRegNumber() == null
                            || vehicleMaintenanceDetails.getVehicle().getRegNumber().isEmpty()))
                throw new CustomException("Vehicle", "Vehicle Registration Number required ");

            if (vehicleMaintenanceDetails.getVehicle() != null
                    && vehicleMaintenanceDetails.getVehicle().getRegNumber() != null) {

                final VehicleSearch vehicleSearch = new VehicleSearch();
                vehicleSearch.setTenantId(vehicleMaintenanceDetails.getTenantId());
                vehicleSearch.setRegNumber(vehicleMaintenanceDetails.getVehicle().getRegNumber());
                final Pagination<Vehicle> vehicleList = vehicleRepository.search(vehicleSearch);

                if (vehicleList == null || vehicleList.getPagedData() == null || vehicleList.getPagedData().isEmpty())
                    throw new CustomException("Vehicle",
                            "Given Vehicle is invalid: " + vehicleMaintenanceDetails.getVehicle().getRegNumber());
                else
                    vehicleMaintenanceDetails.setVehicle(vehicleList.getPagedData().get(0));

            }

            //vehicle downtime actual validation
            if(!isEmpty(vehicleMaintenanceDetails.getVehicleDownTimeActualUom()) &&
                    vehicleMaintenanceDetails.getVehicleDowntimeActual() != null){
                if(vehicleMaintenanceDetails.getVehicleDownTimeActualUom().equalsIgnoreCase("days")){
                    if(vehicleMaintenanceDetails.getVehicleDowntimeActual() < 1.0 ||
                            vehicleMaintenanceDetails.getVehicleDowntimeActual() > 30.0)
                        throw new CustomException("VehicleDownTime",
                                "Vehicle DownTime shall be between 1 and 30 days: "
                                        + vehicleMaintenanceDetails.getVehicleDowntimeActual());
                }
                else{
                    if(vehicleMaintenanceDetails.getVehicleDowntimeActual() < 0.0 ||
                            vehicleMaintenanceDetails.getVehicleDowntimeActual() > 720.0)
                        throw new CustomException("VehicleDownTime",
                                "Vehicle DownTime shall be between 0 and 720 hours: "
                                        + vehicleMaintenanceDetails.getVehicleDowntimeActual());
                }
            }

            // validation for actual maintenance date
            if (new Date(vehicleMaintenanceDetails.getActualMaintenanceDate()).after(new Date()))
                throw new CustomException("ActualMaintenanceDate",
                        "Actual Maintenance date cannot be future date: "
                                + dateFormat.format(new Date(vehicleMaintenanceDetails.getActualMaintenanceDate())));

            // validation for vehiclemaintenance if scheduled
            if (vehicleMaintenanceDetails.getIsScheduled() && vehicleMaintenanceDetails.getVehicle() != null
                    && vehicleMaintenanceDetails.getVehicle().getRegNumber() != null &&
                    !vehicleMaintenanceDetails.getVehicle().getRegNumber().isEmpty()) {

                final Pagination<VehicleMaintenance> vehicleMaintenancePage = fetchVehicleMaintenanceForVehicle(
                        vehicleMaintenanceDetails);

                if (vehicleMaintenancePage.getPagedData().isEmpty())
                    throw new CustomException("VehicleMaintenance",
                            "Vehicle Maintenance not defined for vehicle: "
                                    + vehicleMaintenanceDetails.getVehicle().getRegNumber());
            }
        }
    }

    // Return number of days from vehicle maintenance
    private int fetchMaintenanceAfterFromVehicleMaintenance(final VehicleMaintenanceDetails vehicleMaintenanceDetail) {

        if (vehicleMaintenanceDetail.getVehicle() != null
                && (vehicleMaintenanceDetail.getVehicle().getRegNumber() == null
                        || vehicleMaintenanceDetail.getVehicle().getRegNumber().isEmpty()))
            throw new CustomException("code", "Vehicle code required in maintenance details");

        final Pagination<VehicleMaintenance> vehicleMaintenancePage = fetchVehicleMaintenanceForVehicle(vehicleMaintenanceDetail);

        if (!vehicleMaintenancePage.getPagedData().isEmpty()) {

            final VehicleMaintenance vehicleMaintenance = vehicleMaintenancePage.getPagedData().get(0);

            if (vehicleMaintenance.getMaintenanceUom().equalsIgnoreCase("days"))
                return Math.toIntExact(vehicleMaintenance.getMaintenanceAfter());
            else if (vehicleMaintenance.getMaintenanceUom().equalsIgnoreCase("kms"))
                 Math.toIntExact(vehicleMaintenance.getMaintenanceAfter() / fetchkilometersFromRoutes(
                        vehicleMaintenanceDetail, vehicleMaintenance.getMaintenanceAfter()));
        } else
            throw new CustomException("VehicleMaintenance",
                    "Next scheduled date not applicable since Vehicle Maintenance not defined for vehicle :"
                            + vehicleMaintenanceDetail.getVehicle().getRegNumber());

        return 0;
    }

    private Pagination<VehicleMaintenance> fetchVehicleMaintenanceForVehicle(
            final VehicleMaintenanceDetails vehicleMaintenanceDetail) {
        final VehicleMaintenanceSearch vehicleMaintenanceSearch = new VehicleMaintenanceSearch();
        vehicleMaintenanceSearch.setTenantId(vehicleMaintenanceDetail.getTenantId());
        vehicleMaintenanceSearch.setRegNumber(vehicleMaintenanceDetail.getVehicle().getRegNumber());

        return vehicleMaintenanceService.search(vehicleMaintenanceSearch);
    }
        //To-Do Need to fix
    private int fetchkilometersFromRoutes(final VehicleMaintenanceDetails vehicleMaintenanceDetail,
            final Long vehicleMaintenanceAfter) {
        final VehicleScheduleSearch vehicleScheduleSearch = new VehicleScheduleSearch();
        vehicleScheduleSearch.setTenantId(vehicleMaintenanceDetail.getTenantId());
        vehicleScheduleSearch.setRegNumber(vehicleMaintenanceDetail.getVehicle().getRegNumber());

        final Pagination<VehicleSchedule> vehicleSchedulePage = vehicleScheduleService.search(vehicleScheduleSearch);

        final Long dateFrom = DateUtils.addDays(new Date(vehicleMaintenanceDetail.getActualMaintenanceDate()), 1).getTime();
        final Long dateTo = new Date().getTime();
        // Filtering vehicle schedule list based on from and to date
        final List<VehicleSchedule> vehicleScheduleList = vehicleSchedulePage.getPagedData().stream()
                .filter(v -> (dateFrom >= v.getScheduledFrom() && dateTo <= v.getScheduledTo()))
                .collect(Collectors.toList());
        Double totalKilometers = 0.0;
        Long totalDays = 0l;

        if (!vehicleScheduleList.isEmpty())
            // Days calculation for filtered set of routes
            for (final VehicleSchedule vehicleSchedule : vehicleScheduleList) {

            final RouteSearch routeSearch = new RouteSearch();
            routeSearch.setTenantId(vehicleMaintenanceDetail.getTenantId());
            routeSearch.setCode(vehicleSchedule.getRoute().getCode());

            final Pagination<Route> routePage = routeService.search(routeSearch);

            if (!routePage.getPagedData().isEmpty()) {

                //Search for RouteCollection maps for distance
                final RouteCollectionPointMapSearch routeCollectionPointMapSearch = new RouteCollectionPointMapSearch();
                routeCollectionPointMapSearch.setTenantId(vehicleMaintenanceDetail.getTenantId());
                routeCollectionPointMapSearch.setRoute(routePage.getPagedData().get(0).getCode());

                List<RouteCollectionPointMap> routeCollectionPointMaps = routeCollectionPointMapJdbcRepository.search(routeCollectionPointMapSearch);

                Double distanceSum = routeCollectionPointMaps.stream().mapToDouble(RouteCollectionPointMap::getDistance).sum();

                final Long dateDifferenceInMilliseconds = vehicleSchedule.getScheduledTo()
                        - vehicleSchedule.getScheduledFrom();
                final Long days = TimeUnit.DAYS.convert(dateDifferenceInMilliseconds, TimeUnit.MILLISECONDS);
                totalDays = totalDays + days;
                final Double totalKilometersOnRoute = days * distanceSum;

                if (totalKilometers + totalKilometersOnRoute < vehicleMaintenanceAfter)
                    totalKilometers = totalKilometers + days * distanceSum;
                else {
                final Double distanceDifference = vehicleMaintenanceAfter - totalKilometers;
                    totalDays = totalDays + distanceDifference.longValue()
                        / distanceSum.longValue();

                    return totalDays.intValue();
                }
            } else
            throw new CustomException("Route", "Next scheduled date not applicable since Route not defined for veicle :" +
                    vehicleMaintenanceDetail.getVehicle().getRegNumber());
            }
        else
            throw new CustomException("VehicleSchedule",
                    "Next scheduled date not applicable since Vehicle Schedule not defined for vehicle :" +
                            vehicleMaintenanceDetail.getVehicle().getRegNumber());
        return totalDays.intValue();
    }

    private void setAuditDetails(final VehicleMaintenanceDetails contract, final Long userId) {

        if (contract.getAuditDetails() == null)
            contract.setAuditDetails(new AuditDetails());

        if (null == contract.getTransactionNo() || contract.getTransactionNo().isEmpty()) {
            contract.getAuditDetails().setCreatedBy(null != userId ? userId.toString() : null);
            contract.getAuditDetails().setCreatedTime(new Date().getTime());
        }

        contract.getAuditDetails().setLastModifiedBy(null != userId ? userId.toString() : null);
        contract.getAuditDetails().setLastModifiedTime(new Date().getTime());
    }

    private String generateTransactionNumber(final String tenantId, final RequestInfo requestInfo) {

        return idgenRepository.getIdGeneration(tenantId, requestInfo, idGenNameForTrnNumPath);
    }

}
