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

import java.util.Collections;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.eis.model.Designation;
import org.egov.eis.model.Sequence;
import org.egov.eis.repository.DesignationRepository;
import org.egov.eis.web.contract.DesignationGetRequest;
import org.egov.eis.web.contract.DesignationRequest;
import org.egov.eis.web.contract.DesignationResponse;
import org.egov.eis.web.contract.factory.ResponseInfoFactory;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DesignationService {
	

	   private static final Logger LOGGER = LoggerFactory.getLogger(DesignationService.class);
	
	@Value("${kafka.topics.designation.create.name}")
	private String designationCreateTopic;

	@Value("${kafka.topics.designation.update.name}")
	private String designationUpdateTopic;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;
	
	@Autowired
	private DesignationRepository designationRepository;
	
	@Autowired
	private CommonIdGenerationService commonIdGenerationService;
	

	public List<Designation> getDesignations(DesignationGetRequest designationGetRequest) {
		return designationRepository.findForCriteria(designationGetRequest);
	}
	
	public ResponseEntity<?> createDesignation(DesignationRequest designationRequest) {
		Designation designation = designationRequest.getDesignation();
		designation.setId(commonIdGenerationService.getNextId(Sequence.DESIGNATIONSEQUENCS));
        create(designationRequest);
 		return getSuccessResponseForCreate(Collections.singletonList(designation), designationRequest.getRequestInfo());
	}
	
	public ResponseEntity<?> updateDesignation(DesignationRequest designationRequest) {
		Designation designation = designationRequest.getDesignation();
        update(designationRequest);
   		return getSuccessResponseForCreate(Collections.singletonList(designation), designationRequest.getRequestInfo());
	}
	
	/**
	 * Populate DesignationResponse object & returns ResponseEntity of
	 * type DesignationResponse containing ResponseInfo & array of
	 * Designation objects
	 * 
	 * @param designationList
	 * @param requestInfo
	 * @return ResponseEntity<?>
	 */
	public ResponseEntity<?> getSuccessResponseForCreate(List<Designation> designationList,
			RequestInfo requestInfo) {
		DesignationResponse designationResponse = new DesignationResponse();
		designationResponse.getDesignation().addAll(designationList);

		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		designationResponse.setResponseInfo(responseInfo);
		return new ResponseEntity<DesignationResponse>(designationResponse, HttpStatus.OK);
	}
	
	public void create(DesignationRequest designationRequest) {
		designationRepository.create(designationRequest);
	}

	public void update(DesignationRequest designationRequest) {
		designationRepository.update(designationRequest);
	}

}