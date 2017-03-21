package org.egov.egf.listner;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.egov.egf.json.ObjectMapperFactory;
import org.egov.egf.persistence.entity.Bank;
import org.egov.egf.persistence.queue.contract.BankContract;
import org.egov.egf.persistence.queue.contract.BankContractRequest;
import org.egov.egf.persistence.queue.contract.BankContractResponse;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.repository.BankJpaRepository;
import org.egov.egf.persistence.service.BankService;
import org.egov.egf.producer.FinancialProducer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class FinancialMastersListener {

	private BankService bankService;

	@Autowired
	private FinancialProducer financialProducer;

	@Autowired
	public FinancialMastersListener(BankService bankService) {
		this.bankService = bankService;
	}

	/*
	 * Example message
	 * 
	 * {"Bank":{"requestInfo":{"action":null,"did":null,"msgId":null,
	 * "requesterId":null,"authToken":null,"apiId":null,"ver":null,"ts":null,
	 * "key":null,"tenantId":null},"banks":[{"createdBy":null,"createdDate":null
	 * ,"lastModifiedBy":null,"lastModifiedDate":null,"id":null,"code":"001",
	 * "name":"bank1","description":null,"active":true,"type":"bank-type1"},{
	 * "createdBy":null,"createdDate":null,"lastModifiedBy":null,
	 * "lastModifiedDate":null,"id":null,"code":"002","name":"bank2",
	 * "description":null,"active":true,"type":"bank-type2"},{"createdBy":null,
	 * "createdDate":null,"lastModifiedBy":null,"lastModifiedDate":null,"id":
	 * null,"code":"003","name":"bank3","description":null,"active":true,"type":
	 * "bank-type3"}],"bank":{"createdBy":null,"createdDate":null,
	 * "lastModifiedBy":null,"lastModifiedDate":null,"id":null,"code":null,
	 * "name":null,"description":null,"active":null,"type":null},"page":{
	 * "totalResults":null,"totalPages":null,"pageSize":20,"currentPage":null,
	 * "offSet":0}}}
	 * 
	 */
	@KafkaListener(id = "${kafka.topics.egf.master.validated.id}", topics = "${kafka.topics.egf.master.validated.name}", group = "${kafka.topics.egf.master.validated.group}")
	public void process(HashMap<String, Object> financialContractRequestMap) {
		if (financialContractRequestMap.get("Bank") != null) {
			BankContractResponse bankContractResponse = bankService.create(financialContractRequestMap);
			financialProducer.sendMessage("egov.egf.master.completed", bankContractResponse);
		}

	}

}
