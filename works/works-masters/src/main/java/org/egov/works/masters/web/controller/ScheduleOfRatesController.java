package org.egov.works.masters.web.controller;

import org.egov.works.masters.domain.service.ScheduleOfRateService;
import org.egov.works.masters.domain.validator.ScheduleOfRateValidator;
import org.egov.works.masters.utils.MasterUtils;
import org.egov.works.masters.web.contract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/scheduleofrates")
public class ScheduleOfRatesController {

	@Autowired
	private ScheduleOfRateService scheduleOfRatesService;

	@Autowired
	private MasterUtils masterUtils;

	@Autowired
	private ScheduleOfRateValidator scheduleOfRateValidator;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> create(@Valid @RequestBody ScheduleOfRateRequest scheduleOfRateRequest) {
		scheduleOfRateValidator.validate(scheduleOfRateRequest);
		scheduleOfRateValidator.validateForExistance(scheduleOfRateRequest);
		final List<ScheduleOfRate> scheduleOfRates = scheduleOfRatesService.create(scheduleOfRateRequest);
		final ScheduleOfRateResponse response = new ScheduleOfRateResponse();
		response.setScheduleOfRates(scheduleOfRates);
		response.setResponseInfo(masterUtils.createResponseInfoFromRequestInfo(scheduleOfRateRequest.getRequestInfo(), true));
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PostMapping("/_search")
	public ResponseEntity<?> search(
			@ModelAttribute @Valid ScheduleOfRateSearchCriteria scheduleOfRateSearchCriteria,
			@RequestBody RequestInfo requestInfo, BindingResult errors, @RequestParam String tenantId) {
		final List<ScheduleOfRate> scheduleOfRates = scheduleOfRatesService.search(scheduleOfRateSearchCriteria);
		final ScheduleOfRateResponse response = new ScheduleOfRateResponse();
		response.setScheduleOfRates(scheduleOfRates);
		response.setResponseInfo(masterUtils.createResponseInfoFromRequestInfo(requestInfo, true));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/_update")
	public ResponseEntity<?> update(@RequestBody ScheduleOfRateRequest scheduleOfRateRequest) {
		scheduleOfRateValidator.validate(scheduleOfRateRequest);
		final List<ScheduleOfRate> scheduleOfRates = scheduleOfRatesService.update(scheduleOfRateRequest);
		final ScheduleOfRateResponse response = new ScheduleOfRateResponse();
		response.setScheduleOfRates(scheduleOfRates);
		response.setResponseInfo(masterUtils.createResponseInfoFromRequestInfo(scheduleOfRateRequest.getRequestInfo(), true));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
