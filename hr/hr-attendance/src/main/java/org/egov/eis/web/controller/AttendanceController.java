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

import java.util.List;

import javax.validation.Valid;

import org.egov.eis.model.Attendance;
import org.egov.eis.service.AttendanceService;
import org.egov.eis.web.contract.AttendanceGetRequest;
import org.egov.eis.web.contract.AttendanceRequest;
import org.egov.eis.web.contract.AttendanceResponse;
import org.egov.eis.web.contract.RequestInfo;
import org.egov.eis.web.contract.ResponseInfo;
import org.egov.eis.web.contract.factory.ResponseInfoFactory;
import org.egov.eis.web.errorhandlers.Error;
import org.egov.eis.web.errorhandlers.ErrorHandler;
import org.egov.eis.web.errorhandlers.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/attendances")
public class AttendanceController {

    private static final Logger logger = LoggerFactory.getLogger(AttendanceController.class);

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private ErrorHandler errHandler;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    @PostMapping("_search")
    @ResponseBody
    public ResponseEntity<?> search(@ModelAttribute @Valid final AttendanceGetRequest attendanceGetRequest,
            final BindingResult bindingResult, @RequestBody final RequestInfo requestInfo) {

        // validate header
        if (requestInfo.getApiId() == null || requestInfo.getVer() == null || requestInfo.getTs() == null)
            return errHandler.getErrorResponseEntityForMissingRequestInfo(requestInfo);

        // validate input params
        if (bindingResult.hasErrors())
            return errHandler.getErrorResponseEntityForMissingParameters(bindingResult, requestInfo);

        // Call service
        List<Attendance> attendancesList = null;
        try {
            attendancesList = attendanceService.getAttendances(attendanceGetRequest);
        } catch (final Exception exception) {
            logger.error("Error while processing request " + attendanceGetRequest, exception);
            return errHandler.getResponseEntityForUnexpectedErrors(requestInfo);
        }

        return getSuccessResponse(attendancesList, requestInfo);
    }

    @PostMapping({ "_create", "_update" })
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody @Valid final AttendanceRequest attendanceRequest,
            final BindingResult errors) {
        // validate header
        if (errors.hasErrors()) {
            final ErrorResponse errRes = populateErrors(errors);
            return new ResponseEntity<ErrorResponse>(errRes, HttpStatus.BAD_REQUEST);
        }
        logger.info("attendanceRequest::" + attendanceRequest);

        final List<Attendance> attendances = attendanceService.createAsync(attendanceRequest);

        return getSuccessResponse(attendances, attendanceRequest.getRequestInfo());
    }

    /**
     * Populate Response object and return attendancesList
     *
     * @param attendancesList
     * @return
     */
    private ResponseEntity<?> getSuccessResponse(final List<Attendance> attendancesList, final RequestInfo requestInfo) {
        final AttendanceResponse attendanceRes = new AttendanceResponse();
        attendanceRes.setAttendances(attendancesList);
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        attendanceRes.setResponseInfo(responseInfo);
        return new ResponseEntity<>(attendanceRes, HttpStatus.OK);

    }

    private ErrorResponse populateErrors(final BindingResult errors) {
        final ErrorResponse errRes = new ErrorResponse();

        final Error error = new Error();
        error.setCode(1);
        error.setDescription("Error while binding request");
        if (errors.hasFieldErrors())
            for (final FieldError errs : errors.getFieldErrors()) {
                error.getFields().add(errs.getField());
                error.getFields().add(errs.getRejectedValue());
            }
        errRes.setError(error);
        return errRes;
    }

}
