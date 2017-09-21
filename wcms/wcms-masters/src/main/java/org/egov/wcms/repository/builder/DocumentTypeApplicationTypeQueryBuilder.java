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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DocumentTypeApplicationTypeQueryBuilder {

    @Autowired
    private ApplicationProperties applicationProperties;

    private static final String BASE_QUERY = "SELECT docapp.id AS id ,docapp.code as docapp_code,doctype.id as docTypeId,doctype.name as docTypeName, docapp.applicationtype , "
            + "docapp.documenttypeid,docapp.mandatory,docapp.active "
            + " ,docapp.tenantid FROM egwtr_documenttype_applicationtype docapp ,"
            + " egwtr_document_type doctype where docapp.documenttypeid = doctype.id and "
            + " docapp.tenantid = doctype.tenantid ";

    @SuppressWarnings("rawtypes")
    public String getQuery(final DocumentTypeApplicationTypeGetRequest docNameGetRequest,
            final List preparedStatementValues) {
        final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);

        addWhereClause(selectQuery, preparedStatementValues, docNameGetRequest);
        addOrderByClause(selectQuery, docNameGetRequest);
        addPagingClause(selectQuery, preparedStatementValues, docNameGetRequest);

        log.debug("Query : " + selectQuery);
        return selectQuery.toString();
    }

    public static String insertDocumentApplicationQuery() {
        return "INSERT INTO egwtr_documenttype_applicationtype(id,code,applicationtype,documenttypeid,mandatory,active,createdby,lastmodifiedby,createddate,lastmodifieddate,tenantid) values "
                + "(:id,:code,:applicationtype,:documenttypeid,:mandatory,:active,:createdby,:lastmodifiedby,:createddate,:lastmodifieddate,:tenantid)";
    }

    public static String updateDocumentTypeApplicationTypeQuery() {
        return "UPDATE egwtr_documenttype_applicationtype SET applicationtype = :applicationtype,documenttypeid = :documenttypeid,mandatory= :mandatory,"
                + "active = :active,lastmodifiedby = :lastmodifiedby,lastmodifieddate = :lastmodifieddate where code = :code and tenantid = :tenantid ";
    }

    public static String getDocumentTypeIdQuery() {
        return " select id FROM egwtr_document_type  where name= ? and tenantId = ? ";
    }

    public static String getDocumentName() {
        return "SELECT name FROM egwtr_document_type  WHERE id = ? and tenantId = ? ";
    }

    public static String selectDocumentApplicationIdQuery() {
        return " select code FROM egwtr_documenttype_applicationtype where applicationtype = ? and documenttypeid = ? and tenantId = ?";
    }

    public static String selectDocumentApplicationIdNotInQuery() {
       return " select code from egwtr_documenttype_applicationtype where applicationtype = ? and documenttypeid = ? and tenantId = ? and code != ? ";

    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void addWhereClause(final StringBuilder selectQuery, final List preparedStatementValues,
            final DocumentTypeApplicationTypeGetRequest docNameGetRequest) {

        if (docNameGetRequest.getIds() == null && docNameGetRequest.getApplicationType() == null
                && docNameGetRequest.getDocumentType() == null && docNameGetRequest.getActive() == null
                && docNameGetRequest.getTenantId() == null)
            return;

       // selectQuery.append(" WHERE");
        boolean isAppendAndClause = false;

        if (docNameGetRequest.getTenantId() != null) {
            isAppendAndClause = true;
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" docapp.tenantid = ?");
            preparedStatementValues.add(docNameGetRequest.getTenantId());
        }

        if (docNameGetRequest.getIds() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" docapp.id IN " + getIdQuery(docNameGetRequest.getIds()));
        }

        if (docNameGetRequest.getApplicationType() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" docapp.applicationtype = ?");
            preparedStatementValues.add(docNameGetRequest.getApplicationType());
        }

        if (docNameGetRequest.getDocumentType() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" docapp.documenttypeid = ?");
            preparedStatementValues.add(docNameGetRequest.getDocumentTypeId());
        }

        if (docNameGetRequest.getActive() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" docapp.active = ?");
            preparedStatementValues.add(docNameGetRequest.getActive());
        }
    }

    private void addOrderByClause(final StringBuilder selectQuery,
            final DocumentTypeApplicationTypeGetRequest docNameGetRequest) {
        final String sortBy = docNameGetRequest.getSortBy() == null ? "docapp.code"
                : "docapp." + docNameGetRequest.getSortBy();
        final String sortOrder = docNameGetRequest.getSortOrder() == null ? "DESC" : docNameGetRequest.getSortOrder();
        selectQuery.append(" ORDER BY " + sortBy + " " + sortOrder);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void addPagingClause(final StringBuilder selectQuery, final List preparedStatementValues,
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
     * This method is always called at the beginning of the method so that and is prepended before the field's predicate is
     * handled.
     *
     * @param appendAndClauseFlag
     * @param queryString
     * @return boolean indicates if the next predicate should append an "AND"
     */
    private boolean addAndClauseIfRequired(final boolean appendAndClauseFlag, final StringBuilder queryString) {
        if (appendAndClauseFlag)
            queryString.append(" AND");

        return true;
    }

    private static String getIdQuery(final List<Long> idList) {
        final StringBuilder query = new StringBuilder("(");
        if (idList.size() >= 1) {
            query.append(idList.get(0).toString());
            for (int i = 1; i < idList.size(); i++)
                query.append(", " + idList.get(i));
        }
        return query.append(")").toString();
    }

    public String checkDocTypeApplicationTypeExist() {
        return " select id FROM egwtr_documenttype_applicationtype where applicationtype = ? and documenttypeid=? and tenantid=? ";
    }

}
