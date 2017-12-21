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

package org.egov.eis.repository.builder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.eis.config.ApplicationProperties;
import org.egov.eis.model.Attendance;
import org.egov.eis.web.contract.AttendanceGetRequest;
import org.egov.eis.web.contract.AttendanceReportRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class AttendanceQueryBuilder {

	private static final Logger logger = LoggerFactory.getLogger(AttendanceQueryBuilder.class);
	private static final String BASE_QUERY = "SELECT a.id AS a_id, a.attendancedate AS a_date, a.employee AS "
			+ "a_employee, a.month AS a_month, a.year AS a_year, a.type AS a_type, a.remarks AS "
			+ "a_remarks, a.createdby AS a_createdby, a.createddate AS a_createddate, a.lastmodifiedby"
			+ " AS a_lastmodifiedby, a.lastmodifieddate AS a_lastmodifieddate, a.tenantId AS a_tenantId, "
			+ "t.id AS t_id, t.code AS t_code, t.description AS t_description"
			+ " FROM egeis_attendance AS a JOIN egeis_attendance_type AS t ON a.type = t.id";
	@Autowired
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	@Autowired
	private ApplicationProperties applicationProperties;

	public AttendanceQueryBuilder(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	private static String getIdQuery(final List<Long> idList) {
		final StringBuilder query = new StringBuilder("(");
		if (idList.size() >= 1) {
			query.append(idList.get(0).toString());
			for (int i = 1; i < idList.size(); i++)
				query.append(", " + idList.get(i));
		}
		return query.append(")").toString();
	}

	public static String insertAttendanceQuery() {
		return "INSERT INTO egeis_attendance values " + "(nextval('seq_egeis_attendance'),?,?,?,?,?,?,?,?,?,?,?)";
	}

	public static String updateAttendanceQuery() {
		return "UPDATE egeis_attendance SET attendancedate = ?, employee = ?,"
				+ " month = ?, year = ?, type = ?, remarks = ?, lastmodifiedby = ?, lastmodifieddate = ?,"
				+ " tenantid = ? where id = ? ";
	}

	public static String selectTypeByCodeQuery() {
		return "select * from egeis_attendance_type where upper(code) = ? and tenantId = ?";
	}

	public static String selectAttendanceTypeQuery() {
		return "select * from egeis_attendance_type where tenantId = ?";
	}

	public static String selectAttendanceByEmployeeAndDateQuery() {
		return "SELECT id FROM egeis_attendance where employee = ? and attendancedate = ? and tenantId = ?";
	}

	private static String getCommaSeperatedIds(List<Long> idList) {
		if (idList.isEmpty())
			return "";

		StringBuilder query = new StringBuilder(idList.get(0).toString());
		for (int i = 1; i < idList.size(); i++) {
			query.append("," + idList.get(i));
		}

		return query.toString();
	}

	@SuppressWarnings("rawtypes")
	public String getQuery(final AttendanceGetRequest attendanceGetRequest, final List preparedStatementValues)
			throws ParseException {
		final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);

		addWhereClause(selectQuery, preparedStatementValues, attendanceGetRequest);
		addOrderByClause(selectQuery, attendanceGetRequest);
		addPagingClause(selectQuery, preparedStatementValues, attendanceGetRequest);

		logger.debug("Query : " + selectQuery);
		return selectQuery.toString();
	}

	public List<Attendance> getAttendanceQuery(final AttendanceGetRequest attendanceGetRequest,
			final List<Date> holidays, final List preparedStatementValues) {
		String searchQuery = "select :selectfields from :attendancetablename, :attendancetypetablename :condition  ";
		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();

		searchQuery = searchQuery.replace(":attendancetablename", "egeis_attendance att");
		searchQuery = searchQuery.replace(":attendancetypetablename", "egeis_attendance_type attype");

		searchQuery = searchQuery.replace(":selectfields", " att.employee, att.attendanceDate ");

		if (!StringUtils.isEmpty(attendanceGetRequest.getTenantId())) {

			params.append(" and ");

			params.append("att.tenantId =:tenantId");
			paramValues.put("tenantId", attendanceGetRequest.getTenantId());
		}
		if (holidays != null && !holidays.isEmpty()) {

			params.append(" and ");

			params.append("att.attendancedate IN (:holidays)");
			paramValues.put("holidays", holidays);
		}

		if (attendanceGetRequest.getEmployeeIds() != null && !attendanceGetRequest.getEmployeeIds().isEmpty()) {

			params.append(" and ");

			params.append("att.employee IN (:empIds)");
			paramValues.put("empIds", attendanceGetRequest.getEmployeeIds());
		}

		if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition",
					" where attype.code='P' and att.type=attype.id " + params.toString());

		} else {
			searchQuery = searchQuery.replace(":condition", " where attype.code='P' and att.type=attype.id ");
		}

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(Attendance.class);

		return namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);

	}

	public String getAttendanceReportQuery(final AttendanceReportRequest attendanceReportRequest, Long noofdays,
			Long noOfWorkingDays, final List preparedStatementValues) {
		String searchQuery = "select a.employee AS a_employee , a.presentdays AS a_presentdays, sum(:noofdays-(a.PresentDays+a.LeaveDays+(:noOfHolidays)) ) AS a_absentdays,"
				+ " a.leavedays AS a_leavedays,  0 as a_noofots from ( select employee, sum(case when type =(select id from egeis_attendance_type  where code  ='P' and tenantid=':tenantid')"
				+ "and month=:month and year=':year' and tenantid=':tenantid' then 1 else 0 end) PresentDays, sum(case when type =(select id from egeis_attendance_type "
				+ "where code  ='L' and tenantid=':tenantid') and month=:month and year=':year' and tenantid=':tenantid' then 1 else 0 end) LeaveDays "
				+ "from egeis_attendance where month=:month and year=':year' and tenantid=':tenantid' ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();
		Long noOfHolidays = noofdays - noOfWorkingDays;
		searchQuery = searchQuery.replace(":noofdays", noofdays.toString());
		searchQuery = searchQuery.replace(":noOfHolidays", noOfHolidays.toString());
		searchQuery = searchQuery.replace(":month", attendanceReportRequest.getMonth().toString());
		searchQuery = searchQuery.replace(":year", attendanceReportRequest.getYear());
		searchQuery = searchQuery.replace(":tenantid", attendanceReportRequest.getTenantId());

		final StringBuilder selectQuery = new StringBuilder(searchQuery);

		if (attendanceReportRequest.getEmployeeIds() != null && !attendanceReportRequest.getEmployeeIds().isEmpty()) {

			selectQuery.append(
					" and employee in ( " + getCommaSeperatedIds(attendanceReportRequest.getEmployeeIds()) + " )");
		}
		selectQuery.append(" group by employee) a group by a.employee , a.presentdays, a.leavedays");
		return selectQuery.toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addWhereClause(final StringBuilder selectQuery, final List preparedStatementValues,
			final AttendanceGetRequest attendanceGetRequest) throws ParseException {

		if (attendanceGetRequest.getId() == null && attendanceGetRequest.getApplicableOn() == null
				&& attendanceGetRequest.getCode() == null && attendanceGetRequest.getMonth() == null
				&& attendanceGetRequest.getTenantId() == null && attendanceGetRequest.getYear() == null
				&& attendanceGetRequest.getDepartmentId() == null && attendanceGetRequest.getDesignationId() == null)
			return;

		selectQuery.append(" WHERE");
		boolean isAppendAndClause = false;

		if (attendanceGetRequest.getTenantId() != null) {
			isAppendAndClause = true;
			selectQuery.append(" a.tenantId = ? and t.tenantId = ?");
			preparedStatementValues.add(attendanceGetRequest.getTenantId());
			preparedStatementValues.add(attendanceGetRequest.getTenantId());
		}

		if (attendanceGetRequest.getId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" a.id IN " + getIdQuery(attendanceGetRequest.getId()));
		}

		if (attendanceGetRequest.getApplicableOn() != null) {
			final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" attendancedate = ?");
			preparedStatementValues.add(sdf.parse(attendanceGetRequest.getApplicableOn()));
		}

		if (attendanceGetRequest.getMonth() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" month = ?");
			preparedStatementValues.add(attendanceGetRequest.getMonth());
		}

		if (attendanceGetRequest.getYear() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" year = ?");
			preparedStatementValues.add(attendanceGetRequest.getYear());
		}

		if (attendanceGetRequest.getDesignationId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(
					" employee in (select employee from egeis_assignment where designationId = ? and isprimary = 't')");
			preparedStatementValues.add(attendanceGetRequest.getDesignationId());
		}

		if (attendanceGetRequest.getDepartmentId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(
					" employee in (select employee from egeis_assignment where departmentId = ? and isprimary = 't')");
			preparedStatementValues.add(attendanceGetRequest.getDepartmentId());
		}

		if (attendanceGetRequest.getCode() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" employee = (select id from egeis_employee where upper(code) = ?)");
			preparedStatementValues.add(attendanceGetRequest.getCode().toUpperCase());
		}
	}

	private void addOrderByClause(final StringBuilder selectQuery, final AttendanceGetRequest attendanceGetRequest) {
		final String sortBy = attendanceGetRequest.getSortBy() == null ? "attendancedate"
				: attendanceGetRequest.getSortBy();
		final String sortOrder = attendanceGetRequest.getSortOrder() == null ? "ASC"
				: attendanceGetRequest.getSortOrder();
		selectQuery.append(" ORDER BY " + sortBy + " " + sortOrder);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addPagingClause(final StringBuilder selectQuery, final List preparedStatementValues,
			final AttendanceGetRequest attendanceGetRequest) {
		// handle limit(also called pageSize) here
		selectQuery.append(" LIMIT ?");
		long pageSize = Integer.parseInt(applicationProperties.attendanceSearchPageSizeDefault());
		if (attendanceGetRequest.getPageSize() != null)
			pageSize = attendanceGetRequest.getPageSize();
		preparedStatementValues.add(pageSize); // Set limit to pageSize

		// handle offset here
		selectQuery.append(" OFFSET ?");
		int pageNumber = 0; // Default pageNo is zero meaning first page
		if (attendanceGetRequest.getPageNumber() != null)
			pageNumber = attendanceGetRequest.getPageNumber() - 1;
		preparedStatementValues.add(pageNumber * pageSize); // Set offset to
															// pageNo * pageSize
	}

	/**
	 * This method is always called at the beginning of the method so that and
	 * is prepended before the field's predicate is handled.
	 *
	 * @param appendAndClauseFlag
	 * @param queryString
	 * @return boolean indicates if the next predicate should append an "AND"
	 */
	private boolean addAndClauseIfRequired(final boolean appendAndClauseFlag, final StringBuilder queryString) {
		if (appendAndClauseFlag)
			queryString.append(" AND");

		return true;
	}
}
