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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.egov.wcms.model.DocumentType;
import org.egov.wcms.repository.builder.DocumentTypeQueryBuilder;
import org.egov.wcms.repository.rowmapper.DocumentTypeRowMapper;
import org.egov.wcms.web.contract.DocumentTypeGetReq;
import org.egov.wcms.web.contract.DocumentTypeReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class DocumentTypeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DocumentTypeQueryBuilder documentTypeQueryBuilder;

    @Autowired
    private DocumentTypeRowMapper documentTypeRowMapper;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public DocumentTypeReq create(final DocumentTypeReq documentTypeReq) {
        log.info("DocumentTypeRequest::" + documentTypeReq);
        final String documentTypeInsert = DocumentTypeQueryBuilder.insertDocumentTypeQuery();
        final List<DocumentType> documentTypeList = documentTypeReq.getDocumentTypes();
        final List<Map<String, Object>> batchValues = new ArrayList<>(documentTypeList.size());
        for (final DocumentType documentType : documentTypeList)
            batchValues.add(
                    new MapSqlParameterSource("id", Long.valueOf(documentType.getCode())).addValue("code", documentType.getCode())
                            .addValue("name", documentType.getName()).addValue("description", documentType.getDescription())
                            .addValue("active", documentType.getActive())
                            .addValue("createdby", Long.valueOf(documentTypeReq.getRequestInfo().getUserInfo().getId()))
                            .addValue("lastmodifiedby", Long.valueOf(documentTypeReq.getRequestInfo().getUserInfo().getId()))
                            .addValue("createddate", new Date(new java.util.Date().getTime()))
                            .addValue("lastmodifieddate", new Date(new java.util.Date().getTime()))
                            .addValue("tenantid", documentType.getTenantId())
                            .getValues());
        namedParameterJdbcTemplate.batchUpdate(documentTypeInsert, batchValues.toArray(new Map[documentTypeList.size()]));
        return documentTypeReq;
    }

    public DocumentTypeReq update(final DocumentTypeReq documentTypeReq) {
        log.info("DocumentTypeRequest::" + documentTypeReq);
        final String documentTypeUpdate = DocumentTypeQueryBuilder.updateDocumentTypeQuery();
        final List<DocumentType> documentTypeList = documentTypeReq.getDocumentTypes();
        final List<Map<String, Object>> batchValues = new ArrayList<>(documentTypeList.size());
        for (final DocumentType documentType : documentTypeList)
            batchValues.add(new MapSqlParameterSource("name", documentType.getName())
                    .addValue("description", documentType.getDescription())
                    .addValue("active", documentType.getActive())
                    .addValue("lastmodifiedby", Long.valueOf(documentTypeReq.getRequestInfo().getUserInfo().getId()))
                    .addValue("lastmodifieddate", new Date(new java.util.Date().getTime()))
                    .addValue("code", documentType.getCode())
                    .addValue("tenantid", documentType.getTenantId()).getValues());
        namedParameterJdbcTemplate.batchUpdate(documentTypeUpdate, batchValues.toArray(new Map[documentTypeList.size()]));
        return documentTypeReq;

    }

    public boolean checkDocumentTypeByNameAndCode(final String code, final String name, final String tenantId) {
        final List<Object> preparedStatementValues = new ArrayList<>();
        preparedStatementValues.add(name);
        preparedStatementValues.add(tenantId);
        final String query;
        if (code == null)
            query = DocumentTypeQueryBuilder.selectDocumentTypeByNameAndCodeQuery();
        else {
            preparedStatementValues.add(code);
            query = DocumentTypeQueryBuilder.selectDocumentTypeByNameAndCodeNotInQuery();
        }
        final List<Map<String, Object>> documentTypes = jdbcTemplate.queryForList(query,
                preparedStatementValues.toArray());
        if (!documentTypes.isEmpty())
            return false;

        return true;
    }

    public List<DocumentType> findForCriteria(final DocumentTypeGetReq documentTypeGetRequest) {
        final List<Object> preparedStatementValues = new ArrayList<>();
        final String queryStr = documentTypeQueryBuilder.getQuery(documentTypeGetRequest, preparedStatementValues);
        final List<DocumentType> documentTypes = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(),
                documentTypeRowMapper);
        return documentTypes;
    }

    public List<Long> getMandatoryocs(final String applicationType) {
        List<Long> mandatoryDocs = new ArrayList<>();
        final String query = DocumentTypeQueryBuilder.getMandatoryDocsQuery();
        log.info("Mandatory Doc Query: " + query.toString());

        mandatoryDocs = jdbcTemplate.query(query, new Object[] { applicationType },
                (RowMapper<Long>) (rs, rowNum) -> rs.getLong("id"));

        log.info("Mandatory Doc List: " + mandatoryDocs.toString());

        return mandatoryDocs;

    }

}