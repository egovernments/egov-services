package org.egov.egf.bill.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.egov.common.domain.model.Auditable;
import org.egov.common.persistence.entity.AuditableEntity;
import org.egov.egf.bill.domain.model.BillChecklist;
import org.egov.egf.bill.domain.model.BillRegister;
import org.egov.egf.bill.domain.model.Checklist;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillChecklistEntity extends AuditableEntity {
    public static final String TABLE_NAME = "egf_billchecklist";
	public static final String SEQUENCE_NAME = "seq_egf_billchecklist";
    private String id;
    private String billId;
    private String checklistId;
    private String checklistValue;

    public BillChecklist toDomain() {
	BillChecklist billChecklist = new BillChecklist();
	super.toDomain(billChecklist);
	billChecklist.setId(this.id);
	billChecklist.setBill(BillRegister.builder().id(billId).build());
	billChecklist.setChecklist(Checklist.builder().id(checklistId).build());
	billChecklist.setChecklistValue(this.checklistValue);
	return billChecklist;
    }

    public BillChecklistEntity toEntity(BillChecklist billChecklist) {
	super.toEntity((Auditable) billChecklist);
	this.id = billChecklist.getId();
	this.billId = billChecklist.getBill()!=null?billChecklist.getBill().getId():null;
	this.checklistId = billChecklist.getChecklist()!=null?billChecklist.getChecklist().getId():null;
	this.checklistValue = billChecklist.getChecklistValue();
	return this;
    }
}
