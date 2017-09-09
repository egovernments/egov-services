package org.egov.egf.bill.domain.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.constants.Constants;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.bill.domain.model.BillRegister;
import org.egov.egf.bill.domain.model.BillRegisterSearch;
import org.egov.egf.bill.persistence.entity.BillRegisterEntity;
import org.egov.egf.bill.persistence.queue.repository.BillRegisterQueueRepository;
import org.egov.egf.bill.persistence.repository.BillRegisterJdbcRepository;
import org.egov.egf.bill.web.contract.BillRegisterContract;
import org.egov.egf.bill.web.contract.BillRegisterSearchContract;
import org.egov.egf.bill.web.requests.BillRegisterRequest;
import org.egov.egf.master.web.repository.FinancialConfigurationContractRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BillRegisterRepository {

	private BillRegisterJdbcRepository billRegisterJdbcRepository;

	private BillRegisterQueueRepository billRegisterQueueRepository;

	private FinancialConfigurationContractRepository financialConfigurationContractRepository;

	private BillRegisterESRepository billRegisterESRepository;

	private String persistThroughKafka;

	@Autowired
	public BillRegisterRepository(BillRegisterJdbcRepository billRegisterJdbcRepository, BillRegisterQueueRepository billRegisterQueueRepository,
			FinancialConfigurationContractRepository financialConfigurationContractRepository, BillRegisterESRepository billRegisterESRepository,
			@Value("${persist.through.kafka}") String persistThroughKafka) {
		this.billRegisterJdbcRepository = billRegisterJdbcRepository;
		this.billRegisterQueueRepository = billRegisterQueueRepository;
		this.financialConfigurationContractRepository = financialConfigurationContractRepository;
		this.billRegisterESRepository = billRegisterESRepository;
		this.persistThroughKafka = persistThroughKafka;

	}

	@Transactional
	public List<BillRegister> save(List<BillRegister> billRegisters, RequestInfo requestInfo) {

		ModelMapper mapper = new ModelMapper();
		BillRegisterContract contract;

		if (persistThroughKafka != null && !persistThroughKafka.isEmpty()
				&& persistThroughKafka.equalsIgnoreCase("yes")) {

			BillRegisterRequest request = new BillRegisterRequest();
			request.setRequestInfo(requestInfo);
			request.setBillRegisters(new ArrayList<>());

			for (BillRegister f : billRegisters) {

				contract = new BillRegisterContract();
				contract.setCreatedDate(new Date());
				mapper.map(f, contract);
				request.getBillRegisters().add(contract);
				
			}

			addToQue(request);

			return billRegisters;
		} else {

			List<BillRegister> resultList = new ArrayList<BillRegister>();

			for (BillRegister iac : billRegisters) {

				resultList.add(save(iac));
			}

			BillRegisterRequest request = new BillRegisterRequest();
			request.setRequestInfo(requestInfo);
			request.setBillRegisters(new ArrayList<>());

			for (BillRegister f : billRegisters) {

				contract = new BillRegisterContract();
				contract.setCreatedDate(new Date());
				mapper.map(f, contract);
				request.getBillRegisters().add(contract);
				
			}

			addToSearchQueue(request);

			return resultList;
		}

	}
	
	@Transactional
	public List<BillRegister> update(List<BillRegister> billRegisters, RequestInfo requestInfo) {

		ModelMapper mapper = new ModelMapper();
		BillRegisterContract contract;

		if (persistThroughKafka != null && !persistThroughKafka.isEmpty()
				&& persistThroughKafka.equalsIgnoreCase("yes")) {

			BillRegisterRequest request = new BillRegisterRequest();
			request.setRequestInfo(requestInfo);
			request.setBillRegisters(new ArrayList<>());

			for (BillRegister f : billRegisters) {

				contract = new BillRegisterContract();
				contract.setCreatedDate(new Date());
				mapper.map(f, contract);
				request.getBillRegisters().add(contract);

			}

			addToQue(request);

			return billRegisters;
		} else {

			List<BillRegister> resultList = new ArrayList<BillRegister>();

			for (BillRegister f : billRegisters) {

				resultList.add(update(f));
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

			addToSearchQueue(request);

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
		if (!financialConfigurationContractRepository.fetchDataFrom().isEmpty()
				&& financialConfigurationContractRepository.fetchDataFrom().equalsIgnoreCase("es")) {
			BillRegisterSearchContract billRegisterSearchContract = new BillRegisterSearchContract();
			ModelMapper mapper = new ModelMapper();
			mapper.map(domain, billRegisterSearchContract);
//			return billRegisterESRepository.search(billRegisterSearchContract);
			return null;
		} else {
			return billRegisterJdbcRepository.search(domain);
		}

	}
	
	public void addToQue(BillRegisterRequest request) {

		Map<String, Object> message = new HashMap<>();

		if (request.getRequestInfo().getAction().equalsIgnoreCase(Constants.ACTION_CREATE)) {
			message.put("bilregister_create", request);
		} else {
			message.put("billregister_update", request);
		}
		billRegisterQueueRepository.addToQue(message);

	}
	
	public void addToSearchQueue(BillRegisterRequest request) {

		Map<String, Object> message = new HashMap<>();

		message.put("billregister_persisted", request);

		billRegisterQueueRepository.addToSearchQue(message);
	}

	public boolean uniqueCheck(String fieldName, BillRegister billRegister) {
		
		return	billRegisterJdbcRepository.uniqueCheck(fieldName, new BillRegisterEntity().toEntity(billRegister));
	
	}

}