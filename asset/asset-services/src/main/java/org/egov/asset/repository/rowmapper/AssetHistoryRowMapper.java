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
            final TransactionType transactionType = TransactionType.fromValue(rs.getString("assetTranType"));
            BigDecimal valueBeforeTransaction = BigDecimal.ZERO;
            BigDecimal valueAfterTransaction = BigDecimal.ZERO;
            Long transactionDate = null;

            if (transactionType.equals(TransactionType.REVALUATION)) {
                transactionDate = rs.getLong("revaluationdate");
                transactionAmount = rs.getBigDecimal("revaluationamount");
                valueBeforeTransaction = rs.getBigDecimal("currentcapitalizedvalue");
                valueAfterTransaction = rs.getBigDecimal("valueafterrevaluation");

            } else if (transactionType.equals(TransactionType.DEPRECIATION)) {
                transactionDate = rs.getLong("todate");
                transactionAmount = rs.getBigDecimal("depreciationvalue");
                valueBeforeTransaction = rs.getBigDecimal("valuebeforedepreciation");
                valueAfterTransaction = rs.getBigDecimal("valueafterdepreciation");

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
