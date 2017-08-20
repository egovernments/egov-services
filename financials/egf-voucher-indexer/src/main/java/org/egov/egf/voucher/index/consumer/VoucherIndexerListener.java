package org.egov.egf.voucher.index.consumer;

import java.text.SimpleDateFormat;
import java.util.HashMap;

import org.egov.egf.voucher.index.persistence.repository.ElasticSearchRepository;
import org.egov.egf.voucher.web.contract.VoucherContract;
import org.egov.egf.voucher.web.requests.VoucherRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class VoucherIndexerListener {

	private static final String VOUCHER_OBJECT_TYPE = "voucher";

	private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

	@Autowired
	private ElasticSearchRepository elasticSearchRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@KafkaListener(id = "${kafka.topics.egf.voucher.completed.id}", topics = {
			"${kafka.topics.egf.voucher.completed.topic}" }, group = "${kafka.topics.egf.voucher.completed.group}")
	public void listen(final HashMap<String, Object> voucherRequestMap) {

		if (voucherRequestMap.get("voucher_persisted") != null) {

			final VoucherRequest request = objectMapper.convertValue(voucherRequestMap.get("voucher_persisted"),
					VoucherRequest.class);

			if (request.getVouchers() != null && !request.getVouchers().isEmpty())
				for (final VoucherContract voucher : request.getVouchers()) {
					final HashMap<String, Object> indexObj = getVoucherContractIndexObject(voucher);
					elasticSearchRepository.index(VOUCHER_OBJECT_TYPE, voucher.getTenantId() + "-" + voucher.getName(),
							indexObj);
				}
		}

	}

	private HashMap<String, Object> getVoucherContractIndexObject(final VoucherContract voucherContract) {

		final HashMap<String, Object> indexObj = new HashMap<String, Object>();
		indexObj.put("id", voucherContract.getId());
		indexObj.put("type", voucherContract.getType());
		indexObj.put("name", voucherContract.getName());
		indexObj.put("description", voucherContract.getDescription());
		indexObj.put("voucherNumber", voucherContract.getVoucherNumber());
		indexObj.put("originalVoucherNumber", voucherContract.getOriginalVoucherNumber());
		indexObj.put("refVoucherNumber", voucherContract.getRefVoucherNumber());
		indexObj.put("moduleName", voucherContract.getModuleName());
		indexObj.put("billNumber", voucherContract.getBillNumber());
		indexObj.put("status", voucherContract.getStatus());
		indexObj.put("fund", voucherContract.getFund());
		indexObj.put("function", voucherContract.getFunction());
		indexObj.put("fundsource", voucherContract.getFundsource());
		indexObj.put("scheme", voucherContract.getScheme());
		indexObj.put("subScheme", voucherContract.getSubScheme());
		indexObj.put("functionary", voucherContract.getFunctionary());
		indexObj.put("division", voucherContract.getDivision());
		indexObj.put("department", voucherContract.getDepartment());
		indexObj.put("sourcePath", voucherContract.getSourcePath());
		indexObj.put("budgetCheckRequired", voucherContract.getBudgetCheckRequired());
		indexObj.put("budgetAppropriationNo", voucherContract.getBudgetAppropriationNo());
		indexObj.put("ledgers", voucherContract.getLedgers());
		indexObj.put("tenantId", voucherContract.getTenantId());
		indexObj.put("createdBy", voucherContract.getCreatedBy());
		indexObj.put("lastModifiedBy", voucherContract.getLastModifiedBy());

		if (voucherContract.getVoucherDate() != null)
			indexObj.put("voucherDate", formatter.format(voucherContract.getVoucherDate()));
		else
			indexObj.put("voucherDate", null);

		if (voucherContract.getCreatedDate() != null)
			indexObj.put("createdDate", formatter.format(voucherContract.getCreatedDate()));
		else
			indexObj.put("createdDate", null);
		if (voucherContract.getLastModifiedDate() != null)
			indexObj.put("lastModifiedDate", formatter.format(voucherContract.getLastModifiedDate()));
		else
			indexObj.put("lastModifiedDate", null);

		return indexObj;
	}

}
