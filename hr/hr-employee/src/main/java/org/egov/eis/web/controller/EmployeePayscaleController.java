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


import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.eis.model.EmployeePayscale;
import org.egov.eis.service.EmployeePayscaleService;
import org.egov.eis.web.contract.*;
import org.egov.eis.web.contract.factory.ResponseInfoFactory;
import org.egov.eis.web.errorhandler.Error;
import org.egov.eis.web.errorhandler.ErrorHandler;
import org.egov.eis.web.errorhandler.ErrorResponse;
import org.egov.eis.web.validator.PayscaleValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/employeepayscale")
public class EmployeePayscaleController {

    @Autowired
    private PayscaleValidator payscaleValidator;

    @Autowired
    private EmployeePayscaleService employeePayscaleService;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    @Autowired
    private ErrorHandler errorHandler;

    @PostMapping("_search")
    @ResponseBody
    public ResponseEntity<?> search(@ModelAttribute @Valid final EmployeePayscaleGetRequest employeePayscaleGetRequest,
                                    final BindingResult modelAttributeBindingResult, @RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
                                    final BindingResult bindingResult) {

        RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

        if (bindingResult.hasErrors()) {
            ErrorResponse errorResponse = populateErrors(bindingResult);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        // Call service
        List<EmployeePayscale> employeePayscaleList = null;
        try {
            employeePayscaleList = employeePayscaleService.getEmpPayscale(employeePayscaleGetRequest, requestInfo);
        } catch (final Exception exception) {
            log.error("Error while processing request " + employeePayscaleGetRequest, exception);
            return errorHandler.getResponseEntityForUnexpectedErrors(requestInfo);
        }

        return getSuccessResponse(employeePayscaleList, requestInfo);
    }

    @PostMapping(value = "/_create")
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody @Valid EmployeePayscaleRequest employeePayscaleRequest, BindingResult errors) {
        log.info("Create Employee Payscale Request::" + employeePayscaleRequest);

        List<EmployeePayscale> employeePayscale = new ArrayList<>();
        try {

            if (errors.hasFieldErrors()) {
                ErrorResponse errRes = populateErrors(errors);
                return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
            }

            payscaleValidator.validateEmpPayscaleReuest(employeePayscaleRequest.getEmployeePayscale(), employeePayscaleRequest.getRequestInfo(), errors);

            if (errors.hasFieldErrors()) {
                ErrorResponse errRes = populateErrors(errors);
                return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
            }
            employeePayscale = employeePayscaleService.create(employeePayscaleRequest);
        } catch (Exception exception) {
            log.error("Error while processing request ", exception);
            return errorHandler.getResponseEntityForUnexpectedErrors(employeePayscaleRequest.getRequestInfo());
        }
        return getSuccessResponseForCreate(employeePayscale, employeePayscaleRequest.getRequestInfo());
    }

    public ResponseEntity<?> getSuccessResponseForCreate(List<EmployeePayscale> employeePayscale, RequestInfo requestInfo) {
        EmployeePayscaleResponse empPayscaleResponse = new EmployeePayscaleResponse();
        empPayscaleResponse.setEmployeePayscale(employeePayscale);

        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        empPayscaleResponse.setResponseInfo(responseInfo);
        return new ResponseEntity<EmployeePayscaleResponse>(empPayscaleResponse, HttpStatus.OK);
    }

    private ErrorResponse populateErrors(BindingResult errors) {
        ErrorResponse errRes = new ErrorResponse();
        Error error = new Error();
        error.setCode(1);
        error.setDescription("Error while binding request");
        if (errors.hasFieldErrors()) {
            for (FieldError errs : errors.getFieldErrors()) {
                error.getFields().put(errs.getField(), errs.getDefaultMessage());
            }
        }
        errRes.setError(error);
        return errRes;
    }


    private ResponseEntity<?> getSuccessResponse(final List<EmployeePayscale> employeePayscaleList,
                                                 final RequestInfo requestInfo) {
        final EmployeePayscaleResponse employeePayscaleResponse = new EmployeePayscaleResponse();
        employeePayscaleResponse.setEmployeePayscale(employeePayscaleList);
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        employeePayscaleResponse.setResponseInfo(responseInfo);
        return new ResponseEntity<>(employeePayscaleResponse, HttpStatus.OK);

    }
}
