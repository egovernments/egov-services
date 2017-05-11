package org.egov.lams.repository.builder;

import static org.junit.Assert.*;
import java.util.ArrayList;
import org.egov.lams.model.DocumentType;
import org.egov.lams.model.enums.Application;
import org.junit.Test;

public class DocumentTypeQueryBuilderTest {

	@Test
	public void no_input_test(){
		
		assertEquals("SELECT * FROM eglams_documenttype documenttype", DocumentTypeQueryBuilder.getDocumentTypeQuery(new DocumentType(), new ArrayList<>()));
		
	}
	
	@Test
	public void both_input_test(){
		
		DocumentType documentType = new DocumentType();
		documentType.setTenantId("1");
		documentType.setApplication(Application.CREATE);
		
		assertEquals("SELECT * FROM eglams_documenttype documenttype WHERE documenttype.tenantId=? AND documenttype.application=?", DocumentTypeQueryBuilder.getDocumentTypeQuery(documentType, new ArrayList<>()));
		
	}
	
	@Test
	public void application_input_test(){
		
		DocumentType documentType = new DocumentType();
		documentType.setApplication(Application.CREATE);
		
		assertEquals("SELECT * FROM eglams_documenttype documenttype WHERE documenttype.application=?", DocumentTypeQueryBuilder.getDocumentTypeQuery(documentType, new ArrayList<>()));
		
	}
	
	@Test
	public void tenantId_input_test(){
		
		DocumentType documentType = new DocumentType();
		documentType.setTenantId("1");
		
		assertEquals("SELECT * FROM eglams_documenttype documenttype WHERE documenttype.tenantId=?", DocumentTypeQueryBuilder.getDocumentTypeQuery(documentType, new ArrayList<>()));
		
	}

}
