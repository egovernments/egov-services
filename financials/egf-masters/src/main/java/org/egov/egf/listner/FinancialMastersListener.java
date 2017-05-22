package org.egov.egf.listner;

import java.util.HashMap;

import org.egov.egf.persistence.queue.contract.BankAccountContractResponse;
import org.egov.egf.persistence.queue.contract.BankBranchContractResponse;
import org.egov.egf.persistence.queue.contract.BankContractResponse;
import org.egov.egf.persistence.service.BankAccountService;
import org.egov.egf.persistence.service.BankBranchService;
import org.egov.egf.persistence.service.BankService;
import org.egov.egf.producer.FinancialProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class FinancialMastersListener {

	@Value("${kafka.topics.egf.masters.completed.topic}")
	private String completedTopic;

	@Value("${kafka.topics.egf.masters.bank.completed.key}")
	private String bankCompletedKey;

	@Value("${kafka.topics.egf.masters.bankbranch.completed.key}")
	private String bankBranchCompletedKey;

	@Value("${kafka.topics.egf.masters.bankaccount.completed.key}")
	private String bankAccountCompletedKey;

	private BankService bankService;

	private BankBranchService bankBranchService;

	private BankAccountService bankAccountService;

	@Autowired
	private FinancialProducer financialProducer;

	@Autowired
	public FinancialMastersListener(BankService bankService, BankBranchService bankBranchService,
			BankAccountService bankAccountService) {
		this.bankService = bankService;
		this.bankBranchService = bankBranchService;
		this.bankAccountService = bankAccountService;
	}

	@KafkaListener(id = "${kafka.topics.egf.masters.validated.id}", topics = "${kafka.topics.egf.masters.validated.topic}", group = "${kafka.topics.egf.masters.validated.group}")
	public void process(HashMap<String, Object> financialContractRequestMap) {

		if (financialContractRequestMap.get("BankCreate") != null) {
			BankContractResponse bankContractResponse = bankService.create(financialContractRequestMap);
			HashMap<String, Object> bankContractResponseMap = new HashMap<String, Object>();
			bankContractResponseMap.put("Bank", bankContractResponse);
			financialProducer.sendMessage(completedTopic, bankCompletedKey, bankContractResponseMap);
		}

		if (financialContractRequestMap.get("BankUpdate") != null) {
			BankContractResponse bankContractResponse = bankService.update(financialContractRequestMap);
			HashMap<String, Object> bankContractResponseMap = new HashMap<String, Object>();
			bankContractResponseMap.put("Bank", bankContractResponse);
			financialProducer.sendMessage(completedTopic, bankCompletedKey, bankContractResponseMap);
		}

		if (financialContractRequestMap.get("BankBranchCreate") != null) {
			BankBranchContractResponse bankBranchContractResponse = bankBranchService
					.create(financialContractRequestMap);
			HashMap<String, Object> bankBranchContractResponseMap = new HashMap<String, Object>();
			bankBranchContractResponseMap.put("BankBranch", bankBranchContractResponse);
			financialProducer.sendMessage(completedTopic, bankBranchCompletedKey, bankBranchContractResponseMap);
		}

		if (financialContractRequestMap.get("BankBranchUpdate") != null) {
			BankBranchContractResponse bankBranchContractResponse = bankBranchService
					.update(financialContractRequestMap);
			HashMap<String, Object> bankBranchContractResponseMap = new HashMap<String, Object>();
			bankBranchContractResponseMap.put("BankBranch", bankBranchContractResponse);
			financialProducer.sendMessage(completedTopic, bankBranchCompletedKey, bankBranchContractResponseMap);
		}

		if (financialContractRequestMap.get("BankAccountCreate") != null) {
			BankAccountContractResponse bankAccountContractResponse = bankAccountService
					.create(financialContractRequestMap);
			HashMap<String, Object> bankAccountContractResponseMap = new HashMap<String, Object>();
			bankAccountContractResponseMap.put("BankAccount", bankAccountContractResponse);
			financialProducer.sendMessage(completedTopic, bankAccountCompletedKey, bankAccountContractResponseMap);
		}

		if (financialContractRequestMap.get("BankAccountUpdate") != null) {
			BankAccountContractResponse bankAccountContractResponse = bankAccountService
					.update(financialContractRequestMap);
			HashMap<String, Object> bankAccountContractResponseMap = new HashMap<String, Object>();
			bankAccountContractResponseMap.put("BankAccount", bankAccountContractResponse);
			financialProducer.sendMessage(completedTopic, bankAccountCompletedKey, bankAccountContractResponseMap);
		}

	}

}
