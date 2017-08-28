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

package org.egov.collection.web.controller;


import javax.validation.Valid;

import org.egov.collection.config.CollectionServiceConstants;
import org.egov.collection.model.PositionSearchCriteriaWrapper;
import org.egov.collection.service.WorkflowService;
import org.egov.collection.web.contract.WorkflowDetailsRequest;
import org.egov.common.contract.response.Error;
import org.egov.common.contract.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/receipts-workflow")
public class WorkflowController {
	public static final Logger LOGGER = LoggerFactory
			.getLogger(ReceiptController.class);

	@Autowired
	private WorkflowService workflowService;
			
	@PostMapping("/_start")
	@ResponseBody
	public ResponseEntity<?> startWorkflow(
			@RequestBody @Valid final WorkflowDetailsRequest workflowDetails, BindingResult errors) {
		if(!validator(workflowDetails.getTenantId(), workflowDetails.getReceiptHeaderId())){
			LOGGER.info("Invalid TenantId");
			Error error = new Error();
			error.setMessage(CollectionServiceConstants.TENANT_ID_MISSING_MESSAGE);
			ErrorResponse errorResponse = new ErrorResponse();
			errorResponse.setError(error);
			
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}
		PositionSearchCriteriaWrapper positionSearchCriteriaWrapper = new PositionSearchCriteriaWrapper();
		try{
			positionSearchCriteriaWrapper.getPositionSearchCriteria().setEmployeeId(workflowDetails.getUser());
			workflowDetails.setAssignee(workflowService.getPositionForUser(positionSearchCriteriaWrapper));
		}catch(Exception e){
			LOGGER.error("Positions not found for the given user.");
			workflowDetails.setAssignee(0L);
		}
		WorkflowDetailsRequest workflowDetailsObj = workflowService.start(workflowDetails);
				
		if(null == workflowDetailsObj){
			LOGGER.info("Service returned null");
			Error error = new Error();
			error.setMessage(CollectionServiceConstants.INVALID_WF_REQUEST);
			ErrorResponse errorResponse = new ErrorResponse();
			errorResponse.setError(error);
			
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
			
		}
		
		return new ResponseEntity<>(workflowDetailsObj, HttpStatus.OK);				
	}
	
	@PostMapping("/_update")
	@ResponseBody
	public ResponseEntity<?> updateWorkflow(
			@RequestBody @Valid final WorkflowDetailsRequest workflowDetails, BindingResult errors) {

		if(!validator(workflowDetails.getTenantId(), workflowDetails.getReceiptHeaderId())){
			LOGGER.info("Invalid TenantId");
			Error error = new Error();
			error.setCode(Integer.parseInt(HttpStatus.BAD_REQUEST.toString()));
			error.setMessage(CollectionServiceConstants.TENANT_ID_MISSING_MESSAGE);
			ErrorResponse errorResponse = new ErrorResponse();
			errorResponse.setError(error);
			
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}
		WorkflowDetailsRequest workflowDetailsObj = workflowService.update(workflowDetails);
				
		if(null == workflowDetailsObj){
			LOGGER.info("Service returned null");
			Error error = new Error();
			error.setMessage(CollectionServiceConstants.INVALID_WF_REQUEST);
			ErrorResponse errorResponse = new ErrorResponse();
			errorResponse.setError(error);
			
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
			
		}
		
		return new ResponseEntity<>(workflowDetailsObj, HttpStatus.OK);				
	}
			
	private boolean validator(String tenantId, long receiptHeaderId){
		boolean isTenantValid = true;
		if(null == tenantId || tenantId.isEmpty())
			isTenantValid = false;
		else if(receiptHeaderId == 0L)
			isTenantValid = false;
		
		return isTenantValid;
	}
}
