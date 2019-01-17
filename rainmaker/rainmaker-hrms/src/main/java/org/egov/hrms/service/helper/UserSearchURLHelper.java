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

package org.egov.hrms.service.helper;

import org.egov.hrms.config.ApplicationProperties;
import org.egov.hrms.config.PropertiesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserSearchURLHelper {
	
	@Autowired
	private ApplicationProperties applicationProperties;

	@Autowired
	private PropertiesManager propertiesManager;

	public String searchURL(List<Long> ids, String tenantId) {
		String BASE_URL = propertiesManager.getUsersServiceHostName() + propertiesManager.getUsersServiceUsersBasePath()
				+ propertiesManager.getUsersServiceUsersSearchPath();

		StringBuilder searchURL = new StringBuilder(BASE_URL + "?");

		if (ids == null && tenantId == null)
			return searchURL.toString();

		boolean isAppendAndSeperator = false;

		if (tenantId != null) {
			isAppendAndSeperator = true;
			searchURL.append("tenantId=" + tenantId);
		}

		if (ids != null) {
			isAppendAndSeperator = addAndIfRequired(isAppendAndSeperator, searchURL);
			searchURL.append("id=" + getCommaSeperatedIds(ids));
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