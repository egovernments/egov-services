package org.egov.lams.repository.builder;

import org.egov.lams.model.DocumentType;

import java.util.Map;

public class DocumentTypeQueryBuilder {

    public static String getDocumentTypeQuery(DocumentType documentType, Map params) {

        StringBuilder baseQuery = new StringBuilder("SELECT * FROM eglams_documenttype documenttype");

        if (!(documentType.getApplication() == null && documentType.getTenantId() == null)) {

            baseQuery.append(" WHERE documenttype.id is not null");

            if (documentType.getTenantId() != null) {
                baseQuery.append(" and documenttype.tenantId = :tenantId");
                params.put("tenantId", documentType.getTenantId());
            }

            if (documentType.getApplication() != null) {
                baseQuery.append(" and documenttype.application = :application");
                params.put("application", documentType.getApplication());
            }
        }

        return baseQuery.toString();
    }

}
