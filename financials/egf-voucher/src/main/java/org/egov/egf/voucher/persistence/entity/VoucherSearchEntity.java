package org.egov.egf.voucher.persistence.entity;

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
		return this;
	}

}