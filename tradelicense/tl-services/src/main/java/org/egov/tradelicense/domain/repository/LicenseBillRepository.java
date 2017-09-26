package org.egov.tradelicense.domain.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.tl.commons.web.contract.LicenseBill;
import org.egov.tradelicense.domain.model.LicenseBillSearch;
import org.egov.tradelicense.persistence.entity.LicenseBillEntity;
import org.egov.tradelicense.persistence.repository.LicenseBillJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LicenseBillRepository {

	@Autowired
	LicenseBillJdbcRepository licenseBillJdbcRepository;

	public Long getNextSequence() {

		String id = licenseBillJdbcRepository.getSequence(LicenseBillEntity.SEQUENCE_NAME);
		return Long.valueOf(id);

	}


	public Boolean idExistenceCheck(LicenseBill licenseBill) {

		return licenseBillJdbcRepository
				.idExistenceCheck(new LicenseBillEntity().toEntity(licenseBill));
	}

	public List<LicenseBill> search(LicenseBillSearch licenseBillSearch) {

		List<LicenseBill> licenseBills = new ArrayList<LicenseBill>();

		List<LicenseBillEntity> licenseBillEntities = licenseBillJdbcRepository
				.search(licenseBillSearch);

		for (LicenseBillEntity licenseBillEntity : licenseBillEntities) {

			LicenseBill licenseBill = licenseBillEntity.toDomain();
			licenseBills.add(licenseBill);
		}

		return licenseBills;
	}
}