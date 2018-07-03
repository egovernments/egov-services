package org.egov.pt.calculator.controller;

import java.util.Map;

import javax.validation.Valid;

import org.egov.pt.calculator.service.DemandService;
import org.egov.pt.calculator.service.EstimationService;
import org.egov.pt.calculator.web.models.Calculation;
import org.egov.pt.calculator.web.models.CalculationReq;
import org.egov.pt.calculator.web.models.CalculationRes;
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
	private EstimationService calculatorService;

	@PostMapping("/_estimate")
	public ResponseEntity<CalculationRes> getTaxEstimation(@RequestBody @Valid CalculationReq calculationReq) {

		return new ResponseEntity<>(calculatorService.getTaxCalculation(calculationReq), HttpStatus.OK);
	}

	@PostMapping("/_generate")
	public ResponseEntity<Map<String, Calculation>> generateDemands(@RequestBody @Valid CalculationReq calculationReq) {

		return new ResponseEntity<>(demandService.generateDemands(calculationReq), HttpStatus.OK);
	}
}
