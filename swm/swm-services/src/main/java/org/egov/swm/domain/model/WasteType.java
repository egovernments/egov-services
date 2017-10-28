package org.egov.swm.domain.model;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.egov.swm.domain.model.DumpingGround.DumpingGroundBuilder;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WasteType {
	@Length(min = 1, max = 256)
	@JsonProperty("id")
	private String id = null;

	@NotNull
	@Length(min = 1, max = 128)
	@JsonProperty("tenantId")
	private String tenantId = null;

	@NotNull
	@Length(min = 1, max = 128)
	@JsonProperty("name")
	private String name = null;

	@NotNull
	@Length(min = 1, max = 128)
	@JsonProperty("code")
	private String code = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

}
