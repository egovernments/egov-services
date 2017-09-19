package org.egov.tradelicense.domain.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.tradelicense.domain.model.LicenseFeeDetail;
import org.egov.tradelicense.domain.model.LicenseFeeDetailSearch;
import org.egov.tradelicense.persistence.entity.LicenseFeeDetailEntity;
import org.egov.tradelicense.persistence.repository.LicenseFeeDetailJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LicenseFeeDetailRepository {

	@Autowired
	LicenseFeeDetailJdbcRepository licenseFeeDetailJdbcRepository;

	public Long getNextSequence() {

		String id = licenseFeeDetailJdbcRepository.getSequence(LicenseFeeDetailEntity.SEQUENCE_NAME);
		return Long.valueOf(id);

	}

	public Boolean uniqueCheck(String fieldName, LicenseFeeDetail licenseFeeDetail) {

		return licenseFeeDetailJdbcRepository.uniqueCheck(fieldName,
				new LicenseFeeDetailEntity().toEntity(licenseFeeDetail));
	}

	public Boolean idExistenceCheck(LicenseFeeDetail licenseFeeDetail) {

		return licenseFeeDetailJdbcRepository.idExistenceCheck(new LicenseFeeDetailEntity().toEntity(licenseFeeDetail));
	}

	public List<LicenseFeeDetail> search(LicenseFeeDetailSearch licenseFeeDetailSearch) {

		List<LicenseFeeDetail> licenseFeeDetails = new ArrayList<LicenseFeeDetail>();

		List<LicenseFeeDetailEntity> licenseFeeDetailEntities = licenseFeeDetailJdbcRepository
				.search(licenseFeeDetailSearch);

		for (LicenseFeeDetailEntity licenseFeeDetailEntity : licenseFeeDetailEntities) {

			LicenseFeeDetail licenseFeeDetail = licenseFeeDetailEntity.toDomain();
			licenseFeeDetails.add(licenseFeeDetail);
		}

		return licenseFeeDetails;

	}
}