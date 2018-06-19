package org.egov.pg.web.models;

import lombok.*;
import org.egov.pg.models.Transaction;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class TransactionCriteria {

    private String tenantId;

    private String txnId;

    private String orderId;

    private String userId;

    private String module;

    private Transaction.TxnStatusEnum txnStatus;

    private int limit;

    private int offset;

}
