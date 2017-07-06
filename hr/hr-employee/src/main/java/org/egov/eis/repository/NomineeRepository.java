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

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.eis.model.Nominee;
import org.egov.eis.model.enums.EntityType;
import org.egov.eis.repository.builder.NomineeQueryBuilder;
import org.egov.eis.repository.helper.PreparedStatementHelper;
import org.egov.eis.repository.rowmapper.EmployeeIdsRowMapper;
import org.egov.eis.repository.rowmapper.NomineeRowMapper;
import org.egov.eis.web.contract.NomineeGetRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class NomineeRepository {

    public static final Logger LOGGER = LoggerFactory.getLogger(NomineeRepository.class);

    public static final String INSERT_NOMINEE_QUERY = "INSERT INTO egeis_nominee" +
            " (employeeId, name, gender, dateOfBirth, maritalStatus, relationship, bankId, bankBranchId, bankAccount," +
            " nominated, employed, lastModifiedBy, lastModifiedDate, id, tenantId, createdBy, createdDate)" +
            " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    public static final String UPDATE_NOMINEE_QUERY = "UPDATE egeis_nominee" +
            " SET (employeeId, name, gender, dateOfBirth, maritalStatus, relationship, bankId, bankBranchId," +
            " bankAccount, nominated, employed, lastModifiedBy, lastModifiedDate)" +
            " = (?,?,?,?,?,?,?,?,?,?,?,?,?) WHERE id = ? AND tenantId = ?";

    public static final String RECORD_EXISTENCE_CHECK_QUERY = "SELECT exists(SELECT id FROM $table"
            + " WHERE id = ? AND tenantId = ? $checkNominatorIfUpdate)";

    public static final String UNICITY_CHECK_QUERY = "SELECT NOT exists(SELECT id FROM egeis_nominee"
            + " WHERE employeeId = ? AND name = ? AND gender = ? AND dateOfBirth = ? AND relationship = ?"
            + " AND tenantId = ? $checkIdIfUpdate)";

    public static final String GENERATE_SEQUENCES_QUERY = "SELECT nextval('seq_egeis_nominee') AS id" +
            " from generate_series(1, ?)";

    @Autowired
    private NomineeQueryBuilder nomineeQueryBuilder;

    @Autowired
    private NomineeRowMapper nomineeRowMapper;

    @Autowired
    private EmployeeIdsRowMapper employeeIdsRowMapper;

    @Autowired
    private EmployeeDocumentsRepository documentsRepository;

    @Autowired
    private PreparedStatementHelper psHelper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /**
     * @param namedParameterJdbcTemplate the namedParameterJdbcTemplate to set
     */
    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<Nominee> findForCriteria(NomineeGetRequest nomineeGetRequest) {
        Map<String, Object> namedParameters = new HashMap<>();
        String queryStr = nomineeQueryBuilder.getQuery(nomineeGetRequest, namedParameters);

        List<Nominee> nominees = namedParameterJdbcTemplate.query(queryStr, namedParameters, nomineeRowMapper);

        return nominees;
    }

    public void save(List<Nominee> nominees) {
        jdbcTemplate.batchUpdate(INSERT_NOMINEE_QUERY, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Nominee nominee = nominees.get(i);
                setPreparedStatementValues(ps, nominee, true);

                if (nominee.getDocuments() != null && !nominee.getDocuments().isEmpty()) {
                    documentsRepository.save(nominee.getEmployee().getId(), nominee.getDocuments(),
                            EntityType.NOMINEE.toString(), nominee.getId(), nominee.getTenantId());
                }
            }

            @Override
            public int getBatchSize() {
                return nominees.size();
            }
        });
    }

    public void update(List<Nominee> nominees) {
        jdbcTemplate.batchUpdate(UPDATE_NOMINEE_QUERY, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Nominee nominee = nominees.get(i);
                setPreparedStatementValues(ps, nominee, false);
            }

            @Override
            public int getBatchSize() {
                return nominees.size();
            }
        });
    }

    private void setPreparedStatementValues(PreparedStatement ps, Nominee nominee, boolean isCreate) throws SQLException {
        ps.setLong(1, nominee.getEmployee().getId());
        ps.setString(2, nominee.getName());
        ps.setString(3, nominee.getGender().toString());
        ps.setLong(4, nominee.getDateOfBirth());
        ps.setString(5, nominee.getMaritalStatus().toString());
        ps.setString(6, nominee.getRelationship().toString());
        psHelper.setLongOrNull(ps, 7, nominee.getBank());
        psHelper.setLongOrNull(ps, 8, nominee.getBankBranch());
        ps.setString(9, nominee.getBankAccount());
        psHelper.setBooleanOrNull(ps, 10, nominee.getNominated());
        psHelper.setBooleanOrNull(ps, 11, nominee.getEmployed());
        psHelper.setLongOrNull(ps, 12, nominee.getLastModifiedBy());
        psHelper.setLongOrNull(ps, 13, nominee.getLastModifiedDate());
        ps.setLong(14, nominee.getId());
        ps.setString(15, nominee.getTenantId());

        if (isCreate) {
            ps.setLong(16, nominee.getCreatedBy());
            ps.setLong(17, nominee.getCreatedDate());
        }
    }

    public List<Long> generateSequences(int totalSeq) {
        return jdbcTemplate.query(GENERATE_SEQUENCES_QUERY, new Object[]{totalSeq}, employeeIdsRowMapper);
    }

    public boolean ifExists(String table, Long id, String tenantId) {
        return jdbcTemplate.queryForObject(RECORD_EXISTENCE_CHECK_QUERY.replace("$table", table)
                .replace(" $checkNominatorIfUpdate", ""), new Object[]{id, tenantId}, Boolean.class);
    }

    public boolean ifNominatorSame(String table, Long nomineeId, Long nominatorId, String tenantId) {
        return jdbcTemplate.queryForObject(RECORD_EXISTENCE_CHECK_QUERY.replace("$table", table)
                .replace(" $checkNominatorIfUpdate", " AND employeeId = ?"),
                new Object[]{ nomineeId, tenantId, nominatorId }, Boolean.class);
    }

    public boolean checkIfUnique(Nominee nominee, Boolean isUpdate) {
        Object[] psValues = { nominee.getEmployee().getId(), nominee.getName(), nominee.getGender().toString(),
                nominee.getDateOfBirth(), nominee.getRelationship().toString(), nominee.getTenantId() };
        String query = null;
        if (isUpdate)
            query = UNICITY_CHECK_QUERY.replace("$checkIdIfUpdate", " AND id != " + nominee.getId());
        else
            query = UNICITY_CHECK_QUERY.replace("$checkIdIfUpdate", "");
        return jdbcTemplate.queryForObject(query, psValues, Boolean.class);
    }
}