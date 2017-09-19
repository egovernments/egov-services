package org.egov.egf.bill.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.egov.egf.bill.domain.model.BillChecklist;
import org.egov.egf.bill.domain.model.BillChecklistSearch;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BillChecklistSearchEntity extends BillChecklistEntity {
	private String ids;
	private String sortBy;
	private Integer pageSize;
	private Integer offset;

	public BillChecklist toDomain() {
		BillChecklist billChecklist = new BillChecklist();
		super.toDomain(billChecklist);
		return billChecklist;
	}

	public BillChecklistSearchEntity toEntity(BillChecklistSearch billChecklistSearch) {
		super.toEntity((BillChecklist) billChecklistSearch);
		this.pageSize = billChecklistSearch.getPageSize();
		this.offset = billChecklistSearch.getOffset();
		this.sortBy = billChecklistSearch.getSortBy();
		this.ids = billChecklistSearch.getIds();
		return this;
	}

}