package org.egov.pg.repository;

import lombok.extern.slf4j.Slf4j;
import org.egov.pg.models.Transaction;
import org.egov.pg.web.models.TransactionCriteria;
import org.egov.pg.web.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class TransactionRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    TransactionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Transaction> fetchTransactions(TransactionCriteria transactionCriteria) {
        List<Object> params = new ArrayList<>();
        String query = TransactionQueryBuilder.getPaymentSearchQuery(transactionCriteria, params);
        log.debug(query);
        return jdbcTemplate.query(query, params.toArray(), (rs, rowNum) -> {
            User user = User.builder().userName(rs.getString("user_name")).mobileNumber(rs.getString("mobile_number")
            ).emailId(rs.getString("email_id")).name(rs.getString("name")).tenantId(rs.getString("user_tenant_id"))
                    .build();
            return Transaction.builder()
                    .txnId(rs.getString("txn_id"))
                    .txnAmount(rs.getString("txn_amount"))
                    .txnStatus(Transaction.TxnStatusEnum.fromValue(rs.getString("txn_status")))
                    .gateway(rs.getString("gateway"))
                    .module(rs.getString("module"))
                    .orderId(rs.getString("order_id"))
                    .productInfo(rs.getString("product_info"))
                    .user(user)
                    .tenantId(rs.getString("tenant_id"))
                    .gatewayTxnId(rs.getString("gateway_txn_id"))
                    .gatewayPaymentMode(rs.getString("gateway_payment_mode"))
                    .gatewayStatusCode(rs.getString("gateway_status_code"))
                    .gatewayStatusMsg(rs.getString("gateway_status_msg"))
                    .createdTime(rs.getLong("created_time"))
                    .lastModifiedTime(rs.getLong("last_modified_time"))
                    .build();
        });
    }

}
