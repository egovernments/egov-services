/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.pa.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.pa.model.DocumentTypeContract;
import org.egov.pa.model.KPI;
import org.egov.pa.service.KpiMasterService;
import org.egov.pa.validator.RequestValidator;
import org.egov.pa.web.contract.DocumentTypeResponse;
import org.egov.pa.web.contract.KPIGetRequest;
import org.egov.pa.web.contract.KPIRequest;
import org.egov.pa.web.contract.KPIResponse;
import org.egov.pa.web.contract.RequestInfoWrapper;
import org.egov.pa.web.contract.factory.ResponseInfoFactory;
import org.egov.pa.web.errorhandler.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class KpiMasterController implements KpiMaster {

    @Autowired
    private RequestValidator requestValidator; 

    @Autowired 
	@Qualifier("kpiMasterServ")
	private KpiMasterService kpiMasterService;
    
    @Autowired
    private ResponseInfoFactory responseInfoFactory;
    
    
    
    @Override
	@PostMapping(value = "/_create")
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody @Valid final KPIRequest kpiRequest,
            final BindingResult errors) {
    	log.info("KPI Master Request as recieved in Controller : " + kpiRequest);
        if (errors.hasErrors()) {
            final List<ErrorResponse> errResList = new ArrayList<>();
            errResList.add(requestValidator.populateErrors(errors)); 
            		
            return new ResponseEntity<>(errResList, HttpStatus.BAD_REQUEST);
        }
        final List<ErrorResponse> errorResponses = requestValidator.validateRequest(kpiRequest, Boolean.TRUE);
        if (!errorResponses.isEmpty())
            return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);
        kpiMasterService.createNewKpi(kpiRequest);
        return getCreateUpdateSuccessResponse(kpiRequest); 
    }
    
    @Override
	@PostMapping(value = "/_update")
    @ResponseBody
    public ResponseEntity<?> update(@RequestBody @Valid final KPIRequest kpiRequest,
            final BindingResult errors) {
    	log.info("KPI Master Update Request as recieved in Controller : " + kpiRequest);
    	if (errors.hasErrors()) {
            final List<ErrorResponse> errResList = new ArrayList<>();
            errResList.add(requestValidator.populateErrors(errors)); 
            		
            return new ResponseEntity<>(errResList, HttpStatus.BAD_REQUEST);
        }
        final List<ErrorResponse> errorResponses = requestValidator.validateRequest(kpiRequest, Boolean.FALSE);
        if (!errorResponses.isEmpty())
            return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);
        kpiMasterService.updateNewKpi(kpiRequest);
        return getCreateUpdateSuccessResponse(kpiRequest); 
    }
    
    @Override
	@PostMapping(value = "/_delete")
    @ResponseBody
    public ResponseEntity<?> delete(@RequestBody @Valid final KPIRequest kpiRequest,
            final BindingResult errors) {
    	log.info("KPI Master Delete Request as recieved in Controller : " + kpiRequest);
        if (errors.hasErrors()) {
            final ErrorResponse errRes = requestValidator.populateErrors(errors); 
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }
        /*final List<ErrorResponse> errorResponses = requestValidator.validateRequest(kpiRequest, Boolean.FALSE);
        if (!errorResponses.isEmpty())
            return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);*/
        kpiMasterService.deleteNewKpi(kpiRequest);
        return getCreateUpdateSuccessResponse(kpiRequest);
    }
    
    @Override
	@PostMapping(value = "/_search")
    @ResponseBody
    public ResponseEntity<?> search(@ModelAttribute @Valid final KPIGetRequest kpiGetRequest,
            final BindingResult errors, @RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
            final BindingResult requestBodyBindingResult) {
        final RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

    	log.info("KPI Master Update Request as recieved in Controller : " + kpiGetRequest);
        if (errors.hasErrors()) {
            final ErrorResponse errRes = requestValidator.populateErrors(errors); 
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }
        List<KPI> departmentKpiList = kpiMasterService.searchKpi(kpiGetRequest); 
        return getSuccessResponse(departmentKpiList, requestInfo); 
    }
    
    @Override
	@PostMapping(value = "/_getkpitype")
    @ResponseBody
    public Boolean getKpiType(@RequestParam(value = "kpiCode") String kpiCode) {
    	log.info("KPI Type Request as recieved in Controller : " + kpiCode);
    	String tenantId= "" ; 
        return kpiMasterService.getKpiType(kpiCode, tenantId); 
    }
    
    @Override
	public ResponseEntity<?>  getDocumentForKpi(@RequestParam("kpiCode") String kpiCode,
			 @RequestBody RequestInfoWrapper requestInfo) {
		List<DocumentTypeContract> docTypeList = kpiMasterService.getDocumentForKpi(kpiCode);
		return getDocumentResponse(docTypeList,requestInfo.getRequestInfo()); 
	}
    
    
    public ResponseEntity<?> getSuccessResponse(final List<KPI> kpiList,
            final RequestInfo requestInfo) {
        final KPIResponse kpiResponse = new KPIResponse();
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        kpiResponse.setResponseInfo(responseInfo);
        kpiResponse.setKpIs(kpiList);
        return new ResponseEntity<>(kpiResponse, HttpStatus.OK);
    }
    
    public ResponseEntity<?> getDocumentResponse(final List<DocumentTypeContract> docTypeList,
            final RequestInfo requestInfo) {
        final DocumentTypeResponse response = new DocumentTypeResponse();
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        response.setResponseInfo(responseInfo);
        response.setDocumentTypes(docTypeList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    public ResponseEntity<?> getCreateUpdateSuccessResponse(final KPIRequest kpiRequest) {
        KPIResponse response = new KPIResponse(); 
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(kpiRequest.getRequestInfo(), true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        response.setResponseInfo(responseInfo);
        response.setKpIs(kpiRequest.getKpIs());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    

}
