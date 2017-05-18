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

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.demand.domain.service.DemandReasonService;
import org.egov.demand.persistence.entity.EgDemandReason;
import org.egov.demand.web.contract.DemandReason;
import org.egov.demand.web.contract.DemandReasonCriteria;
import org.egov.demand.web.contract.DemandReasonResponse;
import org.egov.demand.web.contract.RequestInfo;
import org.egov.demand.web.contract.ResponseInfo;
import org.egov.demand.web.contract.factory.ResponseInfoFactory;
import org.egov.demand.web.errorhandler.ErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("demand/demandreason")
public class DemandReasonConroller {

	@Autowired
	private DemandReasonService demandReasonService;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@Autowired
	private ErrorHandler errHandler;

	@PostMapping("_search")
	@ResponseBody
	public ResponseEntity<?> search(@ModelAttribute @Valid DemandReasonCriteria demandReasonCriteria,@RequestBody RequestInfo requestInfo,
			 BindingResult bindingResult) {
		List<EgDemandReason> egDemandReasons = null;
		DemandReason demandReason = null;
		List<DemandReason> demandReasons = new ArrayList<>();
		if (bindingResult.hasErrors()) {
			return errHandler.getErrorResponseEntityForBindingErrors(bindingResult, requestInfo);
		}
		try {
			egDemandReasons = demandReasonService.search(demandReasonCriteria);
			for(EgDemandReason demRes: egDemandReasons){
				demandReason = new DemandReason();
				demandReason.setCategory(demRes.getEgDemandReasonMaster().getEgReasonCategory().getName());
				demandReason.setName(demRes.getEgDemandReasonMaster().getCode());
				demandReason.setTaxPeriod(demRes.getEgInstallmentMaster().getDescription());
				//demandReason.setGlCode(demRes.getGlcodeId().toString());
				demandReasons.add(demandReason);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getSuccessResponseForSearch(demandReasons, requestInfo);
	}

	/**
	 * Populate Response object and return Demand List
	 * 
	 * @param employeesList
	 * @return
	 */
	private ResponseEntity<?> getSuccessResponse(DemandReason demandReason, RequestInfo requestInfo) {
		DemandReasonResponse demandReasonRes = new DemandReasonResponse();
		List<DemandReason> demandReasons = new ArrayList<DemandReason>();
		demandReasons.add(demandReason);
		demandReasonRes.setDemandReasons(demandReasons);

		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		demandReasonRes.setResponseInfo(responseInfo);
		return new ResponseEntity<DemandReasonResponse>(demandReasonRes, HttpStatus.OK);
	}

	private ResponseEntity<?> getSuccessResponseForSearch(List<DemandReason> demandReasonList, RequestInfo requestInfo) {
		DemandReasonResponse demandReasonRes = new DemandReasonResponse();
		demandReasonRes.setDemandReasons(demandReasonList);
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		demandReasonRes.setResponseInfo(responseInfo);
		return new ResponseEntity<DemandReasonResponse>(demandReasonRes, HttpStatus.OK);
	}
}
