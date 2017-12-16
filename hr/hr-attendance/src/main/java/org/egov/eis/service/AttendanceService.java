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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.eis.config.PropertiesManager;
import org.egov.eis.model.Attendance;
import org.egov.eis.model.AttendanceType;
import org.egov.eis.model.EmployeeInfo;
import org.egov.eis.producers.AttendanceProducer;
import org.egov.eis.repository.AttendanceRepository;
import org.egov.eis.web.contract.AttendanceGetRequest;
import org.egov.eis.web.contract.AttendanceReportRequest;
import org.egov.eis.web.contract.AttendanceRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AttendanceService {
	public static final Logger logger = LoggerFactory.getLogger(AttendanceService.class);

	@Autowired
	private AttendanceRepository attendanceRepository;

	@Autowired
	private AttendanceProducer attendanceProducer;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private HolidayService holidayService;

	@Autowired
	private HRConfigurationService hrConfigurationService;

	@Autowired
	private PropertiesManager propertiesManager;

	public List<Attendance> getAttendances(final AttendanceGetRequest attendanceGetRequest) throws ParseException {
		return attendanceRepository.findForCriteria(attendanceGetRequest);
	}

	public List<Attendance> findAttendanceForHolidays(final AttendanceGetRequest attendanceGetRequest,
			RequestInfo requestInfo) throws ParseException {
		List<Date> holidayDates = getHolidayDates(attendanceGetRequest, requestInfo);
		return attendanceRepository.findByCriteria(attendanceGetRequest, holidayDates);
	}

	private List<Date> getHolidayDates(AttendanceGetRequest attendanceGetRequest, RequestInfo requestInfo) {
		return holidayService.getHolidaysBetweenDates(attendanceGetRequest.getTenantId(), requestInfo,
				attendanceGetRequest.getFromDate(), new Date());
	}

	/*
	 * This method is used to create new attendance
	 * 
	 * @return Attendance, return the attendance details with current status
	 * 
	 * @param attendance, hold attendance details
	 */

	public List<Attendance> createAsync(final AttendanceRequest attendanceRequest) {
		String attendanceValue = null;
		try {
			logger.info("createAttendance service::" + attendanceRequest);
			attendanceValue = objectMapper.writeValueAsString(attendanceRequest);
			logger.info("attendanceValue::" + attendanceValue);
		} catch (final JsonProcessingException e) {
			e.printStackTrace();
		}
		try {
			attendanceProducer.sendMessage("egov-hr-attendance", "save-attendance", attendanceValue);
		} catch (final Exception ex) {
			ex.printStackTrace();
		}
		return attendanceRequest.getAttendances();
	}

	public List<Attendance> getAttendanceReport(final AttendanceReportRequest attendanceReportRequest,
			RequestInfo requestInfo) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		List<Long> ids = null;

		final List<EmployeeInfo> employeeInfos = employeeService.getEmployees(attendanceReportRequest, requestInfo);
		if (employeeInfos.isEmpty())
			return Collections.EMPTY_LIST;

		ids = employeeInfos.stream().map(employeeInfo -> employeeInfo.getId()).collect(Collectors.toList());
		attendanceReportRequest.setEmployeeIds(ids);
		Long daysInMonth = getNoOfDaysInMonth(attendanceReportRequest.getMonth(), attendanceReportRequest.getYear());
		return attendanceRepository.findReportQuery(attendanceReportRequest, daysInMonth);
	}

	public Long getNoOfDaysInMonth(Integer month, String year) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, Integer.parseInt(year));
		calendar.set(Calendar.MONTH, month - 1);
		return Long.valueOf(calendar.getActualMaximum(calendar.DAY_OF_MONTH));
	}

	public Long getNoOfWorkingDays(AttendanceReportRequest attendanceReportRequest, RequestInfo requestInfo) {

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, Integer.parseInt(attendanceReportRequest.getYear()));
		calendar.set(Calendar.MONTH, attendanceReportRequest.getMonth() - 1);
		final Map<String, List<String>> weeklyHolidays = hrConfigurationService
				.getWeeklyHolidays(attendanceReportRequest.getTenantId(), requestInfo);

		int count = 0;
		if ((calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY))
			count++;

		if (propertiesManager.getHrMastersServiceConfigurationsFiveDayWeek()
				.equals(weeklyHolidays.get(propertiesManager.getHrMastersServiceConfigurationsKey()).get(0))) {
			if ((calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY))
				count++;
		} else if (propertiesManager.getHrMastersServiceConfigurationsFiveDayWithSecondSaturday()
				.equals(weeklyHolidays.get(propertiesManager.getHrMastersServiceConfigurationsKey()).get(0))) {
			count += 1;
		} else if (propertiesManager.getHrMastersServiceConfigurationsFiveDayWithSecondAndFourthSaturday()
				.equals(weeklyHolidays.get(propertiesManager.getHrMastersServiceConfigurationsKey()).get(0)))
			count += 2;

		calendar.set(Calendar.DAY_OF_MONTH, 1);
		Date monthStart = calendar.getTime();
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		Date monthEnd = calendar.getTime();

		List<Date> holidaysBetweenDates = holidayService.getHolidaysBetweenDates(attendanceReportRequest.getTenantId(),
				requestInfo, monthStart, monthEnd);
		count += holidaysBetweenDates.size();
		count = calendar.getActualMaximum(calendar.DAY_OF_MONTH) - count;
		return Long.valueOf(count);
	}

	public boolean getByEmployeeAndDate(final Long employee, final Date attendanceDate, final String tenantId) {
		return attendanceRepository.checkAttendanceByEmployeeAndDate(employee, attendanceDate, tenantId);
	}

	public AttendanceRequest create(final AttendanceRequest attendanceRequest) {
		return attendanceRepository.saveAttendance(attendanceRequest);
	}

	public AttendanceType getAttendanceTypeByCode(final String code, final String tenantId) {
		return attendanceRepository.findAttendanceTypeByCode(code, tenantId);
	}

	public List<AttendanceType> getAllAttendanceTypes(final String tenantId) {
		return attendanceRepository.findAllAttendanceTypes(tenantId);
	}
}