package org.egov.tradelicense.domain.service;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.tl.commons.web.contract.AuditDetails;
import org.egov.tl.commons.web.contract.DocumentType;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.requests.ResponseInfoFactory;
import org.egov.tl.masters.domain.repository.DocumentTypeDomainRepository;
import org.egov.tl.masters.domain.service.DocumentTypeV2Service;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.configuration.TestConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;

@Import(TestConfiguration.class)
@RunWith(SpringRunner.class)
public class DocumentTypeV2ServiceTest {

	@InjectMocks
	DocumentTypeV2Service tradeLicenseService;

	@Mock
	private SmartValidator validator;

	@Mock
	private DocumentTypeDomainRepository documentTypeDomainRepository;



	@Mock
	private PropertiesManager propertiesManager;

	@Mock
	private ResponseInfoFactory responseInfoFactory;

	private BindingResult errors = new BeanPropertyBindingResult(null, null);

	private List<DocumentType> documentTypes = new ArrayList<>();

	
	@Mock
	private RequestInfo requestInfo = new RequestInfo();

	@Before
	public void setup() {

	}

	@Test
	public final void testvalidateRelated() {

		tradeLicenseService.validateRelated(documentTypes, requestInfo, true);
	}


	@Test
	public final void testAdd() {
		
		tradeLicenseService.add(documentTypes, requestInfo);
	}
	
	
	/*@Test
	public final void testSearch() {
		when(tradeLicenseService.search(any(RequestInfo.class), any(String.class), any(Integer[].class), any(String.class),
				any(String.class), any(String.class), any(String.class), any(Integer.class),
				any(Integer.class), any(Integer.class), any(Integer.class))).thenReturn(documentTypes);
	}*/

	private List<DocumentType> getDocumentTypes() {
		AuditDetails auditDetails = new AuditDetails();
		DocumentType documentType = new DocumentType();
		documentType.setAuditDetails(auditDetails);
		List<DocumentType> documentTypes = new ArrayList<>();
		//documentTypes.add(documentType);
		return documentTypes;
	}
	

}