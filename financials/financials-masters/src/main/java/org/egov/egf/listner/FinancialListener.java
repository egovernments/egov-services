package org.egov.egf.listner;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.egf.persistence.entity.Bank;
import org.egov.egf.persistence.queue.contract.BankContractRequest;
import org.egov.egf.persistence.service.BankService;
import org.egov.egf.producer.FinancialProducer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

public class FinancialListener {

	@Autowired
	private BankService bankService;

	@Autowired
	private FinancialProducer financialProducer;

	@KafkaListener(id = "${kafka.topics.egf.master.id}", topics = "${kafka.topics.egf.master.name}", group = "${kafka.topics.egf.master.group}")

	public void listen(ConsumerRecord<String, BankContractRequest> record) {
		System.err.println("***** received message [key " + record.key() + "] + value [" + record.value()
				+ "] from topic egf.master");
		ModelMapper model = new ModelMapper();
		BankContractRequest bankContractRequest = record.value();
		Bank bank = new Bank();
		model.map(bankContractRequest.getBanks().get(0), bank);
		bank = bankService.create(bank);
		bankContractRequest.getBanks().get(0).setId(bank.getId());
		financialProducer.sendMessage("egov.egf.master.completed", bankContractRequest);

	}

}
