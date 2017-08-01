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

import java.util.List;

import org.egov.pgr.domain.model.EscalationTimeType;
import org.egov.pgr.producers.PGRProducer;
import org.egov.pgr.repository.EscalationTimeTypeRepository;
import org.egov.pgr.web.contract.EscalationTimeTypeGetReq;
import org.egov.pgr.web.contract.EscalationTimeTypeReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EscalationTimeTypeService {
	
	public static final Logger logger = LoggerFactory.getLogger(EscalationTimeTypeService.class);

	@Autowired
	private EscalationTimeTypeRepository escalationTimeTypeRepository;
	
	
	@Autowired
	private EscalationTimeTypeRepository escalationRepository;

	@Autowired
	private PGRProducer pgrProducer;
	
	public EscalationTimeType createEscalationTimeType(final String topic, final String key,
			final EscalationTimeTypeReq escalationTimeTypeReq) {
		final ObjectMapper mapper = new ObjectMapper();
		String escalationTimeTypeValue = null;
		try {
			logger.info("EscalationTimeTypeReq::" + escalationTimeTypeReq);
			escalationTimeTypeValue = mapper.writeValueAsString(escalationTimeTypeReq);
			logger.info("Value being pushed on the Queue, EscalationTimeTypeValue::" + escalationTimeTypeValue);
		} catch (final JsonProcessingException e) {
			logger.error("Exception Encountered : " + e);
		}
		try {
			pgrProducer.sendMessage(topic, key, escalationTimeTypeValue);
		} catch (final Exception e) {
			logger.error("Exception while posting to kafka Queue : " + e);
			return escalationTimeTypeReq.getEscalationTimeType();
		}
		logger.info("Producer successfully posted the request to Queue");
		return escalationTimeTypeReq.getEscalationTimeType();
	}
	
	public EscalationTimeTypeReq create(EscalationTimeTypeReq escalationTimeTypeReq) {
		logger.info("Persisting service group record");
		return escalationTimeTypeRepository.persistCreateEscalationTimeType(escalationTimeTypeReq);
	}
    public List<EscalationTimeType> getAllEscalationTimeTypes(final EscalationTimeTypeGetReq escalationGetRequest) {
        return escalationRepository.getAllEscalationTimeTypes(escalationGetRequest);

    }
	
	
	public EscalationTimeType updateEscalationTimeType(final String topic, final String key,
			final EscalationTimeTypeReq escalationTimeTypeReq) {
		final ObjectMapper mapper = new ObjectMapper();
		String escalationTimeTypeValue = null;
		try {
			logger.info("EscalationTimeTypeReq::" + escalationTimeTypeReq);
			escalationTimeTypeValue = mapper.writeValueAsString(escalationTimeTypeReq);
			logger.info("Value being pushed on the Queue, EscalationTimeTypeValue::" + escalationTimeTypeValue);
		} catch (final JsonProcessingException e) {
			logger.error("Exception Encountered : " + e);
		}
		try {
			pgrProducer.sendMessage(topic, key, escalationTimeTypeValue);
		} catch (final Exception e) {
			logger.error("Exception while posting to kafka Queue : " + e);
			return escalationTimeTypeReq.getEscalationTimeType();
		}
		logger.info("Producer successfully posted the request to Queue");
		return escalationTimeTypeReq.getEscalationTimeType();
	}
	
	public EscalationTimeTypeReq update(EscalationTimeTypeReq escalationTimeTypeReq) {
		logger.info("Updating service group record");
		return escalationTimeTypeRepository.persistUpdateEscalationTimeType(escalationTimeTypeReq);
	}
	

}
