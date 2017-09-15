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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DocumentTypeQueryBuilder {

    @Autowired
    private ApplicationProperties applicationProperties;

    private static final String BASE_QUERY = "SELECT document.id as document_id, document.code as document_code,"
            + " document.name as document_name, document.description as document_description,document.active as document_active, document.tenantId as document_tenantId "
            + " FROM egwtr_document_type document ";

    @SuppressWarnings("rawtypes")
    public String getQuery(final DocumentTypeGetReq documentTypeGetRequest, final List preparedStatementValues) {
        final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);

        addWhereClause(selectQuery, preparedStatementValues, documentTypeGetRequest);
        addOrderByClause(selectQuery, documentTypeGetRequest);
        addPagingClause(selectQuery, preparedStatementValues, documentTypeGetRequest);

        log.debug("Query : " + selectQuery);
        return selectQuery.toString();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void addWhereClause(final StringBuilder selectQuery, final List preparedStatementValues,
            final DocumentTypeGetReq documentTypeGetRequest) {

        if (documentTypeGetRequest.getIds() == null && documentTypeGetRequest.getName() == null
                && documentTypeGetRequest.getActive() == null && documentTypeGetRequest.getTenantId() == null)
            return;

        selectQuery.append(" WHERE");
        boolean isAppendAndClause = false;

        if (documentTypeGetRequest.getTenantId() != null) {
            isAppendAndClause = true;
            selectQuery.append(" document.tenantId = ?");
            preparedStatementValues.add(documentTypeGetRequest.getTenantId());
        }

        if (documentTypeGetRequest.getIds() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" document.id IN " + getIdQuery(documentTypeGetRequest.getIds()));
        }

        if (documentTypeGetRequest.getName() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" document.name = ?");
            preparedStatementValues.add(documentTypeGetRequest.getName());
        }

        if (documentTypeGetRequest.getCode() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" document.code = ?");
            preparedStatementValues.add(documentTypeGetRequest.getCode());
        }

        if (documentTypeGetRequest.getActive() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" document.active = ?");
            preparedStatementValues.add(documentTypeGetRequest.getActive());
        }
    }

    private void addOrderByClause(final StringBuilder selectQuery, final DocumentTypeGetReq documentTypeGetRequest) {
        final String sortBy = documentTypeGetRequest.getSortBy() == null ? "document.id"
                : "document." + documentTypeGetRequest.getSortBy();
        final String sortOrder = documentTypeGetRequest.getSortOrder() == null ? "DESC"
                : documentTypeGetRequest.getSortOrder();
        selectQuery.append(" ORDER BY " + sortBy + " " + sortOrder);
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

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void addPagingClause(final StringBuilder selectQuery, final List preparedStatementValues,
            final DocumentTypeGetReq documentTypeGetRequest) {
        // handle limit(also called pageSize) here
        selectQuery.append(" LIMIT ?");
        long pageSize = Integer.parseInt(applicationProperties.wcmsSearchPageSizeDefault());
        if (documentTypeGetRequest.getPageSize() != null)
            pageSize = documentTypeGetRequest.getPageSize();
        preparedStatementValues.add(pageSize); // Set limit to pageSize

        // handle offset here
        selectQuery.append(" OFFSET ?");
        int pageNumber = 0; // Default pageNo is zero meaning first page
        if (documentTypeGetRequest.getPageNumber() != null)
            pageNumber = documentTypeGetRequest.getPageNumber() - 1;
        preparedStatementValues.add(pageNumber * pageSize); // Set offset to
        // pageNo * pageSize
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

    public static String insertDocumentTypeQuery() {
        return "INSERT INTO egwtr_document_type(id,code,name,description,active,createdby,lastmodifiedby,createddate,lastmodifieddate,tenantid) values "
                + "(:id,:code,:name,:description,:active,:createdby,:lastmodifiedby,:createddate,:lastmodifieddate,:tenantid)";
    }

    public static String updateDocumentTypeQuery() {
        return "UPDATE egwtr_document_type SET name = :name,description = :description,"
                + "active = :active,lastmodifiedby = :lastmodifiedby,lastmodifieddate = :lastmodifieddate where code = :code and tenantid =:tenantid ";
    }

    public static String selectDocumentTypeByNameAndCodeQuery() {
        return " select code FROM egwtr_document_type where name = ? and tenantId = ?";
    }

    public static String selectDocumentTypeByNameAndCodeNotInQuery() {
        return " select code from egwtr_document_type where name = ? and tenantId = ? and code != ? ";
    }

    public static String getMandatoryDocsQuery() {
        return "SELECT id FROM egwtr_documenttype_applicationtype WHERE mandatory IS TRUE AND applicationtype = ?";
    }

}
