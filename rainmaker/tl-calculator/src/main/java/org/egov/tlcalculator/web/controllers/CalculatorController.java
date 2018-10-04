package org.egov.tlcalculator.web.controllers;


import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.egov.tlcalculator.service.CalculationService;
import org.egov.tlcalculator.service.DemandService;
import org.egov.tlcalculator.web.models.*;
import org.egov.tlcalculator.web.models.demand.BillResponse;
import org.egov.tlcalculator.web.models.demand.GenerateBillCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;


@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2018-09-27T14:56:03.454+05:30")

@Controller
@RequestMapping("/v1")
public class CalculatorController {

	private ObjectMapper objectMapper;

	private HttpServletRequest request;

	private CalculationService calculationService;

	private DemandService demandService;

	@Autowired
	public CalculatorController(ObjectMapper objectMapper, HttpServletRequest request,
								CalculationService calculationService,DemandService demandService) {
		this.objectMapper = objectMapper;
		this.request = request;
		this.calculationService=calculationService;
		this.demandService=demandService;
	}

	@RequestMapping(value = "/_calculate", method = RequestMethod.POST)
	public ResponseEntity<CalculationRes> calculate(@Valid @RequestBody CalculationReq calculationReq) {

		 List<Calculation> calculations = calculationService.calculate(calculationReq.getRequestInfo(),
				 calculationReq.getCalulationCriteria());

		 CalculationRes calculationRes = CalculationRes.builder().calculation(calculations).build();

		 demandService.generateDemand(calculationReq.getRequestInfo(),calculationRes);

		 return new ResponseEntity<CalculationRes>(calculationRes,HttpStatus.OK);
	}

	@RequestMapping(value = "/_getbill", method = RequestMethod.POST)
	public ResponseEntity<BillResponse> getBill(@Valid @RequestBody RequestInfoWrapper requestInfoWrapper,
										@ModelAttribute @Valid GenerateBillCriteria generateBillCriteria) {
		BillResponse response = demandService.getBill(requestInfoWrapper.getRequestInfo(),generateBillCriteria);
		return new ResponseEntity<BillResponse>(response,HttpStatus.OK);
	}

}
