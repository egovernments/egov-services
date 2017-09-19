package org.egov.egf.bill.domain.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.constants.Constants;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.bill.domain.model.BillChecklist;
import org.egov.egf.bill.domain.model.BillChecklistSearch;
import org.egov.egf.bill.persistence.entity.BillChecklistEntity;
import org.egov.egf.bill.persistence.queue.repository.BillChecklistQueueRepository;
import org.egov.egf.bill.persistence.repository.BillChecklistJdbcRepository;
import org.egov.egf.bill.web.contract.BillChecklistContract;
import org.egov.egf.bill.web.contract.BillChecklistSearchContract;
import org.egov.egf.bill.web.requests.BillChecklistRequest;
import org.egov.egf.master.web.repository.FinancialConfigurationContractRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BillChecklistRepository {

	private BillChecklistJdbcRepository billChecklistJdbcRepository;

	private BillChecklistQueueRepository billChecklistQueueRepository;

	private FinancialConfigurationContractRepository financialConfigurationContractRepository;

	private BillChecklistESRepository billChecklistESRepository;

	private String persistThroughKafka;

	@Autowired
	public BillChecklistRepository(BillChecklistJdbcRepository billChecklistJdbcRepository, BillChecklistQueueRepository billChecklistQueueRepository,
			FinancialConfigurationContractRepository financialConfigurationContractRepository, BillChecklistESRepository billChecklistESRepository,
			@Value("${persist.through.kafka}") String persistThroughKafka) {
		this.billChecklistJdbcRepository = billChecklistJdbcRepository;
		this.billChecklistQueueRepository = billChecklistQueueRepository;
		this.financialConfigurationContractRepository = financialConfigurationContractRepository;
		this.billChecklistESRepository = billChecklistESRepository;
		this.persistThroughKafka = persistThroughKafka;

	}

	@Transactional
	public List<BillChecklist> save(List<BillChecklist> billChecklists, RequestInfo requestInfo) {

		ModelMapper mapper = new ModelMapper();
		BillChecklistContract contract;

		if (persistThroughKafka != null && !persistThroughKafka.isEmpty()
				&& persistThroughKafka.equalsIgnoreCase("yes")) {

			BillChecklistRequest request = new BillChecklistRequest();
			request.setRequestInfo(requestInfo);
			request.setBillChecklists(new ArrayList<>());

			for (BillChecklist f : billChecklists) {

				contract = new BillChecklistContract();
				contract.setCreatedDate(new Date());
				mapper.map(f, contract);
				request.getBillChecklists().add(contract);

			}
			addToQue(request);

			return billChecklists;
		} else {

			List<BillChecklist> resultList = new ArrayList<BillChecklist>();

			for (BillChecklist f : billChecklists) {

				resultList.add(save(f));
			}

			BillChecklistRequest request = new BillChecklistRequest();
			request.setRequestInfo(requestInfo);
			request.setBillChecklists(new ArrayList<>());

			for (BillChecklist f : resultList) {

				contract = new BillChecklistContract();
				contract.setCreatedDate(new Date());
				mapper.map(f, contract);
				request.getBillChecklists().add(contract);

			}

			addToSearchQueue(request);

			return resultList;
		}

	}

	@Transactional
	public List<BillChecklist> update(List<BillChecklist> billChecklists, RequestInfo requestInfo) {

		ModelMapper mapper = new ModelMapper();
		BillChecklistContract contract;

		if (persistThroughKafka != null && !persistThroughKafka.isEmpty()
				&& persistThroughKafka.equalsIgnoreCase("yes")) {

			BillChecklistRequest request = new BillChecklistRequest();
			request.setRequestInfo(requestInfo);
			request.setBillChecklists(new ArrayList<>());

			for (BillChecklist f : billChecklists) {

				contract = new BillChecklistContract();
				contract.setCreatedDate(new Date());
				mapper.map(f, contract);
				request.getBillChecklists().add(contract);

			}

			addToQue(request);

			return billChecklists;
		} else {

			List<BillChecklist> resultList = new ArrayList<BillChecklist>();

			for (BillChecklist f : billChecklists) {

				resultList.add(update(f));
			}

			BillChecklistRequest request = new BillChecklistRequest();
			request.setRequestInfo(requestInfo);
			request.setBillChecklists(new ArrayList<>());

			for (BillChecklist f : resultList) {

				contract = new BillChecklistContract();
				contract.setCreatedDate(new Date());
				mapper.map(f, contract);
				request.getBillChecklists().add(contract);

			}

			addToSearchQueue(request);

			return resultList;
		}

	}

	public String getNextSequence() {
		return billChecklistJdbcRepository.getSequence(BillChecklistEntity.SEQUENCE_NAME);
	}

	public BillChecklist findById(BillChecklist billChecklist) {
		BillChecklistEntity entity = billChecklistJdbcRepository.findById(new BillChecklistEntity().toEntity(billChecklist));
		return entity.toDomain();

	}

	@Transactional
	public BillChecklist save(BillChecklist billChecklist) {

		BillChecklistEntity entity = billChecklistJdbcRepository.create(new BillChecklistEntity().toEntity(billChecklist));
		return entity.toDomain();
		
	}

	@Transactional
	public BillChecklist update(BillChecklist billChecklist) {
		BillChecklistEntity entity = billChecklistJdbcRepository.update(new BillChecklistEntity().toEntity(billChecklist));
		return entity.toDomain();
	}


	public Pagination<BillChecklist> search(BillChecklistSearch domain) {
		if (!financialConfigurationContractRepository.fetchDataFrom().isEmpty()
				&& financialConfigurationContractRepository.fetchDataFrom().equalsIgnoreCase("es")) {
			BillChecklistSearchContract billChecklistSearchContract = new BillChecklistSearchContract();
			ModelMapper mapper = new ModelMapper();
			mapper.map(domain, billChecklistSearchContract);
			return billChecklistESRepository.search(billChecklistSearchContract);
		} else {
			return billChecklistJdbcRepository.search(domain);
		}

	}
	
	public void addToQue(BillChecklistRequest request) {

		Map<String, Object> message = new HashMap<>();

		if (request.getRequestInfo().getAction().equalsIgnoreCase(Constants.ACTION_CREATE)) {
			message.put("billchecklist_create", request);
		} else {
			message.put("billchecklist_update", request);
		}
		billChecklistQueueRepository.addToQue(message);

	}
	
	public void addToSearchQueue(BillChecklistRequest request) {

		Map<String, Object> message = new HashMap<>();

		message.put("billchecklist_persisted", request);

		billChecklistQueueRepository.addToSearchQue(message);
	}

	public boolean uniqueCheck(String fieldName, BillChecklist billChecklist) {
		return	billChecklistJdbcRepository.uniqueCheck(fieldName, new BillChecklistEntity().toEntity(billChecklist));
	}

}