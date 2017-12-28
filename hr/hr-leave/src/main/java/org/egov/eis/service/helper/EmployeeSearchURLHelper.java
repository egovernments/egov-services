package org.egov.eis.service.helper;

import java.text.SimpleDateFormat;

import org.egov.eis.config.ApplicationProperties;
import org.egov.eis.config.PropertiesManager;
import org.egov.eis.web.contract.LeaveSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class EmployeeSearchURLHelper {

	@Autowired
	private ApplicationProperties applicationProperties;

	@Autowired
	private PropertiesManager propertiesManager;

	public String searchURL(final LeaveSearchRequest leaveSearchRequest, final String url) {
		final StringBuilder searchURL = new StringBuilder(url + "?");
		final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		if (leaveSearchRequest.getTenantId() == null)
			return searchURL.toString();
		else
			searchURL.append("tenantId=" + leaveSearchRequest.getTenantId());

		if (!StringUtils.isEmpty(leaveSearchRequest.getCode()))
			searchURL.append("&code=" + leaveSearchRequest.getCode());

		if (leaveSearchRequest.getDepartmentId() != null)
			searchURL.append("&departmentId=" + leaveSearchRequest.getDepartmentId());

		if (leaveSearchRequest.getDesignationId() != null)
			searchURL.append("&designationId=" + leaveSearchRequest.getDesignationId());

		if (leaveSearchRequest.getEmployeeType() != null)
			searchURL.append("&employeeType=" + leaveSearchRequest.getEmployeeType());

		if (leaveSearchRequest.getEmployeeStatus() != null)
			searchURL.append("&employeeStatus=" + leaveSearchRequest.getEmployeeStatus());

		if (leaveSearchRequest.getIsPrimary() != null)
			searchURL.append("&isPrimary=true");

		if (leaveSearchRequest.getAsOnDate() != null && !leaveSearchRequest.getAsOnDate().equals(""))
			searchURL.append("&asOnDate=" + sdf.format(leaveSearchRequest.getAsOnDate()));

		searchURL.append("&active=true&pageSize=" + applicationProperties.hrLeaveSearchPageSizeMax());

		return searchURL.toString();
	}

}