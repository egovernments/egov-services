package org.egov.egf.bill.persistence.queue;

import java.util.HashMap;

import org.egov.egf.bill.domain.model.BillChecklist;
import org.egov.egf.bill.domain.model.BillDetail;
import org.egov.egf.bill.domain.model.BillPayeeDetail;
import org.egov.egf.bill.domain.model.BillRegister;
import org.egov.egf.bill.domain.model.Checklist;
import org.egov.egf.bill.domain.service.BillChecklistService;
import org.egov.egf.bill.domain.service.BillDetailService;
import org.egov.egf.bill.domain.service.BillPayeeDetailService;
import org.egov.egf.bill.domain.service.BillRegisterService;
import org.egov.egf.bill.domain.service.ChecklistService;
import org.egov.egf.bill.web.contract.BillChecklistContract;
import org.egov.egf.bill.web.contract.BillDetailContract;
import org.egov.egf.bill.web.contract.BillPayeeDetailContract;
import org.egov.egf.bill.web.contract.BillRegisterContract;
import org.egov.egf.bill.web.contract.ChecklistContract;
import org.egov.egf.bill.web.requests.BillChecklistRequest;
import org.egov.egf.bill.web.requests.BillDetailRequest;
import org.egov.egf.bill.web.requests.BillPayeeDetailRequest;
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
	
	@Value("${kafka.topics.egf.bill.bill.register.completed.key}")
	private String billRegisterCompletedKey;
	
	@Value("${kafka.topics.egf.bill.bill.checklist.completed.key}")
	private String checklistCompletedKey;
	
	@Value("${kafka.topics.egf.bill.bill.checklist.completed.key}")
	private String billDetailCompletedKey;

	@Value("${kafka.topics.egf.bill.bill.payeedetail.completed.key}")
	private String billPayeeDetailCompletedKey;

	@Value("${kafka.topics.egf.bill.billchecklist.completed.key}")
	private String billChecklistCompletedKey;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private ObjectMapperFactory objectMapperFactory;
	
	@Autowired
	FinancialProducer financialBillRegisterProducer;
	
	@Autowired
	BillRegisterService billRegisterService;
	
	@Autowired
	BillDetailService billDetailService;
	
	@Autowired
	BillPayeeDetailService billPayeeDetailService;
	
	@Autowired
	ChecklistService checklistService;

	@Autowired
	BillChecklistService billChecklistService;
	
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
		
		if (mastersMap.get("billdetail_create") != null) {

			final BillDetailRequest request = objectMapperFactory.create().convertValue(mastersMap.get("billdetail_create"),
					BillDetailRequest.class);

			for (BillDetailContract billDetailContract : request.getBillDetails()) {
				final BillDetail domain = mapper.map(billDetailContract, BillDetail.class);
				BillDetail billDetail = billDetailService.save(domain);
				billDetailContract = mapper.map(billDetail, BillDetailContract.class);
			}

			mastersMap.clear();
			mastersMap.put("billdetail_persisted", request);
			financialBillRegisterProducer.sendMessage(completedTopic, billDetailCompletedKey, mastersMap);
		}
		
		if (mastersMap.get("billdetail_update") != null) {

			final BillDetailRequest request = objectMapperFactory.create().convertValue(mastersMap.get("billdetail_update"),
					BillDetailRequest.class);

			for (final BillDetailContract billDetailContract : request.getBillDetails()) {
				final BillDetail domain = mapper.map(billDetailContract, BillDetail.class);
				billDetailService.update(domain);
			}

			mastersMap.clear();
			mastersMap.put("billdetail_persisted", request);
			financialBillRegisterProducer.sendMessage(completedTopic, billDetailCompletedKey, mastersMap);
		}
		
		if (mastersMap.get("billpayeedetail_create") != null) {

			final BillPayeeDetailRequest request = objectMapperFactory.create().convertValue(mastersMap.get("billpayeedetail_create"),
					BillPayeeDetailRequest.class);

			for (BillPayeeDetailContract billPayeeDetailContract : request.getBillPayeeDetails()) {
				final BillPayeeDetail domain = mapper.map(billPayeeDetailContract, BillPayeeDetail.class);
				BillPayeeDetail billPayeeDetail = billPayeeDetailService.save(domain);
				billPayeeDetailContract = mapper.map(billPayeeDetail, BillPayeeDetailContract.class);
			}

			mastersMap.clear();
			mastersMap.put("billpayeedetail_persisted", request);
			financialBillRegisterProducer.sendMessage(completedTopic, billPayeeDetailCompletedKey, mastersMap);
		}
		
		if (mastersMap.get("billpayeedetail_update") != null) {

			final BillPayeeDetailRequest request = objectMapperFactory.create().convertValue(mastersMap.get("billpayeedetail_update"),
					BillPayeeDetailRequest.class);

			for (final BillPayeeDetailContract billPayeeDetailContract : request.getBillPayeeDetails()) {
				final BillPayeeDetail domain = mapper.map(billPayeeDetailContract, BillPayeeDetail.class);
				billPayeeDetailService.update(domain);
			}

			mastersMap.clear();
			mastersMap.put("billpayeedetail_persisted", request);
			financialBillRegisterProducer.sendMessage(completedTopic, billPayeeDetailCompletedKey, mastersMap);
		}
		
		if (mastersMap.get("billchecklist_create") != null) {

			final BillChecklistRequest request = objectMapperFactory.create().convertValue(mastersMap.get("billchecklist_create"),
					BillChecklistRequest.class);

			for (BillChecklistContract billChecklistContract : request.getBillChecklists()) {
				final BillChecklist domain = mapper.map(billChecklistContract, BillChecklist.class);
				BillChecklist billChecklist = billChecklistService.save(domain);
				billChecklistContract = mapper.map(billChecklist, BillChecklistContract.class);
			}

			mastersMap.clear();
			mastersMap.put("billchecklist_persisted", request);
			financialBillRegisterProducer.sendMessage(completedTopic, billChecklistCompletedKey, mastersMap);
		}
		
		if (mastersMap.get("billchecklist_update") != null) {

			final BillChecklistRequest request = objectMapperFactory.create().convertValue(mastersMap.get("billchecklist_update"),
					BillChecklistRequest.class);

			for (final BillChecklistContract billChecklistContract : request.getBillChecklists()) {
				final BillChecklist domain = mapper.map(billChecklistContract, BillChecklist.class);
				billChecklistService.update(domain);
			}

			mastersMap.clear();
			mastersMap.put("billchecklist_persisted", request);
			financialBillRegisterProducer.sendMessage(completedTopic, billChecklistCompletedKey, mastersMap);
		}
	}
}
