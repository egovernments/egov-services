package org.egov.egf.voucher.domain.repository;

import org.egov.common.domain.model.Pagination;
import org.egov.egf.voucher.domain.model.LedgerDetail;
import org.egov.egf.voucher.persistence.entity.LedgerDetailEntity;
import org.egov.egf.voucher.persistence.repository.LedgerDetailJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LedgerDetailRepository {

	@Autowired
	private LedgerDetailJdbcRepository ledgerDetailJdbcRepository;
	
	public LedgerDetail findById(LedgerDetail ledgerDetail) {
		return ledgerDetailJdbcRepository.findById(new LedgerDetailEntity().toEntity(ledgerDetail)).toDomain();

	}

	public LedgerDetail save(LedgerDetail ledgerDetail) {
		return ledgerDetailJdbcRepository.create(new LedgerDetailEntity().toEntity(ledgerDetail)).toDomain();
	}

	public LedgerDetail update(LedgerDetail entity) {
		return ledgerDetailJdbcRepository.update(new LedgerDetailEntity().toEntity(entity)).toDomain();
	}

	

/*	public Pagination<LedgerDetail> search(LedgerDetailSearch domain) {

		return ledgerDetailJdbcRepository.search(domain);

	}
*/
}