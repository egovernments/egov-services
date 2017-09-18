package org.egov.tradelicense.domain.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.tradelicense.domain.model.SupportDocument;
import org.egov.tradelicense.domain.model.SupportDocumentSearch;
import org.egov.tradelicense.persistence.entity.SupportDocumentEntity;
import org.egov.tradelicense.persistence.repository.SupportDocumentJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SupportDocumentRepository {

	@Autowired
	SupportDocumentJdbcRepository supportDocumentJdbcRepository;

	public Long getNextSequence() {

		String id = supportDocumentJdbcRepository.getSequence(SupportDocumentEntity.SEQUENCE_NAME);
		return Long.valueOf(id);

	}

	public Boolean uniqueCheck(String fieldName, SupportDocument supportDocument) {

		return supportDocumentJdbcRepository.uniqueCheck(fieldName,
				new SupportDocumentEntity().toEntity(supportDocument));
	}

	public Boolean idExistenceCheck(SupportDocument supportDocument) {

		return supportDocumentJdbcRepository.idExistenceCheck(new SupportDocumentEntity().toEntity(supportDocument));
	}

	public List<SupportDocument> search(SupportDocumentSearch supportDocumentSearch) {

		List<SupportDocument> supportDocuments = new ArrayList<SupportDocument>();

		List<SupportDocumentEntity> supportDocumentEntities = supportDocumentJdbcRepository
				.search(supportDocumentSearch);

		for (SupportDocumentEntity supportDocumentEntity : supportDocumentEntities) {

			SupportDocument supportDocument = supportDocumentEntity.toDomain();
			supportDocuments.add(supportDocument);
		}

		return supportDocuments;

	}
}