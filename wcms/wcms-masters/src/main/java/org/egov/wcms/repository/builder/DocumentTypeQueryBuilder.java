/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.wcms.repository.builder;

import java.util.List;

import org.egov.wcms.config.ApplicationProperties;
import org.egov.wcms.web.contract.DocumentTypeGetReq;
import org.egov.wcms.web.contract.UsageTypeGetRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DocumentTypeQueryBuilder {
	
	@Autowired
	private ApplicationProperties applicationProperties;
	
	private static final Logger logger = LoggerFactory.getLogger(UsageTypeQueryBuilder.class);
    private static final String BASE_QUERY = "SELECT * FROM egwtr_document_type docType";
    
    public String getPersistQuery(){
    	return "INSERT into egwtr_document_type (name, code, description, active, tenantid, createddate, createdby) values (?,?,?,?,?,?,?) ";
    }
    
    public String getQuery(){
    	return "SELECT * FROM egwtr_document_type WHERE tenantid = ? " ;
    }
    
    public String getQueryForCodeTenant(){
    	return "SELECT * FROM egwtr_document_type WHERE tenantid = ? AND code = ?  "; 
    }
    
    @SuppressWarnings("rawtypes")
    public String getQuery(DocumentTypeGetReq documentTypeGetRequest, List preparedStatementValues) {
        StringBuilder selectQuery = new StringBuilder(BASE_QUERY);

        addWhereClause(selectQuery, preparedStatementValues, documentTypeGetRequest);

        logger.debug("Query : " + selectQuery);
        return selectQuery.toString();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void addWhereClause(StringBuilder selectQuery, List preparedStatementValues,
    		DocumentTypeGetReq documentTypeGetRequest) {

        if (documentTypeGetRequest.getId() == null && documentTypeGetRequest.getName() == null 
                && documentTypeGetRequest.getTenantId() == null)
            return;

        selectQuery.append(" WHERE");
        boolean isAppendAndClause = false;

        if (documentTypeGetRequest.getTenantId() != null) {
            isAppendAndClause = true;
            selectQuery.append(" docType.tenantId = ?");
            preparedStatementValues.add(documentTypeGetRequest.getTenantId());
        }

        if (documentTypeGetRequest.getId() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" docType.id IN " + getIdQuery(documentTypeGetRequest.getId()));
        }


        if (documentTypeGetRequest.getName() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" docType.name = ?");
            preparedStatementValues.add(documentTypeGetRequest.getName());
        }

        if (documentTypeGetRequest.getCode() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" docType.code = ?");
            preparedStatementValues.add(documentTypeGetRequest.getCode());
        }

    }

    /**
     * This method is always called at the beginning of the method so that and
     * is prepended before the field's predicate is handled.
     *
     * @param appendAndClauseFlag
     * @param queryString
     * @return boolean indicates if the next predicate should append an "AND"
     */
    private boolean addAndClauseIfRequired(boolean appendAndClauseFlag, StringBuilder queryString) {
        if (appendAndClauseFlag)
            queryString.append(" AND");

        return true;
    }

    private static String getIdQuery(List<Long> idList) {
        StringBuilder query = new StringBuilder("(");
        if (idList.size() >= 1) {
            query.append(idList.get(0).toString());
            for (int i = 1; i < idList.size(); i++) {
                query.append(", " + idList.get(i));
            }
        }
        return query.append(")").toString();
    }
    
    


}
