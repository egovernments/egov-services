package org.egov.pa.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.pa.service.KpiTargetService;
import org.egov.pa.validator.RequestValidator;
import org.egov.pa.web.contract.KPITargetGetRequest;
import org.egov.pa.web.contract.KPITargetRequest;
import org.egov.pa.web.contract.KPITargetResponse;
import org.egov.pa.web.contract.RequestInfoWrapper;
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
public class KpiTargetController implements KpiTarget{
	
	@Autowired
    private RequestValidator requestValidator; 

    @Autowired 
	@Qualifier("kpiTargetServ")
	private KpiTargetService kpiTargetService;
    
    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    @Override
	@PostMapping(value = "/_create")
    @ResponseBody
	public ResponseEntity<?> create(@RequestBody @Valid final KPITargetRequest kpiTargetRequest,
            final BindingResult errors) {
		log.info("KPI Target Request as recieved in Controller : " + kpiTargetRequest);
		if (errors.hasErrors()) {
            final List<ErrorResponse> errResList = new ArrayList<>();
            errResList.add(requestValidator.populateErrors(errors)); 
            return new ResponseEntity<>(errResList, HttpStatus.BAD_REQUEST);
        }
		final List<ErrorResponse> errorResponses = requestValidator.validateRequest(kpiTargetRequest, Boolean.TRUE);
        if (!errorResponses.isEmpty())
            return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);
        kpiTargetService.createNewTarget(kpiTargetRequest); 
		return getCreateUpdateSuccessResponse(kpiTargetRequest);
	}

	@Override
	@PostMapping(value = "/_update")
    @ResponseBody
	public ResponseEntity<?> update(@RequestBody @Valid final KPITargetRequest kpiTargetRequest,
            final BindingResult errors) {
		log.info("KPI Target Update Request as recieved in Controller : " + kpiTargetRequest);
		if (errors.hasErrors()) {
            final List<ErrorResponse> errResList = new ArrayList<>();
            errResList.add(requestValidator.populateErrors(errors)); 
            return new ResponseEntity<>(errResList, HttpStatus.BAD_REQUEST);
        }
		final List<ErrorResponse> errorResponses = requestValidator.validateRequest(kpiTargetRequest, Boolean.FALSE);
        if (!errorResponses.isEmpty())
            return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);
        kpiTargetService.updateNewTarget(kpiTargetRequest); 
        return getCreateUpdateSuccessResponse(kpiTargetRequest);
	}
	
	

	@Override
	public ResponseEntity<?> delete(KPITargetRequest kpiTargetRequest, BindingResult errors) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<?> search(@RequestParam(value="kpiCodes", required = false) List<String> kpiCodes,
			 @RequestParam(value="finYear", required = false) List<String> finYearList,
			 @RequestParam(value="departmentId", required = false) List<Long> departmentId,
			 @RequestParam(value="categoryId", required = false) List<String> categoryId,
			 @RequestParam(value="tenantId", required = false) String tenantId,
			 @RequestBody RequestInfoWrapper requestInfo) {
		log.info("KPI Get Target Request as recieved in Controller : " + kpiCodes + finYearList + departmentId);
		KPITargetGetRequest getReq = new KPITargetGetRequest(); 
		getReq.setFinYear(finYearList);
		getReq.setKpiCode(kpiCodes);
		getReq.setDepartmentId(departmentId);
		getReq.setCategoryId(categoryId);
		getReq.setTenantId(tenantId);
		List<org.egov.pa.model.KpiTarget> targetList = kpiTargetService.searchKpiTarget(getReq); 
		return getSearchSuccessResponse(targetList, requestInfo.getRequestInfo());
	}
	
	public ResponseEntity<?> getCreateUpdateSuccessResponse(final KPITargetRequest kpiTargetRequest) {
        KPITargetResponse response = new KPITargetResponse(); 
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(kpiTargetRequest.getRequestInfo(), true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        response.setResponseInfo(responseInfo);
        response.setKpiTargets(kpiTargetRequest.getKpiTargets());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
	
	public ResponseEntity<?> getSearchSuccessResponse(List<org.egov.pa.model.KpiTarget> targetList, RequestInfo rInfo) {
        KPITargetResponse response = new KPITargetResponse(); 
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(rInfo,true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        response.setResponseInfo(responseInfo);
        response.setKpiTargets(targetList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
