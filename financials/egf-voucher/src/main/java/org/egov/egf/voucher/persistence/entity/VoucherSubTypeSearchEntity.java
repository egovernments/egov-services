package org.egov.egf.voucher.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.egov.egf.voucher.domain.model.VoucherSubType;
import org.egov.egf.voucher.domain.model.VoucherSubTypeSearch;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VoucherSubTypeSearchEntity extends VoucherSubTypeEntity {

    private String ids;

    private String sortBy;

    private Integer pageSize;

    private Integer offset;

    public VoucherSubType toDomain() {

        VoucherSubType voucherSubType = new VoucherSubType();
        super.toDomain(voucherSubType);
        return voucherSubType;

    }

    public VoucherSubTypeSearchEntity toEntity(
            VoucherSubTypeSearch voucherSubTypeSearch) {

        super.toEntity((VoucherSubType) voucherSubTypeSearch);

        this.pageSize = voucherSubTypeSearch.getPageSize();
        this.offset = voucherSubTypeSearch.getOffset();
        this.sortBy = voucherSubTypeSearch.getSortBy();
        this.ids = voucherSubTypeSearch.getIds();

        return this;

    }
}
