package org.egov.wcms.repository;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.wcms.model.DocumentTypeApplicationType;
import org.egov.wcms.repository.builder.DocumentTypeApplicationTypeQueryBuilder;
import org.egov.wcms.repository.rowmapper.DocumentTypeApplicationTypeMapper;
import org.egov.wcms.web.contract.DocumentTypeApplicationTypeGetRequest;
import org.egov.wcms.web.contract.DocumentTypeApplicationTypeReq;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;




@RunWith(MockitoJUnitRunner.class)
public class DocumentTypeApplicationTypeRepositoryTest {

	@Mock
	private JdbcTemplate jdbcTemplate;
	
	
	@Mock
	private DocumentTypeApplicationTypeQueryBuilder docTypeAppTypeQueryBuilder;

	@InjectMocks
	private DocumentTypeApplicationTypeRepository docTypeAppTypeRepository;
	
	  
    @Mock
    private DocumentTypeApplicationTypeMapper docTypeAppliTypeRowMapper;
	
	
	@Test
	public void test_Should_Create_ApplicationTypeDocType() {
		DocumentTypeApplicationTypeReq docNameRequest = new DocumentTypeApplicationTypeReq();
		RequestInfo requestInfo = new RequestInfo();
		User user = new User();
		user.setId(2L);
		requestInfo.setUserInfo(user);
		docNameRequest.setRequestInfo(requestInfo);
		DocumentTypeApplicationType applicationTypDoce = Mockito.mock(DocumentTypeApplicationType.class);
		docNameRequest.setDocumentTypeApplicationType(applicationTypDoce);

		when(jdbcTemplate.update(any(String.class), any(Object[].class))).thenReturn(1);
		assertTrue(docNameRequest.equals(docTypeAppTypeRepository.persistCreateDocTypeApplicationType(docNameRequest)));
	}
	
	@Test
	public void test_Should_Create_ApplicationTypeDocType_NotNullCheck() {
		
		DocumentTypeApplicationTypeReq applicationTypeDocReq = getApplicationTypeDoc();
		Object[] obj = new Object[] {applicationTypeDocReq.getDocumentTypeApplicationType().getId(), applicationTypeDocReq.getDocumentTypeApplicationType().getApplicationType(), applicationTypeDocReq.getDocumentTypeApplicationType().getDocumentType(), applicationTypeDocReq.getDocumentTypeApplicationType().getActive(),
				applicationTypeDocReq.getDocumentTypeApplicationType().getTenantId(), new Date(new java.util.Date().getTime()), applicationTypeDocReq.getRequestInfo().getUserInfo().getId() };
	    when(jdbcTemplate.update("query", obj)).thenReturn(1);
		assertNotNull(docTypeAppTypeRepository.persistCreateDocTypeApplicationType(applicationTypeDocReq));
	}
	
	private DocumentTypeApplicationTypeReq getApplicationTypeDoc() {
		DocumentTypeApplicationTypeReq docAppliTypeRequest = new DocumentTypeApplicationTypeReq();
		DocumentTypeApplicationType applicationDocType = new DocumentTypeApplicationType();
		applicationDocType.setActive(true);
		applicationDocType.setId(2L);
		applicationDocType.setDocumentTypeId(1234);
		applicationDocType.setTenantId("DEFAULT");
		applicationDocType.setApplicationType("New Connection");
		User user = new User();
		user.setId(2L);
		RequestInfo requestInfo = new RequestInfo();
		requestInfo.setUserInfo(user);
		docAppliTypeRequest.setDocumentTypeApplicationType(applicationDocType);
		docAppliTypeRequest.setRequestInfo(requestInfo);
		return docAppliTypeRequest;
	}
	
	@Test
	public void test_Should_Find_ApplicationTypeDocType_Valid() {
		List<Object> preparedStatementValues = new ArrayList<Object>();
		DocumentTypeApplicationTypeGetRequest docNameGetRequest = Mockito.mock(DocumentTypeApplicationTypeGetRequest.class);
		String queryString = "MyQuery" ;
		when(docTypeAppTypeQueryBuilder.getQuery(docNameGetRequest, preparedStatementValues)).thenReturn(queryString);
		List<DocumentTypeApplicationType> appTypeDocs = new ArrayList<>();
		when(jdbcTemplate.query(queryString, preparedStatementValues.toArray(), docTypeAppliTypeRowMapper)).thenReturn(appTypeDocs);

		assertTrue(appTypeDocs.equals(docTypeAppTypeRepository.findForCriteria(docNameGetRequest)));
	}

	@Test
	public void test_Should_Find_ApplicationTypeDocType_Invalid() {
		List<Object> preparedStatementValues = new ArrayList<Object>();
		DocumentTypeApplicationTypeGetRequest applicationTypeGetRequest = Mockito.mock(DocumentTypeApplicationTypeGetRequest.class);
		String queryString = "MyQuery" ;
		when(docTypeAppTypeQueryBuilder.getQuery(applicationTypeGetRequest, preparedStatementValues)).thenReturn(null);
		List<DocumentTypeApplicationType> applicationTypeDocs = new ArrayList<>();
		when(jdbcTemplate.query(queryString, preparedStatementValues.toArray(), docTypeAppliTypeRowMapper))
				.thenReturn(applicationTypeDocs);

		assertTrue(applicationTypeDocs.equals(docTypeAppTypeRepository.findForCriteria(applicationTypeGetRequest)));
	}
	
	@Test
	   public void test_Should_Update_ApplicationTypeDocumentType() {

	       final DocumentTypeApplicationTypeReq applicationDocumentRequest = new DocumentTypeApplicationTypeReq();
	       final RequestInfo requestInfo = new RequestInfo();
	       final User user = new User();
	       user.setId(1l);
	       requestInfo.setUserInfo(user);
	       applicationDocumentRequest.setRequestInfo(requestInfo);
	       final DocumentTypeApplicationType documentApplication = new DocumentTypeApplicationType();
	       applicationDocumentRequest.setDocumentTypeApplicationType(documentApplication);

	       when(jdbcTemplate.update(any(String.class), any(Object[].class))).thenReturn(1);
	       assertTrue(applicationDocumentRequest
	               .equals(docTypeAppTypeRepository.persistModifyDocTypeApplicationType(applicationDocumentRequest)));
	   }

	
	
}

