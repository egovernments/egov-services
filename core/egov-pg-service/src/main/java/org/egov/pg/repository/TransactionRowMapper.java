package org.egov.pg.repository;

import org.egov.pg.models.Transaction;
import org.egov.pg.web.models.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionRowMapper implements RowMapper<Transaction> {

    @Override
    public Transaction mapRow(ResultSet resultSet, int i) throws SQLException {
        User user = User.builder().userName(resultSet.getString("user_name")).mobileNumber(resultSet.getString("mobile_number")
        ).emailId(resultSet.getString("email_id")).name(resultSet.getString("name")).tenantId(resultSet.getString("user_tenant_id"))
                .build();
        return Transaction.builder()
                .txnId(resultSet.getString("txn_id"))
                .txnAmount(resultSet.getString("txn_amount"))
                .txnStatus(Transaction.TxnStatusEnum.fromValue(resultSet.getString("txn_status")))
                .gateway(resultSet.getString("gateway"))
                .module(resultSet.getString("module"))
                .orderId(resultSet.getString("order_id"))
                .productInfo(resultSet.getString("product_info"))
                .user(user)
                .tenantId(resultSet.getString("tenant_id"))
                .gatewayTxnId(resultSet.getString("gateway_txn_id"))
                .gatewayPaymentMode(resultSet.getString("gateway_payment_mode"))
                .gatewayStatusCode(resultSet.getString("gateway_status_code"))
                .gatewayStatusMsg(resultSet.getString("gateway_status_msg"))
                .createdTime(resultSet.getLong("created_time"))
                .lastModifiedTime(resultSet.getLong("last_modified_time"))
                .build();
    }
}
