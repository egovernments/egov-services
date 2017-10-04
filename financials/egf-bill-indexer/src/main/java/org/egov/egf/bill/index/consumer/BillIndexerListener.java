package org.egov.egf.bill.index.consumer;

import java.text.SimpleDateFormat;
import java.util.HashMap;

import org.egov.egf.bill.index.persistence.repository.ElasticSearchRepository;
import org.egov.egf.bill.web.contract.BillRegisterContract;
import org.egov.egf.bill.web.contract.ChecklistContract;
import org.egov.egf.bill.web.requests.BillRegisterRequest;
import org.egov.egf.bill.web.requests.ChecklistRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class BillIndexerListener {

	private static final String BILLREGISTER_OBJECT_TYPE = "billregister";
	private static final String CHECKLIST_OBJECT_TYPE = "checklist";

	private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

	@Autowired
	private ElasticSearchRepository elasticSearchRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@KafkaListener(id = "${kafka.topics.egf.bill.completed.id}", topics = {
			"${kafka.topics.egf.bill.completed.topic}" }, group = "${kafka.topics.egf.bill.completed.group}")
	public void listen(final HashMap<String, Object> billRegisterRequestMap) {

		if (billRegisterRequestMap.get("billregister_persisted") != null) {

			final BillRegisterRequest request = objectMapper.convertValue(billRegisterRequestMap.get("billregister_persisted"),
					BillRegisterRequest.class);

			if (request.getBillRegisters() != null && !request.getBillRegisters().isEmpty())
				for (final BillRegisterContract billRegister : request.getBillRegisters()) {
					final HashMap<String, Object> indexObj = getBillRegisterContractIndexObject(billRegister);
					elasticSearchRepository.index(BILLREGISTER_OBJECT_TYPE, billRegister.getTenantId() + billRegister.getId() + "-" + billRegister.getBillType(),
							indexObj);
				}
		}
		
		if (billRegisterRequestMap.get("checklist_persisted") != null) {

			final ChecklistRequest request = objectMapper.convertValue(billRegisterRequestMap.get("checklist_persisted"),
					ChecklistRequest.class);

			if (request.getChecklists() != null && !request.getChecklists().isEmpty())
				for (final ChecklistContract checklist : request.getChecklists()) {
					final HashMap<String, Object> indexObj = getChecklistContractIndexObject(checklist);
					elasticSearchRepository.index(CHECKLIST_OBJECT_TYPE, checklist.getTenantId() + "-" + checklist.getType(),
							indexObj);
				}
		}
		
	}

	private HashMap<String, Object> getChecklistContractIndexObject(final ChecklistContract checklistContract) {
		
		final HashMap<String, Object> indexObj = new HashMap<String, Object>();
		indexObj.put("id", checklistContract.getId());
		indexObj.put("type", checklistContract.getType());
		indexObj.put("subType", checklistContract.getSubType());
		indexObj.put("key", checklistContract.getKey());
		indexObj.put("description", checklistContract.getDescription());
		
		indexObj.put("tenantId", checklistContract.getTenantId());
		indexObj.put("createdBy", checklistContract.getCreatedBy());
		indexObj.put("lastModifiedBy", checklistContract.getLastModifiedBy());

		if (checklistContract.getCreatedDate() != null)
			indexObj.put("createdDate", formatter.format(checklistContract.getCreatedDate()));
		else
			indexObj.put("createdDate", null);

		if (checklistContract.getLastModifiedDate() != null)
			indexObj.put("lastModifiedDate", formatter.format(checklistContract.getLastModifiedDate()));
		else
			indexObj.put("lastModifiedDate", null);
		
		return indexObj;
	}

	private HashMap<String, Object> getBillRegisterContractIndexObject(final BillRegisterContract billRegisterContract) {

		final HashMap<String, Object> indexObj = new HashMap<String, Object>();
		indexObj.put("id", billRegisterContract.getId());
		indexObj.put("billType", billRegisterContract.getBillType());
		indexObj.put("billSubType", billRegisterContract.getBillSubType());
		indexObj.put("billNumber", billRegisterContract.getBillNumber());
		indexObj.put("billDate", billRegisterContract.getBillDate());
		indexObj.put("billAmount", billRegisterContract.getBillAmount());
		indexObj.put("passedAmount", billRegisterContract.getPassedAmount());
		indexObj.put("moduleName", billRegisterContract.getModuleName());
		indexObj.put("description", billRegisterContract.getDescription());
		indexObj.put("moduleName", billRegisterContract.getModuleName());
		indexObj.put("status", billRegisterContract.getStatus());
		indexObj.put("fund", billRegisterContract.getFund());
		indexObj.put("function", billRegisterContract.getFunction());
		indexObj.put("fundsource", billRegisterContract.getFundsource());
		indexObj.put("scheme", billRegisterContract.getScheme());
		indexObj.put("subScheme", billRegisterContract.getSubScheme());
		indexObj.put("functionary", billRegisterContract.getFunctionary());
		indexObj.put("division", billRegisterContract.getDivision());
		indexObj.put("department", billRegisterContract.getDepartment());
		indexObj.put("sourcePath", billRegisterContract.getSourcePath());
		indexObj.put("budgetCheckRequired", billRegisterContract.getBudgetCheckRequired());
		indexObj.put("budgetAppropriationNo", billRegisterContract.getBudgetAppropriationNo());
		indexObj.put("partyBillDate", billRegisterContract.getPartyBillDate());
		indexObj.put("partyBillNumber", billRegisterContract.getPartyBillNumber());
		indexObj.put("billDetails", billRegisterContract.getBillDetails());
		indexObj.put("tenantId", billRegisterContract.getTenantId());
		indexObj.put("createdBy", billRegisterContract.getCreatedBy());
		indexObj.put("lastModifiedBy", billRegisterContract.getLastModifiedBy());

		if (billRegisterContract.getCreatedDate() != null)
			indexObj.put("createdDate", formatter.format(billRegisterContract.getCreatedDate()));
		else
			indexObj.put("createdDate", null);

		if (billRegisterContract.getLastModifiedDate() != null)
			indexObj.put("lastModifiedDate", formatter.format(billRegisterContract.getLastModifiedDate()));
		else
			indexObj.put("lastModifiedDate", null);

		return indexObj;
	}

}
