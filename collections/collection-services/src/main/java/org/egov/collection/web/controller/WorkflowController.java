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

import java.util.List;

import javax.validation.Valid;

import org.egov.collection.config.CollectionServiceConstants;
import org.egov.collection.model.Department;
import org.egov.collection.model.DepartmentSearchCriteria;
import org.egov.collection.model.EmployeeInfo;
import org.egov.collection.model.UserSearchCriteriaWrapper;
import org.egov.collection.model.WorkflowDetails;
import org.egov.collection.service.WorkflowService;
import org.egov.collection.web.contract.DepartmentResponse;
import org.egov.collection.web.contract.ReceiptReq;
import org.egov.collection.web.contract.UserResponse;
import org.egov.collection.web.contract.factory.ResponseInfoFactory;
import org.egov.collection.web.errorhandlers.Error;
import org.egov.collection.web.errorhandlers.ErrorResponse;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("receipt-workflow")
public class WorkflowController {
	public static final Logger LOGGER = LoggerFactory
			.getLogger(ReceiptController.class);

	@Autowired
	private WorkflowService workflowService;
		
	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@PostMapping("/departments/_search")
	@ResponseBody
	public ResponseEntity<?> getDepartments(
			@RequestBody @Valid final DepartmentSearchCriteria departmentSearchCriteria, BindingResult errors) {

		if (errors.hasFieldErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
		}	
		if(!validateTenantId(departmentSearchCriteria.getTenantId())){
			LOGGER.info("Invalid TenantId");
			Error error = new Error();
			error.setMessage(CollectionServiceConstants.TENANT_ID_MISSING_MESSAGE);
			ErrorResponse errorResponse = new ErrorResponse();
			errorResponse.setError(error);
			
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}
		List<Department> departments = workflowService.getDepartments(departmentSearchCriteria);
				
		if(null == departments){
			LOGGER.info("Service returned null");
			Error error = new Error();
			error.setMessage(CollectionServiceConstants.INVALID_RECEIPT_REQUEST);
			ErrorResponse errorResponse = new ErrorResponse();
			errorResponse.setError(error);
			
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
			
		}
				
		return getDeptSuccessResponse(departments, departmentSearchCriteria.getRequestInfo());
	}
	
/*	@PostMapping("/designations/_search")
	@ResponseBody
	public ResponseEntity<?> getDesignations(
			@RequestBody @Valid final DesignationSearchCriteria designationSearchCriteria, BindingResult errors) {

		if (errors.hasFieldErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
		}	
		
		List<Department> departments = workflowService.getDepartments(designationSearchCriteria.getRequestInfo());
				
		if(null == departments){
			LOGGER.info("Service returned null");
			Error error = new Error();
			error.setMessage(CollectionServiceConstants.INVALID_RECEIPT_REQUEST);
			ErrorResponse errorResponse = new ErrorResponse();
			errorResponse.setError(error);
			
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
			
		}
				
		return getDeptSuccessResponse(departments, designationSearchCriteria.getRequestInfo());
	} */
	
	
	@PostMapping("/users/_search")
	@ResponseBody
	public ResponseEntity<?> getUsers(
			@RequestBody @Valid final UserSearchCriteriaWrapper userSeachCriteriaWrapper, BindingResult errors) {

		if (errors.hasFieldErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
		}				
		if(!validateTenantId(userSeachCriteriaWrapper.getUserSearchCriteria().getTenantId())){
			LOGGER.info("Invalid TenantId");
			Error error = new Error();
			error.setMessage(CollectionServiceConstants.TENANT_ID_MISSING_MESSAGE);
			ErrorResponse errorResponse = new ErrorResponse();
			errorResponse.setError(error);
			
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}
		List<EmployeeInfo> users = workflowService.getUsers(userSeachCriteriaWrapper);
				
		if(null == users){
			LOGGER.info("Service returned null");
			Error error = new Error();
			error.setMessage(CollectionServiceConstants.INVALID_USERS_REQUEST);
			ErrorResponse errorResponse = new ErrorResponse();
			errorResponse.setError(error);
			
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
			
		}
				
		return getUsersSuccessResponse(users, userSeachCriteriaWrapper.getRequestInfo());
	}
	
	
	@PostMapping("/start")
	@ResponseBody
	public ResponseEntity<?> startWorkflow(
			@RequestBody @Valid final WorkflowDetails workflowDetails, BindingResult errors) {

		if (errors.hasFieldErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
		}				
		if(!validateTenantId(workflowDetails.getTenantId())){
			LOGGER.info("Invalid TenantId");
			Error error = new Error();
			error.setMessage(CollectionServiceConstants.TENANT_ID_MISSING_MESSAGE);
			ErrorResponse errorResponse = new ErrorResponse();
			errorResponse.setError(error);
			
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}
		WorkflowDetails workflowDetailsObj = workflowService.pushToQueue(workflowDetails);
				
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
	
	
	
	
	private ResponseEntity<?> getDeptSuccessResponse(List<Department> departments,
			RequestInfo requestInfo) {
		LOGGER.info("Building success response.");
		DepartmentResponse departmentResponse = new DepartmentResponse();
		final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		departmentResponse.setDepartment(departments);
		departmentResponse.setResponseInfo(responseInfo);
		return new ResponseEntity<>(departmentResponse, HttpStatus.OK);
	}
	
	private ResponseEntity<?> getUsersSuccessResponse(List<EmployeeInfo> users,
			RequestInfo requestInfo) {
		LOGGER.info("Building success response.");
		UserResponse userResponse = new UserResponse();
		final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		userResponse.setUsers(users);
		userResponse.setResponseInfo(responseInfo);
		return new ResponseEntity<>(userResponse, HttpStatus.OK);
	}

	private ErrorResponse populateErrors(BindingResult errors) {
		ErrorResponse errRes = new ErrorResponse();		
		Error error = new Error();
		error.setCode(1);
		error.setDescription("Error while binding request");
		if (errors.hasFieldErrors()) {
			for (FieldError errs : errors.getFieldErrors()) {
				error.getFields()
						.put(errs.getField(), errs.getDefaultMessage());
			}
		}
		errRes.setError(error);
		return errRes;
	}
	
	private boolean validateTenantId(String tenantId){
		boolean isTenantValid = true;
		if(null == tenantId || tenantId.isEmpty())
			isTenantValid = false;
		return isTenantValid;
	}
}
