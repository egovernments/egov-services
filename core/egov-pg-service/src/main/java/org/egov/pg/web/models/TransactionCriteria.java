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

    private String billId;

    private String userUuid;

    private String module;

    private String moduleId;

    private String receipt;

    private Long createdTime;

    private Transaction.TxnStatusEnum txnStatus;

    private int limit;

    private int offset;

}
