package org.egov.egf.master.index.consumer;

import java.text.SimpleDateFormat;
import java.util.HashMap;

import org.egov.common.web.contract.CommonRequest;
import org.egov.egf.master.index.domain.model.RequestContext;
import org.egov.egf.master.index.persistence.repository.ElasticSearchRepository;
import org.egov.egf.master.web.contract.FundContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class IndexerListener {

	private static final String BANK_OBJECT_TYPE = "bank";
	private static final String BANKBRANCH_OBJECT_TYPE = "bankbranch";
	private static final String BANKACCOUNT_OBJECT_TYPE = "bankaccount";
	private static final String ACCOUNTCODEPURPOSE_OBJECT_TYPE = "accountcodepurpose";
	private static final String ACCOUNTDETAILKEY_OBJECT_TYPE = "accountdetailkey";
	private static final String ACCOUNTDETAILTYPE_OBJECT_TYPE = "accountdetailtype";
	private static final String ACCOUNTENTITY_OBJECT_TYPE = "accountentity";
	private static final String BUDGETGROUP_OBJECT_TYPE = "budgetgroup";
	private static final String CHARTOFACCOUNT_OBJECT_TYPE = "chartofaccount";
	private static final String CHARTOFACCOUNTDETAIL_OBJECT_TYPE = "chartofaccountdetail";
	private static final String FINANCIALYEAR_OBJECT_TYPE = "financialyear";
	private static final String FISCALPERIOD_OBJECT_TYPE = "fiscalperiod";
	private static final String FUNCTIONARY_OBJECT_TYPE = "functionary";
	private static final String FUNCTION_OBJECT_TYPE = "function";
	private static final String FUND_OBJECT_TYPE = "fund";
	private static final String FUNDSOURCE_OBJECT_TYPE = "fundsource";
	private static final String SCHEME_OBJECT_TYPE = "scheme";
	private static final String SUBSCHEME_OBJECT_TYPE = "subscheme";
	private static final String SUPPLIER_OBJECT_TYPE = "supplier";
	private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

	@Autowired
	private ElasticSearchRepository elasticSearchRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@KafkaListener(id = "${kafka.topics.egov.index.id}", topics = "${kafka.topics.egov.index.name}", group = "${kafka.topics.egov.index.group}")
	public void listen(HashMap<String, Object> financialContractRequestMap) {

		if (financialContractRequestMap.get("fundcontract_completed") != null) {

			CommonRequest<FundContract> request = objectMapper.convertValue(
					financialContractRequestMap.get("fundcontract_completed"),
					new TypeReference<CommonRequest<FundContract>>() {
					});

			if (request.getData() != null && !request.getData().isEmpty()) {
				for (FundContract fundContract : request.getData()) {
					RequestContext.setId("" + fundContract);
					elasticSearchRepository.index(FUND_OBJECT_TYPE,
							fundContract.getTenantId() + "-" + fundContract.getName(), fundContract);
				}
			}
		}

	}

}
