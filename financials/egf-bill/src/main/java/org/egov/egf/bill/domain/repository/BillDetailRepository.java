package org.egov.egf.bill.domain.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.constants.Constants;
import org.egov.common.contract.request.RequestInfo;
import org.egov.egf.bill.domain.model.BillDetail;
import org.egov.egf.bill.persistence.entity.BillDetailEntity;
import org.egov.egf.bill.persistence.queue.repository.BillDetailQueueRepository;
import org.egov.egf.bill.persistence.repository.BillDetailJdbcRepository;
import org.egov.egf.bill.web.contract.BillDetailContract;
import org.egov.egf.bill.web.requests.BillDetailRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BillDetailRepository {

	private BillDetailJdbcRepository billDetailJdbcRepository;

	private BillDetailQueueRepository billDetailQueueRepository;

	private String persistThroughKafka;

	@Autowired
	public BillDetailRepository(BillDetailJdbcRepository billDetailJdbcRepository, BillDetailQueueRepository billDetailQueueRepository,
			@Value("${persist.through.kafka}") String persistThroughKafka) {
		this.billDetailJdbcRepository = billDetailJdbcRepository;
		this.billDetailQueueRepository = billDetailQueueRepository;
		this.persistThroughKafka = persistThroughKafka;

	}

	@Transactional
	public List<BillDetail> save(List<BillDetail> billdetails, RequestInfo requestInfo) {

		ModelMapper mapper = new ModelMapper();
		BillDetailContract contract;

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
			addToQue(request);

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

			addToSearchQueue(request);

			return resultList;
		}

	}

	@Transactional
	public List<BillDetail> update(List<BillDetail> billDetails, RequestInfo requestInfo) {

		ModelMapper mapper = new ModelMapper();
		BillDetailContract contract;

		if (persistThroughKafka != null && !persistThroughKafka.isEmpty()
				&& persistThroughKafka.equalsIgnoreCase("yes")) {

			BillDetailRequest request = new BillDetailRequest();
			request.setRequestInfo(requestInfo);
			request.setBillDetails(new ArrayList<>());

			for (BillDetail f : billDetails) {

				contract = new BillDetailContract();
				contract.setCreatedDate(new Date());
				mapper.map(f, contract);
				request.getBillDetails().add(contract);

			}

			addToQue(request);

			return billDetails;
		} else {

			List<BillDetail> resultList = new ArrayList<BillDetail>();

			for (BillDetail f : billDetails) {

				resultList.add(update(f));
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

			addToSearchQueue(request);

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


	public void addToQue(BillDetailRequest request) {

		Map<String, Object> message = new HashMap<>();

		if (request.getRequestInfo().getAction().equalsIgnoreCase(Constants.ACTION_CREATE)) {
			message.put("billdetail_create", request);
		} else {
			message.put("billdetail_update", request);
		}
		billDetailQueueRepository.addToQue(message);

	}
	
	public void addToSearchQueue(BillDetailRequest request) {

		Map<String, Object> message = new HashMap<>();

		message.put("billdetail_persisted", request);

		billDetailQueueRepository.addToSearchQue(message);
	}

	public boolean uniqueCheck(String fieldName, BillDetail billDetail) {
		return	billDetailJdbcRepository.uniqueCheck(fieldName, new BillDetailEntity().toEntity(billDetail));
	}

}