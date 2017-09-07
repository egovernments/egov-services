package org.egov.egf.bill.domain.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.bill.domain.model.BillRegister;
import org.egov.egf.bill.domain.model.BillRegisterSearch;
import org.egov.egf.bill.domain.service.FinancialConfigurationService;
import org.egov.egf.bill.persistence.entity.BillRegisterEntity;
import org.egov.egf.bill.persistence.queue.MastersQueueRepository;
import org.egov.egf.bill.persistence.repository.BillRegisterJdbcRepository;
import org.egov.egf.bill.web.contract.BillRegisterContract;
import org.egov.egf.bill.web.contract.BillRegisterSearchContract;
import org.egov.egf.bill.web.requests.BillRegisterRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BillRegisterRepository {

	private BillRegisterJdbcRepository billRegisterJdbcRepository;

	private MastersQueueRepository billRegisterQueueRepository;

	private FinancialConfigurationService financialConfigurationService;

	private BillRegisterESRepository billRegisterESRepository;

	private String persistThroughKafka;

	@Autowired
	public BillRegisterRepository(BillRegisterJdbcRepository billRegisterJdbcRepository, MastersQueueRepository billRegisterQueueRepository,
			FinancialConfigurationService financialConfigurationService, BillRegisterESRepository billRegisterESRepository,
			@Value("${persist.through.kafka}") String persistThroughKafka) {
		this.billRegisterJdbcRepository = billRegisterJdbcRepository;
		this.billRegisterQueueRepository = billRegisterQueueRepository;
		this.financialConfigurationService = financialConfigurationService;
		this.billRegisterESRepository = billRegisterESRepository;
		this.persistThroughKafka = persistThroughKafka;

	}

	@Transactional
	public List<BillRegister> save(List<BillRegister> billregisters, RequestInfo requestInfo) {

		ModelMapper mapper = new ModelMapper();
		BillRegisterContract contract;
		Map<String, Object> message = new HashMap<>();

		if (persistThroughKafka != null && !persistThroughKafka.isEmpty()
				&& persistThroughKafka.equalsIgnoreCase("yes")) {

			BillRegisterRequest request = new BillRegisterRequest();
			request.setRequestInfo(requestInfo);
			request.setBillRegisters(new ArrayList<>());

			for (BillRegister f : billregisters) {

				contract = new BillRegisterContract();
				contract.setCreatedDate(new Date());
				mapper.map(f, contract);
				request.getBillRegisters().add(contract);

			}
			message.put("billRegister_create", request);
			billRegisterQueueRepository.add(message);

			return billregisters;
		} else {

			List<BillRegister> resultList = new ArrayList<BillRegister>();

			for (BillRegister f : billregisters) {

				resultList.add(save(f));
			}

			BillRegisterRequest request = new BillRegisterRequest();
			request.setRequestInfo(requestInfo);
			request.setBillRegisters(new ArrayList<>());

			for (BillRegister f : resultList) {

				contract = new BillRegisterContract();
				contract.setCreatedDate(new Date());
				mapper.map(f, contract);
				request.getBillRegisters().add(contract);

			}

			message.put("billRegister_create", request);
			billRegisterQueueRepository.addToSearch(message);

			return resultList;
		}

	}

	@Transactional
	public List<BillRegister> update(List<BillRegister> billregisters, RequestInfo requestInfo) {
		ModelMapper mapper = new ModelMapper();
		Map<String, Object> message = new HashMap<>();
		BillRegisterRequest request = new BillRegisterRequest();
		BillRegisterContract contract;
		if (persistThroughKafka != null && !persistThroughKafka.isEmpty()
				&& persistThroughKafka.equalsIgnoreCase("yes")) {

			request.setRequestInfo(requestInfo);
			request.setBillRegisters(new ArrayList<>());
			for (BillRegister f : billregisters) {
				contract = new BillRegisterContract();
				contract.setCreatedDate(new Date());
				mapper.map(f, contract);
				request.getBillRegisters().add(contract);
			}
			message.put("billRegister_update", request);
			billRegisterQueueRepository.add(message);
			return billregisters;
		} else {
			List<BillRegister> resultList = new ArrayList<BillRegister>();
			for (BillRegister f : billregisters) {
				resultList.add(update(f));
			}
			request.setRequestInfo(requestInfo);
			request.setBillRegisters(new ArrayList<>());
			for (BillRegister f : resultList) {
				contract = new BillRegisterContract();
				contract.setCreatedDate(new Date());
				mapper.map(f, contract);
				request.getBillRegisters().add(contract);
			}
			message.put("billRegister_persisted", request);
			billRegisterQueueRepository.addToSearch(message);
			return resultList;
		}

	}

	public String getNextSequence() {
		return billRegisterJdbcRepository.getSequence(BillRegisterEntity.SEQUENCE_NAME);
	}

	public BillRegister findById(BillRegister billRegister) {
		BillRegisterEntity entity = billRegisterJdbcRepository.findById(new BillRegisterEntity().toEntity(billRegister));
		return entity.toDomain();

	}

	@Transactional
	public BillRegister save(BillRegister billRegister) {
		BillRegisterEntity entity = billRegisterJdbcRepository.create(new BillRegisterEntity().toEntity(billRegister));
		
		return entity.toDomain();
		
	}

	@Transactional
	public BillRegister update(BillRegister billRegister) {
		BillRegisterEntity entity = billRegisterJdbcRepository.update(new BillRegisterEntity().toEntity(billRegister));
		return entity.toDomain();
	}


	public Pagination<BillRegister> search(BillRegisterSearch domain) {
		if (!financialConfigurationService.fetchDataFrom().isEmpty()
				&& financialConfigurationService.fetchDataFrom().equalsIgnoreCase("es")) {
			BillRegisterSearchContract billRegisterSearchContract = new BillRegisterSearchContract();
			ModelMapper mapper = new ModelMapper();
			mapper.map(domain, billRegisterSearchContract);
			return billRegisterESRepository.search(billRegisterSearchContract);
		} else {
			return billRegisterJdbcRepository.search(domain);
		}

	}

	public boolean uniqueCheck(String fieldName, BillRegister billRegister) {
		return	billRegisterJdbcRepository.uniqueCheck(fieldName, new BillRegisterEntity().toEntity(billRegister));
	}

}