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

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.eis.config.PropertiesManager;
import org.egov.eis.model.Attendance;
import org.egov.eis.model.AttendanceType;
import org.egov.eis.model.EmployeeInfo;
import org.egov.eis.model.Holiday;
import org.egov.eis.model.enums.EmployeeStatus;
import org.egov.eis.service.AttendanceService;
import org.egov.eis.service.EmployeeService;
import org.egov.eis.service.HRConfigurationService;
import org.egov.eis.service.HolidayService;
import org.egov.eis.util.ApplicationConstants;
import org.egov.eis.web.contract.*;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

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

    @Autowired
    private HolidayService holidayService;

    @Autowired
    private ApplicationConstants applicationConstants;

    @Autowired
    private HRConfigurationService hrConfigurationService;

    @Autowired
    private PropertiesManager propertiesManager;

    @Autowired
    private EmployeeService employeeService;

    @PostMapping(value = "_search")
    @ResponseBody
    public ResponseEntity<?> search(@ModelAttribute @Valid final AttendanceGetRequest attendanceGetRequest,
                                    final BindingResult bindingResult, @RequestBody final RequestInfoWrapper requestInfoWrapper) {
        final RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

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

    @PostMapping(value = {"_create", "_update"})
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody @Valid final AttendanceRequest attendanceRequest,
                                    @RequestParam("tenantId") final String tenantId,
                                    final BindingResult errors) {
        // validate header
        if (errors.hasErrors()) {
            final ErrorResponse errRes = populateErrors(errors);
            return new ResponseEntity<ErrorResponse>(errRes, HttpStatus.BAD_REQUEST);
        }
        logger.info("attendanceRequest::" + attendanceRequest);

        final List<ErrorResponse> errorResponses = validateAttendanceRequest(attendanceRequest, tenantId);
        if (!errorResponses.isEmpty())
            return new ResponseEntity<List<ErrorResponse>>(errorResponses, HttpStatus.BAD_REQUEST);

        final List<Attendance> attendances = attendanceService.createAsync(attendanceRequest);

        return getSuccessResponse(attendances, attendanceRequest.getRequestInfo());
    }

    @PostMapping(value = "_getattendance")
    @ResponseBody
    public ResponseEntity<?> getAttendance(@ModelAttribute @Valid final AttendanceGetRequest attendanceGetRequest,
                                           final BindingResult bindingResult, @RequestBody final RequestInfoWrapper requestInfoWrapper) {
        final RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

        // validate header
        if (requestInfo.getApiId() == null || requestInfo.getVer() == null || requestInfo.getTs() == null)
            return errHandler.getErrorResponseEntityForMissingRequestInfo(requestInfo);

        // validate input params
        if (bindingResult.hasErrors())
            return errHandler.getErrorResponseEntityForMissingParameters(bindingResult, requestInfo);

        // Call service
        List<Attendance> attendancesList = null;
        try {
            attendancesList = attendanceService.findAttendanceForHolidays(attendanceGetRequest, requestInfo);
        } catch (final Exception exception) {
            logger.error("Error while processing request " + attendanceGetRequest, exception);
            return errHandler.getResponseEntityForUnexpectedErrors(requestInfo);
        }

        return getSuccessResponse(attendancesList, requestInfo);
    }

    @PostMapping(value = "_attendancereport")
    @ResponseBody
    public ResponseEntity<?> getAttendanceReport(@ModelAttribute @Valid final AttendanceReportRequest attendanceReportRequest,
                                                 final BindingResult bindingResult, @RequestBody final RequestInfoWrapper requestInfoWrapper) {
        final RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

        // validate header
        if (requestInfo.getApiId() == null || requestInfo.getVer() == null || requestInfo.getTs() == null)
            return errHandler.getErrorResponseEntityForMissingRequestInfo(requestInfo);

        // validate input params
        if (bindingResult.hasErrors())
            return errHandler.getErrorResponseEntityForMissingParameters(bindingResult, requestInfo);

        // Call service
        List<Attendance> attendancesList = null;
        Long noOfDaysInMonth = null;
        Long noOfWorkingDays = null;
        try {
            noOfDaysInMonth = attendanceService.getNoOfDaysInMonth(attendanceReportRequest.getMonth(), attendanceReportRequest.getYear());
            noOfWorkingDays = attendanceService.getNoOfWorkingDays(attendanceReportRequest, noOfDaysInMonth, requestInfo);
            attendancesList = attendanceService.getAttendanceReport(attendanceReportRequest, noOfDaysInMonth, noOfWorkingDays, requestInfo);
        } catch (final Exception exception) {
            logger.error("Error while processing request " + attendanceReportRequest, exception);
            return errHandler.getResponseEntityForUnexpectedErrors(requestInfo);
        }

        return getSuccessResponseForReport(attendancesList, noOfDaysInMonth, noOfWorkingDays, requestInfo);
    }

    @SuppressWarnings("deprecation")
    private List<ErrorResponse> validateAttendanceRequest(final AttendanceRequest attendanceRequest, final String tenantId) {
        final List<ErrorResponse> errorResponses = new ArrayList<>();
        final List<Holiday> holidays = holidayService.getHolidays(tenantId, attendanceRequest.getRequestInfo());
        final Map<String, List<String>> weeklyHolidays = hrConfigurationService.getWeeklyHolidays(tenantId,
                attendanceRequest.getRequestInfo());
        final List<AttendanceType> attendanceTypes = attendanceService.getAllAttendanceTypes(tenantId);
        final List<Long> employees = new ArrayList<>();
        boolean isHoliday = true;
        boolean isEmployeeDate = true;
        boolean isTypeExists = false;
        boolean isAttendanceExists = false;
        boolean isEmployeePresent = true;
        boolean isEmployeeRetired = false;
        boolean isFutureDate = false;

        for (final Attendance attendance : attendanceRequest.getAttendances()) {
            isTypeExists = false;
            if (attendance.getEmployee() == null || attendance.getAttendanceDate() == null)
                isEmployeeDate = false;
            if (attendance.getAttendanceDate() != null && attendance.getAttendanceDate().after(new Date()))
                isFutureDate = true;
            else if (attendance.getId() == null
                    && attendanceService.getByEmployeeAndDate(attendance.getEmployee(), attendance.getAttendanceDate(), tenantId))
                isAttendanceExists = true;
            if (!employees.contains(attendance.getEmployee()))
                employees.add(attendance.getEmployee());

            if ("".equals(attendance.getType().getCode()))
                isTypeExists = true;
            else
                for (final AttendanceType type : attendanceTypes)
                    if (type.getCode().equalsIgnoreCase(attendance.getType().getCode())) {
                        isTypeExists = true;
                        break;
                    }
            if (!holidays.isEmpty() && attendance.getType() != null && "H".equals(attendance.getType().getCode()))
                for (final Holiday holiday : holidays)
                    if (holiday.getApplicableOn().compareTo(attendance.getAttendanceDate()) == 0) {
                        isHoliday = true;
                        break;
                    } else
                        isHoliday = false;
            if (!isHoliday && attendance.getAttendanceDate() != null)
                if (propertiesManager.getHrMastersServiceConfigurationsFiveDayWeek()
                        .equals(weeklyHolidays.get(propertiesManager.getHrMastersServiceConfigurationsKey()).get(0))) {
                    if (attendance.getAttendanceDate().getDay() == 0 || attendance.getAttendanceDate().getDay() == 6)
                        isHoliday = true;
                } else if (propertiesManager.getHrMastersServiceConfigurationsSixDayWeek()
                        .equals(weeklyHolidays.get(propertiesManager.getHrMastersServiceConfigurationsKey()).get(0))) {
                    if (attendance.getAttendanceDate().getDay() == 0)
                        isHoliday = true;
                } else if (propertiesManager.getHrMastersServiceConfigurationsFiveDayWithSecondSaturday()
                        .equals(weeklyHolidays.get(propertiesManager.getHrMastersServiceConfigurationsKey()).get(0))) {
                    if (attendance.getAttendanceDate().getDay() == 0 || isSecondSaturdayOfMonth(attendance.getAttendanceDate()))
                        isHoliday = true;
                } else if (propertiesManager.getHrMastersServiceConfigurationsFiveDayWithSecondAndFourthSaturday()
                        .equals(weeklyHolidays.get(propertiesManager.getHrMastersServiceConfigurationsKey()).get(0)))
                    if (attendance.getAttendanceDate().getDay() == 0
                            || isSecondOrFourthSaturdayOfMonth(attendance.getAttendanceDate()))
                        isHoliday = true;
            if (!isTypeExists || !isEmployeeDate || !isHoliday || isAttendanceExists)
                break;
        }
        for (final Long employee : employees) {
            final List<EmployeeInfo> employeeInfos = employeeService.getEmployee(employee, tenantId,
                    attendanceRequest.getRequestInfo());
            if (employeeInfos.isEmpty())
                isEmployeePresent = false;
            else if (EmployeeStatus.RETIRED.toString().equals(employeeInfos.get(0).getEmployeeStatus()))
                isEmployeeRetired = true;
            if (!isEmployeePresent || isEmployeeRetired)
                break;
        }
        if (isFutureDate) {
            final ErrorResponse errorResponse = new ErrorResponse();
            final Error error = new Error();
            error.setDescription(applicationConstants.getErrorMessage(ApplicationConstants.MSG_ATTENDANCE_FUTURE_DATE));
            errorResponse.setError(error);
            errorResponses.add(errorResponse);
        }
        if (isAttendanceExists) {
            final ErrorResponse errorResponse = new ErrorResponse();
            final Error error = new Error();
            error.setDescription(applicationConstants.getErrorMessage(ApplicationConstants.MSG_ATTENDANCE_PRESENT));
            errorResponse.setError(error);
            errorResponses.add(errorResponse);
        }
        if (!isEmployeeDate) {
            final ErrorResponse errorResponse = new ErrorResponse();
            final Error error = new Error();
            error.setDescription(
                    applicationConstants.getErrorMessage(ApplicationConstants.MSG_ATTENDANCE_EMPLOYEE_DATE_MANDATORY));
            errorResponse.setError(error);
            errorResponses.add(errorResponse);
        }
        if (!isTypeExists) {
            final ErrorResponse errorResponse = new ErrorResponse();
            final Error error = new Error();
            error.setDescription(applicationConstants.getErrorMessage(ApplicationConstants.MSG_ATTENDANCE_INVALID_TYPE));
            errorResponse.setError(error);
            errorResponses.add(errorResponse);
        }
        if (!isHoliday) {
            final ErrorResponse errorResponse = new ErrorResponse();
            final Error error = new Error();
            error.setDescription(applicationConstants.getErrorMessage(ApplicationConstants.MSG_ATTENDANCE_MARKED_HOLIDAY));
            errorResponse.setError(error);
            errorResponses.add(errorResponse);
        }
        if (!isEmployeePresent) {
            final ErrorResponse errorResponse = new ErrorResponse();
            final Error error = new Error();
            error.setDescription(applicationConstants.getErrorMessage(ApplicationConstants.MSG_ATTENDANCE_INVALID_EMPLOYEE));
            errorResponse.setError(error);
            errorResponses.add(errorResponse);
        }
        if (isEmployeeRetired) {
            final ErrorResponse errorResponse = new ErrorResponse();
            final Error error = new Error();
            error.setDescription(applicationConstants.getErrorMessage(ApplicationConstants.MSG_ATTENDANCE_EMPLOYEE_RETIRED));
            errorResponse.setError(error);
            errorResponses.add(errorResponse);
        }

        return errorResponses;
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


    /**
     * Populate Response object and return attendancesList
     *
     * @param attendancesList
     * @return
     */
    private ResponseEntity<?> getSuccessResponseForReport(final List<Attendance> attendancesList, Long noOfDaysInMonth, Long noOfWorkingDays, final RequestInfo requestInfo) {
        final AttendanceReportResponse attendanceReportResponse = new AttendanceReportResponse();
        attendanceReportResponse.setAttendances(attendancesList);
        attendanceReportResponse.setNoOfDaysInMonth(noOfDaysInMonth);
        attendanceReportResponse.setNoOfWorkingDays(noOfWorkingDays);
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        attendanceReportResponse.setResponseInfo(responseInfo);
        return new ResponseEntity<>(attendanceReportResponse, HttpStatus.OK);

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

    @SuppressWarnings("deprecation")
    public boolean isSecondSaturdayOfMonth(final Date date) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        cal.set(Calendar.WEEK_OF_MONTH, 2);
        return cal.getTime().equals(date);
    }

    @SuppressWarnings("deprecation")
    public boolean isSecondOrFourthSaturdayOfMonth(final Date date) {
        boolean isSecondOrFourthSaturday = false;
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        cal.set(Calendar.WEEK_OF_MONTH, 2);
        isSecondOrFourthSaturday = cal.getTime().equals(date);

        if (!isSecondOrFourthSaturday) {
            cal.set(Calendar.WEEK_OF_MONTH, 4);
            isSecondOrFourthSaturday = cal.getTime().equals(date);
        }

        return isSecondOrFourthSaturday;
    }

}
