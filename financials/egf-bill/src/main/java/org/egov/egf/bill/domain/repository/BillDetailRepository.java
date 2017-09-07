package org.egov.egf.bill.domain.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.bill.domain.model.BillDetail;
import org.egov.egf.bill.persistence.entity.BillDetailEntity;
import org.egov.egf.bill.persistence.repository.BillDetailJdbcRepository;
import org.egov.egf.bill.web.contract.BillDetailContract;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BillDetailRepository {

	private BillDetailJdbcRepository billDetailJdbcRepository;

	private MastersQueueRepository billDetailQueueRepository;

	private FinancialConfigurationService financialConfigurationService;

	private BillDetailESRepository billDetailESRepository;

	private String persistThroughKafka;

	@Autowired
	public BillDetailRepository(BillDetailJdbcRepository billDetailJdbcRepository, MastersQueueRepository billDetailQueueRepository,
			FinancialConfigurationService financialConfigurationService, BillDetailESRepository billDetailESRepository,
			@Value("${persist.through.kafka}") String persistThroughKafka) {
		this.billDetailJdbcRepository = billDetailJdbcRepository;
		this.billDetailQueueRepository = billDetailQueueRepository;
		this.financialConfigurationService = financialConfigurationService;
		this.billDetailESRepository = billDetailESRepository;
		this.persistThroughKafka = persistThroughKafka;

	}

	@Transactional
	public List<BillDetail> save(List<BillDetail> billdetails, RequestInfo requestInfo) {

		ModelMapper mapper = new ModelMapper();
		BillDetailContract contract;
		Map<String, Object> message = new HashMap<>();

		if (persistThroughKafka != null && !persistThroughKafka.isEmpty()
				&& persistThroughKafka.equalsIgnoreCase("yes")) {

			BillDetailRequest request = new BillDetailRequest();
			request.setRequestInfo(requestInfo);
			request.setBillDetails(new ArrayList<>());

			for (BillDetail f : billdetails) {

				contract = new BillDetailContract();
				contract.setCreatedDate(new Date());
				mapper.map(f, contract);
				request.getBillDetails().add(contract);

			}
			message.put("billDetail_create", request);
			billDetailQueueRepository.add(message);

			return billdetails;
		} else {

			List<BillDetail> resultList = new ArrayList<BillDetail>();

			for (BillDetail f : billdetails) {

				resultList.add(save(f));
			}

			BillDetailRequest request = new BillDetailRequest();
			request.setRequestInfo(requestInfo);
			request.setBillDetails(new ArrayList<>());

			for (BillDetail f : resultList) {

				contract = new BillDetailContract();
				contract.setCreatedDate(new Date());
				mapper.map(f, contract);
				request.getBillDetails().add(contract);

			}

			message.put("billDetail_create", request);
			billDetailQueueRepository.addToSearch(message);

			return resultList;
		}

	}

	@Transactional
	public List<BillDetail> update(List<BillDetail> billdetails, RequestInfo requestInfo) {
		ModelMapper mapper = new ModelMapper();
		Map<String, Object> message = new HashMap<>();
		BillDetailRequest request = new BillDetailRequest();
		BillDetailContract contract;
		if (persistThroughKafka != null && !persistThroughKafka.isEmpty()
				&& persistThroughKafka.equalsIgnoreCase("yes")) {

			request.setRequestInfo(requestInfo);
			request.setBillDetails(new ArrayList<>());
			for (BillDetail f : billdetails) {
				contract = new BillDetailContract();
				contract.setCreatedDate(new Date());
				mapper.map(f, contract);
				request.getBillDetails().add(contract);
			}
			message.put("billDetail_update", request);
			billDetailQueueRepository.add(message);
			return billdetails;
		} else {
			List<BillDetail> resultList = new ArrayList<BillDetail>();
			for (BillDetail f : billdetails) {
				resultList.add(update(f));
			}
			request.setRequestInfo(requestInfo);
			request.setBillDetails(new ArrayList<>());
			for (BillDetail f : resultList) {
				contract = new BillDetailContract();
				contract.setCreatedDate(new Date());
				mapper.map(f, contract);
				request.getBillDetails().add(contract);
			}
			message.put("billDetail_persisted", request);
			billDetailQueueRepository.addToSearch(message);
			return resultList;
		}

	}

	public String getNextSequence() {
		return billDetailJdbcRepository.getSequence(BillDetailEntity.SEQUENCE_NAME);
	}

	public BillDetail findById(BillDetail billDetail) {
		BillDetailEntity entity = billDetailJdbcRepository.findById(new BillDetailEntity().toEntity(billDetail));
		return entity.toDomain();

	}

	@Transactional
	public BillDetail save(BillDetail billDetail) {
		BillDetailEntity entity = billDetailJdbcRepository.create(new BillDetailEntity().toEntity(billDetail));
		
		return entity.toDomain();
		
	}

	@Transactional
	public BillDetail update(BillDetail billDetail) {
		BillDetailEntity entity = billDetailJdbcRepository.update(new BillDetailEntity().toEntity(billDetail));
		return entity.toDomain();
	}


	public Pagination<BillDetail> search(BillDetailSearch domain) {
		if (!financialConfigurationService.fetchDataFrom().isEmpty()
				&& financialConfigurationService.fetchDataFrom().equalsIgnoreCase("es")) {
			BillDetailSearchContract billDetailSearchContract = new BillDetailSearchContract();
			ModelMapper mapper = new ModelMapper();
			mapper.map(domain, billDetailSearchContract);
			return billDetailESRepository.search(billDetailSearchContract);
		} else {
			return billDetailJdbcRepository.search(domain);
		}

	}

	public boolean uniqueCheck(String fieldName, BillDetail billDetail) {
		return	billDetailJdbcRepository.uniqueCheck(fieldName, new BillDetailEntity().toEntity(billDetail));
	}

}