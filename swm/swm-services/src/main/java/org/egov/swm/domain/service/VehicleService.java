package org.egov.swm.domain.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import org.egov.swm.constants.Constants;
import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.Documents;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.Vehicle;
import org.egov.swm.domain.model.VehicleSearch;
import org.egov.swm.domain.model.VehicleType;
import org.egov.swm.domain.model.Vendor;
import org.egov.swm.domain.repository.VehicleRepository;
import org.egov.swm.persistence.entity.VendorEntity;
import org.egov.swm.web.contract.MasterDetails;
import org.egov.swm.web.contract.MdmsCriteria;
import org.egov.swm.web.contract.MdmsRequest;
import org.egov.swm.web.contract.MdmsResponse;
import org.egov.swm.web.contract.ModuleDetails;
import org.egov.swm.web.repository.MdmsRepository;
import org.egov.swm.web.requests.VehicleRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONArray;

@Service
@Transactional(readOnly = true)
public class VehicleService {

	@Autowired
	private VehicleRepository vehicleRepository;

	@Autowired
	private MdmsRepository mdmsRepository;

	@Transactional
	public VehicleRequest create(VehicleRequest vehicleRequest) {

		validate(vehicleRequest);

		for (Vehicle v : vehicleRequest.getVehicles()) {

			Long userId = 1l;

			if (vehicleRequest.getRequestInfo() != null && vehicleRequest.getRequestInfo().getUserInfo() != null
					&& null != vehicleRequest.getRequestInfo().getUserInfo().getId()) {
				userId = vehicleRequest.getRequestInfo().getUserInfo().getId();
			}
			setAuditDetails(v, userId);
			v.setId(UUID.randomUUID().toString().replace("-", ""));
			if (v.getInsuranceDocuments() == null) {
				v.setInsuranceDocuments(Documents.builder().id(null).build());
			}

		}

		return vehicleRepository.save(vehicleRequest);

	}

	@Transactional
	public VehicleRequest update(VehicleRequest vehicleRequest) {

		validate(vehicleRequest);

		for (Vehicle v : vehicleRequest.getVehicles()) {

			setAuditDetails(v, vehicleRequest.getRequestInfo().getUserInfo().getId());

		}

		return vehicleRepository.update(vehicleRequest);

	}

	public Pagination<Vehicle> search(VehicleSearch vehicleSearch) {

		return vehicleRepository.search(vehicleSearch);
	}

	private void validate(VehicleRequest vehicleRequest) {

		JSONArray responseJSONArray = null;
		MasterDetails[] masterDetailsArray;
		ModuleDetails[] moduleDetailsArray;
		MdmsRequest request;
		MdmsResponse response;
		ArrayList<VehicleType> vtResponseList;
		ArrayList<Vendor> vResponseList;
		ObjectMapper mapper = new ObjectMapper();

		for (Vehicle details : vehicleRequest.getVehicles()) {

			// Validate VehicleType
			if (details.getVehicleType() != null) {
				masterDetailsArray = new MasterDetails[1];
				masterDetailsArray[0] = MasterDetails.builder().name(Constants.VEHICLETYPE_MASTER_NAME)
						.filter("[?(@.name == '" + details.getVehicleType().getName() + "')]").build();
				moduleDetailsArray = new ModuleDetails[1];
				moduleDetailsArray[0] = ModuleDetails.builder().moduleName(Constants.MODULE_CODE)
						.masterDetails(masterDetailsArray).build();

				request = MdmsRequest.builder()
						.mdmsCriteria(MdmsCriteria.builder().moduleDetails(moduleDetailsArray)
								.tenantId(details.getTenantId()).build())
						.requestInfo(vehicleRequest.getRequestInfo()).build();
				response = mdmsRepository.getByCriteria(request);
				if (response == null || response.getMdmsRes() == null
						|| !response.getMdmsRes().containsKey(Constants.MODULE_CODE)
						|| response.getMdmsRes().get(Constants.MODULE_CODE) == null
						|| !response.getMdmsRes().get(Constants.MODULE_CODE)
								.containsKey(Constants.VEHICLETYPE_MASTER_NAME)
						|| response.getMdmsRes().get(Constants.MODULE_CODE)
								.get(Constants.VEHICLETYPE_MASTER_NAME) == null) {
					throw new CustomException("VehicleType",
							"Given VehicleType is invalid: " + details.getVehicleType().getId());
				} else {
					vtResponseList = new ArrayList<VehicleType>();

					responseJSONArray = response.getMdmsRes().get(Constants.MODULE_CODE)
							.get(Constants.VEHICLETYPE_MASTER_NAME);

					for (int i = 0; i < responseJSONArray.size(); i++) {
						vtResponseList.add(mapper.convertValue(responseJSONArray.get(i), VehicleType.class));
					}

					if (vtResponseList.isEmpty())
						throw new CustomException("VehicleType",
								"Given VehicleType is invalid: " + details.getVehicleType().getId());
					else
						details.setVehicleType(vtResponseList.get(0));
				}
			}

			// Validate vendor
			if (details.getVendor() != null) {
				masterDetailsArray = new MasterDetails[1];
				masterDetailsArray[0] = MasterDetails.builder().name(Constants.VENDOR_MASTER_NAME)
						.filter("[?(@.name == '" + details.getVendor().getName() + "')]").build();
				moduleDetailsArray = new ModuleDetails[1];
				moduleDetailsArray[0] = ModuleDetails.builder().moduleName(Constants.MODULE_CODE)
						.masterDetails(masterDetailsArray).build();

				request = MdmsRequest.builder()
						.mdmsCriteria(MdmsCriteria.builder().moduleDetails(moduleDetailsArray)
								.tenantId(details.getTenantId()).build())
						.requestInfo(vehicleRequest.getRequestInfo()).build();
				response = mdmsRepository.getByCriteria(request);
				if (response == null || response.getMdmsRes() == null
						|| !response.getMdmsRes().containsKey(Constants.MODULE_CODE)
						|| response.getMdmsRes().get(Constants.MODULE_CODE) == null
						|| !response.getMdmsRes().get(Constants.MODULE_CODE).containsKey(Constants.VENDOR_MASTER_NAME)
						|| response.getMdmsRes().get(Constants.MODULE_CODE).get(Constants.VENDOR_MASTER_NAME) == null) {
					throw new CustomException("vender", "Given vender is invalid: " + details.getVendor().getName());
				} else {
					vResponseList = new ArrayList<Vendor>();

					responseJSONArray = response.getMdmsRes().get(Constants.MODULE_CODE)
							.get(Constants.VENDOR_MASTER_NAME);

					for (int i = 0; i < responseJSONArray.size(); i++) {
						vResponseList.add(mapper.convertValue(responseJSONArray.get(i), VendorEntity.class).toDomain());
					}

					if (vResponseList.isEmpty())
						throw new CustomException("vender",
								"Given vender is invalid: " + details.getVendor().getName());
					else
						details.setVendor(vResponseList.get(0));
				}
			}

		}
	}

	private void setAuditDetails(Vehicle contract, Long userId) {

		if (contract.getAuditDetails() == null)
			contract.setAuditDetails(new AuditDetails());

		if (null == contract.getId() || contract.getId().isEmpty()) {
			contract.getAuditDetails().setCreatedBy(null != userId ? userId.toString() : null);
			contract.getAuditDetails().setCreatedTime(new Date().getTime());
		}

		contract.getAuditDetails().setLastModifiedBy(null != userId ? userId.toString() : null);
		contract.getAuditDetails().setLastModifiedTime(new Date().getTime());
	}

}