package org.egov.egf.instrument.index.consumer;

import java.text.SimpleDateFormat;
import java.util.HashMap;

import org.egov.egf.instrument.index.persistence.repository.ElasticSearchRepository;
import org.egov.egf.instrument.web.contract.InstrumentAccountCodeContract;
import org.egov.egf.instrument.web.contract.InstrumentContract;
import org.egov.egf.instrument.web.contract.SurrenderReasonContract;
import org.egov.egf.instrument.web.requests.InstrumentAccountCodeRequest;
import org.egov.egf.instrument.web.requests.InstrumentRequest;
import org.egov.egf.instrument.web.requests.SurrenderReasonRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class InstrumentIndexerListener {

	private static final String INSTRUMENT_OBJECT_TYPE = "instrument";
	private static final String INSTRUMENTACCOUNTCODE_OBJECT_TYPE = "instrumentaccountcode";
	private static final String SURRENDERREASON_OBJECT_TYPE = "surrenderreason";

	private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

	@Autowired
	private ElasticSearchRepository elasticSearchRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@KafkaListener(id = "${kafka.topics.egf.instrument.completed.id}", topics = {
			"${kafka.topics.egf.instrument.completed.topic}" }, group = "${kafka.topics.egf.instrument.completed.group}")
	public void listen(final HashMap<String, Object> instrumentRequestMap) {

		if (instrumentRequestMap.get("instrumentaccountcode_persisted") != null) {

			final InstrumentAccountCodeRequest request = objectMapper.convertValue(
					instrumentRequestMap.get("instrumentaccountcode_persisted"), InstrumentAccountCodeRequest.class);

			if (request.getInstrumentAccountCodes() != null && !request.getInstrumentAccountCodes().isEmpty())
				for (final InstrumentAccountCodeContract instrumentAccountCodeContract : request
						.getInstrumentAccountCodes()) {
					final HashMap<String, Object> indexObj = getInstrumentAccountContractIndexObject(
							instrumentAccountCodeContract);
					elasticSearchRepository.index(INSTRUMENTACCOUNTCODE_OBJECT_TYPE,
							instrumentAccountCodeContract.getTenantId() + "-"
									+ instrumentAccountCodeContract.getInstrumentType().getId(),
							indexObj);
				}
		}

		if (instrumentRequestMap.get("instrument_persisted") != null) {

			final InstrumentRequest request = objectMapper
					.convertValue(instrumentRequestMap.get("instrument_persisted"), InstrumentRequest.class);

			if (request.getInstruments() != null && !request.getInstruments().isEmpty())
				for (final InstrumentContract instrumentContract : request.getInstruments()) {
					final HashMap<String, Object> indexObj = getInstrumentContractIndexObject(instrumentContract);
					elasticSearchRepository.index(INSTRUMENT_OBJECT_TYPE,
							instrumentContract.getTenantId() + "-" + instrumentContract.getTransactionNumber(),
							indexObj);
				}
		}

		if (instrumentRequestMap.get("surrenderreason_persisted") != null) {

			final SurrenderReasonRequest request = objectMapper
					.convertValue(instrumentRequestMap.get("surrenderreason_persisted"), SurrenderReasonRequest.class);

			if (request.getSurrenderReasons() != null && !request.getSurrenderReasons().isEmpty())
				for (final SurrenderReasonContract surrenderReasonContract : request.getSurrenderReasons()) {
					final HashMap<String, Object> indexObj = getSurrenderReasonContractIndexObject(
							surrenderReasonContract);
					elasticSearchRepository.index(SURRENDERREASON_OBJECT_TYPE,
							surrenderReasonContract.getTenantId() + "-" + surrenderReasonContract.getName(), indexObj);
				}
		}
	}

	private HashMap<String, Object> getInstrumentAccountContractIndexObject(
			final InstrumentAccountCodeContract instrumentAccountCodeContract) {

		final HashMap<String, Object> indexObj = new HashMap<String, Object>();
		indexObj.put("id", instrumentAccountCodeContract.getId());
		indexObj.put("instrumentType", instrumentAccountCodeContract.getInstrumentType());
		indexObj.put("accountCode", instrumentAccountCodeContract.getAccountCode());
		indexObj.put("tenantId", instrumentAccountCodeContract.getTenantId());
		indexObj.put("createdBy", instrumentAccountCodeContract.getCreatedBy());
		indexObj.put("lastModifiedBy", instrumentAccountCodeContract.getLastModifiedBy());

		if (instrumentAccountCodeContract.getCreatedDate() != null)
			indexObj.put("createdDate", formatter.format(instrumentAccountCodeContract.getCreatedDate()));
		else
			indexObj.put("createdDate", null);
		if (instrumentAccountCodeContract.getLastModifiedDate() != null)
			indexObj.put("lastModifiedDate", formatter.format(instrumentAccountCodeContract.getLastModifiedDate()));
		else
			indexObj.put("lastModifiedDate", null);

		return indexObj;
	}

	private HashMap<String, Object> getInstrumentContractIndexObject(final InstrumentContract instrumentContract) {

		final HashMap<String, Object> indexObj = new HashMap<String, Object>();
		indexObj.put("id", instrumentContract.getId());
		indexObj.put("transactionNumber", instrumentContract.getTransactionNumber());
		if (instrumentContract.getTransactionDate() != null)
			indexObj.put("transactionDate", formatter.format(instrumentContract.getTransactionDate()));
		else
			indexObj.put("transactionDate", null);
		indexObj.put("amount", instrumentContract.getAmount());
		indexObj.put("instrumentType", instrumentContract.getInstrumentType());
		indexObj.put("bank", instrumentContract.getBank());
		indexObj.put("branchName", instrumentContract.getBranchName());
		indexObj.put("bankAccount", instrumentContract.getBankAccount());
		indexObj.put("financialStatus", instrumentContract.getFinancialStatus());
		indexObj.put("transactionType", instrumentContract.getTransactionType());
		indexObj.put("payee", instrumentContract.getPayee());
		indexObj.put("drawer", instrumentContract.getDrawer());
		indexObj.put("surrenderReason", instrumentContract.getSurrenderReason());
		indexObj.put("serialNo", instrumentContract.getSerialNo());
		indexObj.put("instrumentVouchers", instrumentContract.getInstrumentVouchers());

		indexObj.put("createdBy", instrumentContract.getCreatedBy());
		indexObj.put("lastModifiedBy", instrumentContract.getLastModifiedBy());
		indexObj.put("tenantId", instrumentContract.getTenantId());
		if (instrumentContract.getCreatedDate() != null)
			indexObj.put("createdDate", formatter.format(instrumentContract.getCreatedDate()));
		else
			indexObj.put("createdDate", null);
		if (instrumentContract.getLastModifiedDate() != null)
			indexObj.put("lastModifiedDate", formatter.format(instrumentContract.getLastModifiedDate()));
		else
			indexObj.put("lastModifiedDate", null);

		return indexObj;
	}

	private HashMap<String, Object> getSurrenderReasonContractIndexObject(
			final SurrenderReasonContract surrenderReasonContract) {

		final HashMap<String, Object> indexObj = new HashMap<String, Object>();
		indexObj.put("id", surrenderReasonContract.getId());
		indexObj.put("name", surrenderReasonContract.getName());
		indexObj.put("description", surrenderReasonContract.getDescription());
		indexObj.put("createdBy", surrenderReasonContract.getCreatedBy());
		indexObj.put("lastModifiedBy", surrenderReasonContract.getLastModifiedBy());
		indexObj.put("tenantId", surrenderReasonContract.getTenantId());
		if (surrenderReasonContract.getCreatedDate() != null)
			indexObj.put("createdDate", formatter.format(surrenderReasonContract.getCreatedDate()));
		else
			indexObj.put("createdDate", null);
		if (surrenderReasonContract.getLastModifiedDate() != null)
			indexObj.put("lastModifiedDate", formatter.format(surrenderReasonContract.getLastModifiedDate()));
		else
			indexObj.put("lastModifiedDate", null);

		return indexObj;
	}
}
