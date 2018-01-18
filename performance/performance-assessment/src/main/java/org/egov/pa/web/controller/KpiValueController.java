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

import java.util.List;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.pa.model.ULBKpiValueList;
import org.egov.pa.service.KpiValueService;
import org.egov.pa.validator.RequestValidator;
import org.egov.pa.web.contract.KPIValueCompareSearchResponse;
import org.egov.pa.web.contract.KPIValueRequest;
import org.egov.pa.web.contract.KPIValueResponse;
import org.egov.pa.web.contract.KPIValueSearchRequest;
import org.egov.pa.web.contract.KPIValueSearchResponse;
import org.egov.pa.web.contract.RequestInfoWrapper;
import org.egov.pa.web.contract.ValueResponse;
import org.egov.pa.web.contract.factory.ResponseInfoFactory;
import org.egov.pa.web.errorhandler.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class KpiValueController implements KpiValue {

	@Autowired 
	@Qualifier("kpiValueServ")
	private KpiValueService kpiValueService;
    
    @Autowired
    private RequestValidator requestValidator;
    
    @Autowired
    private ResponseInfoFactory responseInfoFactory;
    
    @Override
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody @Valid final KPIValueRequest kpiValueRequest,
            final BindingResult errors) {
    	log.info("KPI Master Request as recieved in Controller : " + kpiValueRequest);
        if (errors.hasErrors()) {
            final ErrorResponse errRes = requestValidator.populateErrors(errors); 
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }
        final List<ErrorResponse> errorResponses = requestValidator.validateRequest(kpiValueRequest, Boolean.TRUE);
        if (!errorResponses.isEmpty())
        	return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);
        kpiValueService.createKpiValue(kpiValueRequest); 
        return getCreateUpdateSuccessResponse(kpiValueRequest);
    }

    @Override
    @ResponseBody
    public ResponseEntity<?> update(@RequestBody @Valid final KPIValueRequest kpiValueRequest,
            final BindingResult errors) {
    	log.info("KPI Master Request as recieved in Controller : " + kpiValueRequest);
        if (errors.hasErrors()) {
            final ErrorResponse errRes = requestValidator.populateErrors(errors); 
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }
        final List<ErrorResponse> errorResponses = requestValidator.validateRequest(kpiValueRequest, Boolean.FALSE);
        if (!errorResponses.isEmpty())
            return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);
        kpiValueService.updateKpiValue(kpiValueRequest); 
        return getCreateUpdateSuccessResponse(kpiValueRequest);
    }
    
    @Override
    @ResponseBody
    public ResponseEntity<?> compareAndSearch(@RequestParam(value = "tenantId", required=false) List<String> tenantIdList,
			 @RequestParam(value="departmentId", required = false) Long departmentId,
			 @RequestParam(value="kpiCodes", required = false) List<String> kpiCodes,
			 @RequestParam(value="finYear", required = false) List<String> finYearList,
			 @RequestParam(value="ulbs", required = false) List<String> ulbList,
			 @RequestParam(value="categoryId", required = false) Long categoryId,
			 @RequestParam(value="needDocs", required = false) Boolean needDocs,
			 @RequestBody RequestInfoWrapper requestInfo) {
    	log.info("Request Received for Search : " + tenantIdList + "\n" + departmentId + "\n" + finYearList);
    	KPIValueSearchRequest kpiValueSearchReq = new KPIValueSearchRequest();
    	kpiValueSearchReq.setRequestInfo(requestInfo.getRequestInfo());
    	kpiValueSearchReq.setFinYear(finYearList);
    	kpiValueSearchReq.setDepartmentId(departmentId);
    	kpiValueSearchReq.setKpiCodes(kpiCodes);
    	kpiValueSearchReq.setTenantId(tenantIdList);
    	kpiValueSearchReq.setUlbList(ulbList);
    	kpiValueSearchReq.setCategoryId(categoryId);
    	kpiValueSearchReq.setNeedDocs(needDocs);
    	final List<ErrorResponse> errorResponses = requestValidator.validateRequest(kpiValueSearchReq, Boolean.TRUE);
        if (!errorResponses.isEmpty())
            return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);
        List<ULBKpiValueList> list = kpiValueService.compareSearchKpiValue(kpiValueSearchReq); 
        return getCompareSearchSuccessResponse(list, kpiValueSearchReq.getRequestInfo()); 
    }
    
    @Override
    @PostMapping(value = "/_search")
    @ResponseBody
	public ResponseEntity<?> search(@RequestParam("tenantId") List<String> tenantIdList,
			 @RequestParam(value="departmentId", required = false) Long departmentId,
			 @RequestParam(value="kpiCodes", required = false) List<String> kpiCodes,
			 @RequestParam(value="finYear", required = false) List<String> finYearList,
			 @RequestParam(value="ulbs", required = false) List<String> ulbList,
			 @RequestParam(value="categoryId", required = false) Long categoryId,
			 @RequestBody RequestInfoWrapper requestInfo) {
    	
    	log.info("Request Received for Search : " + tenantIdList + "\n" + departmentId + "\n" + finYearList);
    	KPIValueSearchRequest kpiValueSearchReq = new KPIValueSearchRequest();
    	kpiValueSearchReq.setRequestInfo(requestInfo.getRequestInfo());
    	kpiValueSearchReq.setFinYear(finYearList);
    	kpiValueSearchReq.setDepartmentId(departmentId);
    	kpiValueSearchReq.setKpiCodes(kpiCodes);
    	kpiValueSearchReq.setTenantId(tenantIdList);
    	kpiValueSearchReq.setUlbList(ulbList);
    	kpiValueSearchReq.setCategoryId(categoryId);
    	final List<ErrorResponse> errorResponses = requestValidator.validateRequest(kpiValueSearchReq, Boolean.FALSE);
        if (!errorResponses.isEmpty())
            return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);
        List<ValueResponse> valueList = kpiValueService.searchKpiValue(kpiValueSearchReq); 
        return getSearchSuccessResponse(valueList, kpiValueSearchReq.getRequestInfo()); 
    }
    
    public ResponseEntity<?> getSearchSuccessResponse(final List<ValueResponse> kpiValues,
            final RequestInfo requestInfo) {
        final KPIValueSearchResponse kpiValueSearchResponse = new KPIValueSearchResponse();
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        kpiValueSearchResponse.setResponseInfo(responseInfo);
        kpiValueSearchResponse.setKpiValues(kpiValues);
        return new ResponseEntity<>(kpiValueSearchResponse, HttpStatus.OK);
    }
    
    public ResponseEntity<?> getCompareSearchSuccessResponse(final List<ULBKpiValueList> list,
            final RequestInfo requestInfo) {
        final KPIValueCompareSearchResponse kpiValueSearchResponse = new KPIValueCompareSearchResponse();
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        kpiValueSearchResponse.setResponseInfo(responseInfo);
        kpiValueSearchResponse.setUlbKpiValues(list);
        return new ResponseEntity<>(kpiValueSearchResponse, HttpStatus.OK);
    }
    
    public ResponseEntity<?> getCreateUpdateSuccessResponse(final KPIValueRequest kpiValueRequest) {
        KPIValueResponse response = new KPIValueResponse(); 
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(kpiValueRequest.getRequestInfo(), true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        response.setResponseInfo(responseInfo);
        response.setKpiValues(kpiValueRequest.getKpiValues());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
