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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;

import io.swagger.util.Json;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DraftsRowMapper implements ResultSetExtractor<List<Draft>> {

	@Override
	public List<Draft> extractData(ResultSet rs) throws SQLException, DataAccessException {
		Map<String, Draft> draftsMap = new HashMap<>();
		ObjectMapper mapper = new ObjectMapper();
		while(rs.next()) {
			String currentId = rs.getString("id");
			Draft currentDraft = draftsMap.get(currentId);
			if(null == currentDraft) {
				AuditDetails auditDetails = AuditDetails.builder().createdBy(rs.getString("createdby")).createdTime(rs.getLong("createdTime"))
						.lastModifiedBy(rs.getString("lastmodifiedby")).lastModifiedTime(rs.getLong("lastmodifiedtime")).build();
				try {
					Map<String, Object> pGDraft = mapper.convertValue(rs.getObject("draft"), Map.class);
                    Object document = Configuration.defaultConfiguration().jsonProvider().parse(pGDraft.get("value").toString());
					currentDraft = Draft.builder().id(rs.getString("id")).userId(rs.getString("userId")).tenantId(rs.getString("tenantId"))
							.draftRecord(document)
							.auditDetails(auditDetails).build();
				}catch(Exception e) {
					log.error("Exception while formating drafts into json: "+e);
					currentDraft = Draft.builder().id(rs.getString("id")).userId(rs.getString("userId")).tenantId(rs.getString("tenantId"))
							.draftRecord(mapper.convertValue(rs.getObject("draft"), Map.class)).auditDetails(auditDetails).build();
				}
				
				draftsMap.put(currentId, currentDraft);
			}			
		}
		return new ArrayList<>(draftsMap.values());

	}

}
