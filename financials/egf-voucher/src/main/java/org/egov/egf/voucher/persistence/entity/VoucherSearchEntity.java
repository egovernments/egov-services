package org.egov.egf.voucher.persistence.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.egov.egf.voucher.domain.model.Voucher;
import org.egov.egf.voucher.domain.model.VoucherSearch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class VoucherSearchEntity extends VoucherEntity {

    private String ids;

    private String sortBy;

    private Integer pageSize;

    private Integer offset;

    private String glcode;

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

    public Voucher toDomain() {
        Voucher voucher = new Voucher();
        super.toDomain(voucher);
        return voucher;
    }

    public VoucherSearchEntity toEntity(VoucherSearch voucherSearch) {

        super.toEntity((Voucher) voucherSearch);

        this.pageSize = voucherSearch.getPageSize();
        this.offset = voucherSearch.getOffset();
        this.sortBy = voucherSearch.getSortBy();
        this.ids = voucherSearch.getIds();
        this.glcode = voucherSearch.getGlcode();
        this.debitAmount = voucherSearch.getDebitAmount();
        this.creditAmount = voucherSearch.getCreditAmount();
        this.accountDetailKeyId = voucherSearch.getAccountDetailKeyId();
        this.accountDetailTypeId = voucherSearch.getAccountDetailTypeId();
        this.subLedgerAmount = voucherSearch.getSubLedgerAmount();
        this.types = voucherSearch.getTypes();
        this.names = voucherSearch.getNames();
        this.voucherNumbers = voucherSearch.getVoucherNumbers();
        this.voucherFromDate = voucherSearch.getVoucherFromDate();
        this.voucherToDate = voucherSearch.getVoucherToDate();
        this.statuses = voucherSearch.getStatuses();

        return this;

    }

}