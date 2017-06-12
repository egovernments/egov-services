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
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import org.egov.wcms.model.DocumentOwner;
import org.egov.wcms.model.MeterReading;
import org.egov.wcms.repository.builder.WaterConnectionQueryBuilder;
import org.egov.wcms.web.contract.WaterConnectionReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class WaterConnectionRepository {

    public static final Logger LOGGER = LoggerFactory.getLogger(WaterConnectionRepository.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public WaterConnectionReq persistConnection(final WaterConnectionReq waterConnectionRequest) {

        String insertQuery = "";
        if (waterConnectionRequest.getConnection().getLegacyConsumerNumber() != null)
            insertQuery = WaterConnectionQueryBuilder.insertLegacyConnectionQuery();
        else if (waterConnectionRequest.getConnection().getParentConnectionId() != 0)
            insertQuery = WaterConnectionQueryBuilder.insertAdditionalConnectionQuery();
        else
            insertQuery = WaterConnectionQueryBuilder.insertConnectionQuery();

        final String query = insertQuery;

        long connectionId = 0L;
        try {
            final KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update((PreparedStatementCreator) connection -> {
                final String[] returnValColumn = new String[] { "id" };
                final PreparedStatement statement = connection.prepareStatement(query,
                        returnValColumn);
                statement.setString(1, waterConnectionRequest.getConnection().getTenantId());
                statement.setString(2, waterConnectionRequest.getConnection().getConnectionType());
                statement.setString(3, waterConnectionRequest.getConnection().getBillingType());
                statement.setLong(4, 3L); // waterConnectionRequest.getConnection().getCategoryType());
                statement.setLong(5, 1L); // waterConnectionRequest.getConnection().getHscPipeSizeType());
                statement.setString(6, waterConnectionRequest.getConnection().getSupplyType());
                statement.setLong(7, 1L);  // waterConnectionRequest.getConnection().getSourceType());
                statement.setString(8, waterConnectionRequest.getConnection().getConnectionStatus());
                statement.setDouble(9, waterConnectionRequest.getConnection().getSumpCapacity());
                statement.setInt(10, waterConnectionRequest.getConnection().getNumberOfTaps());
                statement.setInt(11, waterConnectionRequest.getConnection().getNumberOfPersons());
                statement.setString(12, waterConnectionRequest.getConnection().getAcknowledgementNumber());
                statement.setLong(13, waterConnectionRequest.getRequestInfo().getUserInfo().getId());
                statement.setLong(14, waterConnectionRequest.getRequestInfo().getUserInfo().getId());
                statement.setDate(15, new Date(new java.util.Date().getTime()));
                statement.setDate(16, new Date(new java.util.Date().getTime()));
                statement.setLong(17, waterConnectionRequest.getConnection().getProperty().getId());
                statement.setString(18, waterConnectionRequest.getConnection().getProperty().getUsageType());
                statement.setString(19, waterConnectionRequest.getConnection().getProperty().getPropertyType());
                statement.setString(20, "AddressTest"); // waterConnectionRequest.getConnection().getProperty().getAddress());
                statement.setString(21, waterConnectionRequest.getConnection().getDonationCharge());

                if (waterConnectionRequest.getConnection().getLegacyConsumerNumber() != null
                        || waterConnectionRequest.getConnection().getParentConnectionId() != 0) {

                    statement.setString(22, waterConnectionRequest.getConnection().getLegacyConsumerNumber());
                    statement.setString(23, waterConnectionRequest.getConnection().getConsumerNumber());
                }

                if (waterConnectionRequest.getConnection().getParentConnectionId() != 0)
                    statement.setLong(24, waterConnectionRequest.getConnection().getParentConnectionId());

                // Please verify if there's proper validation on all these fields to avoid NPE.

                return statement;
            }, keyHolder);

            connectionId = keyHolder.getKey().longValue();
            waterConnectionRequest.getConnection().setId(connectionId);
        } catch (final Exception e) {
            LOGGER.error("Inserting Connection Object failed!", e);
        }

        if (connectionId > 0 && waterConnectionRequest.getConnection().getLegacyConsumerNumber() == null) {
            final List<Object[]> values = new ArrayList<>();
            for (final DocumentOwner document : waterConnectionRequest.getConnection().getDocuments()) {
                final Object[] obj = { document.getDocument().getId(),
                        document.getName(),
                        document.getFileStoreId(),
                        waterConnectionRequest.getConnection().getId(),
                        waterConnectionRequest.getConnection().getTenantId() };

                values.add(obj);
            }
            final String insertDocsQuery = WaterConnectionQueryBuilder.insertDocumentQuery();
            try {
                jdbcTemplate.batchUpdate(insertDocsQuery, values);
            } catch (final Exception e) {
                LOGGER.error("Inserting documents failed!", e);
            }
        } else if (connectionId > 0) {

            final String insertMeterQuery = WaterConnectionQueryBuilder.insertMeterQuery();
            try {
                final Object[] obj = new Object[] { waterConnectionRequest.getConnection().getMeter().getMeterMake(),
                        connectionId,waterConnectionRequest.getConnection().getMeter().getMeterReading(),
                        waterConnectionRequest.getConnection().getTenantId(),
                        waterConnectionRequest.getRequestInfo().getUserInfo().getId(), new Date(new java.util.Date().getTime()),
                        waterConnectionRequest.getRequestInfo().getUserInfo().getId(), new Date(new java.util.Date().getTime()) };

                jdbcTemplate.update(insertMeterQuery, obj);
            } catch (final Exception e) {
                LOGGER.error("Inserting Meter failed!", e);
            }
            final String insertMeterReadingQuery = WaterConnectionQueryBuilder.insertMeterReadingQuery();
            final List<Object[]> values = new ArrayList<>();
            for (final MeterReading meterReading : waterConnectionRequest.getConnection().getMeterReadings()) {

                final Object[] obj = { connectionId,
                        meterReading.getReading(), waterConnectionRequest.getConnection().getTenantId(),
                        waterConnectionRequest.getRequestInfo().getUserInfo().getId(),
                        meterReading.getAuditDetails().getCreatedDate(),
                        waterConnectionRequest.getRequestInfo().getUserInfo().getId(),
                        meterReading.getAuditDetails().getCreatedDate() };

                values.add(obj);
            }
            try {
                jdbcTemplate.batchUpdate(insertMeterReadingQuery, values);
            } catch (final Exception e) {
                LOGGER.error("Inserting documents failed!", e);
            }

        }

        LOGGER.info("Insertion to document owner table left unattempted upon failure of connection object insertion.");
        return waterConnectionRequest;

    }

}
