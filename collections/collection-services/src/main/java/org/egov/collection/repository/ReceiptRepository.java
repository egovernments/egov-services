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

import static java.util.Comparator.comparingLong;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.egov.collection.config.ApplicationProperties;
import org.egov.collection.model.AuditDetails;
import org.egov.collection.model.ReceiptCommonModel;
import org.egov.collection.model.ReceiptDetail;
import org.egov.collection.model.ReceiptHeader;
import org.egov.collection.model.ReceiptSearchCriteria;
import org.egov.collection.model.enums.ReceiptStatus;
import org.egov.collection.producer.CollectionProducer;
import org.egov.collection.repository.QueryBuilder.ReceiptDetailQueryBuilder;
import org.egov.collection.repository.rowmapper.ReceiptRowMapper;
import org.egov.collection.web.contract.BillDetail;
import org.egov.collection.web.contract.Receipt;
import org.egov.collection.web.contract.ReceiptReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Repository
public class ReceiptRepository {
	public static final Logger logger = LoggerFactory.getLogger(ReceiptRepository.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private CollectionProducer collectionProducer;

	@Autowired
	private ApplicationProperties applicationProperties;

	@Autowired
	private ReceiptDetailQueryBuilder receiptDetailQueryBuilder;

	@Autowired
	private ReceiptRowMapper receiptRowMapper;

	@Autowired
	private RestTemplate restTemplate;

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	public ReceiptRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate, JdbcTemplate jdbcTemplate,
			CollectionProducer collectionProducer, ApplicationProperties applicationProperties,
			ReceiptDetailQueryBuilder receiptDetailQueryBuilder, ReceiptRowMapper receiptRowMapper) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
		this.jdbcTemplate = jdbcTemplate;
		this.collectionProducer = collectionProducer;
		this.applicationProperties = applicationProperties;
		this.receiptDetailQueryBuilder = receiptDetailQueryBuilder;
		this.receiptRowMapper = receiptRowMapper;

	}

	public Receipt pushToQueue(ReceiptReq receiptReq) {
		Receipt receiptInfo = receiptReq.getReceipt();
		AuditDetails auditDetails = new AuditDetails();

		// TODO: Trigger Apportioning logic from billingservice if the
		// amountPaid is less than the totalAmount

		auditDetails.setCreatedBy(receiptReq.getRequestInfo().getUserInfo().getId());
		auditDetails.setLastModifiedBy(receiptReq.getRequestInfo().getUserInfo().getId());
		auditDetails.setCreatedDate((new Date(new java.util.Date().getTime())).getTime());
		auditDetails.setLastModifiedDate((new Date(new java.util.Date().getTime())).getTime());
		receiptInfo.setAuditDetails(auditDetails);

		try {
			collectionProducer.producer(applicationProperties.getCreateReceiptTopicName(),
					applicationProperties.getCreateReceiptTopicKey(), receiptReq);

		} catch (Exception e) {
			logger.error("Pushing to Queue FAILED! ", e.getMessage());
			return null;
		}
		return receiptInfo;
	}

	public long persistToReceiptHeader(Map<String, Object> parametersMap, Receipt receiptInfo) {
		long id = 0L;
		String query = ReceiptDetailQueryBuilder.insertReceiptHeader();
		try {
			logger.info("Inserting into receipt header");
			namedParameterJdbcTemplate.update(query, parametersMap);
		} catch (Exception e) {
			logger.error("Persisting to DB FAILED! ", e.getCause());
			return id;
		}

		String receiptHeaderIdQuery = ReceiptDetailQueryBuilder.getreceiptHeaderId();
		try {
			id = jdbcTemplate.queryForObject(
					receiptHeaderIdQuery, new Object[] { receiptInfo.getBill().getPayeeName(),
							receiptInfo.getBill().getPaidBy(), receiptInfo.getAuditDetails().getCreatedDate() },
					Long.class);
		} catch (Exception e) {
			logger.error("Couldn't fetch receiptheader entry id ", e.getCause());
			return id;
		}
		logger.info("receiptheader entry id: " + id);
		return id;
	}

	public boolean persistToReceiptDetails(Map<String, Object>[] parametersReceiptDetails, long receiptHeader) {
		boolean isInsertionSuccessful = false;
		String queryReceiptDetails = ReceiptDetailQueryBuilder.insertReceiptDetails();
		try {
			logger.info("Inserting into receipt details for receipt header record: " + receiptHeader);
			namedParameterJdbcTemplate.batchUpdate(queryReceiptDetails, parametersReceiptDetails);
		} catch (Exception e) {
			logger.error("Persisting to receiptdetails table FAILED! ", e.getCause());
			return isInsertionSuccessful;
		}
		isInsertionSuccessful = true;
		return isInsertionSuccessful;
	}

	public ReceiptCommonModel findAllReceiptsByCriteria(ReceiptSearchCriteria receiptSearchCriteria) {

		List<Object> preparedStatementValues = new ArrayList<>();
		String queryString = receiptDetailQueryBuilder.getQuery(receiptSearchCriteria, preparedStatementValues);
		List<ReceiptHeader> listOfHeadersFromDB = jdbcTemplate.query(queryString, preparedStatementValues.toArray(),
				receiptRowMapper);

		Set<ReceiptDetail> receiptDetails = new LinkedHashSet<>(0);
		for (ReceiptHeader header : listOfHeadersFromDB) {
			receiptDetails.add((ReceiptDetail) header.getReceiptDetails().toArray()[0]);
		}

		List<ReceiptHeader> uniqueReceiptheader = listOfHeadersFromDB.stream().filter(distinctByKey(p -> p.getId()))
				.collect(Collectors.toList());
		List<ReceiptDetail> uniqueReceiptDetails = receiptDetails.stream()
				.filter(accountdetail -> accountdetail.getId() != null).collect(collectingAndThen(
						toCollection(() -> new TreeSet<>(comparingLong(ReceiptDetail::getId))), ArrayList::new));
		List<ReceiptHeader> unqReceiptheader = uniqueReceiptheader.stream().map(unqheader -> unqheader.toDomainModel())
				.collect(Collectors.toList());
		return new ReceiptCommonModel(unqReceiptheader, uniqueReceiptDetails);
	}

	public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
		Map<Object, Boolean> map = new ConcurrentHashMap<>();
		return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

	public ReceiptReq cancelReceipt(ReceiptReq receiptReq) {
		List<BillDetail> details = receiptReq.getReceipt().getBill().getBillDetails();
		List<Object[]> batchArgs = new ArrayList<>();
		for (BillDetail detail : details) {
			Object[] obj = { detail.getStatus(), detail.getReasonForCancellation(), detail.getCancellationRemarks(),
					receiptReq.getRequestInfo().getUserInfo().getId(), new Date(new java.util.Date().getTime()),
					Long.valueOf(detail.getId()), detail.getTenantId() };
			batchArgs.add(obj);
		}
		jdbcTemplate.batchUpdate(receiptDetailQueryBuilder.getCancelQuery(), batchArgs);
		return receiptReq;
	}

	public Receipt pushReceiptCancelDetailsToQueue(ReceiptReq receiptReq) {
		Receipt receiptInfo = receiptReq.getReceipt();

		List<BillDetail> details = receiptReq.getReceipt().getBill().getBillDetails();
		for (BillDetail detail : details) {
			detail.setStatus(ReceiptStatus.CANCELLED.toString());
		}
		try {
			collectionProducer.producer(applicationProperties.getCancelReceiptTopicName(),
					applicationProperties.getCancelReceiptTopicKey(), receiptReq);

		} catch (Exception e) {
			logger.error("Pushing to Queue FAILED! ", e.getMessage());
			return null;
		}
		return receiptInfo;

	}
	
    public long getStateId(String receiptNumber){
    	logger.info("Updating stateId..");
    	long stateId = 0L;
    	String query = "SELECT stateid FROM egcl_receiptheader WHERE receiptnumber=?";
    	try{
    		Long id = jdbcTemplate.queryForObject(query, new Object[] {receiptNumber}, Long.class);
    		stateId = Long.valueOf(id);
    	}catch(Exception e){
    		logger.error("Couldn't fetch stateId for the receipt: "+receiptNumber);
        	return stateId;
    	}
    	logger.info("StateId obtained for receipt: "+receiptNumber+" is: "+stateId);
    	return stateId;
    }

}
