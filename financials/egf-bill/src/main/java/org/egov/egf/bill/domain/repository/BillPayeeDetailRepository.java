package org.egov.egf.bill.domain.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.constants.Constants;
import org.egov.common.contract.request.RequestInfo;
import org.egov.egf.bill.domain.model.BillPayeeDetail;
import org.egov.egf.bill.persistence.entity.BillPayeeDetailEntity;
import org.egov.egf.bill.persistence.queue.repository.BillPayeeDetailQueueRepository;
import org.egov.egf.bill.persistence.repository.BillPayeeDetailJdbcRepository;
import org.egov.egf.bill.web.contract.BillPayeeDetailContract;
import org.egov.egf.bill.web.requests.BillPayeeDetailRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BillPayeeDetailRepository {

	private BillPayeeDetailJdbcRepository billPayeeDetailJdbcRepository;

	private BillPayeeDetailQueueRepository billPayeeDetailQueueRepository;

	private String persistThroughKafka;

	@Autowired
	public BillPayeeDetailRepository(BillPayeeDetailJdbcRepository billPayeeDetailJdbcRepository, BillPayeeDetailQueueRepository billPayeeDetailQueueRepository,
			@Value("${persist.through.kafka}") String persistThroughKafka) {
		this.billPayeeDetailJdbcRepository = billPayeeDetailJdbcRepository;
		this.billPayeeDetailQueueRepository = billPayeeDetailQueueRepository;
		this.persistThroughKafka = persistThroughKafka;

	}

	@Transactional
	public List<BillPayeeDetail> save(List<BillPayeeDetail> billPayeeDetails, RequestInfo requestInfo) {

		ModelMapper mapper = new ModelMapper();
		BillPayeeDetailContract contract;

		if (persistThroughKafka != null && !persistThroughKafka.isEmpty()
				&& persistThroughKafka.equalsIgnoreCase("yes")) {

			BillPayeeDetailRequest request = new BillPayeeDetailRequest();
			request.setRequestInfo(requestInfo);
			request.setBillPayeeDetails(new ArrayList<>());

			for (BillPayeeDetail f : billPayeeDetails) {

				contract = new BillPayeeDetailContract();
				contract.setCreatedDate(new Date());
				mapper.map(f, contract);
				request.getBillPayeeDetails().add(contract);

			}
			addToQue(request);

			return billPayeeDetails;
		} else {

			List<BillPayeeDetail> resultList = new ArrayList<BillPayeeDetail>();

			for (BillPayeeDetail f : billPayeeDetails) {

				resultList.add(save(f));
			}

			BillPayeeDetailRequest request = new BillPayeeDetailRequest();
			request.setRequestInfo(requestInfo);
			request.setBillPayeeDetails(new ArrayList<>());

			for (BillPayeeDetail f : resultList) {

				contract = new BillPayeeDetailContract();
				contract.setCreatedDate(new Date());
				mapper.map(f, contract);
				request.getBillPayeeDetails().add(contract);

			}

			addToSearchQueue(request);

			return resultList;
		}

	}

	@Transactional
	public List<BillPayeeDetail> update(List<BillPayeeDetail> billPayeeDetails, RequestInfo requestInfo) {

		ModelMapper mapper = new ModelMapper();
		BillPayeeDetailContract contract;

		if (persistThroughKafka != null && !persistThroughKafka.isEmpty()
				&& persistThroughKafka.equalsIgnoreCase("yes")) {

			BillPayeeDetailRequest request = new BillPayeeDetailRequest();
			request.setRequestInfo(requestInfo);
			request.setBillPayeeDetails(new ArrayList<>());

			for (BillPayeeDetail f : billPayeeDetails) {

				contract = new BillPayeeDetailContract();
				contract.setCreatedDate(new Date());
				mapper.map(f, contract);
				request.getBillPayeeDetails().add(contract);

			}

			addToQue(request);

			return billPayeeDetails;
		} else {

			List<BillPayeeDetail> resultList = new ArrayList<BillPayeeDetail>();

			for (BillPayeeDetail f : billPayeeDetails) {

				resultList.add(update(f));
			}

			BillPayeeDetailRequest request = new BillPayeeDetailRequest();
			request.setRequestInfo(requestInfo);
			request.setBillPayeeDetails(new ArrayList<>());

			for (BillPayeeDetail f : resultList) {

				contract = new BillPayeeDetailContract();
				contract.setCreatedDate(new Date());
				mapper.map(f, contract);
				request.getBillPayeeDetails().add(contract);

			}

			addToSearchQueue(request);

			return resultList;
		}

	}

	public String getNextSequence() {
		return billPayeeDetailJdbcRepository.getSequence(BillPayeeDetailEntity.SEQUENCE_NAME);
	}

	public BillPayeeDetail findById(BillPayeeDetail billPayeeDetail) {
		BillPayeeDetailEntity entity = billPayeeDetailJdbcRepository.findById(new BillPayeeDetailEntity().toEntity(billPayeeDetail));
		return entity.toDomain();

	}

	@Transactional
	public BillPayeeDetail save(BillPayeeDetail billPayeeDetail) {
		BillPayeeDetailEntity entity = billPayeeDetailJdbcRepository.create(new BillPayeeDetailEntity().toEntity(billPayeeDetail));
		
		return entity.toDomain();
		
	}

	@Transactional
	public BillPayeeDetail update(BillPayeeDetail billPayeeDetail) {
		BillPayeeDetailEntity entity = billPayeeDetailJdbcRepository.update(new BillPayeeDetailEntity().toEntity(billPayeeDetail));
		return entity.toDomain();
	}


	public void addToQue(BillPayeeDetailRequest request) {

		Map<String, Object> message = new HashMap<>();

		if (request.getRequestInfo().getAction().equalsIgnoreCase(Constants.ACTION_CREATE)) {
			message.put("billpayeedetail_create", request);
		} else {
			message.put("billpayeedetail_update", request);
		}
		billPayeeDetailQueueRepository.addToQue(message);

	}
	
	public void addToSearchQueue(BillPayeeDetailRequest request) {

		Map<String, Object> message = new HashMap<>();

		message.put("billPayeeDetail_persisted", request);

		billPayeeDetailQueueRepository.addToSearchQue(message);
	}

	public boolean uniqueCheck(String fieldName, BillPayeeDetail billPayeeDetail) {
		return	billPayeeDetailJdbcRepository.uniqueCheck(fieldName, new BillPayeeDetailEntity().toEntity(billPayeeDetail));
	}

}