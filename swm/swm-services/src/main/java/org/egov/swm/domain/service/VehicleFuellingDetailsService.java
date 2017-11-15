package org.egov.swm.domain.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.constants.Constants;
import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.FuelType;
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
import org.egov.swm.web.repository.MdmsRepository;
import org.egov.swm.web.requests.VehicleFuellingDetailsRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONArray;

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
	private MdmsRepository mdmsRepository;

	@Autowired
	private RefillingPumpStationRepository refillingPumpStationRepository;

	@Value("${egov.swm.vehiclefuellingdetails.transaction.num.idgen.name}")
	private String idGenNameForTrnNumPath;

	@Transactional
	public VehicleFuellingDetailsRequest create(VehicleFuellingDetailsRequest vehicleFuellingDetailsRequest) {

		validate(vehicleFuellingDetailsRequest);

		Long userId = null;

		if (vehicleFuellingDetailsRequest.getRequestInfo() != null
				&& vehicleFuellingDetailsRequest.getRequestInfo().getUserInfo() != null
				&& null != vehicleFuellingDetailsRequest.getRequestInfo().getUserInfo().getId()) {
			userId = vehicleFuellingDetailsRequest.getRequestInfo().getUserInfo().getId();
		}

		for (VehicleFuellingDetails vfd : vehicleFuellingDetailsRequest.getVehicleFuellingDetails()) {

			setAuditDetails(vfd, userId);

			vfd.setTransactionNo(
					generateTransactionNumber(vfd.getTenantId(), vehicleFuellingDetailsRequest.getRequestInfo()));

			prepareReceiptCopy(vfd);

		}

		return vehicleFuellingDetailsRepository.save(vehicleFuellingDetailsRequest);

	}

	@Transactional
	public VehicleFuellingDetailsRequest update(VehicleFuellingDetailsRequest vehicleFuellingDetailsRequest) {

		Long userId = null;

		if (vehicleFuellingDetailsRequest.getRequestInfo() != null
				&& vehicleFuellingDetailsRequest.getRequestInfo().getUserInfo() != null
				&& null != vehicleFuellingDetailsRequest.getRequestInfo().getUserInfo().getId()) {
			userId = vehicleFuellingDetailsRequest.getRequestInfo().getUserInfo().getId();
		}

		for (VehicleFuellingDetails vfd : vehicleFuellingDetailsRequest.getVehicleFuellingDetails()) {

			setAuditDetails(vfd, userId);

			prepareReceiptCopy(vfd);

		}

		validate(vehicleFuellingDetailsRequest);

		return vehicleFuellingDetailsRepository.update(vehicleFuellingDetailsRequest);

	}

	private void prepareReceiptCopy(VehicleFuellingDetails vfd) {

		if (vfd.getReceiptCopy() != null && vfd.getReceiptCopy().getFileStoreId() != null) {
			vfd.getReceiptCopy().setId(UUID.randomUUID().toString().replace("-", ""));
			vfd.getReceiptCopy().setTenantId(vfd.getTenantId());
			vfd.getReceiptCopy().setRefCode(vfd.getTransactionNo());
			vfd.getReceiptCopy().setAuditDetails(vfd.getAuditDetails());
		}
	}

	public Pagination<VehicleFuellingDetails> search(VehicleFuellingDetailsSearch vehicleFuellingDetailsSearch) {

		return vehicleFuellingDetailsRepository.search(vehicleFuellingDetailsSearch);
	}

	private void validate(VehicleFuellingDetailsRequest vehicleFuellingDetailsRequest) {

		JSONArray responseJSONArray = null;
		ObjectMapper mapper = new ObjectMapper();
		Pagination<RefillingPumpStation> refillingPumpStationList;
		findDuplicatesInUniqueFields(vehicleFuellingDetailsRequest);
		VehicleSearch vehicleSearch;
		Pagination<Vehicle> vehicleList;
		RefillingPumpStationSearch refillingPumpStationSearch;

		for (VehicleFuellingDetails details : vehicleFuellingDetailsRequest.getVehicleFuellingDetails()) {

			if (details.getTypeOfFuel() != null
					&& (details.getTypeOfFuel().getCode() == null || details.getTypeOfFuel().getCode().isEmpty()))
				throw new CustomException("FuelType",
						"The field FuelType Code is Mandatory . It cannot be not be null or empty.Please provide correct value ");

			// Validate Fuel Type
			if (details.getTypeOfFuel() != null) {

				responseJSONArray = mdmsRepository.getByCriteria(details.getTenantId(), Constants.MODULE_CODE,
						Constants.FUELTYPE_MASTER_NAME, "code", details.getTypeOfFuel().getCode(),
						vehicleFuellingDetailsRequest.getRequestInfo());

				if (responseJSONArray != null && responseJSONArray.size() > 0)
					details.setTypeOfFuel(mapper.convertValue(responseJSONArray.get(0), FuelType.class));
				else
					throw new CustomException("FuelType",
							"Given FuelType is invalid: " + details.getTypeOfFuel().getCode());

			}

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
				else {
					details.setVehicle(vehicleList.getPagedData().get(0));
				}

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

			if (details.getReceiptDate() != null && details.getTransactionDate() != null) {

				if (new Date(details.getReceiptDate()).compareTo(new Date(details.getTransactionDate())) > 0) {
					throw new CustomException("ReceiptDate",
							"Given ReceiptDate is invalid: " + new Date(details.getReceiptDate())
									+ " Receipt date should not be after transaction Date");
				}
			}

			validateUniqueFields(details);
		}
	}

	private void findDuplicatesInUniqueFields(VehicleFuellingDetailsRequest vehicleFuellingDetailsRequest) {

		Map<String, String> receiptNoMap = new HashMap<>();

		for (VehicleFuellingDetails details : vehicleFuellingDetailsRequest.getVehicleFuellingDetails()) {

			if (details.getReceiptNo() != null) {
				if (receiptNoMap.get(details.getReceiptNo()) != null)
					throw new CustomException("name",
							"Duplicate ReceiptNos in given Vehicle Fuelling Details : " + details.getReceiptNo());

				receiptNoMap.put(details.getReceiptNo(), details.getReceiptNo());
			}

		}

	}

	private void validateUniqueFields(VehicleFuellingDetails details) {

		if (details.getReceiptNo() != null) {
			if (!vehicleFuellingDetailsRepository.uniqueCheck(details.getTenantId(), "receiptNo",
					details.getReceiptNo(), "transactionNo", details.getTransactionNo())) {

				throw new CustomException("receiptNo", "The field receiptNo must be unique in the system The  value "
						+ details.getReceiptNo()
						+ " for the field receiptNo already exists in the system. Please provide different value ");

			}
		}

	}

	private void setAuditDetails(VehicleFuellingDetails contract, Long userId) {

		if (contract.getAuditDetails() == null)
			contract.setAuditDetails(new AuditDetails());

		if (null == contract.getTransactionNo() || contract.getTransactionNo().isEmpty()) {
			contract.getAuditDetails().setCreatedBy(null != userId ? userId.toString() : null);
			contract.getAuditDetails().setCreatedTime(new Date().getTime());
		}

		contract.getAuditDetails().setLastModifiedBy(null != userId ? userId.toString() : null);
		contract.getAuditDetails().setLastModifiedTime(new Date().getTime());
	}

	private String generateTransactionNumber(String tenantId, RequestInfo requestInfo) {

		return idgenRepository.getIdGeneration(tenantId, requestInfo, idGenNameForTrnNumPath);
	}

}