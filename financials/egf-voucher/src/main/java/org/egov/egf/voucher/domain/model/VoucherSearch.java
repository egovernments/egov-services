package org.egov.egf.voucher.domain.model;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VoucherSearch extends Voucher {

    private String ids;

    private String sortBy;

    private Integer pageSize;

    private Integer offset;

    private String glcode;
    
    private String glcodes;

    private BigDecimal debitAmount;

    private BigDecimal creditAmount;

    private String accountDetailTypeId;

    private String accountDetailKeyId;

    private BigDecimal subLedgerAmount;

    private String types;

    private String names;

    private String voucherNumbers;

    private String statuses;

    private Date voucherFromDate;

    private Date voucherToDate;
}