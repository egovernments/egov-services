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

package org.egov.mseva.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.egov.mseva.config.PropertiesManager;
import org.egov.mseva.model.AuditDetails;
import org.egov.mseva.producer.MsevaEventsProducer;
import org.egov.mseva.utils.ResponseInfoFactory;
import org.egov.mseva.web.contract.EventRequest;
import org.egov.mseva.web.contract.EventResponse;
import org.egov.mseva.web.validator.MsevaValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Service
public class MsevaService {
	
	@Autowired
	private PropertiesManager properties;
	
	@Autowired
	private MsevaEventsProducer producer;
	
	@Autowired
	private ResponseInfoFactory responseInfo;
	
	@Autowired
	private MsevaValidator validator;
	
	public EventResponse createEvents(EventRequest request) {
		validator.validateCreateEvent(request);
		log.info("enriching and storing the event......");
		enrichCreateEvent(request);
		producer.push(properties.getSaveEventsTopic(), request);
		
		return EventResponse.builder()
				.responseInfo(responseInfo.createResponseInfoFromRequestInfo(request.getRequestInfo(), true))
				.events(request.getEvents()).build();
	}
	
	public EventResponse updateEvents(EventRequest request) {
		validator.validateUpdateEvent(request);
		log.info("enriching and storing the event......");
		enrichUpdateEvent(request);
		producer.push(properties.getUpdateEventsTopic(), request);
		
		return EventResponse.builder()
				.responseInfo(responseInfo.createResponseInfoFromRequestInfo(request.getRequestInfo(), true))
				.events(request.getEvents()).build();
	}
	
	private void enrichCreateEvent(EventRequest request) {
		Map<String, String> recepientEventMap = new HashMap<>();
		request.getEvents().forEach(event -> {
			event.setId(UUID.randomUUID().toString());
			event.getActions().setId(UUID.randomUUID().toString());
			event.getActions().setEventId(event.getId());
			
			if(!event.getToUsers().isEmpty()) {
				event.getToRoles().clear();
			} //toUsers will take precedence over toRoles.
			
			if(!event.getToUsers().isEmpty()) {
				event.getToUsers().forEach(user -> recepientEventMap.put(user, event.getId()));
			}
			if(!event.getToRoles().isEmpty()) {
				event.getToRoles().forEach(role -> recepientEventMap.put(role, event.getId()));
			}
			AuditDetails auditDetails = AuditDetails.builder().createdBy(request.getRequestInfo().getUserInfo().getUuid())
					.createdTime(new Date().getTime()).build();
			
			event.setAuditDetails(auditDetails);

		});
	}
	
	private void enrichUpdateEvent(EventRequest request) {
		Map<String, String> recepientEventMap = new HashMap<>();
		request.getEvents().forEach(event -> {			
			if(!event.getToUsers().isEmpty()) {
				event.getToRoles().clear();
			} //toUsers will take precedence over toRoles.
			
			if(!event.getToUsers().isEmpty()) {
				event.getToUsers().forEach(user -> recepientEventMap.put(user, event.getId()));
			}
			if(!event.getToRoles().isEmpty()) {
				event.getToRoles().forEach(role -> recepientEventMap.put(role, event.getId()));
			}
			AuditDetails auditDetails = event.getAuditDetails();
			auditDetails.setLastModifiedBy(request.getRequestInfo().getUserInfo().getUuid());
			auditDetails.setLastModifiedTime(new Date().getTime());
			
			event.setAuditDetails(auditDetails);

		});
	}
	
}