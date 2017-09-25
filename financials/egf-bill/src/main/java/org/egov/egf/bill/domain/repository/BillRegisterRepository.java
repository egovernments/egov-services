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
import org.egov.egf.bill.domain.model.BillDetail;
import org.egov.egf.bill.domain.model.BillPayeeDetail;
import org.egov.egf.bill.domain.model.BillRegister;
import org.egov.egf.bill.domain.model.BillRegisterSearch;
import org.egov.egf.bill.persistence.entity.BillChecklistEntity;
import org.egov.egf.bill.persistence.entity.BillDetailEntity;
import org.egov.egf.bill.persistence.entity.BillPayeeDetailEntity;
import org.egov.egf.bill.persistence.entity.BillRegisterEntity;
import org.egov.egf.bill.persistence.queue.repository.BillRegisterQueueRepository;
import org.egov.egf.bill.persistence.repository.BillChecklistJdbcRepository;
import org.egov.egf.bill.persistence.repository.BillDetailJdbcRepository;
import org.egov.egf.bill.persistence.repository.BillPayeeDetailJdbcRepository;
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
	
	private BillDetailJdbcRepository billDetailJdbcRepository;

	private BillPayeeDetailJdbcRepository billPayeeDetailJdbcRepository;

	private BillRegisterQueueRepository billRegisterQueueRepository;
	
	private BillChecklistJdbcRepository billChecklistJdbcRepository;

	private FinancialConfigurationContractRepository financialConfigurationContractRepository;

	private BillRegisterESRepository billRegisterESRepository;

	private String persistThroughKafka;

	@Autowired
	public BillRegisterRepository(BillRegisterJdbcRepository billRegisterJdbcRepository, BillDetailJdbcRepository billDetailJdbcRepository,
			BillPayeeDetailJdbcRepository billPayeeDetailJdbcRepository, BillRegisterQueueRepository billRegisterQueueRepository,
			BillChecklistJdbcRepository billChecklistJdbcRepository, 
			FinancialConfigurationContractRepository financialConfigurationContractRepository, BillRegisterESRepository billRegisterESRepository,
			@Value("${persist.through.kafka}") String persistThroughKafka) {
		this.billRegisterJdbcRepository = billRegisterJdbcRepository;
		this.billRegisterQueueRepository = billRegisterQueueRepository;
		this.billChecklistJdbcRepository = billChecklistJdbcRepository;
		this.billDetailJdbcRepository = billDetailJdbcRepository;
		this.billPayeeDetailJdbcRepository = billPayeeDetailJdbcRepository;
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

		BillRegister savedBillRegister = billRegisterJdbcRepository.create(new BillRegisterEntity().toEntity(billRegister)).toDomain();

		List<BillDetail> savedBillDetails = new ArrayList<>();
		List<BillChecklist> savedBillChecklists = new ArrayList<>();
		BillDetail savedBillDetail = null;
		BillDetailEntity billDetailEntity = null;
		BillChecklist savedBillChecklist = null;
		BillChecklistEntity billChecklistEntity = null;
		BillPayeeDetail savedDetail = null;
		BillPayeeDetailEntity billPayeeDetailEntity = null;
		
		for (BillDetail billDetail : billRegister.getBillDetails()) {

			billDetailEntity = new BillDetailEntity().toEntity(billDetail);
			billDetailEntity.setBillRegisterId(savedBillRegister.getId());
			savedBillDetail = billDetailJdbcRepository.create(billDetailEntity).toDomain();

			if (billDetail.getBillPayeeDetails() != null && !billDetail.getBillPayeeDetails().isEmpty()) {

				List<BillPayeeDetail> savedBillPayeeDetails = new ArrayList<>();
				for (BillPayeeDetail detail : billDetail.getBillPayeeDetails()) {
					billPayeeDetailEntity = new BillPayeeDetailEntity().toEntity(detail);
					billPayeeDetailEntity.setBillDetailId(savedBillDetail.getId());
					savedDetail = billPayeeDetailJdbcRepository.create(billPayeeDetailEntity).toDomain();
					savedBillPayeeDetails.add(savedDetail);

				}

				savedBillDetail.setBillPayeeDetails(savedBillPayeeDetails);
			}

			savedBillDetails.add(savedBillDetail);

		}
		
		for(BillChecklist billChecklist : billRegister.getCheckLists()){
			
			billChecklistEntity = new BillChecklistEntity().toEntity(billChecklist);
			billChecklistEntity.setBillId(savedBillRegister.getId());
			billChecklistEntity.setChecklistId(billChecklist.getChecklist().getId());
			savedBillChecklist = billChecklistJdbcRepository.create(billChecklistEntity).toDomain();
			savedBillChecklists.add(savedBillChecklist);
			
		}
		
		savedBillRegister.setCheckLists(savedBillChecklists);
		savedBillRegister.setBillDetails(savedBillDetails);

		return savedBillRegister;

	}

	@Transactional
	public BillRegister update(BillRegister billRegister) {

		BillRegister updatedBillRegister = billRegisterJdbcRepository.update(new BillRegisterEntity().toEntity(billRegister)).toDomain();

		List<BillDetail> updatedBillDetails = new ArrayList<>();
		BillDetail updatedBillDetail = null;
		BillDetailEntity billDetailEntity = null;
		BillPayeeDetail updatedDetail = null;
		BillPayeeDetailEntity billPayeeDetailEntity = null;

		BillRegisterSearch billRegisterSearch = new BillRegisterSearch();

		billRegisterSearch.setId(updatedBillRegister.getId());
		billRegisterSearch.setTenantId(updatedBillRegister.getTenantId());

		Pagination<BillRegister> oldBillRegister = search(billRegisterSearch);

		// Clear old billDetail and billPayeeDetails

		if (null != oldBillRegister && null != oldBillRegister.getPagedData() && !oldBillRegister.getPagedData().isEmpty())
			for (BillDetail billDetail : oldBillRegister.getPagedData().get(0).getBillDetails()) {

				if (billDetail.getBillPayeeDetails() != null && !billDetail.getBillPayeeDetails().isEmpty()) {

					for (BillPayeeDetail detail : billDetail.getBillPayeeDetails()) {
						billPayeeDetailEntity = new BillPayeeDetailEntity().toEntity(detail);
						billPayeeDetailJdbcRepository.delete(billPayeeDetailEntity);

					}

				}
				billDetailEntity = new BillDetailEntity().toEntity(billDetail);
				billDetailJdbcRepository.delete(billDetailEntity);


			}

		// Add new billDetails and billPayeeDetails

		for (BillDetail billDetail : billRegister.getBillDetails()) {

			billDetailEntity = new BillDetailEntity().toEntity(billDetail);
			billDetailEntity.setBillRegisterId(updatedBillRegister.getId());
			updatedBillDetail = billDetailJdbcRepository.create(billDetailEntity).toDomain();

			if (billDetail.getBillPayeeDetails() != null && !billDetail.getBillPayeeDetails().isEmpty()) {

				List<BillPayeeDetail> updatedBillPayeeDetails = new ArrayList<>();
				for (BillPayeeDetail detail : billDetail.getBillPayeeDetails()) {
					billPayeeDetailEntity = new BillPayeeDetailEntity().toEntity(detail);
					billPayeeDetailEntity.setBillDetailId(updatedBillDetail.getId());
					updatedDetail = billPayeeDetailJdbcRepository.create(billPayeeDetailEntity).toDomain();
					updatedBillPayeeDetails.add(updatedDetail);

				}

				updatedBillDetail.setBillPayeeDetails(updatedBillPayeeDetails);
			}

			updatedBillDetails.add(updatedBillDetail);

		}
		
		updatedBillRegister.setBillDetails(updatedBillDetails);

		return updatedBillRegister;
	}


	public Pagination<BillRegister> search(BillRegisterSearch domain) {
		if (!financialConfigurationContractRepository.fetchDataFrom().isEmpty()
				&& financialConfigurationContractRepository.fetchDataFrom().equalsIgnoreCase("es")) {
			BillRegisterSearchContract billRegisterSearchContract = new BillRegisterSearchContract();
			ModelMapper mapper = new ModelMapper();
			mapper.map(domain, billRegisterSearchContract);
			return billRegisterESRepository.search(billRegisterSearchContract);
		} else {
			return billRegisterJdbcRepository.search(domain);
		}

	}
	
	public void addToQue(BillRegisterRequest request) {

		Map<String, Object> message = new HashMap<>();

		if (request.getRequestInfo().getAction().equalsIgnoreCase(Constants.ACTION_CREATE)) {
			message.put("billregister_create", request);
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