package org.egov.egf.bill.persistence.queue;

import java.util.HashMap;

import org.egov.egf.bill.domain.model.BillRegister;
import org.egov.egf.bill.domain.service.BillRegisterService;
import org.egov.egf.bill.web.contract.BillRegisterContract;
import org.egov.egf.bill.web.mapper.BillRegisterMapper;
import org.egov.egf.bill.web.requests.BillRegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FinancialBillListener {

	@Value("${kafka.topics.egf.bill.completed.topic}")
	private String completedTopic;
	
	@Value("${kafka.topics.egf.bill.completed.key}")
	private String completedKey;
	
	@Value("${kafka.topics.egf.bill.register.completed.key}")
	private String billRegisterCompletedKey;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	FinancialProducer financialBillRegisterProducer;
	
	@Autowired
	BillRegisterService billRegisterService;
	
	@KafkaListener(id = "${kafka.topics.egf.bill.validated.id}", topics = "${kafka.topics.egf.bill.validated.topic}", group = "${kafka.topics.egf.bill.validated.group}")
	public void process(HashMap<String, Object> mastersMap) {

		BillRegisterMapper billRegisterMapper = new BillRegisterMapper();
		
		if (mastersMap.get("billregister_create") != null) {

			BillRegisterRequest request = objectMapper
					.convertValue(mastersMap.get("billregister_create"), BillRegisterRequest.class);

			for (BillRegisterContract billRegisterContract : request.getBillRegisters()) {
				BillRegister domain = billRegisterMapper.toDomain(billRegisterContract);
				billRegisterService.save(domain);
			}

			mastersMap.clear();
			mastersMap.put("billregister_persisted", request);
			financialBillRegisterProducer.sendMessage(completedTopic, billRegisterCompletedKey, mastersMap);
		}
	}
}
