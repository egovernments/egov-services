package org.egov.tl.masters.domain.repository;

import java.util.HashMap;
import java.util.Map;

import org.egov.tl.commons.web.contract.DocumentType;
import org.egov.tl.commons.web.requests.DocumentTypeV2Request;
import org.egov.tl.masters.persistence.entity.DocumentTypeEntity;
import org.egov.tl.masters.persistence.queue.DocumentTypeQueueRepository;
import org.egov.tl.masters.persistence.repository.DocumentTypeJdbcRepository;
import org.egov.tradelicense.config.PropertiesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DocumentTypeDomainRepository {

	@Autowired
	DocumentTypeQueueRepository documentTypeQueueRepository;
	
	@Autowired
	PropertiesManager propertiesManager;
	
	@Autowired
	DocumentTypeJdbcRepository documentTypeJdbcRepository;
	
	
	public void add(DocumentTypeV2Request request) {

		Map<String, Object> message = new HashMap<>();
		message.put(propertiesManager.getCreateDocumentTypeV2Validated(), request);
		documentTypeQueueRepository.add(message);
	}

	public void update(DocumentTypeV2Request request) {

		Map<String, Object> message = new HashMap<>();
		message.put(propertiesManager.getUpdateDocumentTypeV2Validated(), request);
		documentTypeQueueRepository.add(message);
	}
	
	
	public String getCategoryName(String category) {

		return documentTypeJdbcRepository.getCategoryName(category);
	}
	
	@Transactional
	public DocumentType save(DocumentType documentType) {

		DocumentTypeEntity entity = documentTypeJdbcRepository.create(new DocumentTypeEntity().toEntity(documentType));

		return entity.toDomain();
	}

	@Transactional
	public DocumentType update(DocumentType documentType) {

		DocumentTypeEntity entity = documentTypeJdbcRepository.update(new DocumentTypeEntity().toEntity(documentType));

		return entity.toDomain();
	}

}
