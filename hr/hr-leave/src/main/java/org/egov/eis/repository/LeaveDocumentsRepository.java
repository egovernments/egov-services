/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.eis.repository;

import org.egov.eis.model.Document;
import org.egov.eis.repository.rowmapper.LeaveDocumentsTableRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Component
public class LeaveDocumentsRepository {

    public static final Logger LOGGER = LoggerFactory.getLogger(LeaveDocumentsRepository.class);

    public static final String SELECT_BY_LEAVEAPPLICATION_QUERY = "SELECT id, leaveapplicationid, document, tenantId"
            + " FROM egeis_leaveapplication_documents WHERE leaveapplicationId = ? AND tenantId = ? ";

    public static final String INSERT_LEAVEAPPLICATION_DOCUMENTS_QUERY = "INSERT INTO egeis_leaveapplication_documents"
            + " (id, leaveapplicationId, document, tenantId)"
            + " VALUES (nextval('seq_egeis_leaveapplication_documents'),?,?,?)";

    public static final String DELETE_LEAVEAPPLICATION_DOCUMENTS_QUERY = "Delete from egeis_leaveapplication_documents"
            + " where leaveapplicationId = ? and document = ? and tenantId = ? ";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private LeaveDocumentsTableRowMapper leaveDocumentsTableRowMapper;

    public List<Document> findByLeaveApplication(final Long id, final String tenantId) {
        try {
            return jdbcTemplate.query(SELECT_BY_LEAVEAPPLICATION_QUERY, new Object[]{id, tenantId},
                    leaveDocumentsTableRowMapper);
        } catch (final EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void save(final Long leaveapplicationId, final List<String> documents, final String tenantId) {
        jdbcTemplate.batchUpdate(INSERT_LEAVEAPPLICATION_DOCUMENTS_QUERY, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                ps.setLong(1, leaveapplicationId);
                ps.setString(2, documents.get(i));
                ps.setString(3, tenantId);
            }

            @Override
            public int getBatchSize() {
                return documents.size();
            }
        });
    }


    public int delete(Long leaveapplicationId, String document, String tenantId) {
        return jdbcTemplate.update(DELETE_LEAVEAPPLICATION_DOCUMENTS_QUERY, leaveapplicationId, document, tenantId);
    }
}