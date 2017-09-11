package org.egov.egf.bill.persistence.queue;

import java.util.HashMap;

import org.egov.egf.bill.domain.model.BillRegister;
import org.egov.egf.bill.domain.model.Checklist;
import org.egov.egf.bill.domain.service.BillRegisterService;
import org.egov.egf.bill.domain.service.ChecklistService;
import org.egov.egf.bill.web.contract.BillRegisterContract;
import org.egov.egf.bill.web.contract.ChecklistContract;
import org.egov.egf.bill.web.requests.BillRegisterRequest;
import org.egov.egf.bill.web.requests.ChecklistRequest;
import org.modelmapper.ModelMapper;
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
	
	@Value("${kafka.topics.egf.bill.bill.register.completed.key}")
	private String billRegisterCompletedKey;
	
	@Value("${kafka.topics.egf.bill.bill.checklist.completed.key}")
	private String checklistCompletedKey;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private ObjectMapperFactory objectMapperFactory;
	
	@Autowired
	FinancialProducer financialBillRegisterProducer;
	
	@Autowired
	BillRegisterService billRegisterService;
	
	@Autowired
	ChecklistService checklistService;
	
	@KafkaListener(id = "${kafka.topics.egf.bill.validated.id}", topics = "${kafka.topics.egf.bill.validated.topic}", group = "${kafka.topics.egf.bill.validated.group}")
	public void process(HashMap<String, Object> mastersMap) {

		objectMapperFactory = new ObjectMapperFactory(objectMapper);
		ModelMapper mapper = new ModelMapper();
		
		if (mastersMap.get("billregister_create") != null) {

			final BillRegisterRequest request = objectMapperFactory.create().convertValue(mastersMap.get("billregister_create"),
					BillRegisterRequest.class);

			for (BillRegisterContract billRegisterContract : request.getBillRegisters()) {
				final BillRegister domain = mapper.map(billRegisterContract, BillRegister.class);
				BillRegister billRegister = billRegisterService.save(domain);
				billRegisterContract = mapper.map(billRegister, BillRegisterContract.class);
			}

			mastersMap.clear();
			mastersMap.put("billregister_persisted", request);
			financialBillRegisterProducer.sendMessage(completedTopic, billRegisterCompletedKey, mastersMap);
		}
		
		if (mastersMap.get("billregister_update") != null) {

			final BillRegisterRequest request = objectMapperFactory.create().convertValue(mastersMap.get("billregister_update"),
					BillRegisterRequest.class);

			for (final BillRegisterContract billRegisterContract : request.getBillRegisters()) {
				final BillRegister domain = mapper.map(billRegisterContract, BillRegister.class);
				billRegisterService.update(domain);
			}

			mastersMap.clear();
			mastersMap.put("billregister_persisted", request);
			financialBillRegisterProducer.sendMessage(completedTopic, billRegisterCompletedKey, mastersMap);
		}
		
		if (mastersMap.get("checklist_create") != null) {

			final ChecklistRequest request = objectMapperFactory.create().convertValue(mastersMap.get("checklist_create"),
					ChecklistRequest.class);

			for (ChecklistContract checklistContract : request.getChecklists()) {
				final Checklist domain = mapper.map(checklistContract, Checklist.class);
				Checklist checklist = checklistService.save(domain);
				checklistContract = mapper.map(checklist, ChecklistContract.class);
			}

			mastersMap.clear();
			mastersMap.put("checklist_persisted", request);
			financialBillRegisterProducer.sendMessage(completedTopic, checklistCompletedKey, mastersMap);
		}
		
		if (mastersMap.get("checklist_update") != null) {

			final ChecklistRequest request = objectMapperFactory.create().convertValue(mastersMap.get("checklist_update"),
					ChecklistRequest.class);

			for (final ChecklistContract checklistContract : request.getChecklists()) {
				final Checklist domain = mapper.map(checklistContract, Checklist.class);
				checklistService.update(domain);
			}

			mastersMap.clear();
			mastersMap.put("checklist_persisted", request);
			financialBillRegisterProducer.sendMessage(completedTopic, checklistCompletedKey, mastersMap);
		}
	}
}
