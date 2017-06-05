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
import org.egov.wcms.web.contract.DocumentTypeApplicationTypeGetRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DocumentTypeApplicationTypeQueryBuilder {

    @Autowired
    private ApplicationProperties applicationProperties;


    private static final Logger logger = LoggerFactory.getLogger(DocumentTypeApplicationTypeQueryBuilder.class);

/*    private static final String BASE_QUERY = "SELECT doc.id , doc.applicationtype , doc.documenttypeid,doc.mandatory,doc.active,doc.createddate,"
    		+ "doc.lastmodifieddate,doc.createdby,doc.lastmodifiedby,doc.tenantid from egwtr_documenttype_applicationtype doc";*/
    
    
    private static final String BASE_QUERY = "SELECT docapp.id AS id ,doctype.id as docTypeId,doctype.name as docTypeName, docapp.applicationtype , docapp.documenttypeid,docapp.mandatory,docapp.active,docapp.createddate,"
    		                                 +" docapp.lastmodifieddate,docapp.createdby,docapp.lastmodifiedby,docapp.tenantid from egwtr_documenttype_applicationtype docapp" 
                                             +" LEFT JOIN egwtr_document_type doctype ON docapp.documenttypeid = doctype.id";
           

   
    @SuppressWarnings("rawtypes")
    public String getQuery(DocumentTypeApplicationTypeGetRequest docNameGetRequest, List preparedStatementValues) {
        StringBuilder selectQuery = new StringBuilder(BASE_QUERY);

        addWhereClause(selectQuery, preparedStatementValues, docNameGetRequest);
        addOrderByClause(selectQuery, docNameGetRequest);
        addPagingClause(selectQuery, preparedStatementValues, docNameGetRequest);

        logger.debug("Query : " + selectQuery);
        return selectQuery.toString();
    }

    public static String insertDocNameQuery() {
        return "INSERT INTO egwtr_documenttype_applicationtype(applicationtype,documenttypeid,mandatory,active,createdby,lastmodifiedby,createddate,lastmodifieddate,tenantid) values "
                + "(?,?,?,?,?,?,?,?,?)";
    }
    
 
    public static String updateDocumentTypeApplicationTypeQuery() {
        return "UPDATE egwtr_documenttype_applicationtype SET applicationtype = ?,documenttypeid = ?,mandatory=?,"
                + "active = ?,lastmodifiedby = ?,lastmodifieddate = ? where id = ?";
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void addWhereClause(StringBuilder selectQuery, List preparedStatementValues,
                                final DocumentTypeApplicationTypeGetRequest docNameGetRequest) {

        if (docNameGetRequest.getId() == null && docNameGetRequest.getApplicationType() == null && docNameGetRequest.getActive() == null && docNameGetRequest.getTenantId() == null)
            return;

        selectQuery.append(" WHERE");
        boolean isAppendAndClause = false;

        if (docNameGetRequest.getTenantId() != null) {
            isAppendAndClause = true;
            selectQuery.append(" docapp.tenantid = ?");
            preparedStatementValues.add(docNameGetRequest.getTenantId());
        }

        if (docNameGetRequest.getId() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" docapp.id IN " + getIdQuery(docNameGetRequest.getId()));
        }


        if (docNameGetRequest.getApplicationType() !=  null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" docapp.applicationtype = ?");
            preparedStatementValues.add(docNameGetRequest.getApplicationType());
        }

         if (docNameGetRequest.getActive() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" docapp.active = ?");
            preparedStatementValues.add(docNameGetRequest.getActive());
        }
    }

    private void addOrderByClause(StringBuilder selectQuery, final DocumentTypeApplicationTypeGetRequest docNameGetRequest) {
        String sortBy = (docNameGetRequest.getSortBy() == null ? "docapp.id"  : "docapp." + docNameGetRequest.getSortBy());
        String sortOrder = (docNameGetRequest.getSortOrder() == null ? "DESC" : docNameGetRequest.getSortOrder());
        selectQuery.append(" ORDER BY " + sortBy + " " + sortOrder);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void addPagingClause(StringBuilder selectQuery, List preparedStatementValues,
                                 final DocumentTypeApplicationTypeGetRequest docNameGetRequest) {
        selectQuery.append(" LIMIT ?");
        long pageSize = Integer.parseInt(applicationProperties.wcmsSearchPageSizeDefault());
        if (docNameGetRequest.getPageSize() != null)
            pageSize = docNameGetRequest.getPageSize();
        preparedStatementValues.add(pageSize); 

        selectQuery.append(" OFFSET ?");
        int pageNumber = 0; 
        if (docNameGetRequest.getPageNumber() != null)
            pageNumber = docNameGetRequest.getPageNumber() - 1;
        preparedStatementValues.add(pageNumber * pageSize); 
       
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

    public  String checkDocTypeApplicationTypeExist() {
        return " select id FROM egwtr_documenttype_applicationtype where applicationtype = ? and documenttypeid=? and tenantid=? ";
    }
    
}
