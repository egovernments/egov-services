package org.egov.egf.instrument.persistence.entity;

import org.egov.common.persistence.entity.AuditableEntity;
import org.egov.egf.instrument.domain.model.InstrumentType;

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
public class InstrumentTypeEntity extends AuditableEntity {
	public static final String TABLE_NAME = "egf_instrumenttype";
	private String id;
	private String name;
	private String description;
	private Boolean active;

	public InstrumentType toDomain() {
		InstrumentType instrumentType = new InstrumentType();
		super.toDomain(instrumentType);
		instrumentType.setId(this.id);
		instrumentType.setName(this.name);
		instrumentType.setDescription(this.description);
		instrumentType.setActive(this.active);
		return instrumentType;
	}

	public InstrumentTypeEntity toEntity(InstrumentType instrumentType) {
		super.toEntity(instrumentType);
		this.id = instrumentType.getId();
		this.name = instrumentType.getName();
		this.description = instrumentType.getDescription();
		this.active = instrumentType.getActive();
		return this;
	}

}
