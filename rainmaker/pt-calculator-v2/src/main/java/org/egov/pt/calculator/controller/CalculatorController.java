package org.egov.pt.calculator.controller;

import java.util.List;

import javax.validation.Valid;

import org.egov.pt.calculator.service.CalculatorService;
import org.egov.pt.calculator.service.DemandService;
import org.egov.pt.calculator.web.models.CalculationReq;
import org.egov.pt.calculator.web.models.CalculationRes;
import org.egov.pt.calculator.web.models.demand.Demand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tax")
public class CalculatorController {

	@Autowired
	private DemandService demandService;

	@Autowired
	private CalculatorService calculatorService;

	@PostMapping("/_estimate")
	public ResponseEntity<CalculationRes> getTaxEstimation(@RequestBody @Valid CalculationReq calculationReq) {

		return new ResponseEntity<>(calculatorService.getEstimates(calculationReq), HttpStatus.OK);
	}

	@PostMapping("/_generate")
	public ResponseEntity<List<Demand>> generateDemands(@RequestBody @Valid CalculationReq calculationReq) {

		return new ResponseEntity<>(demandService.generateDemands(calculationReq), HttpStatus.OK);
	}
}
