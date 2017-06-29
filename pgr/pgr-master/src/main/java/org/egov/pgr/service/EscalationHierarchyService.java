/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.pgr.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.pgr.model.EscalationHierarchy;
import org.egov.pgr.producers.PGRProducer;
import org.egov.pgr.repository.EscalationHierarchyRespository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EscalationHierarchyService {

	public static final Logger logger = LoggerFactory.getLogger(EscalationHierarchyService.class);
	
	@Autowired
	private EscalationHierarchyRespository escalationHierarchyRepository;
	
	@Autowired
	private PGRProducer pgrProducer;
	
	public org.egov.pgr.web.contract.EscalationHierarchy createEscalationHierarchy(final String topic, final String key,
			final EscalationHierarchy escalationHierarchy) {
		final ObjectMapper mapper = new ObjectMapper();
		String escalationHierarchyValue = null;
		try {
			logger.info("EscalationHierarchy::" + escalationHierarchy);
			escalationHierarchyValue = mapper.writeValueAsString(escalationHierarchy);
			logger.info("Value being pushed on the Queue, EscalationHierarchyValue::" + escalationHierarchyValue);
		} catch (final JsonProcessingException e) {
			logger.error("Exception Encountered : " + e);
		}
		try {
			pgrProducer.sendMessage(topic, key, escalationHierarchyValue);
		} catch (final Exception e) {
			logger.error("Exception while posting to kafka Queue : " + e);
			return new org.egov.pgr.web.contract.EscalationHierarchy();
		}
		logger.error("Producer successfully posted the request to Queue");
		return new org.egov.pgr.web.contract.EscalationHierarchy();
	}
	
	public org.egov.pgr.web.contract.EscalationHierarchy updateEscalationHierarchy(final String topic, final String key,
			final EscalationHierarchy escalationHierarchy) {
		final ObjectMapper mapper = new ObjectMapper();
		String escalationHierarchyValue = null;
		try {
			logger.info("EscalationHierarchy::" + escalationHierarchy);
			escalationHierarchyValue = mapper.writeValueAsString(escalationHierarchy);
			logger.info("Value being pushed on the Queue, EscalationHierarchyValue::" + escalationHierarchyValue);
		} catch (final JsonProcessingException e) {
			logger.error("Exception Encountered : " + e);
		}
		try {
			pgrProducer.sendMessage(topic, key, escalationHierarchyValue);
		} catch (final Exception e) {
			logger.error("Exception while posting to kafka Queue : " + e);
			return new org.egov.pgr.web.contract.EscalationHierarchy();
		}
		logger.error("Producer successfully posted the request to Queue");
		return new org.egov.pgr.web.contract.EscalationHierarchy();
	}
	
	public void create(EscalationHierarchy escalationHierarchy) {
		logger.info("Persisting Escalation Hierarchy record");
		escalationHierarchyRepository.persistEscalationHierarchy(escalationHierarchy);
	}
	
	public void update(EscalationHierarchy escalationHierarchy) {
		logger.info("Updating Escalation Hierarchy record");
		escalationHierarchyRepository.deleteEscalationHierarchy(escalationHierarchy);
		escalationHierarchyRepository.persistEscalationHierarchy(escalationHierarchy);
	}
	
	public List<org.egov.pgr.web.contract.EscalationHierarchy> getAllEscalationHierarchy(EscalationHierarchy escalationHierarchy) {
		List<EscalationHierarchy> escHierarchyList = escalationHierarchyRepository.getAllEscalationHierarchy(escalationHierarchy);
		return convertModelToContract(escHierarchyList);
	}
	
	private List<org.egov.pgr.web.contract.EscalationHierarchy> convertModelToContract(
			List<EscalationHierarchy> escHierarchyList) {
		List<org.egov.pgr.web.contract.EscalationHierarchy> list = new ArrayList<>();
		if (null != escHierarchyList && escHierarchyList.size() <= 0) {
			for (int i = 0; i < escHierarchyList.size(); i++) {
				org.egov.pgr.web.contract.EscalationHierarchy escHierarchy = new org.egov.pgr.web.contract.EscalationHierarchy();
				escHierarchy.setFromPosition(escHierarchyList.get(i).getFromPosition());
				escHierarchy.setToPosition(escHierarchyList.get(i).getToPosition());
				escHierarchy.setServiceCode(escHierarchyList.get(i).getServiceCode());
				escHierarchy.setTenantId(escHierarchyList.get(i).getTenantId());
				list.add(escHierarchy);
			}
		}
		return list;
	}
}

