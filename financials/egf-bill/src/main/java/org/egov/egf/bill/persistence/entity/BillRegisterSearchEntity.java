package org.egov.egf.bill.persistence.entity;

import org.egov.egf.bill.domain.model.BillRegister;
import org.egov.egf.bill.domain.model.BillRegisterSearch;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BillRegisterSearchEntity extends BillRegisterEntity {
    private String ids;
    private String sortBy;
    private Integer pageSize;
    private Integer offset;

    public BillRegister toDomain() {
	BillRegister billRegister = new BillRegister();
	super.toDomain(billRegister);
	return billRegister;
    }

    public BillRegisterSearchEntity toEntity(BillRegisterSearch billRegisterSearch) {
	super.toEntity((BillRegister) billRegisterSearch);
	this.pageSize = billRegisterSearch.getPageSize();
	this.offset = billRegisterSearch.getOffset();
	this.sortBy = billRegisterSearch.getSortBy();
	this.ids = billRegisterSearch.getIds();
	return this;
    }
}