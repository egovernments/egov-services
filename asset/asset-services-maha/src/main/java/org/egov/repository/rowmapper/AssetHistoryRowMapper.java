package org.egov.repository.rowmapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.egov.model.TransactionHistory;
import org.egov.model.enums.TransactionType;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AssetHistoryRowMapper implements ResultSetExtractor<Map<Long, List<TransactionHistory>>> {

	@Override
	public Map<Long, List<TransactionHistory>> extractData(ResultSet rs) throws SQLException, DataAccessException {
		
		
		final Map<Long, List<TransactionHistory>> transactionHistoryMap = new TreeMap<>();


        while (rs.next()) {
            final Long assetId = rs.getLong("assetid");

            log.info("AssetHistory in row mapper" + assetId);
            
            
            List<TransactionHistory> transactionHistoryList = transactionHistoryMap.get(assetId);
            
            BigDecimal transactionAmount = null;
            TransactionType transactionType = TransactionType.fromValue(rs.getString("assetTranType"));
            BigDecimal valueBeforeTransaction=null;
            BigDecimal valueAfterTransaction=null;
            
            if(transactionType.equals(TransactionType.REVALUATION)) {
            	
            	transactionAmount = rs.getBigDecimal("revaluationamount");
                valueBeforeTransaction=rs.getBigDecimal("currentcapitalizedvalue");
                valueAfterTransaction=rs.getBigDecimal("valueafterrevaluation");
            
            }else if(transactionType.equals(TransactionType.DEPRECIATION)) {
            	
         		transactionAmount = rs.getBigDecimal("depreciationvalue");
                valueBeforeTransaction=rs.getBigDecimal("valuebeforedepreciation");
                valueAfterTransaction=rs.getBigDecimal("valueafterdepreciation");
         	}
          
			TransactionHistory history = TransactionHistory.builder().valueAfterTransaction(valueAfterTransaction)
					.valueBeforeTransaction(valueBeforeTransaction).transactionDate(rs.getLong("transactiondate")).transactionAmount(transactionAmount)
					.transactionType(transactionType).build();
         	 
            
            if (transactionHistoryList == null) {
            	transactionHistoryList = new ArrayList<>();
            	transactionHistoryList.add(history);
            	transactionHistoryMap.put(assetId, transactionHistoryList);
            	
            }else {
            	transactionHistoryList.add(history);
            }
            
     }
        return transactionHistoryMap;

}
}
	
