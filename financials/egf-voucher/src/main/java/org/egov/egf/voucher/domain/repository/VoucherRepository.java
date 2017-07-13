package org.egov.egf.voucher.domain.repository;

import java.util.LinkedHashSet;
import java.util.Set;

import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.CommonRequest;
import org.egov.egf.voucher.domain.model.Ledger;
import org.egov.egf.voucher.domain.model.LedgerDetail;
import org.egov.egf.voucher.domain.model.Voucher;
import org.egov.egf.voucher.domain.model.VoucherSearch;
import org.egov.egf.voucher.domain.model.Vouchermis;
import org.egov.egf.voucher.persistence.entity.LedgerDetailEntity;
import org.egov.egf.voucher.persistence.entity.LedgerEntity;
import org.egov.egf.voucher.persistence.entity.VoucherEntity;
import org.egov.egf.voucher.persistence.entity.VouchermisEntity;
import org.egov.egf.voucher.persistence.repository.LedgerDetailJdbcRepository;
import org.egov.egf.voucher.persistence.repository.LedgerJdbcRepository;
import org.egov.egf.voucher.persistence.repository.VoucherJdbcRepository;
import org.egov.egf.voucher.persistence.repository.VouchermisJdbcRepository;
import org.egov.egf.voucher.web.contract.VoucherContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VoucherRepository {

	@Autowired
	private VoucherJdbcRepository voucherJdbcRepository;
	/*@Autowired
	private MastersQueueRepository voucherQueueRepository;*/
	@Autowired
	private VouchermisJdbcRepository vouchermisJdbcRepository;
	@Autowired
	private LedgerJdbcRepository ledgerJdbcRepository;
	@Autowired
	private LedgerDetailJdbcRepository ledgerDetailJdbcRepository;

	public Voucher findById(Voucher voucher) {
		return voucherJdbcRepository.findById(new VoucherEntity().toEntity(voucher)).toDomain();

	}

	@Transactional
	public Voucher save(Voucher voucher) {
		Voucher savedVoucher= voucherJdbcRepository.create(new VoucherEntity().toEntity(voucher)).toDomain();
		VouchermisEntity misEntity = new VouchermisEntity().toEntity(voucher.getVouchermis());
		misEntity.setVoucherId(savedVoucher.getId());
		Vouchermis savedMis=vouchermisJdbcRepository.create(misEntity).toDomain();
		savedVoucher.setVouchermis(savedMis);
		Set<Ledger> savedLedgers=new LinkedHashSet<>();
		Ledger savedLedger=null;
		LedgerEntity ledgerEntity =null;
		LedgerDetail savedDetail=null;
		LedgerDetailEntity ledgerDetailEntity=null;
		for(Ledger ledger: voucher.getLedgers() )
		{
			ledgerEntity = new LedgerEntity().toEntity(ledger);
			ledgerEntity.setVoucherId(savedVoucher.getId());
			savedLedger=ledgerJdbcRepository.create(ledgerEntity).toDomain();
			if(ledger.getLedgerDetails()!=null && !ledger.getLedgerDetails().isEmpty())
			{
				Set<LedgerDetail> savedLedgerDetails=new LinkedHashSet<>();
				for(LedgerDetail detail:ledger.getLedgerDetails())
				{
					ledgerDetailEntity=new LedgerDetailEntity().toEntity(detail);
					ledgerDetailEntity.setLedgerId(savedLedger.getId());
					savedDetail=ledgerDetailJdbcRepository.create(ledgerDetailEntity).toDomain();
					savedLedgerDetails.add(savedDetail); 
					
				}
				savedLedger.setLedgerDetails(savedLedgerDetails);
			}
			savedLedgers.add(savedLedger);
		}
		savedVoucher.setLedgers(savedLedgers);
		return savedVoucher;
	}

	public Voucher update(Voucher entity) {
		return voucherJdbcRepository.update(new VoucherEntity().toEntity(entity)).toDomain();
	}

	public void add(CommonRequest<VoucherContract> request) {
		
		
		//return voucherJdbcRepository.create(new VoucherEntity().toEntity(entity)).toDomain();
	}

	public Pagination<Voucher> search(VoucherSearch domain) {

		return voucherJdbcRepository.search(domain);

	}

}