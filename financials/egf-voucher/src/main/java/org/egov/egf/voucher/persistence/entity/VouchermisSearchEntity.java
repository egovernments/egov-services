package org.egov.egf.voucher.persistence.entity;

import java.util.ArrayList;
import java.util.List;

import org.egov.egf.voucher.domain.model.VouchermisSearch;

import lombok.Data;

@Data
public class VouchermisSearchEntity extends VouchermisEntity {
	private Integer pageSize;
	private Integer offset = 0;
	private String sortBy;
	private List<Long> ids = new ArrayList<Long>();

	public VouchermisSearchEntity toEntity(VouchermisSearch vouchermisSearch) {

		super.toEntity(vouchermisSearch);
		this.pageSize = vouchermisSearch.getPageSize();
		this.sortBy = vouchermisSearch.getSortBy();
		this.ids = vouchermisSearch.getIds();
		
		return this;

	}

}
