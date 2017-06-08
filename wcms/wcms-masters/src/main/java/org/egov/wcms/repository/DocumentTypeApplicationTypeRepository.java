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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DocumentTypeApplicationTypeRepository {

    public static final Logger LOGGER = LoggerFactory.getLogger(DocumentTypeApplicationTypeRepository.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DocumentTypeApplicationTypeQueryBuilder documentTypeApplicationTypeQueryBuilder;

    @Autowired
    private DocumentTypeApplicationTypeMapper documentTypeApplicationTypeRowMapper;

    public DocumentTypeApplicationTypeReq persistCreateDocTypeApplicationType(
            final DocumentTypeApplicationTypeReq docTypeAppliTypeRequest) {
        LOGGER.info("DocumentTypeApplicationTypeRequest::" + docTypeAppliTypeRequest);
        final String docNameInsert = DocumentTypeApplicationTypeQueryBuilder.insertDocNameQuery();
        final DocumentTypeApplicationType docName = docTypeAppliTypeRequest.getDocumentTypeApplicationType();
        final Object[] obj = new Object[] { docName.getApplicationType(), docName.getDocumentTypeId(), docName.getMandatory(),
                docName.getActive(), Long.valueOf(docTypeAppliTypeRequest.getRequestInfo().getUserInfo().getId()),
                Long.valueOf(docTypeAppliTypeRequest.getRequestInfo().getUserInfo().getId()),
                new Date(new java.util.Date().getTime()),
                new Date(new java.util.Date().getTime()), docName.getTenantId() };

        jdbcTemplate.update(docNameInsert, obj);
        return docTypeAppliTypeRequest;
    }

    public List<DocumentTypeApplicationType> findForCriteria(
            final DocumentTypeApplicationTypeGetRequest docTypeAppliTypeGetRequest) {
        final List<Object> preparedStatementValues = new ArrayList<>();
        final String queryStr = documentTypeApplicationTypeQueryBuilder.getQuery(docTypeAppliTypeGetRequest,
                preparedStatementValues);
        final List<DocumentTypeApplicationType> docTypeAppliTypes = jdbcTemplate.query(queryStr,
                preparedStatementValues.toArray(),
                documentTypeApplicationTypeRowMapper);
        return docTypeAppliTypes;
    }

    public DocumentTypeApplicationTypeReq persistModifyDocTypeApplicationType(
            final DocumentTypeApplicationTypeReq docTypeAppliTypeRequest) {
        LOGGER.info("DocumentTypeApplicationTypeRequest::" + docTypeAppliTypeRequest);
        final String documentTypeApplicationTypeUpdate = DocumentTypeApplicationTypeQueryBuilder
                .updateDocumentTypeApplicationTypeQuery();
        final DocumentTypeApplicationType docTypeAppliType = docTypeAppliTypeRequest.getDocumentTypeApplicationType();
        final Object[] obj = new Object[] { docTypeAppliType.getApplicationType(), docTypeAppliType.getDocumentTypeId(),
                docTypeAppliType.getMandatory(), docTypeAppliType.getActive(),
                Long.valueOf(docTypeAppliTypeRequest.getRequestInfo().getUserInfo().getId()),
                new Date(new java.util.Date().getTime()), docTypeAppliType.getId() };
        jdbcTemplate.update(documentTypeApplicationTypeUpdate, obj);
        return docTypeAppliTypeRequest;

    }

    public boolean checkDocumentTypeApplicationTypeExist(final long documentType, final String applicationType,
            final String tenantid) {

        final List<Object> preparedStatementValues = new ArrayList<>();

        preparedStatementValues.add(applicationType);
        preparedStatementValues.add(documentType);
        preparedStatementValues.add(tenantid);
        String query = null;
        if (documentType != 0 && applicationType != null && tenantid != null)
            query = documentTypeApplicationTypeQueryBuilder.checkDocTypeApplicationTypeExist();

        final List<Map<String, Object>> documentAndAppliTypes = jdbcTemplate.queryForList(query,
                preparedStatementValues.toArray());
        if (!documentAndAppliTypes.isEmpty())
            return false;

        return true;

    }
}
