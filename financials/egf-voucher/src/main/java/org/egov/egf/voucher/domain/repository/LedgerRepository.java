package org.egov.egf.voucher.domain.repository;

import org.egov.egf.voucher.domain.model.Ledger;
import org.egov.egf.voucher.persistence.entity.LedgerEntity;
import org.egov.egf.voucher.persistence.repository.LedgerJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LedgerRepository {

	@Autowired
	private LedgerJdbcRepository ledgerJdbcRepository;
	
	public Ledger findById(Ledger ledger) {
		return ledgerJdbcRepository.findById(new LedgerEntity().toEntity(ledger)).toDomain();

	}

	public Ledger save(Ledger ledger) {
		return ledgerJdbcRepository.create(new LedgerEntity().toEntity(ledger)).toDomain();
	}

	public Ledger update(Ledger entity) {
		return ledgerJdbcRepository.update(new LedgerEntity().toEntity(entity)).toDomain();
	}

	

	/*public Pagination<Ledger> search(LedgerSearch domain) {

		return ledgerJdbcRepository.search(domain);

	}
*/
}