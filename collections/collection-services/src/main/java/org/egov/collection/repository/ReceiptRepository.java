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

import lombok.AllArgsConstructor;

import org.egov.collection.config.ApplicationProperties;
import org.egov.collection.model.ReceiptCommonModel;
import org.egov.collection.model.ReceiptDetail;
import org.egov.collection.model.ReceiptHeader;
import org.egov.collection.model.ReceiptSearchCriteria;
import org.egov.collection.model.enums.ReceiptStatus;
import org.egov.collection.producer.CollectionProducer;
import org.egov.collection.repository.QueryBuilder.ReceiptDetailQueryBuilder;
import org.egov.collection.repository.rowmapper.ReceiptRowMapper;
import org.egov.collection.web.contract.*;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Comparator.comparingLong;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

@AllArgsConstructor
@Repository
public class ReceiptRepository {
	public static final Logger logger = LoggerFactory
			.getLogger(ReceiptRepository.class);

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
	private UserRepository userRepository;

	@Autowired
	private BusinessDetailsRepository businessDetailsRepository;

	@Autowired
	private ChartOfAccountsRepository chartOfAccountsRepository;

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	public ReceiptRepository(
			NamedParameterJdbcTemplate namedParameterJdbcTemplate,
			JdbcTemplate jdbcTemplate, CollectionProducer collectionProducer,
			ApplicationProperties applicationProperties,
			ReceiptDetailQueryBuilder receiptDetailQueryBuilder,
			ReceiptRowMapper receiptRowMapper) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
		this.jdbcTemplate = jdbcTemplate;
		this.collectionProducer = collectionProducer;
		this.applicationProperties = applicationProperties;
		this.receiptDetailQueryBuilder = receiptDetailQueryBuilder;
		this.receiptRowMapper = receiptRowMapper;

	}

	public Receipt pushToQueue(ReceiptReq receiptReq) {
		try {
			collectionProducer.producer(
					applicationProperties.getCreateReceiptTopicName(),
					applicationProperties.getCreateReceiptTopicKey(),
					receiptReq);

		} catch (Exception e) {
			logger.error("Pushing to Queue FAILED! ", e.getMessage());
			return null;
		}
		return receiptReq.getReceipt().get(0);
	}

	public void persistToReceiptHeader(Map<String, Object> parametersMap) {
		logger.info("Inserting into receipt header");
		String query = ReceiptDetailQueryBuilder.insertReceiptHeader();
		namedParameterJdbcTemplate.update(query, parametersMap);
	}

	public void persistToReceiptDetails(Map<String, Object>[] parametersReceiptDetails, long receiptHeader) {
		String queryReceiptDetails = ReceiptDetailQueryBuilder
				.insertReceiptDetails();
			namedParameterJdbcTemplate.batchUpdate(queryReceiptDetails,
					parametersReceiptDetails);
	}
	
	public boolean persistReceipt(Map<String, Object> parametersMap, 
			     Map<String, Object>[] parametersReceiptDetails, long receiptHeader, long instrumentId){
		boolean isInsertionSuccessful = false;
		try{
			persistToReceiptHeader(parametersMap);
			persistToReceiptDetails(parametersReceiptDetails, receiptHeader);
		//	persistIntoReceiptInstrument(instrumentId, receiptHeader);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Persistingreceipt FAILED! ",e.getCause());
			return isInsertionSuccessful;
		}
		isInsertionSuccessful = true;
		return isInsertionSuccessful;


	}

	public ReceiptCommonModel findAllReceiptsByCriteria(
			ReceiptSearchCriteria receiptSearchCriteria) throws ParseException {
		List<Object> preparedStatementValues = new ArrayList<>();
		String queryString = receiptDetailQueryBuilder.getQuery(
				receiptSearchCriteria, preparedStatementValues);
		List<ReceiptHeader> listOfHeadersFromDB = jdbcTemplate.query(
				queryString, preparedStatementValues.toArray(),
				receiptRowMapper);
		Set<ReceiptDetail> receiptDetails = new LinkedHashSet<>(0);
		for (ReceiptHeader header : listOfHeadersFromDB) {
			receiptDetails.add((ReceiptDetail) header.getReceiptDetails()
					.toArray()[0]);
		}

		List<ReceiptHeader> uniqueReceiptheader = listOfHeadersFromDB.stream()
				.filter(distinctByKey(p -> p.getId()))
				.collect(Collectors.toList());
		List<ReceiptDetail> uniqueReceiptDetails = receiptDetails
				.stream()
				.filter(accountdetail -> accountdetail.getId() != null)
				.collect(
						collectingAndThen(toCollection(() -> new TreeSet<>(
								comparingLong(ReceiptDetail::getId))),
								ArrayList::new));
		List<ReceiptHeader> unqReceiptheader = uniqueReceiptheader.stream()
				.map(unqheader -> unqheader.toDomainModel())
				.collect(Collectors.toList());
		return new ReceiptCommonModel(unqReceiptheader, uniqueReceiptDetails);
	}

	public static <T> Predicate<T> distinctByKey(
			Function<? super T, Object> keyExtractor) {
		Map<Object, Boolean> map = new ConcurrentHashMap<>();
		return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

	public ReceiptReq cancelReceipt(ReceiptReq receiptReq) {
		List<Receipt> receiptInfo = receiptReq.getReceipt();
		List<Bill> billInfo = new ArrayList<>();
		for (Receipt receipt : receiptInfo) {
			billInfo.addAll(receipt.getBill());
		}
		List<BillDetail> details = new ArrayList<>();
		for (Bill bill : billInfo) {
			details.addAll(bill.getBillDetails());
		}

		List<Object[]> batchArgs = new ArrayList<>();
		for (BillDetail detail : details) {
			Object[] obj = { detail.getStatus(),
					detail.getReasonForCancellation(),
					detail.getCancellationRemarks(),
					receiptReq.getRequestInfo().getUserInfo().getId(),
					new java.sql.Date(new Date().getTime()),
					Long.valueOf(detail.getId()), detail.getTenantId() };
			batchArgs.add(obj);
		}
		jdbcTemplate.batchUpdate(receiptDetailQueryBuilder.getCancelQuery(),
				batchArgs);
		return receiptReq;
	}

	public List<Receipt> pushReceiptCancelDetailsToQueue(ReceiptReq receiptReq) {
		List<Receipt> receiptInfo = receiptReq.getReceipt();
		List<Bill> billInfo = new ArrayList<>();
		for (Receipt receipt : receiptInfo) {
			billInfo.addAll(receipt.getBill());
		}
		List<BillDetail> details = new ArrayList<>();
		for (Bill bill : billInfo) {
			details.addAll(bill.getBillDetails());
		}

		for (BillDetail detail : details) {
			detail.setStatus(ReceiptStatus.CANCELLED.toString());
		}
		try {
			collectionProducer.producer(
					applicationProperties.getCancelReceiptTopicName(),
					applicationProperties.getCancelReceiptTopicKey(),
					receiptReq);

		} catch (Exception e) {
			logger.error("Pushing to Queue FAILED! ", e.getMessage());
			return null;
		}
		return receiptInfo;

	}

	public long getStateId(String receiptNumber) {
		logger.info("Updating stateId..");
		long stateId = 0L;
		String query = "SELECT stateid FROM egcl_receiptheader WHERE receiptnumber=?";
		try {
			Long id = jdbcTemplate.queryForObject(query,
					new Object[] { receiptNumber }, Long.class);
			stateId = Long.valueOf(id);
		} catch (Exception e) {
			logger.error("Couldn't fetch stateId for the receipt: "
					+ receiptNumber);
			return stateId;
		}
		logger.info("StateId obtained for receipt: " + receiptNumber + " is: "
				+ stateId);
		return stateId;
	}

	public List<User> getReceiptCreators(final RequestInfo requestInfo,
			final String tenantId) {
		String queryString = receiptDetailQueryBuilder.searchQuery();
		List<Long> receiptCreators = jdbcTemplate.queryForList(queryString,
				Long.class, new Object[] { tenantId });
		return userRepository.getUsersById(receiptCreators, requestInfo,
				tenantId);
	}

	public List<String> getReceiptStatus(final String tenantId) {
		String queryString = receiptDetailQueryBuilder.searchStatusQuery();
		List<String> statusList = jdbcTemplate.queryForList(queryString,
				String.class, new Object[] { tenantId });
		return statusList;
	}

	public void persistIntoReceiptInstrument(Long instrumentId,
			Long receiptHeaderId) {
		logger.info("Persisting into receipt Instrument");
		String queryString = receiptDetailQueryBuilder.insertInstrumentId();
		jdbcTemplate.update(queryString, new Object[] {instrumentId, receiptHeaderId});
	}

	public Boolean updateReceipt(ReceiptReq receiptRequest) {
		Receipt receipt = receiptRequest.getReceipt().get(0);
		BillDetail billDetail = receipt.getBill().get(0).getBillDetails()
				.get(0);

		String updateQuery = receiptDetailQueryBuilder.getQueryForUpdate(
				receipt.getStateId(), billDetail.getStatus(), new Long(
						billDetail.getId()), billDetail.getTenantId());
		PreparedStatementSetter pss = new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {

				int i = 1;
				if (receipt.getStateId() != null)
					ps.setLong(i++, receipt.getStateId());

				if (billDetail.getStatus() != null)
					ps.setString(i++, billDetail.getStatus());

				if (receiptRequest.getRequestInfo().getUserInfo().getId() != null)
					ps.setLong(i++, receiptRequest.getRequestInfo()
							.getUserInfo().getId());

				ps.setLong(i++, new Date().getTime());

				if (billDetail.getId() != null)
					ps.setLong(i++, new Long(billDetail.getId()));
				if (billDetail.getTenantId() != null)
					ps.setString(i++, billDetail.getTenantId());

			}
		};
		try {
			jdbcTemplate.update(updateQuery, pss);
		} catch (Exception e) {
			logger.error(
					"could not update status and stateId in db for ReceiptRequest:",
					receiptRequest);
			return false;
		}
		return true;
	}

	public void pushUpdateDetailsToQueque(ReceiptReq receiptRequest) {
		logger.info("Pushing updateReceiptDetails to queue");
		try {
			collectionProducer.producer(
					applicationProperties.getUpdateReceiptTopicName(),
					applicationProperties.getUpdateReceiptTopicKey(),
					receiptRequest);
		} catch (Exception e) {
			logger.error("Pushing To Queue Failed! ", e.getMessage());
		}
	}

	public List<BusinessDetailsRequestInfo> getBusinessDetails(
			final RequestInfo requestInfo, final String tenantId) {
		String queryString = receiptDetailQueryBuilder
				.searchBusinessDetailsQuery();
		List<String> businessDetailsList = jdbcTemplate.queryForList(
				queryString, String.class, new Object[] { tenantId });
		return businessDetailsRepository.getBusinessDetails(
				businessDetailsList, tenantId, requestInfo)
				.getBusinessDetails();
	}

	public List<ChartOfAccount> getChartOfAccounts(final String tenantId,
			final RequestInfo requestInfo) {
		String queryString = receiptDetailQueryBuilder
				.searchChartOfAccountsQuery();
		List<String> chartOfAccountsList = jdbcTemplate.queryForList(
				queryString, String.class, new Object[] { tenantId });
		return chartOfAccountsRepository.getChartOfAccounts(
				chartOfAccountsList, tenantId, requestInfo);
	}
	
	public Long getNextSeqForRcptHeader(){
		Long sequence = null;
		String queryString = receiptDetailQueryBuilder.getNextSeqForRcptHeader();
		try{
			sequence = jdbcTemplate.queryForObject(queryString, Long.class);
		}catch(Exception e){
			logger.error("Next sequence number for receiptheader couldn't be fetched", e.getCause());
		}
		return sequence;
	}

}
