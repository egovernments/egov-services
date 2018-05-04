package org.egov.wcms.web.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.egov.wcms.service.WaterConnectionService;
import org.egov.wcms.validator.WaterConnectionValidator;
import org.egov.wcms.web.models.RequestInfoWrapper;
import org.egov.wcms.web.models.WaterConnectionReq;
import org.egov.wcms.web.models.WaterConnectionRes;
import org.egov.wcms.web.models.WaterConnectionSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiParam;

@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2018-05-03T01:09:48.367+05:30")

@Controller
@RequestMapping("/connection/v2")
public class WaterConnectionController {

	private final ObjectMapper objectMapper;

	private final HttpServletRequest request;

	@Autowired
	private WaterConnectionService waterConnectionService;

	@Autowired
	private WaterConnectionValidator waterConnectionValidator;

	@Autowired
	public WaterConnectionController(ObjectMapper objectMapper, HttpServletRequest request) {
		this.objectMapper = objectMapper;
		this.request = request;
	}

	@RequestMapping(value = "/_create", method = RequestMethod.POST)
	public ResponseEntity<WaterConnectionRes> create(
			@ApiParam(value = "required parameters have to be populated", required = true) @Valid @RequestBody WaterConnectionReq connections) {
		waterConnectionValidator.validateCreateRequest(connections);
		WaterConnectionRes response = waterConnectionService.createWaterConnections(connections);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/_update", method = RequestMethod.POST)
	public ResponseEntity<WaterConnectionRes> update(@Valid @RequestBody WaterConnectionReq connections) {
		waterConnectionValidator.validateUpdateRequest(connections);
		WaterConnectionRes response = waterConnectionService.updateWaterConnections(connections);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/_search", method = RequestMethod.POST)
	public ResponseEntity<WaterConnectionRes> search(@Valid @RequestBody RequestInfoWrapper requestInfoWrapper,
			@Valid @ModelAttribute WaterConnectionSearchCriteria WaterConnectionSearchCriteria) {
		waterConnectionValidator.validateSearchRequest(WaterConnectionSearchCriteria);
		WaterConnectionRes response = waterConnectionService.getWaterConnections(WaterConnectionSearchCriteria,
				requestInfoWrapper.getRequestInfo());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
