package org.egov.lams.service;

import java.util.List;

import org.egov.lams.model.DocumentType;
import org.egov.lams.repository.DocumentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentTypeService {

	@Autowired
	private DocumentTypeRepository documentTypeRepository;

	public List<DocumentType> getDocumentTypes(DocumentType documentType) {
		return documentTypeRepository.getDocumentTypes(documentType);
	}
}
