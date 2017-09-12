package org.egov.mr.model;

import javax.validation.constraints.NotNull;

import org.egov.mr.model.enums.ApplicationType;
import org.egov.mr.model.enums.DocumentProof;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@Builder
public class MarriageDocumentType {

	private Long id;

	@NotNull
	private String name;

	private String code;

	@NotNull
	private DocumentProof proof;

	@NotNull
	private ApplicationType applicationType;

	@NotNull
	private Boolean isActive;

	@NotNull
	private Boolean isIndividual;

	@NotNull
	private Boolean isRequired;

	@NotNull
	private String tenantId;
}
