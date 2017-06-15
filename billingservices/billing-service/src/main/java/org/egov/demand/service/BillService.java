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

package org.egov.demand.service;

import java.util.List;

import org.egov.demand.config.ApplicationProperties;
import org.egov.demand.model.Bill;
import org.egov.demand.web.contract.BillRequest;
import org.egov.demand.web.contract.BillResponse;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class BillService {
	
	private static final Logger logger = LoggerFactory.getLogger(BillService.class);
	
	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private ApplicationProperties applicationProperties;
	
	public BillResponse createAsync(BillRequest billRequest){
		
		try {
			kafkaTemplate.send(applicationProperties.getCreateBillTopic(),applicationProperties.getCreateBillTopicKey(),
								objectMapper.writeValueAsString(billRequest));
		} catch (Exception e) {
			logger.info("BillService createAsync:"+e);
			throw new RuntimeException(e);
		}
		return getBillResponse(billRequest.getBillInfos());
	}
	
	public BillResponse getBillResponse(List<Bill> bills){
		BillResponse billResponse = new BillResponse();
		billResponse.setBillInfos(bills);
		return billResponse;
	}

}
