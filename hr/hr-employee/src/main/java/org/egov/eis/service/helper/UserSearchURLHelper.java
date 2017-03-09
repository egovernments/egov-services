package org.egov.eis.service.helper;

import java.util.List;

import org.egov.eis.config.ApplicationProperties;
import org.egov.eis.web.contract.EmployeeGetRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserSearchURLHelper {
	
	@Autowired
	private ApplicationProperties applicationProperties;

	public String searchURL(EmployeeGetRequest employeeGetRequest) {
		String BASE_URL = applicationProperties.empServicesUsersServiceGetUsersHostname();
		StringBuilder searchURL = new StringBuilder(BASE_URL + "?");

		if (employeeGetRequest.getId() == null && employeeGetRequest.getTenantId() == null)
			return searchURL.toString();

		boolean isAppendAndSeperator = false;

		if (employeeGetRequest.getTenantId() != null) {
			isAppendAndSeperator = true;
			searchURL.append("tenantId=" + employeeGetRequest.getTenantId());
		}

		if (employeeGetRequest.getId() != null) {
			isAppendAndSeperator = addAndIfRequired(isAppendAndSeperator, searchURL);
			searchURL.append("id=" + getCommaSeperatedIds(employeeGetRequest.getId()));
		}

		searchURL.append("&pageSize=" + applicationProperties.empSearchPageSizeMax());
		searchURL.append("&sortBy=id" + "&sortOrder=ASC");

		return searchURL.toString();
	}

	/**
	 * This method is always called at the beginning of the method so that &
	 * is prepended before the next parameter is appended.
	 * 
	 * @param appendAndFlag
	 * @param searchURL
	 * @return boolean indicates if the next parameter should append an "&"
	 */
	private boolean addAndIfRequired(boolean appendAndSeperatorFlag, StringBuilder searchURL) {
		if (appendAndSeperatorFlag)
			searchURL.append("&");

		return true;
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
}