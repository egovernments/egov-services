package org.egov.works.services.domain.repository;

import java.util.List;

import org.egov.works.services.web.contract.OfflineStatus;
import org.egov.works.services.web.contract.OfflineStatusSearchContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OfflineStatusRepository {

	@Autowired
	private OfflineStatusJdbcRepository offlineStatusJdbcRepository;

	public List<OfflineStatus> search(OfflineStatusSearchContract offlineStatusSearchContract) {
		return offlineStatusJdbcRepository.search(offlineStatusSearchContract);
	}
}
