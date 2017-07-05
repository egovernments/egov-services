package org.egov.egf.master.persistence.entity;

import org.egov.common.domain.model.Auditable;
import org.egov.common.persistence.entity.AuditableEntity;
import org.egov.egf.master.domain.model.EgfStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Builder
public class EgfStatusEntity extends AuditableEntity {
	public static final String TABLE_NAME = "egf_egfstatus";
	private String id;
	private String moduleType;
	private String code;
	private String description;

	public EgfStatus toDomain() {
		EgfStatus egfStatus = new EgfStatus();
		super.toDomain(egfStatus);
		egfStatus.setId(this.id);
		egfStatus.setModuleType(this.moduleType);
		egfStatus.setCode(this.code);
		egfStatus.setDescription(this.description);
		return egfStatus;
	}

	public EgfStatusEntity toEntity(EgfStatus egfStatus) {
		super.toEntity((Auditable) egfStatus);
		this.id = egfStatus.getId();
		this.moduleType = egfStatus.getModuleType();
		this.code = egfStatus.getCode();
		this.description = egfStatus.getDescription();
		return this;
	}

}
