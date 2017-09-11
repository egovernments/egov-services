package org.egov.egf.voucher.domain.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.egov.common.constants.Constants;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.web.repository.FinancialConfigurationContractRepository;
import org.egov.egf.voucher.domain.model.Ledger;
import org.egov.egf.voucher.domain.model.LedgerDetail;
import org.egov.egf.voucher.domain.model.Voucher;
import org.egov.egf.voucher.domain.model.VoucherSearch;
import org.egov.egf.voucher.persistence.entity.LedgerDetailEntity;
import org.egov.egf.voucher.persistence.entity.LedgerEntity;
import org.egov.egf.voucher.persistence.entity.VoucherEntity;
import org.egov.egf.voucher.persistence.queue.repository.VoucherQueueRepository;
import org.egov.egf.voucher.persistence.repository.LedgerDetailJdbcRepository;
import org.egov.egf.voucher.persistence.repository.LedgerJdbcRepository;
import org.egov.egf.voucher.persistence.repository.VoucherJdbcRepository;
import org.egov.egf.voucher.web.contract.VoucherContract;
import org.egov.egf.voucher.web.contract.VoucherSearchContract;
import org.egov.egf.voucher.web.requests.VoucherRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VoucherRepository {

	private VoucherJdbcRepository voucherJdbcRepository;

	private VoucherQueueRepository voucherQueueRepository;

	private FinancialConfigurationContractRepository financialConfigurationContractRepository;

	private VoucherESRepository voucherESRepository;

	@Autowired
	private LedgerJdbcRepository ledgerJdbcRepository;

	@Autowired
	private LedgerDetailJdbcRepository ledgerDetailJdbcRepository;

	private String persistThroughKafka;

	@Autowired
	public VoucherRepository(VoucherJdbcRepository voucherJdbcRepository, VoucherQueueRepository voucherQueueRepository,
			FinancialConfigurationContractRepository financialConfigurationContractRepository,
			VoucherESRepository voucherESRepository, @Value("${persist.through.kafka}") String persistThroughKafka) {
		this.voucherJdbcRepository = voucherJdbcRepository;
		this.voucherQueueRepository = voucherQueueRepository;
		this.financialConfigurationContractRepository = financialConfigurationContractRepository;
		this.voucherESRepository = voucherESRepository;
		this.persistThroughKafka = persistThroughKafka;

	}

	@Transactional
	public List<Voucher> save(List<Voucher> vouchers, RequestInfo requestInfo) {

		ModelMapper mapper = new ModelMapper();
		VoucherContract contract;

		if (persistThroughKafka != null && !persistThroughKafka.isEmpty()
				&& persistThroughKafka.equalsIgnoreCase("yes")) {

			VoucherRequest request = new VoucherRequest();
			request.setRequestInfo(requestInfo);
			request.setVouchers(new ArrayList<>());

			for (Voucher f : vouchers) {

				contract = new VoucherContract();
				contract.setCreatedDate(new Date());
				mapper.map(f, contract);
				request.getVouchers().add(contract);

			}

			addToQue(request);

			return vouchers;
		} else {

			List<Voucher> resultList = new ArrayList<Voucher>();

			for (Voucher f : vouchers) {

				resultList.add(save(f));
			}

			VoucherRequest request = new VoucherRequest();
			request.setRequestInfo(requestInfo);
			request.setVouchers(new ArrayList<>());

			for (Voucher f : resultList) {

				contract = new VoucherContract();
				contract.setCreatedDate(new Date());
				mapper.map(f, contract);
				request.getVouchers().add(contract);

			}

			addToSearchQueue(request);

			return resultList;
		}

	}

	@Transactional
	public List<Voucher> update(List<Voucher> vouchers, RequestInfo requestInfo) {

		ModelMapper mapper = new ModelMapper();
		VoucherContract contract;

		if (persistThroughKafka != null && !persistThroughKafka.isEmpty()
				&& persistThroughKafka.equalsIgnoreCase("yes")) {

			VoucherRequest request = new VoucherRequest();
			request.setRequestInfo(requestInfo);
			request.setVouchers(new ArrayList<>());

			for (Voucher f : vouchers) {

				contract = new VoucherContract();
				contract.setCreatedDate(new Date());
				mapper.map(f, contract);
				request.getVouchers().add(contract);

			}

			addToQue(request);

			return vouchers;
		} else {

			List<Voucher> resultList = new ArrayList<Voucher>();

			for (Voucher f : vouchers) {

				resultList.add(update(f));
			}

			VoucherRequest request = new VoucherRequest();
			request.setRequestInfo(requestInfo);
			request.setVouchers(new ArrayList<>());

			for (Voucher f : resultList) {

				contract = new VoucherContract();
				contract.setCreatedDate(new Date());
				mapper.map(f, contract);
				request.getVouchers().add(contract);

			}

			addToSearchQueue(request);

			return resultList;
		}

	}

	public String getNextSequence() {
		return voucherJdbcRepository.getSequence(VoucherEntity.SEQUENCE_NAME);
	}

	public Voucher findById(Voucher voucher) {
		VoucherEntity entity = voucherJdbcRepository.findById(new VoucherEntity().toEntity(voucher));
		return entity.toDomain();

	}

	@Transactional
	public Voucher save(Voucher voucher) {

		Voucher savedVoucher = voucherJdbcRepository.create(new VoucherEntity().toEntity(voucher)).toDomain();

		Set<Ledger> savedLedgers = new LinkedHashSet<>();
		Ledger savedLedger = null;
		LedgerEntity ledgerEntity = null;
		LedgerDetail savedDetail = null;
		LedgerDetailEntity ledgerDetailEntity = null;

		for (Ledger ledger : voucher.getLedgers()) {

			ledgerEntity = new LedgerEntity().toEntity(ledger);
			ledgerEntity.setVoucherId(savedVoucher.getId());
			savedLedger = ledgerJdbcRepository.create(ledgerEntity).toDomain();

			if (ledger.getLedgerDetails() != null && !ledger.getLedgerDetails().isEmpty()) {

				Set<LedgerDetail> savedLedgerDetails = new LinkedHashSet<>();
				for (LedgerDetail detail : ledger.getLedgerDetails()) {
					ledgerDetailEntity = new LedgerDetailEntity().toEntity(detail);
					ledgerDetailEntity.setLedgerId(savedLedger.getId());
					savedDetail = ledgerDetailJdbcRepository.create(ledgerDetailEntity).toDomain();
					savedLedgerDetails.add(savedDetail);

				}

				savedLedger.setLedgerDetails(savedLedgerDetails);
			}

			savedLedgers.add(savedLedger);

		}
		savedVoucher.setLedgers(savedLedgers);

		return savedVoucher;

	}

	@Transactional
	public Voucher update(Voucher voucher) {
		VoucherEntity entity = voucherJdbcRepository.update(new VoucherEntity().toEntity(voucher));
		return entity.toDomain();
	}

	public void addToQue(VoucherRequest request) {

		Map<String, Object> message = new HashMap<>();

		if (request.getRequestInfo().getAction().equalsIgnoreCase(Constants.ACTION_CREATE)) {
			message.put("voucher_create", request);
		} else {
			message.put("voucher_update", request);
		}
		voucherQueueRepository.addToQue(message);

	}

	public void addToSearchQueue(VoucherRequest request) {

		Map<String, Object> message = new HashMap<>();

		message.put("voucher_persisted", request);

		voucherQueueRepository.addToSearchQue(message);
	}

	public Pagination<Voucher> search(VoucherSearch domain) {

		if (!financialConfigurationContractRepository.fetchDataFrom().isEmpty()
				&& financialConfigurationContractRepository.fetchDataFrom().equalsIgnoreCase("es")) {
			VoucherSearchContract voucherSearchContract = new VoucherSearchContract();
			ModelMapper mapper = new ModelMapper();
			mapper.map(domain, voucherSearchContract);
			return voucherESRepository.search(voucherSearchContract);
		}

		return voucherJdbcRepository.search(domain);

	}
	
	public boolean uniqueCheck(String fieldName, Voucher voucher) {
		return	voucherJdbcRepository.uniqueCheck(fieldName, new VoucherEntity().toEntity(voucher));
	}

}