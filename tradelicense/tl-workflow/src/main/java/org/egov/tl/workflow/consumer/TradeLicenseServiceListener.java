/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *      accountability and the service delivery of the government  organizations.
 *  
 *       Copyright (C) <2015>  eGovernments Foundation
 *  
 *       The updated version of eGov suite of products as by eGovernments Foundation
 *       is available at http://www.egovernments.org
 *  
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       any later version.
 *  
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *  
 *       You should have received a copy of the GNU General Public License
 *       along with this program. If not, see http://www.gnu.org/licenses/ or
 *       http://www.gnu.org/licenses/gpl.html .
 *  
 *       In addition to the terms of the GPL license to be adhered to in using this
 *       program, the following additional terms are to be complied with:
 *  
 *           1) All versions of this program, verbatim or modified must carry this
 *              Legal Notice.
 *  
 *           2) Any misrepresentation of the origin of the material is prohibited. It
 *              is required that all modified versions of this material be marked in
 *              reasonable ways as different from the original version.
 *  
 *           3) This license does not grant any rights to any user of the program
 *              with regards to rights under trademark law for use of the trade names
 *              or trademarks of eGovernments Foundation.
 *  
 *     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.tl.workflow.consumer;

import java.util.HashMap;

import org.egov.tl.workflow.model.TradeLicenseContract;
import org.egov.tl.workflow.model.TradeLicenseRequest;
import org.egov.tl.workflow.repository.MessageQueueRepository;
import org.egov.tl.workflow.service.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TradeLicenseServiceListener {

	@Value("${kafka.topics.trade.license.workflow.enriched.topic}")
	private String workflowEnrichedTopic;

	@Value("${kafka.topics.trade.license.workflow.enriched.key}")
	private String workflowEnrichedKey;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MessageQueueRepository messageQueueRepository;

	@Autowired
	private WorkflowService workflowService;

	@KafkaListener(id = "${kafka.topics.trade.license.validated.id}", topics = "${kafka.topics.trade.license.validated.topic}", group = "${kafka.topics.trade.license.validated.group}")
	public void process(final HashMap<String, Object> tlRequestMap) {

		HashMap<String, Object> tlWorkflowEnrichedMap = new HashMap<>();

		final TradeLicenseRequest request = objectMapper.convertValue(tlRequestMap.get("trade_license_request"),
				TradeLicenseRequest.class);

		for (final TradeLicenseContract tradeLicense : request.getLicenses()) {

			workflowService.enrichWorkflow(tradeLicense, request.getRequestInfo());

		}

		tlWorkflowEnrichedMap.put("trade_license_employee_enriched", request);

		messageQueueRepository.save(tlWorkflowEnrichedMap);

	}

}
