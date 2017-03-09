package org.egov.eis.service.helper;

import java.util.List;

import org.egov.eis.config.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PositionSearchURLHelper {
	
	@Autowired
	private ApplicationProperties applicationProperties;

	public String searchURL(List<Long> positionIdList, String tenantId) {
		String BASE_URL = applicationProperties.empServicesHrMastersServiceGetPositionsHostname();
		String ids = getCommaSeperatedIds(positionIdList);
		String searchURL = BASE_URL + "?tenantId=" + tenantId + "&id=" + ids;

		return searchURL;
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