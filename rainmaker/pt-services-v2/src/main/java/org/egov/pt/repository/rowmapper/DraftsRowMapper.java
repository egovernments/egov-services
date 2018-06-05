package org.egov.pt.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.pt.web.models.AuditDetails;
import org.egov.pt.web.models.Draft;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

@Component
public class DraftsRowMapper implements ResultSetExtractor<List<Draft>> {

	@Override
	public List<Draft> extractData(ResultSet rs) throws SQLException, DataAccessException {
		Map<String, Draft> draftsMap = new HashMap<>();
		while(rs.next()) {
			String currentId = rs.getString("id");
			Draft currentDraft = draftsMap.get(currentId);
			if(null == currentDraft) {
				AuditDetails auditDetails = AuditDetails.builder().createdBy(rs.getString("createdby")).createdTime(rs.getLong("createdTime"))
						.lastModifiedBy(rs.getString("lastmodifiedby")).lastModifiedTime(rs.getLong("lastmodifiedtime")).build();
				
				currentDraft = Draft.builder().id(rs.getString("id")).userId(rs.getString("userId")).tenantId(rs.getString("tenantId"))
						.draftRecord(rs.getObject("draft")).auditDetails(auditDetails).build();
				
				draftsMap.put(currentId, currentDraft);
			}			
		}
		return new ArrayList<>(draftsMap.values());

	}

}
