package org.egov.egf.bill.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.egov.common.domain.model.Auditable;
import org.egov.common.persistence.entity.AuditableEntity;
import org.egov.egf.bill.domain.model.Checklist;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChecklistEntity extends AuditableEntity {
    public static final String TABLE_NAME = "egf_checklist";
	public static final String SEQUENCE_NAME = "seq_egf_checklist";
    private String id;
    private String type;
    private String subType;
    private String key;
    private String description;

    public Checklist toDomain() {
	Checklist checklist = new Checklist();
	super.toDomain(checklist);
	checklist.setId(this.id);
	checklist.setType(this.type);
	checklist.setSubType(this.subType);
	checklist.setKey(this.key);
	checklist.setDescription(this.description);
	return checklist;
    }

    public ChecklistEntity toEntity(Checklist checklist) {
	super.toEntity((Auditable) checklist);
	this.id = checklist.getId();
	this.type = checklist.getType();
	this.subType = checklist.getSubType();
	this.key = checklist.getKey();
	this.description = checklist.getDescription();
	return this;
    }
}
