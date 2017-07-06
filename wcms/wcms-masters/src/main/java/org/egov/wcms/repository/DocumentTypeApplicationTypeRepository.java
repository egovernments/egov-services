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

package org.egov.wcms.repository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.egov.wcms.model.DocumentTypeApplicationType;
import org.egov.wcms.repository.builder.DocumentTypeApplicationTypeQueryBuilder;
import org.egov.wcms.repository.rowmapper.DocumentTypeApplicationTypeMapper;
import org.egov.wcms.web.contract.DocumentTypeApplicationTypeGetRequest;
import org.egov.wcms.web.contract.DocumentTypeApplicationTypeReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class DocumentTypeApplicationTypeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DocumentTypeApplicationTypeQueryBuilder documentApplicationQueryBuilder;

    @Autowired
    private DocumentTypeApplicationTypeMapper documentTypeApplicationTypeRowMapper;

    public DocumentTypeApplicationTypeReq persistCreateDocTypeApplicationType(
            final DocumentTypeApplicationTypeReq docTypeAppliTypeRequest) {

        log.info("DocumentTypeApplicationTypeRequest::" + docTypeAppliTypeRequest);
        final String docNameInsert = DocumentTypeApplicationTypeQueryBuilder.insertDocNameQuery();
        final DocumentTypeApplicationType docApplication = docTypeAppliTypeRequest.getDocumentTypeApplicationType();
        final String documentQuery = DocumentTypeApplicationTypeQueryBuilder.getDocumentTypeIdQuery();
        Long documentId = 0L;
        try {
            documentId = jdbcTemplate.queryForObject(documentQuery,
                    new Object[] { docApplication.getDocumentType(), docApplication.getTenantId() }, Long.class);
        } catch (final EmptyResultDataAccessException e) {
            log.info("EmptyResultDataAccessException: Query returned empty result set");
        }
        if (documentId == null)
            log.info("Invalid input.");
        final Object[] obj = new Object[] { docApplication.getApplicationType(), documentId,
                docApplication.getMandatory(), docApplication.getActive(),
                Long.valueOf(docTypeAppliTypeRequest.getRequestInfo().getUserInfo().getId()),
                Long.valueOf(docTypeAppliTypeRequest.getRequestInfo().getUserInfo().getId()),
                new Date(new java.util.Date().getTime()), new Date(new java.util.Date().getTime()),
                docApplication.getTenantId() };

        jdbcTemplate.update(docNameInsert, obj);
        return docTypeAppliTypeRequest;
    }

    public List<DocumentTypeApplicationType> findForCriteria(
            final DocumentTypeApplicationTypeGetRequest docTypeAppliTypeGetRequest) {
        final List<Object> preparedStatementValues = new ArrayList<>();
        try {
            if (docTypeAppliTypeGetRequest.getDocumentType() != null)
                docTypeAppliTypeGetRequest.setDocumentTypeId(
                        jdbcTemplate.queryForObject(DocumentTypeApplicationTypeQueryBuilder.getDocumentTypeIdQuery(),
                                new Object[] { docTypeAppliTypeGetRequest.getDocumentType(),
                                        docTypeAppliTypeGetRequest.getTenantId() },
                                Long.class));
        } catch (final EmptyResultDataAccessException e) {
            log.info("EmptyResultDataAccessException: Query returned empty RS.");

        }
        final String queryStr = documentApplicationQueryBuilder.getQuery(docTypeAppliTypeGetRequest,
                preparedStatementValues);
        final List<DocumentTypeApplicationType> docTypeAppliTypes = jdbcTemplate.query(queryStr,
                preparedStatementValues.toArray(), documentTypeApplicationTypeRowMapper);
        return docTypeAppliTypes;
    }

    public DocumentTypeApplicationTypeReq persistModifyDocTypeApplicationType(
            final DocumentTypeApplicationTypeReq docTypeAppliTypeRequest) {
        log.info("DocumentTypeApplicationTypeRequest::" + docTypeAppliTypeRequest);
        final String documentTypeApplicationTypeUpdate = DocumentTypeApplicationTypeQueryBuilder
                .updateDocumentTypeApplicationTypeQuery();
        final DocumentTypeApplicationType docApplication = docTypeAppliTypeRequest.getDocumentTypeApplicationType();
        final String documentQuery = DocumentTypeApplicationTypeQueryBuilder.getDocumentTypeIdQuery();
        Long documentId = 0L;
        try {
            documentId = jdbcTemplate.queryForObject(documentQuery,
                    new Object[] { docApplication.getDocumentType(), docApplication.getTenantId() }, Long.class);
        } catch (final EmptyResultDataAccessException e) {
            log.info("EmptyResultDataAccessException: Query returned empty result set");
        }
        if (documentId == null)
            log.info("Invalid input.");
        final Object[] obj = new Object[] { docApplication.getApplicationType(), documentId,
                docApplication.getMandatory(), docApplication.getActive(),
                Long.valueOf(docTypeAppliTypeRequest.getRequestInfo().getUserInfo().getId()),
                new Date(new java.util.Date().getTime()), docApplication.getId() };
        jdbcTemplate.update(documentTypeApplicationTypeUpdate, obj);
        return docTypeAppliTypeRequest;

    }

    public boolean checkDocumentTypeApplicationTypeExist(final Long id, final String applicationType,
            final String documentType, final String tenantId) {

        final List<Object> preparedStatementValues = new ArrayList<>();
        final Long documentId = jdbcTemplate.queryForObject(
                DocumentTypeApplicationTypeQueryBuilder.getDocumentTypeIdQuery(),
                new Object[] { documentType, tenantId }, Long.class);
        preparedStatementValues.add(applicationType);
        preparedStatementValues.add(documentId);
        preparedStatementValues.add(tenantId);
        String query = null;
        if (id == null)
            query = DocumentTypeApplicationTypeQueryBuilder.selectDocumentApplicationIdQuery();
        else {
            preparedStatementValues.add(id);
            query = DocumentTypeApplicationTypeQueryBuilder.selectDocumentApplicationIdNotInQuery();
        }
        final List<Map<String, Object>> appDocs = jdbcTemplate.queryForList(query, preparedStatementValues.toArray());
        if (!appDocs.isEmpty())
            return false;

        return true;
    }

}
