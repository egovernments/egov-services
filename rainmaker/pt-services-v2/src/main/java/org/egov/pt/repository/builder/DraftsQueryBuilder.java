package org.egov.pt.repository.builder;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class DraftsQueryBuilder {

	public String getDraftsSearchQuery(String tenantId, String userId) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT * FROM eg_pt_drafts_v2 WHERE ");
		query.append(" tenantId = '"+tenantId+"'");
		if(!StringUtils.isEmpty(userId))
			query.append(" AND userId = '"+userId+"'");
		
		return query.toString();
	}
}
