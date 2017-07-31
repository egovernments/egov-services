package org.egov.egf.instrument.web.mapper;

import java.util.HashSet;
import java.util.Set;

import org.egov.egf.instrument.domain.model.Instrument;
import org.egov.egf.instrument.domain.model.InstrumentSearch;
import org.egov.egf.instrument.domain.model.InstrumentVoucher;
import org.egov.egf.instrument.domain.model.TransactionType;
import org.egov.egf.instrument.web.contract.InstrumentContract;
import org.egov.egf.instrument.web.contract.InstrumentSearchContract;
import org.egov.egf.instrument.web.contract.InstrumentVoucherContract;
import org.egov.egf.instrument.web.contract.TransactionTypeContract;

public class InstrumentMapper {

	private InstrumentTypeMapper typeMapper = new InstrumentTypeMapper();
	private SurrenderReasonMapper srMapper = new SurrenderReasonMapper();

	public Instrument toDomain(InstrumentContract contract) {

		Instrument instrument = new Instrument();

		instrument.setId(contract.getId());
		instrument.setAmount(contract.getAmount());
		instrument.setBank(contract.getBank());
		instrument.setBankAccount(contract.getBankAccount());
		instrument.setBranchName(contract.getBranchName());
		instrument.setDrawer(contract.getDrawer());
		instrument.setFinancialStatus(contract.getFinancialStatus());

		if (contract.getInstrumentType() != null)
			instrument.setInstrumentType(typeMapper.toDomain(contract.getInstrumentType()));

		if (contract.getSurrenderReason() != null)
			instrument.setSurrenderReason(srMapper.toDomain(contract.getSurrenderReason()));

		if (contract.getInstrumentVouchers() != null) {

			Set<InstrumentVoucher> instrumentVouchers = new HashSet<>();

			if (contract.getInstrumentVouchers() != null)
				for (InstrumentVoucherContract ivc : contract.getInstrumentVouchers()) {
					instrumentVouchers.add(InstrumentVoucher.builder().instrument(toDomain(ivc.getInstrument()))
							.voucherHeaderId(ivc.getVoucherHeaderId()).build());
				}

			instrument.setInstrumentVouchers(instrumentVouchers);

		}

		instrument.setPayee(contract.getPayee());
		instrument.setSerialNo(contract.getSerialNo());
		instrument.setTransactionDate(contract.getTransactionDate());
		instrument.setTransactionNumber(contract.getTransactionNumber());
		instrument.setTransactionType(contract.getTransactionType() != null
				? TransactionType.valueOf(contract.getTransactionType().name()) : null);
		instrument.setCreatedBy(contract.getCreatedBy());
		instrument.setCreatedDate(contract.getCreatedDate());
		instrument.setLastModifiedBy(contract.getLastModifiedBy());
		instrument.setLastModifiedDate(contract.getLastModifiedDate());
		instrument.setTenantId(contract.getTenantId());

		return instrument;
	}

	public InstrumentContract toContract(Instrument instrument) {

		InstrumentContract contract = new InstrumentContract();

		contract.setId(instrument.getId());
		contract.setAmount(instrument.getAmount());
		contract.setBank(instrument.getBank());
		contract.setBankAccount(instrument.getBankAccount());
		contract.setBranchName(instrument.getBranchName());
		contract.setDrawer(instrument.getDrawer());
		contract.setFinancialStatus(instrument.getFinancialStatus());

		if (instrument.getInstrumentType() != null)
			contract.setInstrumentType(typeMapper.toContract(instrument.getInstrumentType()));

		if (instrument.getSurrenderReason() != null)
			contract.setSurrenderReason(srMapper.toContract(instrument.getSurrenderReason()));

		if (instrument.getInstrumentVouchers() != null) {

			Set<InstrumentVoucherContract> instrumentVouchers = new HashSet<>();

			if (instrument.getInstrumentVouchers() != null)
				for (InstrumentVoucher iv : instrument.getInstrumentVouchers()) {
					instrumentVouchers
							.add(InstrumentVoucherContract.builder().instrument(toContract(iv.getInstrument()))
									.voucherHeaderId(iv.getVoucherHeaderId()).build());
				}

			contract.setInstrumentVouchers(instrumentVouchers);

		}

		contract.setPayee(instrument.getPayee());
		contract.setSerialNo(instrument.getSerialNo());
		contract.setTransactionDate(instrument.getTransactionDate());
		contract.setTransactionNumber(instrument.getTransactionNumber());
		contract.setTransactionType(instrument.getTransactionType() != null
				? TransactionTypeContract.valueOf(instrument.getTransactionType().name()) : null);
		contract.setCreatedBy(instrument.getCreatedBy());
		contract.setCreatedDate(instrument.getCreatedDate());
		contract.setLastModifiedBy(instrument.getLastModifiedBy());
		contract.setLastModifiedDate(instrument.getLastModifiedDate());
		contract.setTenantId(instrument.getTenantId());

		return contract;
	}

	public InstrumentSearch toSearchDomain(InstrumentSearchContract contract) {

		InstrumentSearch instrumentSearch = new InstrumentSearch();

		instrumentSearch.setId(contract.getId());
		instrumentSearch.setAmount(contract.getAmount());
		instrumentSearch.setBank(contract.getBank());
		instrumentSearch.setBankAccount(contract.getBankAccount());
		instrumentSearch.setBranchName(contract.getBranchName());
		instrumentSearch.setDrawer(contract.getDrawer());
		instrumentSearch.setFinancialStatus(contract.getFinancialStatus());

		if (contract.getInstrumentType() != null)
			instrumentSearch.setInstrumentType(typeMapper.toDomain(contract.getInstrumentType()));

		if (contract.getSurrenderReason() != null)
			instrumentSearch.setSurrenderReason(srMapper.toDomain(contract.getSurrenderReason()));

		if (contract.getInstrumentVouchers() != null) {

			Set<InstrumentVoucher> instrumentVouchers = new HashSet<>();
			if (contract.getInstrumentVouchers() != null)
				for (InstrumentVoucherContract ivc : contract.getInstrumentVouchers()) {
					instrumentVouchers.add(InstrumentVoucher.builder().instrument(toDomain(ivc.getInstrument()))
							.voucherHeaderId(ivc.getVoucherHeaderId()).build());
				}

			instrumentSearch.setInstrumentVouchers(instrumentVouchers);

		}
		instrumentSearch.setPayee(contract.getPayee());
		instrumentSearch.setSerialNo(contract.getSerialNo());
		instrumentSearch.setTransactionDate(contract.getTransactionDate());
		instrumentSearch.setTransactionNumber(contract.getTransactionNumber());
		instrumentSearch.setTransactionType(contract.getTransactionType() != null
				? TransactionType.valueOf(contract.getTransactionType().name()) : null);
		instrumentSearch.setCreatedBy(contract.getCreatedBy());
		instrumentSearch.setCreatedDate(contract.getCreatedDate());
		instrumentSearch.setLastModifiedBy(contract.getLastModifiedBy());
		instrumentSearch.setLastModifiedDate(contract.getLastModifiedDate());
		instrumentSearch.setTenantId(contract.getTenantId());
		instrumentSearch.setPageSize(contract.getPageSize());
		instrumentSearch.setOffset(contract.getOffset());
		instrumentSearch.setSortBy(contract.getSortBy());
		instrumentSearch.setIds(contract.getIds());

		return instrumentSearch;
	}

	public InstrumentSearchContract toSearchContract(InstrumentSearch instrumentSearch) {

		InstrumentSearchContract contract = new InstrumentSearchContract();

		contract.setId(instrumentSearch.getId());
		contract.setAmount(instrumentSearch.getAmount());
		contract.setBank(instrumentSearch.getBank());
		contract.setBankAccount(instrumentSearch.getBankAccount());
		contract.setBranchName(instrumentSearch.getBranchName());
		contract.setDrawer(instrumentSearch.getDrawer());
		contract.setFinancialStatus(instrumentSearch.getFinancialStatus());

		if (instrumentSearch.getInstrumentType() != null)
			contract.setInstrumentType(typeMapper.toContract(instrumentSearch.getInstrumentType()));

		if (instrumentSearch.getSurrenderReason() != null)
			contract.setSurrenderReason(srMapper.toContract(instrumentSearch.getSurrenderReason()));

		if (instrumentSearch.getInstrumentVouchers() != null) {

			Set<InstrumentVoucherContract> instrumentVouchers = new HashSet<>();

			if (instrumentSearch.getInstrumentVouchers() != null)
				for (InstrumentVoucher iv : instrumentSearch.getInstrumentVouchers()) {
					instrumentVouchers
							.add(InstrumentVoucherContract.builder().instrument(toContract(iv.getInstrument()))
									.voucherHeaderId(iv.getVoucherHeaderId()).build());
				}

			contract.setInstrumentVouchers(instrumentVouchers);

		}

		contract.setPayee(instrumentSearch.getPayee());
		contract.setSerialNo(instrumentSearch.getSerialNo());
		contract.setTransactionDate(instrumentSearch.getTransactionDate());
		contract.setTransactionNumber(instrumentSearch.getTransactionNumber());
		contract.setTransactionType(instrumentSearch.getTransactionType() != null
				? TransactionTypeContract.valueOf(instrumentSearch.getTransactionType().name()) : null);
		contract.setCreatedBy(instrumentSearch.getCreatedBy());
		contract.setCreatedDate(instrumentSearch.getCreatedDate());
		contract.setLastModifiedBy(instrumentSearch.getLastModifiedBy());
		contract.setLastModifiedDate(instrumentSearch.getLastModifiedDate());
		contract.setTenantId(instrumentSearch.getTenantId());
		contract.setPageSize(instrumentSearch.getPageSize());
		contract.setOffset(instrumentSearch.getOffset());
		contract.setSortBy(instrumentSearch.getSortBy());
		contract.setIds(instrumentSearch.getIds());

		return contract;
	}

}