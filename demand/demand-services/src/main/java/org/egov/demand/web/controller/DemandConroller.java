/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.demand.web.controller;

import org.apache.log4j.Logger;
import org.egov.demand.domain.service.DemandService;
import org.egov.demand.persistence.entity.EgDemand;
import org.egov.demand.persistence.entity.EgDemandDetails;
import org.egov.demand.persistence.repository.DemandRepository;
import org.egov.demand.web.contract.Demand;
import org.egov.demand.web.contract.DemandDetails;
import org.egov.demand.web.contract.DemandRequest;
import org.egov.demand.web.contract.DemandResponse;
import org.egov.demand.web.contract.DemandSearchCriteria;
import org.egov.demand.web.contract.RequestInfo;
import org.egov.demand.web.contract.ResponseInfo;
import org.egov.demand.web.contract.factory.ResponseInfoFactory;
import org.egov.demand.web.errorhandler.ErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/demand")
public class DemandConroller {
	private static final Logger LOGGER = Logger.getLogger(DemandConroller.class);
	@Autowired
	private DemandRepository demandRepository;
	@Autowired
	private DemandService demandService;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@Autowired
	private ErrorHandler errHandler;

	@PostMapping("_search")
	@ResponseBody
	public ResponseEntity<?> search(@ModelAttribute @Valid DemandSearchCriteria demandSearchCriteria,@RequestBody RequestInfo requestInfo,
									BindingResult bindingResult) {

		LOGGER.info("the request info object : "+requestInfo + "the modelatribute values ::: "+demandSearchCriteria);
		List<Demand> demands = new ArrayList<Demand>();
		List<DemandDetails> demandDetails = new ArrayList<DemandDetails>();
		EgDemand egDemand = null;
		DemandDetails demandDetail = null;
		if (bindingResult.hasErrors() || demandSearchCriteria == null) {
			return errHandler.getErrorResponseEntityForBindingErrors(bindingResult, requestInfo);
		}
		try {
			LOGGER.info("before calling demand repository the id value of demand :::  "+demandSearchCriteria.getDemandId());
			egDemand = demandRepository.findOne(demandSearchCriteria.getDemandId());
			LOGGER.info("before calling todomain ::::   "+demandSearchCriteria.getDemandId());
			Demand demand = egDemand.toDomain();
			demand.setTenantId(egDemand.getTenantId());
			for(EgDemandDetails egdemandDetails: egDemand.getEgDemandDetails()){
				LOGGER.info("inside the loop");
				demandDetail = egdemandDetails.toDomain();
				demandDetails.add(demandDetail);
			}
			demandDetails.sort((d1, d2) -> d1.getPeriodStartDate().compareTo(d2.getPeriodStartDate()));
			demand.setDemandDetails(demandDetails);
			demands.add(demand);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getSuccessResponseForSearch(demands, requestInfo);
	}

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> create(@RequestBody @Valid DemandRequest demandRequest, BindingResult bindingResult) {
		EgDemand egDemand = null;

		LOGGER.info("DemandConroller.create - demandRequest - "+demandRequest);
		if (bindingResult.hasErrors()) {
			return errHandler.getErrorResponseEntityForBindingErrors(bindingResult, demandRequest.getRequestInfo());
		}
		try {
			egDemand = demandService.createDemand(demandRequest.getDemand().get(0));
		} catch (Exception e) {
			e.printStackTrace();
		}
		demandRequest.getDemand().get(0).setId(egDemand.getId());
		return getSuccessResponse(demandRequest.getDemand().get(0), demandRequest.getRequestInfo());
	}

	@PostMapping("/{id}/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> update(@PathVariable final Long id, @RequestBody @Valid DemandRequest demandRequest, BindingResult bindingResult) {
		EgDemand egDemand = null;
		if (bindingResult.hasErrors()) {
			return errHandler.getErrorResponseEntityForBindingErrors(bindingResult, demandRequest.getRequestInfo());
		}
		try {
			egDemand = demandService.updateDemandForCollection(demandRequest.getDemand().get(0));
		} catch (Exception e) {
			e.printStackTrace();
		}
		demandRequest.getDemand().get(0).setId(egDemand.getId());
		return getSuccessResponse(demandRequest.getDemand().get(0), demandRequest.getRequestInfo());
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> update(@RequestBody @Valid DemandRequest demandRequest, BindingResult bindingResult) {
		EgDemand egDemand = null;
		if (bindingResult.hasErrors()) {
			return errHandler.getErrorResponseEntityForBindingErrors(bindingResult, demandRequest.getRequestInfo());
		}
		try {
			egDemand = demandService.updateDemand(demandRequest.getDemand().get(0));
		} catch (Exception e) {
			e.printStackTrace();
		}
		demandRequest.getDemand().get(0).setId(egDemand.getId());
		return getSuccessResponse(demandRequest.getDemand().get(0), demandRequest.getRequestInfo());
	}

	@PostMapping("/_cancelreceipt")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> cancelReceipt(@RequestBody @Valid DemandRequest demandRequest, BindingResult bindingResult) {
	    LOGGER.info("cancel receipt hit the api");
		EgDemand egDemand = null;
		if (bindingResult.hasErrors()) {
			return errHandler.getErrorResponseEntityForBindingErrors(bindingResult, demandRequest.getRequestInfo());
		}
		try {
			egDemand = demandService.updateDemandForCollectionWithCancelReceipt(demandRequest.getDemand().get(0));
			LOGGER.info("update Demand For Collection With Cancel Receipt");
		} catch (Exception e) {
			e.printStackTrace();
		}
		demandRequest.getDemand().get(0).setId(egDemand.getId());
		return getSuccessResponse(demandRequest.getDemand().get(0), demandRequest.getRequestInfo());
	}

	/**
	 * Populate Response object and return Demand List
	 *
	 * @param employeesList
	 * @return
	 */
	private ResponseEntity<?> getSuccessResponse(Demand demand, RequestInfo requestInfo) {
		DemandResponse demandRes = new DemandResponse();
		List<Demand> demands = new ArrayList<Demand>();
		demands.add(demand);
		demandRes.setDemands(demands);

		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		demandRes.setResponseInfo(responseInfo);
		return new ResponseEntity<DemandResponse>(demandRes, HttpStatus.OK);
	}

	private ResponseEntity<?> getSuccessResponseForSearch(List<Demand> demandsList, RequestInfo requestInfo) {
		DemandResponse employeeRes = new DemandResponse();
		employeeRes.setDemands(demandsList);
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		employeeRes.setResponseInfo(responseInfo);
		return new ResponseEntity<DemandResponse>(employeeRes, HttpStatus.OK);
	}
}