package org.egov.swm.domain.service;

import java.util.Date;
import java.util.UUID;

import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.VehicleFuellingDetails;
import org.egov.swm.domain.repository.VehicleFuellingDetailsRepository;
import org.egov.swm.web.requests.VehicleFuellingDetailsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class VehicleFuellingDetailsService {

	@Autowired
	private VehicleFuellingDetailsRepository vehicleFuellingDetailsRepository;

	@Transactional
	public VehicleFuellingDetailsRequest create(VehicleFuellingDetailsRequest vehicleFuellingDetailsRequest) {

		for (VehicleFuellingDetails v : vehicleFuellingDetailsRequest.getVehicleFuellingDetailses()) {

			setAuditDetails(v, vehicleFuellingDetailsRequest.getRequestInfo().getUserInfo().getId());
			v.setId(UUID.randomUUID().toString().replace("-", ""));
			

		}

		return vehicleFuellingDetailsRepository.save(vehicleFuellingDetailsRequest);

	}

	@Transactional
	public VehicleFuellingDetailsRequest update(VehicleFuellingDetailsRequest vehicleFuellingDetailsRequest) {

		for (VehicleFuellingDetails v : vehicleFuellingDetailsRequest.getVehicleFuellingDetailses()) {

			setAuditDetails(v, vehicleFuellingDetailsRequest.getRequestInfo().getUserInfo().getId());

		}

		return vehicleFuellingDetailsRepository.update(vehicleFuellingDetailsRequest);

	}

	private void setAuditDetails(VehicleFuellingDetails contract, Long userId) {

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