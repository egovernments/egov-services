package org.egov.lams.repository.builder;

import java.util.List;

import org.egov.lams.model.DocumentType;

public class DocumentTypeQueryBuilder {

	 public static String getDocumentTypeQuery(DocumentType documentType,List<Object> preparedStatementValues){
		 
		StringBuilder baseQuery = new StringBuilder("SELECT * FROM eglams_documenttype documenttype");
		 
		 if(!(documentType.getApplication() == null && documentType.getTenantId() == null)){
			
			 baseQuery.append(" WHERE");
			 boolean isAppendAndClause = false;
			 
			 if (documentType.getTenantId() != null) {
					baseQuery.append(" documenttype.tenantId=?");
					isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, baseQuery);
					preparedStatementValues.add(documentType.getTenantId());
				}

				if (documentType.getApplication() != null) {
					isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, baseQuery);
					baseQuery.append(" documenttype.application=?");
					preparedStatementValues.add(documentType.getApplication());
				}
					 
		 }
		 
		 return baseQuery.toString();
	 }
	 
	 private static boolean addAndClauseIfRequired(boolean appendAndClauseFlag, StringBuilder queryString) {

			if (appendAndClauseFlag) {
				queryString.append(" AND");
			}
			return true;
		}

	 
}
