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

package org.egov.eis.service;

import java.util.List;

import org.egov.eis.broker.PositionProducer;
import org.egov.eis.model.Position;
import org.egov.eis.repository.PositionRepository;
import org.egov.eis.web.contract.PositionGetRequest;
import org.egov.eis.web.contract.PositionRequest;
import org.egov.eis.web.contract.PositionResponse;
import org.egov.eis.web.contract.RequestInfo;
import org.egov.eis.web.contract.ResponseInfo;
import org.egov.eis.web.contract.factory.ResponseInfoFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PositionService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PositionService.class);

	@Value("${kafka.topics.position.create.name}")
	private String positionCreateTopic;

	@Value("${kafka.topics.position.create.key}")
	private String positionCreateKey;

	@Value("${kafka.topics.position.update.name}")
	private String positionUpdateTopic;

	@Value("${kafka.topics.position.update.key}")
	private String positionUpdateKey;

	@Autowired
	private PositionRepository positionRepository;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@Autowired
	private PositionProducer positionProducer;
	
	@Autowired
	private ObjectMapper objectMapper;

	public List<Position> getPositions(PositionGetRequest positionGetRequest) {
		return positionRepository.findForCriteria(positionGetRequest);
	}

	public ResponseEntity<?> createPosition(PositionRequest positionRequest) {
		List<Position> positions = positionRequest.getPosition();
		String positionJson = null;
		try {
			positionJson = objectMapper.writeValueAsString(positionRequest);
			LOGGER.info("positionJson::" + positionJson);
		} catch (JsonProcessingException e) {
			LOGGER.error("Error while converting position to JSON", e);
			e.printStackTrace();
		}
		try {
			positionProducer.sendMessage(positionCreateTopic, positionCreateKey, positionJson);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return getSuccessResponseForCreate(positions, positionRequest.getRequestInfo());
	}

	public ResponseEntity<?> updatePosition(PositionRequest positionRequest) {
		List<Position> positions = positionRequest.getPosition();
		String positionRequestJson = null;
		try {
			positionRequestJson = objectMapper.writeValueAsString(positionRequest);
			LOGGER.info("positionRequestJson::" + positionRequestJson);
		} catch (JsonProcessingException e) {
			LOGGER.error("Error while converting Employee to JSON", e);
			e.printStackTrace();
		}
		try {
			positionProducer.sendMessage(positionUpdateTopic, positionUpdateKey, positionRequestJson);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return getSuccessResponseForCreate(positions, positionRequest.getRequestInfo());
	}

	/**
	 * Populate PositionResponse object & returns ResponseEntity of type
	 * PositionResponse containing ResponseInfo & array of Position objects
	 * 
	 * @param Position
	 * @param requestInfo
	 * @param headers
	 * @return ResponseEntity<?>
	 */
	public ResponseEntity<?> getSuccessResponseForCreate(List<Position> positionList, RequestInfo requestInfo) {
		PositionResponse positionResponse = new PositionResponse();
		positionResponse.getPosition().addAll(positionList);

		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		positionResponse.setResponseInfo(responseInfo);
		return new ResponseEntity<PositionResponse>(positionResponse, HttpStatus.OK);
	}

	public void create(PositionRequest positionRequest) {
		positionRepository.create(positionRequest);
	}

	public void update(PositionRequest positionRequest) {
		positionRepository.update(positionRequest);
	}

}