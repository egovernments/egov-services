/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */

package org.egov.asset.repository.rowmapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.egov.asset.model.TransactionHistory;
import org.egov.asset.model.enums.TransactionType;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AssetHistoryRowMapper implements ResultSetExtractor<Map<Long, List<TransactionHistory>>> {

    @Override
    public Map<Long, List<TransactionHistory>> extractData(final ResultSet rs) throws SQLException, DataAccessException {

        final Map<Long, List<TransactionHistory>> transactionHistoryMap = new TreeMap<>();

        while (rs.next()) {
            final Long assetId = rs.getLong("assetid");

            log.info("AssetHistory in row mapper" + assetId);

            List<TransactionHistory> transactionHistoryList = transactionHistoryMap.get(assetId);

            BigDecimal transactionAmount = BigDecimal.ZERO;
            TransactionType transactionType = TransactionType.fromValue(rs.getString("assetTranType"));
            BigDecimal valueBeforeTransaction = BigDecimal.ZERO;
            BigDecimal valueAfterTransaction = BigDecimal.ZERO;
            Long transactionDate = null;

            if (transactionType.equals(TransactionType.REVALUATION)) {
                transactionDate = rs.getLong("revaluationdate");
                transactionAmount = rs.getBigDecimal("revaluationamount");
                valueBeforeTransaction = rs.getBigDecimal("currentcapitalizedvalue");
                valueAfterTransaction = rs.getBigDecimal("valueafterrevaluation");

            }
            if (transactionType.equals(TransactionType.DEPRECIATION)) {
                transactionDate = rs.getLong("todate");
                transactionAmount = rs.getBigDecimal("depreciationvalue");
                valueBeforeTransaction = rs.getBigDecimal("valuebeforedepreciation");
                valueAfterTransaction = rs.getBigDecimal("valueafterdepreciation");

            }
            if (transactionType.equals(TransactionType.SALE)) {
                transactionDate = rs.getLong("disposaldate");
                transactionType = TransactionType.fromValue(rs.getString("transactiontype"));
                valueBeforeTransaction=rs.getBigDecimal("assetcurrentvalue");
                transactionAmount = rs.getBigDecimal("salevalue");
            }
            if ( transactionType.equals(TransactionType.DISPOSAL)) {
                transactionDate = rs.getLong("disposaldate");
                transactionType = TransactionType.fromValue(rs.getString("transactiontype"));

            }

            final TransactionHistory history = TransactionHistory.builder().valueAfterTransaction(valueAfterTransaction)
                    .valueBeforeTransaction(valueBeforeTransaction).transactionAmount(transactionAmount)
                    .transactionDate(transactionDate)
                    .transactionType(transactionType).build();

            if (transactionHistoryList == null) {
                transactionHistoryList = new ArrayList<>();
                transactionHistoryList.add(history);
                transactionHistoryMap.put(assetId, transactionHistoryList);

            } else
                transactionHistoryList.add(history);

        }
        return transactionHistoryMap;

    }
}
