package org.egov.egf.master.persistence.queue;

import java.util.HashMap;

import org.egov.common.web.contract.CommonRequest;
import org.egov.egf.master.domain.model.Bank;
import org.egov.egf.master.domain.model.Function;
import org.egov.egf.master.domain.model.Fund;
import org.egov.egf.master.domain.repository.BankRepository;
import org.egov.egf.master.domain.repository.FunctionRepository;
import org.egov.egf.master.domain.repository.FundRepository;
import org.egov.egf.master.web.contract.BankContract;
import org.egov.egf.master.web.contract.FunctionContract;
import org.egov.egf.master.web.contract.FundContract;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FinancialMastersListener {

	@Value("${kafka.topics.egf.masters.completed.topic}")
	private String completedTopic;

	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private FinancialProducer financialProducer;

	@Autowired
	private FundRepository fundRepository;
	
	@Autowired
	private BankRepository bankRepository;
	
	@Autowired
	private FunctionRepository functionRepository;

	@KafkaListener(id = "${kafka.topics.egf.masters.validated.id}", topics = "${kafka.topics.egf.masters.validated.topic}", group = "${kafka.topics.egf.masters.validated.group}")
	public void process(HashMap<String, CommonRequest<?>> mastersMap) {

		// CommonRequest request =(CommonRequest)
		// mastersMap.values().toArray()[0];
		System.out.println("consuming topic" + mastersMap);

		if (mastersMap.get("fundcontract_create") != null) {

			CommonRequest<FundContract> request = objectMapper.convertValue(mastersMap.get("fundcontract_create"),
					new TypeReference<CommonRequest<FundContract>>() {
					});

			System.out.println(request.getRequestInfo().getAction());

			ModelMapper mapper = new ModelMapper();
			for (FundContract fundContract : request.getData()) {
				Fund domain = mapper.map(fundContract, Fund.class);
				fundRepository.save(domain);
			}

			mastersMap.clear();
			mastersMap.put("FundContract_Completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}
		
		if (mastersMap.get("bankcontract_create") != null) {

			CommonRequest<BankContract> request = objectMapper.convertValue(mastersMap.get("bankcontract_create"),
					new TypeReference<CommonRequest<BankContract>>() {
					});

			System.out.println(request.getRequestInfo().getAction());

			ModelMapper mapper = new ModelMapper();
			for (BankContract bankContract : request.getData()) {
				Bank domain = mapper.map(bankContract, Bank.class);
				bankRepository.save(domain);
			}

			mastersMap.clear();
			mastersMap.put("bankcontract_completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}
		if (mastersMap.get("bankcontract_update") != null) {

			CommonRequest<BankContract> request = objectMapper.convertValue(mastersMap.get("bankcontract_update"),
					new TypeReference<CommonRequest<BankContract>>() {
					});

			System.out.println(request.getRequestInfo().getAction());

			ModelMapper mapper = new ModelMapper();
			for (BankContract bankContract : request.getData()) {
				Bank domain = mapper.map(bankContract, Bank.class);
				bankRepository.update(domain);
			}

			mastersMap.clear();
			mastersMap.put("bankcontract_completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}
		
		if (mastersMap.get("functioncontract_create") != null) {

			CommonRequest<FunctionContract> request = objectMapper.convertValue(mastersMap.get("functioncontract_create"),
					new TypeReference<CommonRequest<FunctionContract>>() {
					});

			System.out.println(request.getRequestInfo().getAction());

			ModelMapper mapper = new ModelMapper();
			for (FunctionContract functionContract : request.getData()) {
				Function domain = mapper.map(functionContract, Function.class);
				functionRepository.save(domain);
			}

			mastersMap.clear();
			mastersMap.put("functioncontract_completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}
		if (mastersMap.get("functioncontract_update") != null) {

			CommonRequest<FunctionContract> request = objectMapper.convertValue(mastersMap.get("functioncontract_update"),
					new TypeReference<CommonRequest<FunctionContract>>() {
					});

			System.out.println(request.getRequestInfo().getAction());

			ModelMapper mapper = new ModelMapper();
			for (FunctionContract functionContract : request.getData()) {
				Function domain = mapper.map(functionContract, Function.class);
				functionRepository.update(domain);
			}

			mastersMap.clear();
			mastersMap.put("functioncontract_completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}

	}

}
