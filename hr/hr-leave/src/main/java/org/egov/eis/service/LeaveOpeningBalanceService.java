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

package org.egov.eis.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.eis.model.LeaveApplication;
import org.egov.eis.model.LeaveOpeningBalance;
import org.egov.eis.model.LeaveType;
import org.egov.eis.repository.CommonMastersRepository;
import org.egov.eis.repository.EmployeeRepository;
import org.egov.eis.repository.LeaveAllotmentRepository;
import org.egov.eis.repository.LeaveApplicationRepository;
import org.egov.eis.repository.LeaveOpeningBalanceRepository;
import org.egov.eis.util.ApplicationConstants;
import org.egov.eis.web.contract.Assignment;
import org.egov.eis.web.contract.CalendarYear;
import org.egov.eis.web.contract.CalendarYearResponse;
import org.egov.eis.web.contract.EmployeeInfo;
import org.egov.eis.web.contract.EmployeeInfoResponse;
import org.egov.eis.web.contract.LeaveApplicationGetRequest;
import org.egov.eis.web.contract.LeaveOpeningBalanceGetRequest;
import org.egov.eis.web.contract.LeaveOpeningBalanceRequest;
import org.egov.eis.web.contract.LeaveOpeningBalanceResponse;
import org.egov.eis.web.contract.LeaveOpeningBalanceUploadResponse;
import org.egov.eis.web.contract.LeaveSearchRequest;
import org.egov.eis.web.contract.LeaveTypeGetRequest;
import org.egov.eis.web.contract.factory.ResponseInfoFactory;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class LeaveOpeningBalanceService {

	public static final Logger LOGGER = LoggerFactory.getLogger(LeaveOpeningBalanceService.class);

	@Autowired
	private LeaveOpeningBalanceRepository leaveOpeningBalanceRepository;

	@Autowired
	private CommonMastersRepository commonMastersRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private LeaveAllotmentRepository leaveAllotmentRepository;

	@Autowired
	private LeaveTypeService leaveTypeService;

	@Autowired
	private HRStatusService hrStatusService;

	@Autowired
	private LeaveApplicationService leaveApplicationService;

	@Autowired
	private LeaveOpeningBalanceService leaveOpeningBalanceService;

	@Autowired
	private ApplicationConstants applicationConstants;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	public List<LeaveOpeningBalance> getLeaveOpeningBalances(
			LeaveOpeningBalanceGetRequest leaveOpeningBalanceGetRequest) {
		return leaveOpeningBalanceRepository.findForCriteria(leaveOpeningBalanceGetRequest);
	}

	public List<LeaveOpeningBalance> getLeaveOpeningBalance(LeaveOpeningBalanceGetRequest leaveOpeningBalanceGetRequest,
															RequestInfo requestInfo) {
		LeaveSearchRequest leaveSearchRequest = new LeaveSearchRequest();
		leaveSearchRequest.setIsPrimary(true);
		if (leaveOpeningBalanceGetRequest.getCode()!=null && !leaveOpeningBalanceGetRequest.getCode().equals(""))
			leaveSearchRequest.setCode(leaveOpeningBalanceGetRequest.getCode());
		else
			leaveSearchRequest.setDepartmentId(leaveOpeningBalanceGetRequest.getDepartmentId());

		leaveSearchRequest.setAsOnDate(new Date());
		leaveSearchRequest.setTenantId(leaveOpeningBalanceGetRequest.getTenantId());
		List<Long> employeeIds ;

		EmployeeInfoResponse employeeResponse = employeeRepository.getEmployeesForLeaveRequest(leaveSearchRequest,
				requestInfo);
		employeeIds = employeeResponse.getEmployees().stream().map(employeeInfo -> employeeInfo.getId())
				.collect(Collectors.toList());
		if (employeeIds.isEmpty())
			return Collections.EMPTY_LIST;

		leaveOpeningBalanceGetRequest.setEmployee(employeeIds);
		return leaveOpeningBalanceRepository.findForCriteria(leaveOpeningBalanceGetRequest);
	}

	public ResponseEntity<?> createLeaveOpeningBalance(LeaveOpeningBalanceRequest leaveOpeningBalanceRequest,
													   String type) {
		List<LeaveOpeningBalance> leaveOpeningBalanceList = validate(leaveOpeningBalanceRequest,type);

		List<LeaveOpeningBalance> successLeaveOpeningBalanceList = new ArrayList<LeaveOpeningBalance>();
		List<LeaveOpeningBalance> errorLeaveOpeningBalanceList = new ArrayList<LeaveOpeningBalance>();
		for (LeaveOpeningBalance leaveOpeningBalance : leaveOpeningBalanceList) {
			if (leaveOpeningBalance.getErrorMsg().isEmpty())
				successLeaveOpeningBalanceList.add(leaveOpeningBalance);
			else
				errorLeaveOpeningBalanceList.add(leaveOpeningBalance);
		}
		leaveOpeningBalanceRequest.setLeaveOpeningBalance(successLeaveOpeningBalanceList);
		leaveOpeningBalanceRepository.create(leaveOpeningBalanceRequest);
		if (type != null && "upload".equalsIgnoreCase(type))
			return getSuccessResponseForUpload(successLeaveOpeningBalanceList, errorLeaveOpeningBalanceList,
					leaveOpeningBalanceRequest.getRequestInfo());
		else
			return getSuccessResponseForCreate(leaveOpeningBalanceList, leaveOpeningBalanceRequest.getRequestInfo());
	}

	public ResponseEntity<?> carryForwardLeaveOpeningBalance(
			LeaveOpeningBalanceGetRequest leaveOpeningBalanceGetRequest, RequestInfo requestInfo) {

		List<LeaveOpeningBalance> successLeaveOpeningBalanceList = new ArrayList<LeaveOpeningBalance>();

		LeaveSearchRequest leaveSearchRequest = new LeaveSearchRequest();
		leaveSearchRequest.setIsPrimary(true);
		leaveSearchRequest.setActive(true);
		leaveSearchRequest.setTenantId(leaveOpeningBalanceGetRequest.getTenantId());
		List<Long> employeeIds ;

		EmployeeInfoResponse employeeResponse = employeeRepository.getEmployeesForLeaveRequest(leaveSearchRequest,
				requestInfo);
		leaveOpeningBalanceGetRequest.setStatusId(hrStatusService
				.getHRStatuses("APPROVED", leaveOpeningBalanceGetRequest.getTenantId(), requestInfo).get(0).getId());
		employeeResponse.getEmployees().stream().forEach(employee -> {
			carryForwardLOB(leaveOpeningBalanceGetRequest, employee, successLeaveOpeningBalanceList, requestInfo);
		});
		employeeIds = employeeResponse.getEmployees().stream().map(employeeInfo -> employeeInfo.getId())
				.collect(Collectors.toList());
		leaveOpeningBalanceGetRequest.setEmployee(employeeIds);
		leaveOpeningBalanceGetRequest.setYear(leaveOpeningBalanceGetRequest.getYear() + 1);
		LeaveOpeningBalanceRequest createLeaveOpeningBalanceRequest = new LeaveOpeningBalanceRequest();
		LeaveOpeningBalanceRequest updateLeaveOpeningBalanceRequest = new LeaveOpeningBalanceRequest();
		List<LeaveOpeningBalance> removeLOB = new ArrayList<LeaveOpeningBalance>();
		createLeaveOpeningBalanceRequest.setLeaveOpeningBalance(successLeaveOpeningBalanceList);
		createLeaveOpeningBalanceRequest.setRequestInfo(requestInfo);
		updateLeaveOpeningBalanceRequest.setRequestInfo(requestInfo);
		List<LeaveOpeningBalance> lobFromDb = getLeaveOpeningBalances(leaveOpeningBalanceGetRequest);
		Map<String, LeaveOpeningBalance> lobMap = new HashMap<String, LeaveOpeningBalance>();
		for (LeaveOpeningBalance leaveOpeningBalance : lobFromDb) {
			lobMap.put(leaveOpeningBalance.getEmployee() + "-" + leaveOpeningBalance.getLeaveType().getId() + "-"
					+ leaveOpeningBalance.getCalendarYear(), leaveOpeningBalance);
		}
		for (LeaveOpeningBalance leaveOpeningBalance : successLeaveOpeningBalanceList) {
			if (lobMap.get(leaveOpeningBalance.getEmployee() + "-" + leaveOpeningBalance.getLeaveType().getId() + "-"
					+ leaveOpeningBalance.getCalendarYear()) != null) {
				removeLOB.add(leaveOpeningBalance);
				updateLeaveOpeningBalanceRequest.getLeaveOpeningBalance().add(leaveOpeningBalance);
			}
		}
		createLeaveOpeningBalanceRequest.getLeaveOpeningBalance().removeAll(removeLOB);

		leaveOpeningBalanceRepository.create(createLeaveOpeningBalanceRequest);
		if (!updateLeaveOpeningBalanceRequest.getLeaveOpeningBalance().isEmpty())
			leaveOpeningBalanceRepository.updateLOBCarryForward(updateLeaveOpeningBalanceRequest);
		return getSuccessResponseForCreate(successLeaveOpeningBalanceList, requestInfo);
	}

	public void carryForwardLOB(LeaveOpeningBalanceGetRequest leaveOpeningBalanceGetRequest, EmployeeInfo employee,
								List<LeaveOpeningBalance> successLeaveOpeningBalanceList, RequestInfo requestInfo) {
		Long designationid = null;
		Float allotmentValue = 0f, applicationValue = 0f, openingBalanceValue = 0f;
		LeaveOpeningBalance leaveOpeningBalance = new LeaveOpeningBalance();

		if (!employee.getAssignments().isEmpty()) {
			List<Assignment> assignments = employee.getAssignments().stream()
					.filter(assign -> (assign.getIsPrimary().equals(true) && assign.getToDate().after(new Date())))
					.collect(Collectors.toList());
			if (!assignments.isEmpty())
				designationid = assignments.stream().map(assign -> assign.getDesignation()).collect(Collectors.toList())
						.get(0);
		}
		List<Map<String, Object>> leaveAllotmentsList = leaveAllotmentRepository.getLeaveAllotmentByDesignation(
				leaveOpeningBalanceGetRequest.getLeaveType().get(0), designationid,
				leaveOpeningBalanceGetRequest.getTenantId());

		if (leaveAllotmentsList.isEmpty()) {
			leaveAllotmentsList = leaveAllotmentRepository.getLeaveAllotmentByDesignation(
					leaveOpeningBalanceGetRequest.getLeaveType().get(0), null,
					leaveOpeningBalanceGetRequest.getTenantId());
		}

		if (leaveAllotmentsList != null && !leaveAllotmentsList.isEmpty()) {
			Object noofdays = leaveAllotmentsList.get(0).get("noofdays");
			allotmentValue = Float.valueOf(noofdays.toString());
		}

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.MONTH, 0);
		calendar.set(Calendar.YEAR, leaveOpeningBalanceGetRequest.getYear());
		Date fromDate = calendar.getTime();
		calendar.set(Calendar.DAY_OF_MONTH, 31);
		calendar.set(Calendar.MONTH, 11);
		Date toDate = calendar.getTime();

		final LeaveOpeningBalanceGetRequest leaveOpeningBalanceRequest = new LeaveOpeningBalanceGetRequest();

		leaveOpeningBalanceRequest.getEmployee().add(employee.getId());
		leaveOpeningBalanceRequest.getLeaveType().add(leaveOpeningBalanceGetRequest.getLeaveType().get(0));
		leaveOpeningBalanceRequest.setTenantId(leaveOpeningBalanceGetRequest.getTenantId());
		leaveOpeningBalanceRequest.setYear(leaveOpeningBalanceGetRequest.getYear());

		List<LeaveOpeningBalance> leaveOpeningBalancesList = null;
		leaveOpeningBalancesList = leaveOpeningBalanceService.getLeaveOpeningBalances(leaveOpeningBalanceRequest);

		if (leaveOpeningBalancesList != null && !leaveOpeningBalancesList.isEmpty())
			openingBalanceValue = leaveOpeningBalancesList.get(0).getNoOfDays();

		final LeaveApplicationGetRequest leaveApplicationGetRequest = new LeaveApplicationGetRequest();

		leaveApplicationGetRequest.getEmployee().add(employee.getId());
		leaveApplicationGetRequest.setLeaveType(leaveOpeningBalanceGetRequest.getLeaveType().get(0));
		leaveApplicationGetRequest.setStatusId(leaveOpeningBalanceGetRequest.getStatusId());
		leaveApplicationGetRequest.setStatus(null);
		leaveApplicationGetRequest.setTenantId(leaveOpeningBalanceGetRequest.getTenantId());
		leaveApplicationGetRequest.setFromDate(fromDate);
		leaveApplicationGetRequest.setToDate(toDate);

		List<LeaveApplication> leaveApplicationsList = null;
		leaveApplicationsList = leaveApplicationService.getLeaveApplications(leaveApplicationGetRequest, requestInfo);

		if (leaveApplicationsList != null && !leaveApplicationsList.isEmpty())
			applicationValue = leaveApplicationsList.get(0).getLeaveDays();

		LeaveType leaveType = new LeaveType();
		leaveType.setId(leaveOpeningBalanceGetRequest.getLeaveType().get(0));
		leaveType.setTenantId(leaveOpeningBalanceGetRequest.getTenantId());
		leaveOpeningBalance.setEmployee(employee.getId());
		leaveOpeningBalance.setCalendarYear(leaveOpeningBalanceGetRequest.getYear() + 1);
		leaveOpeningBalance.setLeaveType(leaveType);
		leaveOpeningBalance.setNoOfDays(openingBalanceValue + allotmentValue - applicationValue);
		leaveOpeningBalance.setTenantId(leaveOpeningBalanceGetRequest.getTenantId());
		successLeaveOpeningBalanceList.add(leaveOpeningBalance);
	}

	private List<LeaveOpeningBalance> validate(LeaveOpeningBalanceRequest leaveOpeningBalanceRequest, String type) {

		String tenantId = "", errorMsg = "";
		Integer year = null;

		if (leaveOpeningBalanceRequest.getLeaveOpeningBalance() != null
				&& !leaveOpeningBalanceRequest.getLeaveOpeningBalance().isEmpty()) {
			tenantId = leaveOpeningBalanceRequest.getLeaveOpeningBalance().get(0).getTenantId();
			year = leaveOpeningBalanceRequest.getLeaveOpeningBalance().get(0).getCalendarYear();
		}
		CalendarYearResponse calendarYearResponse = commonMastersRepository
				.getCalendaryears(leaveOpeningBalanceRequest.getRequestInfo(), tenantId);
		LeaveTypeGetRequest leaveTypeGetRequest = new LeaveTypeGetRequest();
		leaveTypeGetRequest.setTenantId(tenantId);
		leaveTypeGetRequest.setAccumulative(true);
		leaveTypeGetRequest.setPageSize((short) 500);
		List<LeaveType> leaveTypes = leaveTypeService.getLeaveTypes(leaveTypeGetRequest);
		Map<Integer, CalendarYear> calendarYearMap = new HashMap<Integer, CalendarYear>();
		Map<Long, LeaveType> leaveTypeMap = new HashMap<Long, LeaveType>();
		for (CalendarYear cy : calendarYearResponse.getCalendarYear()) {
			calendarYearMap.put(cy.getName(), cy);
		}

		for (LeaveType lt : leaveTypes) {
			leaveTypeMap.put(lt.getId(), lt);
		}
		for (LeaveOpeningBalance leaveOpeningBalance : leaveOpeningBalanceRequest.getLeaveOpeningBalance()) {
			errorMsg = "";
			if (calendarYearMap.get(leaveOpeningBalance.getCalendarYear()) == null) {
				errorMsg = "CalendarYear " + leaveOpeningBalance.getCalendarYear() + " does not exist in the system";
			}
			if (leaveTypeMap.get(leaveOpeningBalance.getLeaveType().getId()) == null) {
				errorMsg = errorMsg + " Leave Type with id " + leaveOpeningBalance.getLeaveType().getId()
						+ " does not exist in the system";
			} else {
				leaveOpeningBalance.getLeaveType()
						.setName(leaveTypeMap.get(leaveOpeningBalance.getLeaveType().getId()).getName());
			}
			if (type != null && "upload".equalsIgnoreCase(type)){
				boolean lobExists = leaveOpeningBalanceRepository.checkLOBExists(leaveOpeningBalance.getEmployee(), leaveOpeningBalance.getLeaveType().getId(), leaveOpeningBalance.getCalendarYear(), leaveOpeningBalance.getTenantId());
				if(lobExists)
					errorMsg = applicationConstants.getErrorMessage(ApplicationConstants.MSG_LOB_EXISTS) + " ";
			}
			leaveOpeningBalance.setErrorMsg(errorMsg);

		}
		LeaveOpeningBalanceGetRequest leaveOpeningBalanceGetRequest = new LeaveOpeningBalanceGetRequest();
		leaveOpeningBalanceGetRequest.setTenantId(tenantId);
		leaveOpeningBalanceGetRequest.setYear(year);
		List<LeaveOpeningBalance> lobFromDb = getLeaveOpeningBalances(leaveOpeningBalanceGetRequest);
		Map<String, LeaveOpeningBalance> lobMap = new HashMap<String, LeaveOpeningBalance>();
		for (LeaveOpeningBalance leaveOpeningBalance : lobFromDb) {
			lobMap.put(leaveOpeningBalance.getEmployee() + "-" + leaveOpeningBalance.getLeaveType().getId() + "-"
					+ leaveOpeningBalance.getCalendarYear(), leaveOpeningBalance);
		}
		for (LeaveOpeningBalance leaveOpeningBalance : leaveOpeningBalanceRequest.getLeaveOpeningBalance()) {
			errorMsg = "";
			if (lobMap.get(leaveOpeningBalance.getEmployee() + "-" + leaveOpeningBalance.getLeaveType().getId() + "-"
					+ leaveOpeningBalance.getCalendarYear()) == null) {
				errorMsg = applicationConstants.getErrorMessage(ApplicationConstants.MSG_LOB_EXISTS) + " ";
			}
		}

		return leaveOpeningBalanceRequest.getLeaveOpeningBalance();
	}

	public ResponseEntity<?> updateLeaveOpeningBalance(LeaveOpeningBalanceRequest leaveOpeningBalanceRequest) {
		List<LeaveOpeningBalance> leaveOpeningBalance = leaveOpeningBalanceRequest.getLeaveOpeningBalance();
		leaveOpeningBalanceRepository.update(leaveOpeningBalanceRequest);
		return getSuccessResponseForCreate(leaveOpeningBalance, leaveOpeningBalanceRequest.getRequestInfo());
	}

	/**
	 * Populate LeaveOpeningBalanceResponse object & returns ResponseEntity of
	 * type LeaveOpeningBalanceResponse containing ResponseInfo & array of
	 * LeaveOpeningBalance objects
	 *
	 * @param leaveOpeningBalance
	 * @param requestInfo
	 * @return ResponseEntity<?>
	 */
	public ResponseEntity<?> getSuccessResponseForCreate(List<LeaveOpeningBalance> leaveOpeningBalance,
														 RequestInfo requestInfo) {
		LeaveOpeningBalanceResponse leaveOpeningBalanceResponse = new LeaveOpeningBalanceResponse();
		leaveOpeningBalanceResponse.getLeaveOpeningBalance().addAll(leaveOpeningBalance);

		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		leaveOpeningBalanceResponse.setResponseInfo(responseInfo);
		return new ResponseEntity<LeaveOpeningBalanceResponse>(leaveOpeningBalanceResponse, HttpStatus.OK);
	}

	public ResponseEntity<?> getSuccessResponseForUpload(List<LeaveOpeningBalance> successLeaveOpeningBalanceList,
														 List<LeaveOpeningBalance> errorLeaveOpeningBalanceList, RequestInfo requestInfo) {
		LeaveOpeningBalanceUploadResponse leaveOpeningBalanceResponse = new LeaveOpeningBalanceUploadResponse();
		leaveOpeningBalanceResponse.getSuccessList().addAll(successLeaveOpeningBalanceList);
		leaveOpeningBalanceResponse.getErrorList().addAll(errorLeaveOpeningBalanceList);

		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		leaveOpeningBalanceResponse.setResponseInfo(responseInfo);
		return new ResponseEntity<LeaveOpeningBalanceUploadResponse>(leaveOpeningBalanceResponse, HttpStatus.OK);
	}

	public void create(LeaveOpeningBalanceRequest leaveOpeningBalanceRequest) {
		leaveOpeningBalanceRepository.create(leaveOpeningBalanceRequest);
	}

	public void update(LeaveOpeningBalanceRequest leaveOpeningBalanceRequest) {
		leaveOpeningBalanceRepository.update(leaveOpeningBalanceRequest);
	}
}