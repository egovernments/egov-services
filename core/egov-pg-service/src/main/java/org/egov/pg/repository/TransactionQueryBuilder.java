package org.egov.pg.repository;


import org.egov.pg.web.models.TransactionCriteria;

import java.util.*;

class TransactionQueryBuilder {
    private static final String SEARCH_TXN_SQL = "SELECT pg.txn_id, pg.txn_amount, pg.txn_status, pg.gateway, pg" +
            ".module, pg.order_id, pg.product_info, pg.user_name, pg_user.mobile_number, pg_user.email_id, pg_user" +
            ".name, pg_user.tenant_id as user_Tenant_Id , pg.tenant_id, pg.gateway_txn_id, pg.gateway_payment_mode, pg.gateway_status_code," +
            " pg.gateway_status_msg, pg.created_time, pg.last_modified_time " +
            "FROM eg_pg_transactions pg INNER JOIN eg_pg_user pg_user ON pg.user_name = pg_user.user_name ";

    private TransactionQueryBuilder() {
    }

    static String getPaymentSearchQuery(TransactionCriteria transactionCriteria, List<Object> preparedStmtList) {

        StringBuilder builder = new StringBuilder(SEARCH_TXN_SQL);
        Map<String, String> query = new HashMap<>();

        if (!Objects.isNull(transactionCriteria.getTenantId())) {
            query.put("pg.tenant_id", transactionCriteria.getTenantId());
        }

        if (!Objects.isNull(transactionCriteria.getTxnId())) {
            query.put("pg.txn_id", transactionCriteria.getTxnId());
        }

        if (!Objects.isNull(transactionCriteria.getOrderId())) {
            query.put("pg.order_id", transactionCriteria.getOrderId());
        }

        if (!Objects.isNull(transactionCriteria.getTxnStatus())) {
            query.put("pg.txn_status", transactionCriteria.getTxnStatus().toString());
        }

        if (!Objects.isNull(transactionCriteria.getModule())) {
            query.put("pg.module", transactionCriteria.getModule());
        }

        if (!query.isEmpty()) {

            builder.append(" WHERE ");

            Iterator<Map.Entry<String, String>> iterator = query.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                builder.append(entry.getKey()).append(" = ? ");
                preparedStmtList.add(entry.getValue());

                if (iterator.hasNext())
                    builder.append(" AND ");
            }
        }

        builder.append(" limit ? offset ?");
        preparedStmtList.add(transactionCriteria.getLimit() > 0 ? transactionCriteria.getLimit() : 1);
        preparedStmtList.add(transactionCriteria.getOffset());

        return builder.toString();
    }

}
