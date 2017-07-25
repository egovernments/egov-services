package org.egov.egf.master.persistence.queue;

import java.util.Map;

import org.egov.egf.master.domain.model.Bank;
import org.egov.egf.master.domain.service.BankService;
import org.egov.egf.master.web.contract.BankContract;
import org.egov.egf.master.web.requests.BankRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FinancialMastersListener {

	@Value("${kafka.topics.egf.masters.completed.topic}")
	private String completedTopic;
	
	@Value("${kafka.topics.egf.masters.fund.completed.key}")
	private String fundCompletedKey;
	
	@Value("${kafka.topics.egf.masters.bank.completed.key}")
	private String bankCompletedKey;

	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private FinancialProducer financialProducer;
	
	@Autowired 
	private BankService bankService;

	

	@KafkaListener(id = "${kafka.topics.egf.masters.validated.id}", topics = "${kafka.topics.egf.masters.validated.topic}", group = "${kafka.topics.egf.masters.validated.group}")
	public void process(Map<String, Object> mastersMap) {
    //implement the details here

		if (mastersMap.get("bank_create") != null) {
			BankRequest request = objectMapper.convertValue(mastersMap.get("bank_create"),BankRequest.class);
			ModelMapper mapper = new ModelMapper();
			for (BankContract bankContract : request.getBanks()) {
				Bank domain = mapper.map(bankContract, Bank.class);
				bankService.save(domain);
			}

			mastersMap.clear();
			mastersMap.put("bank_persisted", request);
			financialProducer.sendMessage(completedTopic, bankCompletedKey, mastersMap);
		}
		if (mastersMap.get("bank_update") != null) {

			BankRequest request = objectMapper.convertValue(mastersMap.get("bank_update"),BankRequest.class);

			ModelMapper mapper = new ModelMapper();
			for (BankContract bankContract : request.getBanks()) {
				Bank domain = mapper.map(bankContract, Bank.class);
				bankService.update(domain);
			}
			mastersMap.clear();
			mastersMap.put("bank_persisted", request);
			financialProducer.sendMessage(completedTopic, bankCompletedKey, mastersMap);
		}



 
	}

}
