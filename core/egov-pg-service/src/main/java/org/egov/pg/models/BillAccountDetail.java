package org.egov.pg.models;


import lombok.*;

import java.math.BigDecimal;


@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class BillAccountDetail {

    private String glcode;

    private Integer order;

    private String accountDescription;

    private Boolean isActualDemand;

    private String id;

    private String tenantId;

    private String billDetail;

    private BigDecimal crAmountToBePaid = BigDecimal.ZERO;

    private BigDecimal creditAmount = BigDecimal.ZERO;

    private BigDecimal debitAmount = BigDecimal.ZERO;

    private Purpose purpose;

}

