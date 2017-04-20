package org.egov.eis.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class Designation {
	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("name")
	private String name = null;

	@JsonProperty("code")
	private String code = null;

	@JsonProperty("description")
	private String description = null;

	@JsonProperty("tenantId")
	private String tenantId = null;

	public Designation(org.egov.eis.persistence.entity.Designation designationEntity) {
		this.id = designationEntity.getId();
		this.name = designationEntity.getName();
		this.code = designationEntity.getCode();
		this.description = designationEntity.getDescription();
		this.tenantId = designationEntity.getTenantId();
	}
}