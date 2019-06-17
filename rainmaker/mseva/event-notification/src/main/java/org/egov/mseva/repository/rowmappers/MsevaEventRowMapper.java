package org.egov.mseva.repository.rowmappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.egov.mseva.model.AuditDetails;
import org.egov.mseva.model.enums.Status;
import org.egov.mseva.web.contract.Action;
import org.egov.mseva.web.contract.Event;
import org.egov.mseva.web.contract.EventDetails;
import org.egov.mseva.web.contract.Recepient;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MsevaEventRowMapper implements ResultSetExtractor <List<Event>> {

	@Override
	public List<Event> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
		List<Event> events = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		while(resultSet.next()) {
			Event event = Event.builder()
					.id(resultSet.getString("id"))
					.tenantId(resultSet.getString("tenantid"))
					.eventType(resultSet.getString("eventtype"))
					.source(resultSet.getString("source"))
					.description(resultSet.getString("description"))
					.status(Status.valueOf(resultSet.getString("status"))).build();
			try {
				event.setEventDetails(mapper.convertValue(resultSet.getObject("eventdetails"), EventDetails.class));
				event.setActions(mapper.convertValue(resultSet.getObject("actions"), Action.class));
				event.setRecepient(mapper.convertValue(resultSet.getObject("recepient"), Recepient.class));
			}catch(Exception e) {
				log.error("Error while adding jsonb fields: ", e);
				continue;
			}
			AuditDetails audit = AuditDetails.builder()
					.createdBy(resultSet.getString("createdby"))
					.createdTime(resultSet.getLong("createdtime"))
					.lastModifiedBy(resultSet.getString("lastmodifiedby"))
					.lastModifiedTime(resultSet.getLong("lastmodifiedtime")).build();
			
			event.setAuditDetails(audit);
			
			events.add(event);		
		}
		
		return events;
	}

}
