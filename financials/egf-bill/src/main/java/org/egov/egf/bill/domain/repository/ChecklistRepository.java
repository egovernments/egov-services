package org.egov.egf.bill.domain.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.constants.Constants;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.bill.domain.model.Checklist;
import org.egov.egf.bill.domain.model.ChecklistSearch;
import org.egov.egf.bill.persistence.entity.ChecklistEntity;
import org.egov.egf.bill.persistence.queue.repository.ChecklistQueueRepository;
import org.egov.egf.bill.persistence.repository.ChecklistJdbcRepository;
import org.egov.egf.bill.web.contract.ChecklistContract;
import org.egov.egf.bill.web.contract.ChecklistSearchContract;
import org.egov.egf.bill.web.requests.ChecklistRequest;
import org.egov.egf.master.web.repository.FinancialConfigurationContractRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChecklistRepository {

	private ChecklistJdbcRepository checklistJdbcRepository;

	private ChecklistQueueRepository checklistQueueRepository;

	private FinancialConfigurationContractRepository financialConfigurationContractRepository;

	private ChecklistESRepository checklistESRepository;

	private String persistThroughKafka;

	@Autowired
	public ChecklistRepository(ChecklistJdbcRepository checklistJdbcRepository, ChecklistQueueRepository checklistQueueRepository,
			FinancialConfigurationContractRepository financialConfigurationContractRepository, ChecklistESRepository checklistESRepository,
			@Value("${persist.through.kafka}") String persistThroughKafka) {
		this.checklistJdbcRepository = checklistJdbcRepository;
		this.checklistQueueRepository = checklistQueueRepository;
		this.financialConfigurationContractRepository = financialConfigurationContractRepository;
		this.checklistESRepository = checklistESRepository;
		this.persistThroughKafka = persistThroughKafka;

	}

	@Transactional
	public List<Checklist> save(List<Checklist> checklists, RequestInfo requestInfo) {

		ModelMapper mapper = new ModelMapper();
		ChecklistContract contract;

		if (persistThroughKafka != null && !persistThroughKafka.isEmpty()
				&& persistThroughKafka.equalsIgnoreCase("yes")) {

			ChecklistRequest request = new ChecklistRequest();
			request.setRequestInfo(requestInfo);
			request.setChecklists(new ArrayList<>());

			for (Checklist f : checklists) {

				contract = new ChecklistContract();
				contract.setCreatedDate(new Date());
				mapper.map(f, contract);
				request.getChecklists().add(contract);

			}
			addToQue(request);

			return checklists;
		} else {

			List<Checklist> resultList = new ArrayList<Checklist>();

			for (Checklist f : checklists) {

				resultList.add(save(f));
			}

			ChecklistRequest request = new ChecklistRequest();
			request.setRequestInfo(requestInfo);
			request.setChecklists(new ArrayList<>());

			for (Checklist f : resultList) {

				contract = new ChecklistContract();
				contract.setCreatedDate(new Date());
				mapper.map(f, contract);
				request.getChecklists().add(contract);

			}

			addToSearchQueue(request);

			return resultList;
		}

	}

	@Transactional
	public List<Checklist> update(List<Checklist> checklists, RequestInfo requestInfo) {

		ModelMapper mapper = new ModelMapper();
		ChecklistContract contract;

		if (persistThroughKafka != null && !persistThroughKafka.isEmpty()
				&& persistThroughKafka.equalsIgnoreCase("yes")) {

			ChecklistRequest request = new ChecklistRequest();
			request.setRequestInfo(requestInfo);
			request.setChecklists(new ArrayList<>());

			for (Checklist f : checklists) {

				contract = new ChecklistContract();
				contract.setCreatedDate(new Date());
				mapper.map(f, contract);
				request.getChecklists().add(contract);

			}

			addToQue(request);

			return checklists;
		} else {

			List<Checklist> resultList = new ArrayList<Checklist>();

			for (Checklist f : checklists) {

				resultList.add(update(f));
			}

			ChecklistRequest request = new ChecklistRequest();
			request.setRequestInfo(requestInfo);
			request.setChecklists(new ArrayList<>());

			for (Checklist f : resultList) {

				contract = new ChecklistContract();
				contract.setCreatedDate(new Date());
				mapper.map(f, contract);
				request.getChecklists().add(contract);

			}

			addToSearchQueue(request);

			return resultList;
		}

	}

	public String getNextSequence() {
		return checklistJdbcRepository.getSequence(ChecklistEntity.SEQUENCE_NAME);
	}

	public Checklist findById(Checklist checklist) {
		ChecklistEntity entity = checklistJdbcRepository.findById(new ChecklistEntity().toEntity(checklist));
		return entity.toDomain();

	}

	@Transactional
	public Checklist save(Checklist checklist) {
		ChecklistEntity entity = checklistJdbcRepository.create(new ChecklistEntity().toEntity(checklist));
		
		return entity.toDomain();
		
	}

	@Transactional
	public Checklist update(Checklist checklist) {
		ChecklistEntity entity = checklistJdbcRepository.update(new ChecklistEntity().toEntity(checklist));
		return entity.toDomain();
	}


	public Pagination<Checklist> search(ChecklistSearch domain) {
		if (!financialConfigurationContractRepository.fetchDataFrom().isEmpty()
				&& financialConfigurationContractRepository.fetchDataFrom().equalsIgnoreCase("es")) {
			ChecklistSearchContract checklistSearchContract = new ChecklistSearchContract();
			ModelMapper mapper = new ModelMapper();
			mapper.map(domain, checklistSearchContract);
			return checklistESRepository.search(checklistSearchContract);
		} else {
			return checklistJdbcRepository.search(domain);
		}

	}
	
	public void addToQue(ChecklistRequest request) {

		Map<String, Object> message = new HashMap<>();

		if (request.getRequestInfo().getAction().equalsIgnoreCase(Constants.ACTION_CREATE)) {
			message.put("checklist_create", request);
		} else {
			message.put("checklist_update", request);
		}
		checklistQueueRepository.addToQue(message);

	}
	
	public void addToSearchQueue(ChecklistRequest request) {

		Map<String, Object> message = new HashMap<>();

		message.put("checklist_persisted", request);

		checklistQueueRepository.addToSearchQue(message);
	}

	public boolean uniqueCheck(String fieldName, Checklist checklist) {
		return	checklistJdbcRepository.uniqueCheck(fieldName, new ChecklistEntity().toEntity(checklist));
	}
	
	public boolean uniqueCheck(String fieldName1, String fieldName2, Checklist checklist) {
		return	checklistJdbcRepository.uniqueCheck(fieldName1, fieldName2, new ChecklistEntity().toEntity(checklist));
	}

}