package org.egov.egf.bill.domain.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.bill.domain.model.Checklist;
import org.egov.egf.bill.domain.model.ChecklistSearch;
import org.egov.egf.bill.domain.service.FinancialConfigurationService;
import org.egov.egf.bill.persistence.entity.ChecklistEntity;
import org.egov.egf.bill.persistence.queue.MastersQueueRepository;
import org.egov.egf.bill.persistence.repository.ChecklistJdbcRepository;
import org.egov.egf.bill.web.contract.ChecklistContract;
import org.egov.egf.bill.web.contract.ChecklistSearchContract;
import org.egov.egf.bill.web.requests.ChecklistRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChecklistRepository {

	private ChecklistJdbcRepository checklistJdbcRepository;

	private MastersQueueRepository checklistQueueRepository;

	private FinancialConfigurationService financialConfigurationService;

	private ChecklistESRepository checklistESRepository;

	private String persistThroughKafka;

	@Autowired
	public ChecklistRepository(ChecklistJdbcRepository checklistJdbcRepository, MastersQueueRepository checklistQueueRepository,
			FinancialConfigurationService financialConfigurationService, ChecklistESRepository checklistESRepository,
			@Value("${persist.through.kafka}") String persistThroughKafka) {
		this.checklistJdbcRepository = checklistJdbcRepository;
		this.checklistQueueRepository = checklistQueueRepository;
		this.financialConfigurationService = financialConfigurationService;
		this.checklistESRepository = checklistESRepository;
		this.persistThroughKafka = persistThroughKafka;

	}

	@Transactional
	public List<Checklist> save(List<Checklist> checklists, RequestInfo requestInfo) {

		ModelMapper mapper = new ModelMapper();
		ChecklistContract contract;
		Map<String, Object> message = new HashMap<>();

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
			message.put("checklist_create", request);
			checklistQueueRepository.add(message);

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

			message.put("checklist_create", request);
			checklistQueueRepository.addToSearch(message);

			return resultList;
		}

	}

	@Transactional
	public List<Checklist> update(List<Checklist> checklists, RequestInfo requestInfo) {
		ModelMapper mapper = new ModelMapper();
		Map<String, Object> message = new HashMap<>();
		ChecklistRequest request = new ChecklistRequest();
		ChecklistContract contract;
		if (persistThroughKafka != null && !persistThroughKafka.isEmpty()
				&& persistThroughKafka.equalsIgnoreCase("yes")) {

			request.setRequestInfo(requestInfo);
			request.setChecklists(new ArrayList<>());
			for (Checklist f : checklists) {
				contract = new ChecklistContract();
				contract.setCreatedDate(new Date());
				mapper.map(f, contract);
				request.getChecklists().add(contract);
			}
			message.put("checklist_update", request);
			checklistQueueRepository.add(message);
			return checklists;
		} else {
			List<Checklist> resultList = new ArrayList<Checklist>();
			for (Checklist f : checklists) {
				resultList.add(update(f));
			}
			request.setRequestInfo(requestInfo);
			request.setChecklists(new ArrayList<>());
			for (Checklist f : resultList) {
				contract = new ChecklistContract();
				contract.setCreatedDate(new Date());
				mapper.map(f, contract);
				request.getChecklists().add(contract);
			}
			message.put("checklist_persisted", request);
			checklistQueueRepository.addToSearch(message);
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
		if (!financialConfigurationService.fetchDataFrom().isEmpty()
				&& financialConfigurationService.fetchDataFrom().equalsIgnoreCase("es")) {
			ChecklistSearchContract checklistSearchContract = new ChecklistSearchContract();
			ModelMapper mapper = new ModelMapper();
			mapper.map(domain, checklistSearchContract);
			return checklistESRepository.search(checklistSearchContract);
		} else {
			return checklistJdbcRepository.search(domain);
		}

	}

	public boolean uniqueCheck(String fieldName, Checklist checklist) {
		return	checklistJdbcRepository.uniqueCheck(fieldName, new ChecklistEntity().toEntity(checklist));
	}

}