package org.egov.lams.repository.builder;

import org.egov.lams.model.DocumentType;
import org.egov.lams.model.enums.Application;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DocumentTypeQueryBuilderTest {

    @Test
    public void no_input_test() {
        Map params = new HashMap<>();
        assertEquals("SELECT * FROM eglams_documenttype documenttype", DocumentTypeQueryBuilder.getDocumentTypeQuery(new DocumentType(), params));
        assertEquals(0, params.size());
    }

    @Test
    public void both_input_test() {

        DocumentType documentType = new DocumentType();
        documentType.setTenantId("1");
        documentType.setApplication(Application.CREATE);
        Map params = new HashMap();
        assertEquals("SELECT * FROM eglams_documenttype documenttype WHERE documenttype.id is not null and documenttype.tenantId = :tenantId and documenttype.application = :application", DocumentTypeQueryBuilder.getDocumentTypeQuery(documentType, params));
        params.put(2, params.size());
    }

    @Test
    public void application_input_test() {

        DocumentType documentType = new DocumentType();
        documentType.setApplication(Application.CREATE);
        Map params = new HashMap();
        assertEquals("SELECT * FROM eglams_documenttype documenttype WHERE documenttype.id is not null and documenttype.application = :application", DocumentTypeQueryBuilder.getDocumentTypeQuery(documentType, params));
        assertEquals(1, params.size());
    }

    @Test
    public void tenantId_input_test() {

        DocumentType documentType = new DocumentType();
        documentType.setTenantId("1");
        Map params = new HashMap();
        assertEquals("SELECT * FROM eglams_documenttype documenttype WHERE documenttype.id is not null and documenttype.tenantId = :tenantId", DocumentTypeQueryBuilder.getDocumentTypeQuery(documentType, params));
        assertEquals(1, params.size());
    }

}
