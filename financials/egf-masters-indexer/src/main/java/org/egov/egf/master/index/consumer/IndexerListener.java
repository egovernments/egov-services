package org.egov.egf.master.index.consumer;

import java.util.HashMap;

import org.egov.egf.master.index.domain.model.RequestContext;
import org.egov.egf.master.index.json.ObjectMapperFactory;
import org.egov.egf.master.index.persistence.queue.contract.BankAccountContract;
import org.egov.egf.master.index.persistence.queue.contract.BankAccountContractResponse;
import org.egov.egf.master.index.persistence.queue.contract.BankBranchContract;
import org.egov.egf.master.index.persistence.queue.contract.BankBranchContractResponse;
import org.egov.egf.master.index.persistence.queue.contract.BankContract;
import org.egov.egf.master.index.persistence.queue.contract.BankContractResponse;
import org.egov.egf.master.index.persistence.repository.ElasticSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class IndexerListener {

	private static final String BANK_OBJECT_TYPE = "bank";
	private static final String BANKBRANCH_OBJECT_TYPE = "bankbranch";
	private static final String BANKACCOUNT_OBJECT_TYPE = "bankaccount";

	@Autowired
	private ElasticSearchRepository elasticSearchRepository;

	@KafkaListener(id = "${kafka.topics.egov.index.id}", topics = "${kafka.topics.egov.index.name}", group = "${kafka.topics.egov.index.group}")
	public void listen(HashMap<String, Object> financialContractRequestMap) {

		if (financialContractRequestMap.get("Bank") != null) {

			BankContractResponse bankContractResponse;

			bankContractResponse = ObjectMapperFactory.create().convertValue(financialContractRequestMap.get("Bank"),
					BankContractResponse.class);

			if (bankContractResponse.getBanks() != null && !bankContractResponse.getBanks().isEmpty()) {
				for (BankContract bankContract : bankContractResponse.getBanks()) {
					RequestContext.setId("" + bankContract);
					elasticSearchRepository.index(BANK_OBJECT_TYPE,
							bankContract.getTenantId() + "" + bankContract.getCode(), bankContract);
				}
			} else if (bankContractResponse.getBank() != null) {
				RequestContext.setId("" + bankContractResponse.getBank());
				elasticSearchRepository.index(BANK_OBJECT_TYPE,
						bankContractResponse.getBank().getTenantId() + "" + bankContractResponse.getBank().getCode(),
						bankContractResponse.getBank());
			}
		}

		if (financialContractRequestMap.get("BankBranch") != null) {

			BankBranchContractResponse bankBranchContractResponse;

			bankBranchContractResponse = ObjectMapperFactory.create()
					.convertValue(financialContractRequestMap.get("BankBranch"), BankBranchContractResponse.class);

			if (bankBranchContractResponse.getBankBranches() != null
					&& !bankBranchContractResponse.getBankBranches().isEmpty()) {
				for (BankBranchContract bankBranchContract : bankBranchContractResponse.getBankBranches()) {
					RequestContext.setId("" + bankBranchContract);
					elasticSearchRepository.index(BANKBRANCH_OBJECT_TYPE,
							bankBranchContract.getTenantId() + "" + bankBranchContract.getCode(), bankBranchContract);
				}
			} else if (bankBranchContractResponse.getBankBranch() != null) {
				RequestContext.setId("" + bankBranchContractResponse.getBankBranch());
				elasticSearchRepository.index(BANKBRANCH_OBJECT_TYPE,
						bankBranchContractResponse.getBankBranch().getTenantId() + ""
								+ bankBranchContractResponse.getBankBranch().getCode(),
						bankBranchContractResponse.getBankBranch());
			}
		}

		if (financialContractRequestMap.get("BankAccount") != null) {

			BankAccountContractResponse bankAccountContractResponse;

			bankAccountContractResponse = ObjectMapperFactory.create()
					.convertValue(financialContractRequestMap.get("BankAccount"), BankAccountContractResponse.class);

			if (bankAccountContractResponse.getBankAccounts() != null
					&& !bankAccountContractResponse.getBankAccounts().isEmpty()) {
				for (BankAccountContract bankAccountContract : bankAccountContractResponse.getBankAccounts()) {
					RequestContext.setId("" + bankAccountContract);
					elasticSearchRepository.index(BANKACCOUNT_OBJECT_TYPE,
							bankAccountContract.getTenantId() + "" + bankAccountContract.getAccountNumber(),
							bankAccountContract);
				}
			} else if (bankAccountContractResponse.getBankAccount() != null) {
				RequestContext.setId("" + bankAccountContractResponse.getBankAccount());
				elasticSearchRepository.index(BANKACCOUNT_OBJECT_TYPE,
						bankAccountContractResponse.getBankAccount().getTenantId() + ""
								+ bankAccountContractResponse.getBankAccount().getAccountNumber(),
						bankAccountContractResponse.getBankAccount());
			}
		}
	}

}
