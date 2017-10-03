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
import org.egov.egf.voucher.domain.model.SubLedger;
import org.egov.egf.voucher.domain.model.Voucher;
import org.egov.egf.voucher.domain.model.VoucherSearch;
import org.egov.egf.voucher.persistence.entity.LedgerEntity;
import org.egov.egf.voucher.persistence.entity.SubLedgerEntity;
import org.egov.egf.voucher.persistence.entity.VoucherEntity;
import org.egov.egf.voucher.persistence.queue.repository.VoucherQueueRepository;
import org.egov.egf.voucher.persistence.repository.LedgerJdbcRepository;
import org.egov.egf.voucher.persistence.repository.SubLedgerJdbcRepository;
import org.egov.egf.voucher.persistence.repository.VoucherJdbcRepository;
import org.egov.egf.voucher.web.contract.VoucherContract;
import org.egov.egf.voucher.web.contract.VoucherSearchContract;
import org.egov.egf.voucher.web.requests.VoucherRequest;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VoucherRepository {

	private static final Logger LOG = LoggerFactory.getLogger(VoucherRepository.class);

	private VoucherJdbcRepository voucherJdbcRepository;

	private VoucherQueueRepository voucherQueueRepository;

	private FinancialConfigurationContractRepository financialConfigurationContractRepository;

	private VoucherESRepository voucherESRepository;

	@Autowired
	private LedgerJdbcRepository ledgerJdbcRepository;

	@Autowired
	private SubLedgerJdbcRepository subLedgerJdbcRepository;

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
		SubLedger savedDetail = null;
		SubLedgerEntity subLedgerEntity = null;

		for (Ledger ledger : voucher.getLedgers()) {

			ledgerEntity = new LedgerEntity().toEntity(ledger);
			ledgerEntity.setVoucherId(savedVoucher.getId());
			savedLedger = ledgerJdbcRepository.create(ledgerEntity).toDomain();

			if (ledger.getSubLedgers() != null && !ledger.getSubLedgers().isEmpty()) {

				Set<SubLedger> savedSubLedger = new LinkedHashSet<>();
				for (SubLedger detail : ledger.getSubLedgers()) {
					subLedgerEntity = new SubLedgerEntity().toEntity(detail);
					subLedgerEntity.setLedgerId(savedLedger.getId());
					savedDetail = subLedgerJdbcRepository.create(subLedgerEntity).toDomain();
					savedSubLedger.add(savedDetail);

				}

				savedLedger.setSubLedgers(savedSubLedger);
			}

			savedLedgers.add(savedLedger);

		}
		savedVoucher.setLedgers(savedLedgers);

		return savedVoucher;

	}

	@Transactional
	public Voucher update(Voucher voucher) {

		Voucher updatedVoucher = voucherJdbcRepository.update(new VoucherEntity().toEntity(voucher)).toDomain();

		Set<Ledger> updatedLedgers = new LinkedHashSet<>();
		Ledger updatedLedger = null;
		LedgerEntity ledgerEntity = null;
		SubLedger updatedDetail = null;
		SubLedgerEntity subLedgerEntity = null;

		VoucherSearch voucherSearch = new VoucherSearch();

		voucherSearch.setId(updatedVoucher.getId());
		voucherSearch.setTenantId(updatedVoucher.getTenantId());

		Pagination<Voucher> oldVoucher = search(voucherSearch);

		// Clear old ledger and subLedger

		if (null != oldVoucher && null != oldVoucher.getPagedData() && !oldVoucher.getPagedData().isEmpty()) {
			LOG.warn("oldVoucher---------------------" + oldVoucher);
			LOG.warn("oldVoucher.getPagedData()---------------------" + oldVoucher.getPagedData());

			for (Ledger ledger : oldVoucher.getPagedData().get(0).getLedgers()) {
				LOG.warn("oldVoucher ledger ---------------------" + ledger);
				if (ledger.getSubLedgers() != null && !ledger.getSubLedgers().isEmpty()) {

					for (SubLedger detail : ledger.getSubLedgers()) {
						subLedgerEntity = new SubLedgerEntity().toEntity(detail);
						subLedgerJdbcRepository.delete(subLedgerEntity);

					}

				}
				ledgerEntity = new LedgerEntity().toEntity(ledger);
				LOG.warn("ledgerEntity deleting ledgerEntity ---------------------" + ledgerEntity.getId()
						+ ledgerEntity.getTenantId());
				ledgerJdbcRepository.delete(ledgerEntity);

			}
		}
		// Add new ledgers and subLedger

		for (Ledger ledger : voucher.getLedgers()) {

			ledgerEntity = new LedgerEntity().toEntity(ledger);
			ledgerEntity.setVoucherId(updatedVoucher.getId());
			updatedLedger = ledgerJdbcRepository.create(ledgerEntity).toDomain();

			if (ledger.getSubLedgers() != null && !ledger.getSubLedgers().isEmpty()) {

				Set<SubLedger> updatedSubLedger = new LinkedHashSet<>();
				for (SubLedger detail : ledger.getSubLedgers()) {
					subLedgerEntity = new SubLedgerEntity().toEntity(detail);
					subLedgerEntity.setLedgerId(updatedLedger.getId());
					updatedDetail = subLedgerJdbcRepository.create(subLedgerEntity).toDomain();
					updatedSubLedger.add(updatedDetail);

				}

				updatedLedger.setSubLedgers(updatedSubLedger);
			}

			updatedLedgers.add(updatedLedger);

		}

		updatedVoucher.setLedgers(updatedLedgers);

		return updatedVoucher;
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
		return voucherJdbcRepository.uniqueCheck(fieldName, new VoucherEntity().toEntity(voucher));
	}

}