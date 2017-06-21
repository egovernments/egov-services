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

package org.egov.eis.web.controller;

import org.egov.eis.model.Employee;
import org.egov.eis.model.EmployeeInfo;
import org.egov.eis.model.User;
import org.egov.eis.service.EmployeeService;
import org.egov.eis.service.exception.EmployeeIdNotFoundException;
import org.egov.eis.service.exception.UserException;
import org.egov.eis.service.helper.UserSearchURLHelper;
import org.egov.eis.web.contract.*;
import org.egov.eis.web.contract.factory.ResponseInfoFactory;
import org.egov.eis.web.errorhandler.ErrorHandler;
import org.egov.eis.web.validator.DataIntegrityValidatorForCreate;
import org.egov.eis.web.validator.DataIntegrityValidatorForUpdate;
import org.egov.eis.web.validator.EmployeeAssignmentValidator;
import org.egov.eis.web.validator.RequestValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private RequestValidator requestValidator;

	@Autowired
	private ErrorHandler errorHandler;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@Autowired
	private EmployeeAssignmentValidator employeeAssignmentValidator;

	@Autowired
	private DataIntegrityValidatorForCreate dataIntegrityValidatorForCreate;

	@Autowired
	private DataIntegrityValidatorForUpdate dataIntegrityValidatorForUpdate;
	
	@Autowired
        private UserSearchURLHelper userSearchURLHelper;

	/**
	 * Maps Post Requests for _search & returns ResponseEntity of either
	 * EmployeeResponse type or ErrorResponse type
	 *
	 * @param employeeCriteria,
	 * @param modelAttributeBindingResult
	 * @param requestInfoWrapper
	 * @param requestBodyBindingResult
	 * @return ResponseEntity<?>
	 */
	@PostMapping("_search")
	@ResponseBody
	public ResponseEntity<?> search(@ModelAttribute @Valid EmployeeCriteria employeeCriteria,
			BindingResult modelAttributeBindingResult, @RequestBody @Valid RequestInfoWrapper requestInfoWrapper,
			BindingResult requestBodyBindingResult) {
		RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

		ResponseEntity<?> errorResponseEntity = requestValidator.validateSearchRequest(requestInfo,
				modelAttributeBindingResult, requestBodyBindingResult);

		if (errorResponseEntity != null)
			return errorResponseEntity;

		// Call service
		List<EmployeeInfo> employeesList = null;
		try {
			employeesList = employeeService.getEmployees(employeeCriteria, requestInfo);
		} catch (Exception exception) {
			LOGGER.error("Error while processing request " + employeeCriteria, exception);
			return errorHandler.getResponseEntityForUnexpectedErrors(requestInfo);
		}

		return getSuccessResponseForSearch(employeesList, requestInfo);
	}
	
	/**
         * Maps Post Requests for _loggedinemployee & returns ResponseEntity of either
         * EmployeeResponse type or ErrorResponse type
         * 
         * @param requestInfoWrapper
         * @param requestBodyBindingResult
         * @param tenantId
         * @param modelAttributeBindingResult
         * @return ResponseEntity<?>
         */
        @PostMapping("_loggedinemployee")
        @ResponseBody
        public ResponseEntity<?> loggedInEmployee(@RequestBody @Valid RequestInfoWrapper requestInfoWrapper,
                        BindingResult requestBodyBindingResult,
                        @RequestParam(value = "tenantId", required = true) String tenantId,
                        BindingResult modelAttributeBindingResult) {
                RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();
                UserGetRequest userGetRequest = new UserGetRequest();
                userGetRequest.setUserName(requestInfo.getUserInfo().getUserName());
                userGetRequest.setTenantId(tenantId);
                UserResponse userResponse = null;
                List<User> users = null;
                List<Long> userIds = new ArrayList<>();
                String url = userSearchURLHelper.searchURL(null, tenantId);
                try {
                    userResponse = new RestTemplate().postForObject(url, userGetRequest, UserResponse.class);
                    users = userResponse.getUser();
                } catch (Exception e) {
                        LOGGER.debug("Following Exception Occurred While Calling User Service : " + e.getMessage());
                        e.printStackTrace();
                }
                for (final User user : users) {
                    userIds.add(user.getId());
                }
                EmployeeCriteria employeeCriteria = new EmployeeCriteria();
                employeeCriteria.setId(userIds);
                employeeCriteria.setTenantId(tenantId);

                ResponseEntity<?> errorResponseEntity = requestValidator.validateSearchRequest(requestInfo,
                                modelAttributeBindingResult, requestBodyBindingResult);

                if (errorResponseEntity != null)
                        return errorResponseEntity;

                // Call service
                List<EmployeeInfo> employeesList = null;
                try {
                        employeesList = employeeService.getEmployees(employeeCriteria, requestInfo);
                } catch (Exception exception) {
                        LOGGER.error("Error while processing request " + employeeCriteria, exception);
                        return errorHandler.getResponseEntityForUnexpectedErrors(requestInfo);
                }

                return getSuccessResponseForSearch(employeesList, requestInfo);
        }

	/**
	 * Maps Post Requests for _search & returns ResponseEntity of either
	 * EmployeeResponse type or ErrorResponse type
	 *
	 * @param employeeGetRequest,
	 * @param modelAttributeBindingResult
	 * @param requestInfoWrapper
	 * @param requestBodyBindingResult
	 * @return ResponseEntity<?>
	 */
	@PostMapping("{id}/_search")
	@ResponseBody
	public ResponseEntity<?> get(@ModelAttribute @Valid EmployeeGetRequest employeeGetRequest,
			BindingResult modelAttributeBindingResult, @RequestBody @Valid RequestInfoWrapper requestInfoWrapper,
			BindingResult requestBodyBindingResult) {

		RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();
		ResponseEntity<?> errorResponseEntity = requestValidator.validateSearchRequest(requestInfo,
				modelAttributeBindingResult, requestBodyBindingResult);

		if (errorResponseEntity != null)
			return errorResponseEntity;

		// Call service
		Employee employee = null;
		try {
			employee = employeeService.getEmployee(employeeGetRequest.getId(), employeeGetRequest.getTenantId(),
					requestInfo);
			System.out.println("employee=" + employee);
		} catch (EmployeeIdNotFoundException idNotFoundException) {
			LOGGER.error("Error while processing request " + employeeGetRequest.getId(), idNotFoundException);
			return errorHandler.getResponseEntityForInvalidEmployeeId(requestInfo);
		} catch (Exception exception) {
			LOGGER.error("Error while processing request " + employeeGetRequest.getId(), exception);
			return errorHandler.getResponseEntityForUnexpectedErrors(requestInfo);
		}
		return getSuccessResponseForGet(employee, requestInfo);
	}

	/**
	 * Maps Post Requests for _create & returns ResponseEntity of either
	 * EmployeeResponse type or ErrorResponse type
	 *
	 * @param employeeRequest
	 * @param bindingResult
	 * @return ResponseEntity<?>
	 */
	@PostMapping(value = "/_create")
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody @Valid EmployeeRequest employeeRequest, BindingResult bindingResult) {
		LOGGER.debug("employeeRequest::" + employeeRequest);

		ResponseEntity<?> errorResponseEntity = validateEmployeeRequest(employeeRequest, bindingResult, false);
		if (errorResponseEntity != null)
			return errorResponseEntity;

		Employee employee = null;
		try {
			employee = employeeService.createAsync(employeeRequest);
		} catch (UserException ue) {
			LOGGER.error("Error while processing request ", ue);
			return errorHandler.getResponseEntityForUserErrors(ue);
		} catch (Exception exception) {
			LOGGER.error("Error while processing request ", exception);
			return errorHandler.getResponseEntityForUnexpectedErrors(employeeRequest.getRequestInfo());
		}
		return getSuccessResponseForCreate(employee, employeeRequest.getRequestInfo());
	}

	/**
	 * Maps Post Requests for _update & returns ResponseEntity of either
	 * EmployeeResponse type or ErrorResponse type
	 *
	 * @param employeeRequest
	 * @param bindingResult
	 * @return ResponseEntity<?>
	 */
	@PostMapping(value = "/_update")
	@ResponseBody
	public ResponseEntity<?> update(@RequestBody @Valid EmployeeRequest employeeRequest, BindingResult bindingResult) {
		LOGGER.debug("employeeRequest::" + employeeRequest);

		ResponseEntity<?> errorResponseEntity = validateEmployeeRequest(employeeRequest, bindingResult, true);
		if (errorResponseEntity != null)
			return errorResponseEntity;

		Employee employee = null;
		try {
			employee = employeeService.updateAsync(employeeRequest);
		} catch (UserException ue) {
			LOGGER.error("Error while processing request ", ue);
			return errorHandler.getResponseEntityForUserErrors(ue);
		} catch (Exception exception) {
			LOGGER.error("Error while processing request ", exception);
			return errorHandler.getResponseEntityForUnexpectedErrors(employeeRequest.getRequestInfo());
		}
		return getSuccessResponseForUpdate(employee, employeeRequest.getRequestInfo());
	}

	/**
	 * Validate EmployeeRequest object & returns ErrorResponseEntity if there
	 * are any errors or else returns null
	 *
	 * @param employeeRequest
	 * @param bindingResult
	 * @param isUpdate
	 * @return ResponseEntity<?>
	 */
	private ResponseEntity<?> validateEmployeeRequest(EmployeeRequest employeeRequest, BindingResult bindingResult,
			boolean isUpdate) {
		// validate input params that can be handled by annotations
		if (bindingResult.hasErrors()) {
			return errorHandler.getErrorResponseEntityForInvalidRequest(bindingResult, employeeRequest.getRequestInfo());
		}

		// validate input params that can't be handled by annotations
		ValidationUtils.invokeValidator(employeeAssignmentValidator, employeeRequest.getEmployee(), bindingResult);
		if (isUpdate)
			ValidationUtils.invokeValidator(dataIntegrityValidatorForUpdate, employeeRequest.getEmployee(),
					bindingResult);
		else
			ValidationUtils.invokeValidator(dataIntegrityValidatorForCreate, employeeRequest.getEmployee(),
					bindingResult);

		if (bindingResult.hasErrors()) {
			return errorHandler.getErrorResponseEntityForInvalidRequest(bindingResult, employeeRequest.getRequestInfo());
		}
		return null;
	}

	/**
	 * Populate EmployeeInfoResponse object & returns ResponseEntity of type
	 * EmployeeInfoResponse containing ResponseInfo & List of EmployeeInfo
	 *
	 * @param employeesList
	 * @param requestInfo
	 * @return ResponseEntity<?>
	 */
	private ResponseEntity<?> getSuccessResponseForSearch(List<EmployeeInfo> employeesList, RequestInfo requestInfo) {
		EmployeeInfoResponse employeeInfoResponse = new EmployeeInfoResponse();
		employeeInfoResponse.setEmployees(employeesList);
		System.out.println("employeesList=" + employeesList);
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		employeeInfoResponse.setResponseInfo(responseInfo);
		return new ResponseEntity<EmployeeInfoResponse>(employeeInfoResponse, HttpStatus.OK);
	}

	/**
	 * Populate EmployeeResponse object & returns ResponseEntity of type
	 * EmployeeResponse containing ResponseInfo & Employee objects
	 *
	 * @param employee
	 * @param requestInfo
	 * @return ResponseEntity<?>
	 */
	public ResponseEntity<?> getSuccessResponseForCreate(Employee employee, RequestInfo requestInfo) {
		EmployeeResponse employeeResponse = new EmployeeResponse();
		employeeResponse.setEmployee(employee);

		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		employeeResponse.setResponseInfo(responseInfo);
		return new ResponseEntity<EmployeeResponse>(employeeResponse, HttpStatus.OK);
	}

	public ResponseEntity<?> getSuccessResponseForGet(Employee employee, RequestInfo requestInfo) {
		return getSuccessResponseForCreate(employee, requestInfo);
	}
	
	public ResponseEntity<?> getSuccessResponseForUpdate(Employee employee, RequestInfo requestInfo) {
		return getSuccessResponseForCreate(employee, requestInfo);
	}
}