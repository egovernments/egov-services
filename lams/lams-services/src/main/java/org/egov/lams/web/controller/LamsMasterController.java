package org.egov.lams.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.lams.model.RentIncrementType;
import org.egov.lams.model.enums.NatureOfAllotment;
import org.egov.lams.model.enums.PaymentCycle;
import org.egov.lams.model.enums.Status;
import org.egov.lams.service.RentIncrementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LamsMasterController {

	@Autowired
	RentIncrementService getRentIncrementService;

	@RequestMapping(value = "/getstatus")
	public Map<String, Status> getSatusEnum() {
		Map<String, Status> status = new HashMap<>();
		for (Status key : Status.values()) {
			status.put(key.name(),key);
		}
		return status;
	}

	@RequestMapping(value = "/getpaymentcycle")
	public Map<String, PaymentCycle> getPayementCycleEnum() {
		Map<String, PaymentCycle> payementCycle = new HashMap<>();
		for (PaymentCycle key : PaymentCycle.values()) {
			payementCycle.put(key.name(),key);
		}
		return payementCycle;
	}

	@RequestMapping(value = "/getnatureofallotment")
	public Map<String, NatureOfAllotment> getNatureOfAllotmentEnum() {
		Map<String, NatureOfAllotment> natureOfAllotment = new HashMap<>();
		for (NatureOfAllotment key : NatureOfAllotment.values()) {
			natureOfAllotment.put(key.name(),key);
		}
		return natureOfAllotment;
	}

	@RequestMapping(value = "/getrentincrements")
	public List<RentIncrementType> rentIncrementService() {
		return getRentIncrementService.getRentIncrements();
	}
}
