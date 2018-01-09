package org.egov.swm.domain.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.Route;
import org.egov.swm.domain.model.RouteSearch;
import org.egov.swm.domain.model.Vehicle;
import org.egov.swm.domain.model.VehicleSearch;
import org.egov.swm.domain.model.VehicleTripSheetDetails;
import org.egov.swm.domain.model.VehicleTripSheetDetailsSearch;
import org.egov.swm.domain.repository.VehicleTripSheetDetailsRepository;
import org.egov.swm.web.repository.IdgenRepository;
import org.egov.swm.web.requests.VehicleTripSheetDetailsRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class VehicleTripSheetDetailsService {

    @Autowired
    private VehicleTripSheetDetailsRepository vehicleTripSheetDetailsRepository;

    @Autowired
    private RouteService routeService;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private IdgenRepository idgenRepository;

    @Value("${egov.swm.vehicle.trip.num.idgen.name}")
    private String idGenNameForVehicleTripNumberPath;

    @Transactional
    public VehicleTripSheetDetailsRequest create(final VehicleTripSheetDetailsRequest vehicleTripSheetDetailsRequest) {

        validate(vehicleTripSheetDetailsRequest);

        Long userId = null;

        if (vehicleTripSheetDetailsRequest.getRequestInfo() != null
                && vehicleTripSheetDetailsRequest.getRequestInfo().getUserInfo() != null
                && null != vehicleTripSheetDetailsRequest.getRequestInfo().getUserInfo().getId())
            userId = vehicleTripSheetDetailsRequest.getRequestInfo().getUserInfo().getId();

        for (final VehicleTripSheetDetails vtsd : vehicleTripSheetDetailsRequest.getVehicleTripSheetDetails()) {

            setAuditDetails(vtsd, userId);

            vtsd.setTripNo(generateTripNumber(vtsd.getTenantId(), vehicleTripSheetDetailsRequest.getRequestInfo()));

        }

        return vehicleTripSheetDetailsRepository.save(vehicleTripSheetDetailsRequest);

    }

    @Transactional
    public VehicleTripSheetDetailsRequest update(final VehicleTripSheetDetailsRequest vehicleTripSheetDetailsRequest) {

        Long userId = null;

        if (vehicleTripSheetDetailsRequest.getRequestInfo() != null
                && vehicleTripSheetDetailsRequest.getRequestInfo().getUserInfo() != null
                && null != vehicleTripSheetDetailsRequest.getRequestInfo().getUserInfo().getId())
            userId = vehicleTripSheetDetailsRequest.getRequestInfo().getUserInfo().getId();

        for (final VehicleTripSheetDetails vtsd : vehicleTripSheetDetailsRequest.getVehicleTripSheetDetails())
            setAuditDetails(vtsd, userId);

        validate(vehicleTripSheetDetailsRequest);

        return vehicleTripSheetDetailsRepository.update(vehicleTripSheetDetailsRequest);

    }

    public Pagination<VehicleTripSheetDetails> search(final VehicleTripSheetDetailsSearch vehicleTripSheetDetailsSearch) {

        return vehicleTripSheetDetailsRepository.search(vehicleTripSheetDetailsSearch);
    }

    private void validate(final VehicleTripSheetDetailsRequest vehicleTripSheetDetailsRequest) {

        final RouteSearch routeSearch = new RouteSearch();
        Pagination<Route> routes;
        VehicleSearch vehicleSearch;
        Pagination<Vehicle> vehicleList;
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        for (final VehicleTripSheetDetails vehicleTripSheetDetails : vehicleTripSheetDetailsRequest
                .getVehicleTripSheetDetails()) {

            // Validation for toDate to be greater than fromDate
            if (vehicleTripSheetDetails.getTripStartDate() != null && vehicleTripSheetDetails.getTripEndDate() != null)
                if (new Date(vehicleTripSheetDetails.getTripEndDate())
                        .before(new Date(vehicleTripSheetDetails.getTripStartDate())))
                    throw new CustomException("TripEndDate",
                            "Vehicle Trip end date shall be greater than Vehicle Trip start date: "
                                    + dateFormat.format(new Date(vehicleTripSheetDetails.getTripEndDate())));

            if (vehicleTripSheetDetails.getOutTime() != null && vehicleTripSheetDetails.getInTime() != null
                    && vehicleTripSheetDetails.getOutTime() < vehicleTripSheetDetails.getInTime())
                throw new CustomException("OutTime",
                        "Out time should be greater than or equal to In time");

            if (vehicleTripSheetDetails.getRoute() != null && (vehicleTripSheetDetails.getRoute().getCode() == null
                    || vehicleTripSheetDetails.getRoute().getCode().isEmpty()))
                throw new CustomException("Route",
                        "The field Route Code is Mandatory . It cannot be not be null or empty.Please provide correct value ");

            // Validate Route

            if (vehicleTripSheetDetails.getRoute() != null && vehicleTripSheetDetails.getRoute().getCode() != null) {

                routeSearch.setTenantId(vehicleTripSheetDetails.getTenantId());
                routeSearch.setCode(vehicleTripSheetDetails.getRoute().getCode());
                routes = routeService.search(routeSearch);

                if (routes == null || routes.getPagedData() == null || routes.getPagedData().isEmpty())
                    throw new CustomException("Route",
                            "Given Route is invalid: " + vehicleTripSheetDetails.getRoute().getCode());
                else
                    vehicleTripSheetDetails.setRoute(routes.getPagedData().get(0));

            }

            if (vehicleTripSheetDetails.getVehicle() != null
                    && (vehicleTripSheetDetails.getVehicle().getRegNumber() == null
                            || vehicleTripSheetDetails.getVehicle().getRegNumber().isEmpty()))
                throw new CustomException("Vehicle",
                        "The field Vehicle registration number is Mandatory . It cannot be not be null or empty.Please provide correct value ");

            // Validate Vehicle

            if (vehicleTripSheetDetails.getVehicle() != null
                    && vehicleTripSheetDetails.getVehicle().getRegNumber() != null) {

                vehicleSearch = new VehicleSearch();
                vehicleSearch.setTenantId(vehicleTripSheetDetails.getTenantId());
                vehicleSearch.setRegNumber(vehicleTripSheetDetails.getVehicle().getRegNumber());
                vehicleList = vehicleService.search(vehicleSearch);

                if (vehicleList == null || vehicleList.getPagedData() == null || vehicleList.getPagedData().isEmpty())
                    throw new CustomException("Vehicle",
                            "Given Vehicle is invalid: " + vehicleTripSheetDetails.getVehicle().getRegNumber());
                else
                    vehicleTripSheetDetails.setVehicle(vehicleList.getPagedData().get(0));

            }

            //Entry weight and Exit weight validations
            if(vehicleTripSheetDetails.getEntryWeight() != null &&
                    vehicleTripSheetDetails.getExitWeight() != null &&
                    vehicleTripSheetDetails.getExitWeight() >= vehicleTripSheetDetails.getEntryWeight()){
                throw new CustomException("Invalid Weights",
                        " Entry weight shall be more than exit weight. ");
            }

        }

    }

    private String generateTripNumber(final String tenantId, final RequestInfo requestInfo) {

        return idgenRepository.getIdGeneration(tenantId, requestInfo, idGenNameForVehicleTripNumberPath);
    }

    private void setAuditDetails(final VehicleTripSheetDetails contract, final Long userId) {

        if (contract.getAuditDetails() == null)
            contract.setAuditDetails(new AuditDetails());

        if (null == contract.getTripNo() || contract.getTripNo().isEmpty()) {
            contract.getAuditDetails().setCreatedBy(null != userId ? userId.toString() : null);
            contract.getAuditDetails().setCreatedTime(new Date().getTime());
        }

        contract.getAuditDetails().setLastModifiedBy(null != userId ? userId.toString() : null);
        contract.getAuditDetails().setLastModifiedTime(new Date().getTime());
    }

}