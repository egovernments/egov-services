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

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.eis.model.*;
import org.egov.eis.model.bulk.Department;
import org.egov.eis.service.EmployeeService;
import org.egov.eis.service.PayscaleService;
import org.egov.eis.service.exception.EmployeeIdNotFoundException;
import org.egov.eis.service.exception.IdGenerationException;
import org.egov.eis.service.exception.UserException;
import org.egov.eis.web.contract.*;
import org.egov.eis.web.contract.factory.ResponseEntityFactory;
import org.egov.eis.web.contract.factory.ResponseInfoFactory;
import org.egov.eis.web.errorhandler.ErrorHandler;
import org.egov.eis.web.errorhandler.ErrorResponse;
import org.egov.eis.web.errorhandler.InvalidDataException;
import org.egov.eis.web.errorhandler.Error;
import org.egov.eis.web.validator.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@RestController
@RequestMapping("/payscale")
public class PayscaleController {

    @Autowired
    private PayscaleService payscaleService;

    @Autowired
    private PayscaleValidator payscaleValidator;

    @Autowired
    private ErrorHandler errorHandler;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    @Autowired
    private ResponseEntityFactory responseEntityFactory;


    @PostMapping(value = "/_create")
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody @Valid PayscaleRequest payscaleRequest, BindingResult errors) {
        log.info("Payscale Request::" + payscaleRequest);

        PayscaleHeader payscaleHeader = null;
        try {

            if (errors.hasFieldErrors()) {
                ErrorResponse errRes = populateErrors(errors);
                return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
            }

            payscaleValidator.validatePayscaleRequest(payscaleRequest.getPayscaleHeader(), errors);

            if (errors.hasFieldErrors()) {
                ErrorResponse errRes = populateErrors(errors);
                return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
            }
            payscaleHeader = payscaleService.create(payscaleRequest);
        } catch (Exception exception) {
            log.error("Error while processing request ", exception);
            return errorHandler.getResponseEntityForUnexpectedErrors(payscaleRequest.getRequestInfo());
        }
        return getSuccessResponseForCreate(payscaleHeader, payscaleRequest.getRequestInfo());
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

    public ResponseEntity<?> getSuccessResponseForCreate(PayscaleHeader payscaleHeader, RequestInfo requestInfo) {
        PayscaleResponse payscaleResponse = new PayscaleResponse();
        payscaleResponse.setPayscaleHeader(payscaleHeader);

        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        payscaleResponse.setResponseInfo(responseInfo);
        return new ResponseEntity<PayscaleResponse>(payscaleResponse, HttpStatus.OK);
    }

}