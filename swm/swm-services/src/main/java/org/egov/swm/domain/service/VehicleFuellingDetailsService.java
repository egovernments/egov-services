package org.egov.swm.domain.service;

import static org.springframework.util.StringUtils.isEmpty;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.RefillingPumpStation;
import org.egov.swm.domain.model.RefillingPumpStationSearch;
import org.egov.swm.domain.model.Vehicle;
import org.egov.swm.domain.model.VehicleFuellingDetails;
import org.egov.swm.domain.model.VehicleFuellingDetailsSearch;
import org.egov.swm.domain.model.VehicleSearch;
import org.egov.swm.domain.repository.RefillingPumpStationRepository;
import org.egov.swm.domain.repository.VehicleFuellingDetailsRepository;
import org.egov.swm.domain.repository.VehicleRepository;
import org.egov.swm.web.repository.IdgenRepository;
import org.egov.swm.web.requests.VehicleFuellingDetailsRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class VehicleFuellingDetailsService {

    @Autowired
    private VehicleFuellingDetailsRepository vehicleFuellingDetailsRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private IdgenRepository idgenRepository;

    @Autowired
    private RefillingPumpStationRepository refillingPumpStationRepository;

    @Value("${egov.swm.vehiclefuellingdetails.transaction.num.idgen.name}")
    private String idGenNameForTrnNumPath;

    @Transactional
    public VehicleFuellingDetailsRequest create(final VehicleFuellingDetailsRequest vehicleFuellingDetailsRequest) {

        validate(vehicleFuellingDetailsRequest);

        Long userId = null;

        if (vehicleFuellingDetailsRequest.getRequestInfo() != null
                && vehicleFuellingDetailsRequest.getRequestInfo().getUserInfo() != null
                && null != vehicleFuellingDetailsRequest.getRequestInfo().getUserInfo().getId())
            userId = vehicleFuellingDetailsRequest.getRequestInfo().getUserInfo().getId();

        for (final VehicleFuellingDetails vfd : vehicleFuellingDetailsRequest.getVehicleFuellingDetails()) {

            setAuditDetails(vfd, userId);

            vfd.setTransactionNo(
                    generateTransactionNumber(vfd.getTenantId(), vehicleFuellingDetailsRequest.getRequestInfo()));

        }

        return vehicleFuellingDetailsRepository.save(vehicleFuellingDetailsRequest);

    }

    @Transactional
    public VehicleFuellingDetailsRequest update(final VehicleFuellingDetailsRequest vehicleFuellingDetailsRequest) {

        Long userId = null;

        if (vehicleFuellingDetailsRequest.getRequestInfo() != null
                && vehicleFuellingDetailsRequest.getRequestInfo().getUserInfo() != null
                && null != vehicleFuellingDetailsRequest.getRequestInfo().getUserInfo().getId())
            userId = vehicleFuellingDetailsRequest.getRequestInfo().getUserInfo().getId();

        for (final VehicleFuellingDetails vfd : vehicleFuellingDetailsRequest.getVehicleFuellingDetails())
            setAuditDetails(vfd, userId);

        validate(vehicleFuellingDetailsRequest);

        return vehicleFuellingDetailsRepository.update(vehicleFuellingDetailsRequest);

    }

    public Pagination<VehicleFuellingDetails> search(final VehicleFuellingDetailsSearch vehicleFuellingDetailsSearch) {

        final Pagination<VehicleFuellingDetails> vehicleFuellingDetailsPage = vehicleFuellingDetailsRepository
                .search(vehicleFuellingDetailsSearch);

        List<VehicleFuellingDetails> vehicleFuellingDetailsList = new ArrayList<>();

        if (!vehicleFuellingDetailsPage.getPagedData().isEmpty() && !isEmpty(vehicleFuellingDetailsSearch.getFuelTypeCode())) {
            vehicleFuellingDetailsList = filterByFuelType(vehicleFuellingDetailsPage.getPagedData(),
                    vehicleFuellingDetailsSearch.getFuelTypeCode());
            vehicleFuellingDetailsPage.setPagedData(vehicleFuellingDetailsList);
        }

        if (!vehicleFuellingDetailsPage.getPagedData().isEmpty() && !isEmpty(vehicleFuellingDetailsSearch.getVehicleTypeCode())) {
            vehicleFuellingDetailsList = filterByVehicleType(vehicleFuellingDetailsPage.getPagedData(),
                    vehicleFuellingDetailsSearch.getVehicleTypeCode());
            vehicleFuellingDetailsPage.setPagedData(vehicleFuellingDetailsList);
        }

        return vehicleFuellingDetailsPage;
    }

    private List<VehicleFuellingDetails> filterByFuelType(final List<VehicleFuellingDetails> vehicleFuellingDetailsList,
            final String fuelTypeCode) {
        return vehicleFuellingDetailsList.stream()
                .filter(fuellingDetail -> fuellingDetail.getVehicle() != null &&
                        fuellingDetail.getVehicle().getFuelType() != null &&
                        !isEmpty(fuellingDetail.getVehicle().getFuelType().getCode()) &&
                        fuellingDetail.getVehicle().getFuelType().getCode()
                                .equals(fuelTypeCode))
                .collect(Collectors.toList());
    }

    private List<VehicleFuellingDetails> filterByVehicleType(final List<VehicleFuellingDetails> vehicleFuellingDetailsList,
            final String vehicleTypeCode) {
        return vehicleFuellingDetailsList.stream()
                .filter(fuellingDetail -> fuellingDetail.getVehicle() != null &&
                        fuellingDetail.getVehicle().getVehicleType() != null &&
                        !isEmpty(fuellingDetail.getVehicle().getVehicleType().getCode()) &&
                        fuellingDetail.getVehicle().getVehicleType().getCode()
                                .equals(vehicleTypeCode))
                .collect(Collectors.toList());
    }

    private void validate(final VehicleFuellingDetailsRequest vehicleFuellingDetailsRequest) {

        Pagination<RefillingPumpStation> refillingPumpStationList;
        VehicleSearch vehicleSearch;
        Pagination<Vehicle> vehicleList;
        RefillingPumpStationSearch refillingPumpStationSearch;
        VehicleFuellingDetailsSearch vehicleFuellingDetailsSearch;
        Pagination<VehicleFuellingDetails> fuellingDetails;
        final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        for (final VehicleFuellingDetails details : vehicleFuellingDetailsRequest.getVehicleFuellingDetails()) {

            if (details.getVehicle() != null
                    && (details.getVehicle().getRegNumber() == null || details.getVehicle().getRegNumber().isEmpty()))
                throw new CustomException("Vehicle",
                        "The field Vehicle registration number is Mandatory . It cannot be not be null or empty.Please provide correct value ");

            // Validate Vehicle
            if (details.getVehicle() != null && details.getVehicle().getRegNumber() != null) {

                vehicleSearch = new VehicleSearch();
                vehicleSearch.setTenantId(details.getTenantId());
                vehicleSearch.setRegNumber(details.getVehicle().getRegNumber());
                vehicleList = vehicleRepository.search(vehicleSearch);

                if (vehicleList == null || vehicleList.getPagedData() == null || vehicleList.getPagedData().isEmpty())
                    throw new CustomException("Vehicle",
                            "Given Vehicle is invalid: " + details.getVehicle().getRegNumber());
                else
                    details.setVehicle(vehicleList.getPagedData().get(0));

            }

            // Validate RefuellingPumpStation
            if (details.getRefuellingStation() != null && (details.getRefuellingStation().getCode() == null
                    || details.getRefuellingStation().getCode().isEmpty()))
                throw new CustomException("RefuellingPumpStation",
                        "RefuellingPumpStation code required: " + details.getRefuellingStation().getName());

            if (details.getRefuellingStation() != null) {
                refillingPumpStationSearch = new RefillingPumpStationSearch();
                refillingPumpStationSearch.setTenantId(details.getTenantId());
                refillingPumpStationSearch.setCode(details.getRefuellingStation().getCode());

                refillingPumpStationList = refillingPumpStationRepository.search(refillingPumpStationSearch);

                if (refillingPumpStationList == null && refillingPumpStationList.getPagedData() == null
                        && refillingPumpStationList.getPagedData().isEmpty())
                    throw new CustomException("RefuellingPumpStation",
                            "Given RefuellingPumpStation is invalid: " + details.getRefuellingStation().getName());
                else
                    details.setRefuellingStation(refillingPumpStationList.getPagedData().get(0));
            }

            if (details.getReceiptDate() != null && details.getTransactionDate() != null)
                if (new Date(details.getReceiptDate()).compareTo(new Date(details.getTransactionDate())) > 0)
                    throw new CustomException("ReceiptDate",
                            "Given ReceiptDate is invalid: " + dateFormat.format(new Date(details.getReceiptDate()))
                                    + " Receipt date should not be after transaction Date");

            vehicleFuellingDetailsSearch = new VehicleFuellingDetailsSearch();
            vehicleFuellingDetailsSearch.setTenantId(details.getTenantId());
            vehicleFuellingDetailsSearch.setRefuellingStationName(details.getRefuellingStation().getCode());
            vehicleFuellingDetailsSearch.setReceiptNo(details.getReceiptNo());

            fuellingDetails = search(vehicleFuellingDetailsSearch);

            if ((details.getTransactionNo() == null || details.getTransactionNo().isEmpty())
                    && fuellingDetails != null && fuellingDetails.getPagedData() != null
                    && !fuellingDetails.getPagedData().isEmpty())
                throw new CustomException("VehicleFuellingDetails",
                        "Vehicle Fuelling data already exist for the selected Pump Station:"
                                + details.getRefuellingStation().getName() + " and receipt number: "
                                + details.getReceiptNo());

            if (details.getTransactionNo() != null && !details.getTransactionNo().isEmpty()
                    && fuellingDetails != null && fuellingDetails.getPagedData() != null
                    && !fuellingDetails.getPagedData().isEmpty()
                    && !details.getTransactionNo()
                            .equalsIgnoreCase(fuellingDetails.getPagedData().get(0).getTransactionNo()))
                throw new CustomException("VehicleFuellingDetails",
                        "Vehicle Fuelling data already exist for the selected Pump Station:"
                                + details.getRefuellingStation().getName() + " and receipt number: "
                                + details.getReceiptNo());

        }
    }

    private void setAuditDetails(final VehicleFuellingDetails contract, final Long userId) {

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