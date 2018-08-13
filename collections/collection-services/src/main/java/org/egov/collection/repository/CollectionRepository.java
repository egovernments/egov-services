package org.egov.collection.repository;

import lombok.extern.slf4j.Slf4j;
import org.egov.collection.model.ReceiptSearchCriteria;
import org.egov.collection.repository.querybuilder.CollectionsQueryBuilder;
import org.egov.collection.repository.rowmapper.ReceiptResultSetExtractor;
import org.egov.collection.web.contract.Bill;
import org.egov.collection.web.contract.BillAccountDetail;
import org.egov.collection.web.contract.BillDetail;
import org.egov.collection.web.contract.Receipt;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

import static org.egov.collection.repository.querybuilder.CollectionsQueryBuilder.*;

@Repository
@Slf4j
public class CollectionRepository {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private ReceiptResultSetExtractor receiptResultSetExtractor = new ReceiptResultSetExtractor();


    public void saveReceipt(Receipt receipt){
        Bill bill = receipt.getBill().get(0);
        try {

            List<MapSqlParameterSource> receiptHeaderSource = new ArrayList<>();
            List<MapSqlParameterSource> receiptDetailSource = new ArrayList<>();

            for (BillDetail billDetail : bill.getBillDetails()) {
                Long receiptHeaderId = namedParameterJdbcTemplate.queryForObject(SELECT_RECEIPT_HEADER_SEQ_NEXT, Collections
                        .emptyMap(), Long.class);
                receiptHeaderSource.add(getParametersForReceiptHeader(receipt, billDetail, receiptHeaderId));
                billDetail.setId(String.valueOf(receiptHeaderId));

                for (BillAccountDetail billAccountDetail : billDetail.getBillAccountDetails()) {
                    receiptDetailSource.add(getParametersForReceiptDetails(billAccountDetail, receiptHeaderId));
                }

            }

            namedParameterJdbcTemplate.batchUpdate(INSERT_RECEIPT_HEADER_SQL, receiptHeaderSource.toArray(new MapSqlParameterSource[0]));
            namedParameterJdbcTemplate.batchUpdate(INSERT_RECEIPT_DETAILS_SQL,  receiptDetailSource.toArray(new MapSqlParameterSource[0]));

        }catch (Exception e){
            log.error("Failed to persist receipt to database", e);
            throw new CustomException("RECEIPT_CREATION_FAILED", "Unable to create receipt");
        }
    }

    public void saveInstrument(Receipt receipt){
        Bill bill = receipt.getBill().get(0);
        try {

            List<MapSqlParameterSource> instrumentSource = new ArrayList<>();

            for (BillDetail billDetail : bill.getBillDetails()) {
                instrumentSource.add(getParametersForInstrument(receipt.getInstrument(), Long.valueOf(billDetail
                        .getId())));
            }

            namedParameterJdbcTemplate.batchUpdate(INSERT_INSTRUMENT_SQL, instrumentSource.toArray(new MapSqlParameterSource[0]));
        }catch (Exception e){
            log.error("Failed to persist instrument to database", e);
            throw new CustomException("INSTRUMENT_CREATION_FAILED", "Unable to create instrument");
        }
    }

    public List<Receipt> fetchReceipts(ReceiptSearchCriteria receiptSearchCriteria){
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = CollectionsQueryBuilder.getReceiptSearchQuery(receiptSearchCriteria, preparedStatementValues);
        List<Receipt> receipts = namedParameterJdbcTemplate.query(query, preparedStatementValues,
                receiptResultSetExtractor);
        return receipts;
    }

}
