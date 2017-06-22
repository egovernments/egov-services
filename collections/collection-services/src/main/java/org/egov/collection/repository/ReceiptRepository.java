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

package org.egov.collection.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.collection.config.ApplicationProperties;
import org.egov.collection.model.ReceiptHeader;
import org.egov.collection.model.ReceiptSearchCriteria;
import org.egov.collection.producer.CollectionProducer;
import org.egov.collection.repository.QueryBuilder.ReceiptDetailQueryBuilder;
import org.egov.collection.repository.rowmapper.ReceiptRowMapper;
import org.egov.collection.web.contract.Receipt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class ReceiptRepository {
	public static final Logger logger = LoggerFactory
			.getLogger(ReceiptRepository.class);
	
	@Autowired
	private CollectionProducer collectionProducer;
	
	@Autowired
	private ApplicationProperties applicationProperties;
	

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	private static final String SELECT_RECEIPTHEADER_RCPT_NO = "SELECT * FROM egcl_receiptheader WHERE receiptnumber = :receiptnumber";

	public ReceiptRepository(
			NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	public List<ReceiptHeader> find(ReceiptSearchCriteria receiptSearchCriteria) {

		return getReceiptForGivenReceiptNumber(receiptSearchCriteria);
	}

	private List<ReceiptHeader> getReceiptForGivenReceiptNumber(
			ReceiptSearchCriteria receiptSearchCriteria) {
		final Map<String, Object> parametersMap = new HashMap<>();
		parametersMap.put("receiptnumber",
				receiptSearchCriteria.getReceiptNumber());
		return namedParameterJdbcTemplate.query(
				SELECT_RECEIPTHEADER_RCPT_NO, parametersMap,
				new ReceiptRowMapper());
	}
	
	public Receipt pushToQueue(Receipt receiptInfo) {
		logger.info("Pushing recieptdetail to kafka queue");
		try{
			collectionProducer.producer(applicationProperties.getCreateReceiptTopicName(),
					applicationProperties.getCreateReceiptTopicKey(), receiptInfo);
		}catch(Exception e){
			logger.error("Pushing to Queue FAILED! ", e.getMessage());
			return null;
		}
		return receiptInfo;
	}
	
	public boolean persistCreateRequest(Receipt receiptInfo){
		boolean isInsertionSuccessfull = false;
		String query = ReceiptDetailQueryBuilder.insertReceiptDetails();
		final Map<String, Object> parametersMap = new HashMap<>();
		
		try{
			namedParameterJdbcTemplate.update(query, parametersMap);
		}catch(Exception e){
			logger.error("Persisting to DB FAILED! ",e.getMessage());
			return isInsertionSuccessfull;

		}
		isInsertionSuccessfull= true;
		return isInsertionSuccessfull;
	}

}
