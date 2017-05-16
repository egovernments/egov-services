package org.egov.egf.listner;

import java.util.HashMap;

import org.egov.egf.persistence.queue.contract.BankBranchContractResponse;
import org.egov.egf.persistence.queue.contract.BankContractResponse;
import org.egov.egf.persistence.service.BankBranchService;
import org.egov.egf.persistence.service.BankService;
import org.egov.egf.producer.FinancialProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Service;

@Service
public class FinancialMastersListener {

	@Value("${kafka.topics.egf.master.completed.topic}")
	private String completedTopic;

	@Value("${kafka.topics.egf.master.bank.completed.key}")
	private String bankCompletedKey;

	@Value("${kafka.topics.egf.master.bankbranch.completed.key}")
	private String bankBranchCompletedKey;

	private BankService bankService;
	private BankBranchService bankBranchService;

	@Autowired
	private FinancialProducer financialProducer;

	@Autowired
	public FinancialMastersListener(BankService bankService, BankBranchService bankBranchService) {
		this.bankService = bankService;
		this.bankBranchService = bankBranchService;
	}

	@KafkaListener(id = "${kafka.topics.egf.master.bank.validated.id}", topicPartitions = {
			@TopicPartition(topic = "${kafka.topics.egf.master.validated.topic}", partitions = {
					"0" }) }, group = "${kafka.topics.egf.master.validated.group}")
	public void listenPartition0(HashMap<String, Object> financialContractRequestMap) {
		if (financialContractRequestMap.get("Bank") != null) {
			BankContractResponse bankContractResponse = bankService.create(financialContractRequestMap);
			financialProducer.sendMessage(completedTopic, bankCompletedKey, bankContractResponse);
		}
	}

	@KafkaListener(id = "${kafka.topics.egf.master.bankbranch.validated.id}", topicPartitions = {
			@TopicPartition(topic = "${kafka.topics.egf.master.validated.topic}", partitions = {
					"1" }) }, group = "${kafka.topics.egf.master.validated.group}")
	public void listenPartition1(HashMap<String, Object> financialContractRequestMap) {
		if (financialContractRequestMap.get("BankBranch") != null) {
			BankBranchContractResponse bankBranchContractResponse = bankBranchService
					.create(financialContractRequestMap);
			financialProducer.sendMessage(completedTopic, bankBranchCompletedKey, bankBranchContractResponse);
		}
	}

}
