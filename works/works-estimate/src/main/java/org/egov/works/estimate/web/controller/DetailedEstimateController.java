package org.egov.works.estimate.web.controller;

import org.egov.works.estimate.domain.service.DetailedEstimateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/detailedestimates")
public class DetailedEstimateController {

	@Autowired
	private DetailedEstimateService detailedEstimateService;


}
