package org.egov.lams.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;

import org.egov.lams.model.DocumentType;
import org.egov.lams.repository.DocumentTypeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class DocumentTypeServiceTest {

	@Mock
	private DocumentTypeRepository documentTypeRepository;

	@InjectMocks
	private DocumentTypeService documentTypeService;

	@Test
	public void getDocumentTypesTest() {
		List<DocumentType> documentTypes = new ArrayList<>();
		DocumentType documentType = new DocumentType();
		documentType.setId(1l);

		// getDocumentTypes(DocumentType documentType)
		when(documentTypeRepository.getDocumentTypes(any(DocumentType.class))).thenReturn(documentTypes);
		assertTrue(documentTypes.equals(documentTypeService.getDocumentTypes(any(DocumentType.class))));
	}
}