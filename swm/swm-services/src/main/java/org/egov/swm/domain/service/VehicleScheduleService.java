package org.egov.swm.domain.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.Route;
import org.egov.swm.domain.model.RouteSearch;
import org.egov.swm.domain.model.Vehicle;
import org.egov.swm.domain.model.VehicleSchedule;
import org.egov.swm.domain.model.VehicleScheduleSearch;
import org.egov.swm.domain.model.VehicleSearch;
import org.egov.swm.domain.repository.VehicleScheduleRepository;
import org.egov.swm.web.repository.IdgenRepository;
import org.egov.swm.web.requests.VehicleScheduleRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class VehicleScheduleService {

    @Autowired
    private VehicleScheduleRepository vehicleScheduleRepository;

    @Autowired
    private IdgenRepository idgenRepository;

    @Value("${egov.swm.vehicleschedule.transaction.num.idgen.name}")
    private String idGenNameForVehicleScheduleTNRNumPath;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private RouteService routeService;

    @Transactional
    public VehicleScheduleRequest create(final VehicleScheduleRequest vehicleScheduleRequest) {

        validate(vehicleScheduleRequest);

        Long userId = null;

        if (vehicleScheduleRequest.getRequestInfo() != null
                && vehicleScheduleRequest.getRequestInfo().getUserInfo() != null
                && null != vehicleScheduleRequest.getRequestInfo().getUserInfo().getId())
            userId = vehicleScheduleRequest.getRequestInfo().getUserInfo().getId();

        for (final VehicleSchedule v : vehicleScheduleRequest.getVehicleSchedules()) {

            setAuditDetails(v, userId);

            v.setTransactionNo(generateTransactionNumber(v.getTenantId(), vehicleScheduleRequest.getRequestInfo()));
        }

        return vehicleScheduleRepository.save(vehicleScheduleRequest);

    }

    @Transactional
    public VehicleScheduleRequest update(final VehicleScheduleRequest vehicleScheduleRequest) {

        final Long userId = null;

        for (final VehicleSchedule v : vehicleScheduleRequest.getVehicleSchedules())
            setAuditDetails(v, userId);

        validate(vehicleScheduleRequest);

        return vehicleScheduleRepository.update(vehicleScheduleRequest);

    }

    private void validate(final VehicleScheduleRequest vehicleScheduleRequest) {

        RouteSearch routeSearch;
        Pagination<Route> routes;
        VehicleSearch vehicleSearch;
        Pagination<Vehicle> vehicleList;

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        DateFormat validationDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        validationDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        for (final VehicleSchedule vehicleSchedule : vehicleScheduleRequest.getVehicleSchedules()) {

            if (vehicleSchedule.getVehicle() != null && (vehicleSchedule.getVehicle().getRegNumber() == null
                    || vehicleSchedule.getVehicle().getRegNumber().isEmpty()))
                throw new CustomException("Vehicle",
                        "The field Vehicle registration number is Mandatory. It cannot be not be null or empty.Please provide correct value ");

            // Validate Vehicle

            if (vehicleSchedule.getVehicle() != null && vehicleSchedule.getVehicle().getRegNumber() != null
                    && !vehicleSchedule.getVehicle().getRegNumber().isEmpty()) {

                vehicleSearch = new VehicleSearch();
                vehicleSearch.setTenantId(vehicleSchedule.getTenantId());
                vehicleSearch.setRegNumber(vehicleSchedule.getVehicle().getRegNumber());
                vehicleList = vehicleService.search(vehicleSearch);

                if (vehicleList == null || vehicleList.getPagedData() == null || vehicleList.getPagedData().isEmpty())
                    throw new CustomException("Vehicle",
                            "Given Vehicle is invalid: " + vehicleSchedule.getVehicle().getRegNumber());
                else
                    vehicleSchedule.setVehicle(vehicleList.getPagedData().get(0));

            } else {
                throw new CustomException("Vehicle",
                        "The field Vehicle is Mandatory. It cannot be not be null or empty.Please provide correct value ");
            }

            if (vehicleSchedule.getRoute() != null && (vehicleSchedule.getRoute().getCode() == null
                    || vehicleSchedule.getRoute().getCode().isEmpty()))
                throw new CustomException("Route",
                        "Given Route is invalid: " + vehicleSchedule.getRoute().getCode());

            // Validate Route

            if (vehicleSchedule.getRoute() != null && vehicleSchedule.getRoute().getCode() != null
                    && !vehicleSchedule.getRoute().getCode().isEmpty()) {

                routeSearch = new RouteSearch();
                routeSearch.setTenantId(vehicleSchedule.getTenantId());
                routeSearch.setCode(vehicleSchedule.getRoute().getCode());
                routes = routeService.search(routeSearch);

                if (routes == null || routes.getPagedData() == null || routes.getPagedData().isEmpty())
                    throw new CustomException("Route",
                            "Given Route is invalid: " + vehicleSchedule.getRoute().getCode());
                else
                    vehicleSchedule.setRoute(routes.getPagedData().get(0));

            } else {

                throw new CustomException("Route",
                        "The field Route is Mandatory . It cannot be not be null or empty.Please provide correct value ");
            }

            try {
                if (!validationDateFormat.parse(validationDateFormat.format(vehicleSchedule.getScheduledFrom())).equals(
                        validationDateFormat.parse(validationDateFormat.format(new Date())))
                        && new Date(vehicleSchedule.getScheduledFrom()).before(new Date()))
                    throw new CustomException("ScheduledFromDate",
                            "Schedule can not be created for past dates. Please select Today's or Future date: " +
                                    dateFormat.format(new Date(vehicleSchedule.getScheduledFrom())));
            } catch (ParseException e) {
                throw new CustomException("ScheduleFrom", "Invalid From Date");
            }
        }
    }

    private String generateTransactionNumber(final String tenantId, final RequestInfo requestInfo) {

        return idgenRepository.getIdGeneration(tenantId, requestInfo, idGenNameForVehicleScheduleTNRNumPath);
    }

    public Pagination<VehicleSchedule> search(final VehicleScheduleSearch vehicleScheduleSearch) {

        return vehicleScheduleRepository.search(vehicleScheduleSearch);
    }

    private void setAuditDetails(final VehicleSchedule contract, final Long userId) {

        if (contract.getAuditDetails() == null)
            contract.setAuditDetails(new AuditDetails());

        if (null == contract.getTransactionNo() || contract.getTransactionNo().isEmpty()) {
            contract.getAuditDetails().setCreatedBy(null != userId ? userId.toString() : null);
            contract.getAuditDetails().setCreatedTime(new Date().getTime());
        }

        contract.getAuditDetails().setLastModifiedBy(null != userId ? userId.toString() : null);
        contract.getAuditDetails().setLastModifiedTime(new Date().getTime());
    }

}