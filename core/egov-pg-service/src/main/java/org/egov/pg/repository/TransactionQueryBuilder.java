package org.egov.pg.repository;


import org.egov.pg.web.models.TransactionCriteria;

import java.util.*;

class TransactionQueryBuilder {
    private static final String SEARCH_TXN_SQL = "SELECT pg.txn_id, pg.txn_amount, pg.txn_status, pg.txn_status_msg, " +
            "pg.gateway, pg.module, pg.module_id, pg.bill_id, pg.product_info, pg.user_uuid, pg.user_name, pg" +
            ".mobile_number, pg.email_id, pg.name, pg.user_tenant_id, pg.tenant_id, pg.gateway_txn_id, pg.gateway_payment_mode, " +
            "pg.gateway_status_code, pg.gateway_status_msg, pg.receipt,  pg.created_by, pg.created_time, pg" +
            ".last_modified_by, pg.last_modified_time " +
            "FROM eg_pg_transactions pg ";

    private TransactionQueryBuilder() {
    }

    static String getPaymentSearchQuery(TransactionCriteria transactionCriteria, List<Object> preparedStmtList) {

        return buildQuery(transactionCriteria, preparedStmtList);
    }


    private static String buildQuery(TransactionCriteria transactionCriteria, List<Object> preparedStmtList) {
        StringBuilder builder = new StringBuilder(TransactionQueryBuilder.SEARCH_TXN_SQL);
        Map<String, Object> queryParams = new HashMap<>();

        if (!Objects.isNull(transactionCriteria.getTenantId())) {
            queryParams.put("pg.tenant_id", transactionCriteria.getTenantId());
        }

        if (!Objects.isNull(transactionCriteria.getTxnId())) {
            queryParams.put("pg.txn_id", transactionCriteria.getTxnId());
        }

        if (!Objects.isNull(transactionCriteria.getUserUuid())) {
            queryParams.put("pg.user_uuid", transactionCriteria.getUserUuid());
        }

        if (!Objects.isNull(transactionCriteria.getBillId())) {
            queryParams.put("pg.bill_id", transactionCriteria.getBillId());
        }

        if (!Objects.isNull(transactionCriteria.getTxnStatus())) {
            queryParams.put("pg.txn_status", transactionCriteria.getTxnStatus().toString());
        }

        if (!Objects.isNull(transactionCriteria.getModule())) {
            queryParams.put("pg.module", transactionCriteria.getModule());
        }

        if (!Objects.isNull(transactionCriteria.getModuleId())) {
            queryParams.put("pg.module_id", transactionCriteria.getModuleId());
        }

        if (!Objects.isNull(transactionCriteria.getReceipt())) {
            queryParams.put("pg.receipt", transactionCriteria.getReceipt());
        }


        if (!Objects.isNull(transactionCriteria.getCreatedTime())) {
            queryParams.put("pg.created_time", transactionCriteria.getCreatedTime());
        }

        if (!queryParams.isEmpty()) {

            builder.append(" WHERE ");

            Iterator<Map.Entry<String, Object>> iterator = queryParams.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                if (entry.getKey().equalsIgnoreCase("pg.created_time")) {
                    builder.append(entry.getKey()).append(" > ? ");
                } else {
                    builder.append(entry.getKey()).append(" = ? ");
                }

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
