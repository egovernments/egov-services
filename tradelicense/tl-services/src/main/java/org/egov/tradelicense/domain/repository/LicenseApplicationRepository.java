package org.egov.tradelicense.domain.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.tradelicense.domain.model.LicenseApplication;
import org.egov.tradelicense.domain.model.LicenseApplicationSearch;
import org.egov.tradelicense.persistence.entity.LicenseApplicationEntity;
import org.egov.tradelicense.persistence.repository.LicenseApplicationJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LicenseApplicationRepository {

	@Autowired
	LicenseApplicationJdbcRepository licenseApplicationJdbcRepository;

	public Long getNextSequence() {

		String id = licenseApplicationJdbcRepository.getSequence(LicenseApplicationEntity.SEQUENCE_NAME);
		return Long.valueOf(id);

	}

	public Boolean uniqueCheck(String fieldName, LicenseApplication licenseApplication) {

		return licenseApplicationJdbcRepository.uniqueCheck(fieldName,
				new LicenseApplicationEntity().toEntity(licenseApplication));
	}

	public Boolean idExistenceCheck(LicenseApplication licenseApplication) {

		return licenseApplicationJdbcRepository
				.idExistenceCheck(new LicenseApplicationEntity().toEntity(licenseApplication));
	}

	public List<LicenseApplication> search(LicenseApplicationSearch licenseApplicationSearch) {

		List<LicenseApplication> licenseApplications = new ArrayList<LicenseApplication>();

		List<LicenseApplicationEntity> licenseApplicationEntities = licenseApplicationJdbcRepository
				.search(licenseApplicationSearch);

		for (LicenseApplicationEntity licenseApplicationEntity : licenseApplicationEntities) {

			LicenseApplication licenseApplication = licenseApplicationEntity.toDomain();
			licenseApplications.add(licenseApplication);
		}

		return licenseApplications;
	}
}