package org.egov.lams.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.lams.model.RentIncrementType;
import org.egov.lams.model.ReservationCategory;
import org.egov.lams.model.enums.NatureOfAllotment;
import org.egov.lams.model.enums.PaymentCycle;
import org.egov.lams.model.enums.Source;
import org.egov.lams.model.enums.Status;
import org.egov.lams.repository.RentIncrementRepository;
import org.egov.lams.service.AgreementMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LamsMasterController {

	@Autowired
	private RentIncrementRepository RentIncrementService;
	
	@Autowired
	private AgreementMasterService agreementMasterService;

	@RequestMapping(value = "/getstatus")
	public Map<String, Status> getSatusEnum() {
		Map<String, Status> status = new HashMap<>();
		for (Status key : Status.values()) {
			status.put(key.name(),key);
		}
		return status;
	}

	@RequestMapping(value = "/getpaymentcycle")
	public Map<String, PaymentCycle> getPaymentCycleEnum() {
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
	
	@RequestMapping(value = "/getsource")
	public Map<String, Source> getSourceEnum() {
		Map<String, Source> source = new HashMap<>();
		for (Source key : Source.values()) {
			source.put(key.name(),key);
		}
		return source;
	}

	@RequestMapping(value = "/getrentincrements")
	public List<RentIncrementType> rentIncrementService(@RequestParam("tenantId") String tenantId,
			@RequestParam("basisOfAllotment") String basisOfAllotment) {
		return RentIncrementService.getRentIncrements(tenantId, basisOfAllotment);
	}
	
	@RequestMapping(value = "/getreservations")
	public List<ReservationCategory> getReservations(@RequestParam("tenantId") String tenantId) {
		return agreementMasterService.getReservationCategories(tenantId);
	}
	
}
