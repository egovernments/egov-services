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
package org.egov.wcms.transaction.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.wcms.transaction.model.ConnectionDocument;
import org.egov.wcms.transaction.repository.builder.WaterConnectionQueryBuilder;
import org.egov.wcms.transaction.repository.rowmapper.WaterConnectionRowMapper;
import org.egov.wcms.transaction.web.contract.ConnectionDocumentGetReq;
import org.egov.wcms.transaction.web.contract.ConnectionDocumentReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ConnectionDocumentRepository {

    @Autowired
    private WaterConnectionQueryBuilder waterConnectionQueryBuilder;
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<ConnectionDocument> create(ConnectionDocumentReq connectionDocumentReq) {
        List<ConnectionDocument> connDocuments = connectionDocumentReq.getConnectionDocuments();
        List<Map<String, Object>> documentValues = new ArrayList<>(connDocuments.size());
        String insertQuery = waterConnectionQueryBuilder.getConnectionDocumentCreateQuery();
        for (ConnectionDocument connDoc : connDocuments) {
            documentValues.add(new MapSqlParameterSource("documenttype", connDoc.getDocumentType())
                    .addValue("referencenumber", connDoc.getReferenceNumber())
                    .addValue("connectionid", connDoc.getConnectionId())
                    .addValue("filestoreid", connDoc.getFileStoreId())
                    .addValue("tenantid", connDoc.getTenantId())
                    .addValue("createdby", connectionDocumentReq.getRequestInfo().getUserInfo().getId())
                    .addValue("createddate", new Date().getTime())
                    .addValue("lastmodifiedby", connectionDocumentReq.getRequestInfo().getUserInfo().getId())
                    .addValue("lastmodifieddate", new Date().getTime()).getValues());
        }
        namedParameterJdbcTemplate.batchUpdate(insertQuery, documentValues.toArray(new Map[connDocuments.size()]));
        return connectionDocumentReq.getConnectionDocuments();
    }

    public List<ConnectionDocument> search(ConnectionDocumentGetReq connectionDocGetReq) {
        if (connectionDocGetReq.getConsumerNumbers() != null) {
            Map<String, Object> preparedValues = new HashMap<>();
            preparedValues.put("consumernumber", connectionDocGetReq.getConsumerNumbers());
            preparedValues.put("tenantid", connectionDocGetReq.getTenantId());
            String connectionIdQuery = waterConnectionQueryBuilder.getConnectionIdQuery();
            List<Long> connectionIds = namedParameterJdbcTemplate.queryForList(connectionIdQuery, preparedValues, Long.class);
            connectionDocGetReq.setConnectionIds(connectionIds);
        }
        Map<String, Object> documentValues = new HashMap<>();
        String searchQuery = waterConnectionQueryBuilder.getConnectionDocumentQuery(connectionDocGetReq, documentValues);
        return namedParameterJdbcTemplate.query(searchQuery, documentValues,
                new WaterConnectionRowMapper().new WaterConnectionDocumentRowMapper());

    }

}
