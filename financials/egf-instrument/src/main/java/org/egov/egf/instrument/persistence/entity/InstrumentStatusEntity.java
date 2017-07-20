package org.egov.egf.instrument.persistence.entity;

import org.egov.common.domain.model.Auditable;
import org.egov.common.persistence.entity.AuditableEntity;
import org.egov.egf.instrument.domain.model.InstrumentStatus;

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
public class InstrumentStatusEntity extends AuditableEntity {
	public static final String TABLE_NAME = "egf_instrumentstatus";
	private String id;
	private String moduleType;
	private String name;
	private String description;

	public InstrumentStatus toDomain() {
		InstrumentStatus instrumentStatus = new InstrumentStatus();
		super.toDomain(instrumentStatus);
		instrumentStatus.setId(this.id);
		instrumentStatus.setModuleType(this.moduleType);
		instrumentStatus.setName(this.name);
		instrumentStatus.setDescription(this.description);
		return instrumentStatus;
	}

	public InstrumentStatusEntity toEntity(InstrumentStatus instrumentStatus) {
		super.toEntity((Auditable) instrumentStatus);
		this.id = instrumentStatus.getId();
		this.moduleType = instrumentStatus.getModuleType();
		this.name = instrumentStatus.getName();
		this.description = instrumentStatus.getDescription();
		return this;
	}

}
