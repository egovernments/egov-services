package org.egov.pt.repository.builder;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class DraftsQueryBuilder {

	public String getDraftsSearchQuery(String tenantId, String userId, List<Object> preparedStatementList) {
		StringBuilder query = new StringBuilder();
		
		query.append("SELECT id, userId, tenantId, draft as draftRecord FROM eg_pt_drafts_v2 WHERE tenantId = ?");
		preparedStatementList.add(tenantId);
		
		if(!StringUtils.isEmpty(userId)) {
			query.append(" AND userId = ?");
			preparedStatementList.add(userId);
		}
		return new StringBuilder("select (select array_to_json(array_agg(row_to_json(t))) from ({query}) t) as drafts").toString().replace("{query}", query);
	}
}
