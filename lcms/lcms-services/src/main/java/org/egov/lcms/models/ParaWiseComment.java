package org.egov.lcms.models;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This object holds information about parawise comments about the case
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParaWiseComment {

	private Long id;

	@JsonProperty("parawiseCommentsAskedDate")
	@NotNull
	@NotEmpty
	private Long parawiseCommentsAskedDate = null;

	@JsonProperty("parawiseCommentsReceivedDate")
	@NotNull
	@NotEmpty
	private Long parawiseCommentsReceivedDate = null;

	@JsonProperty("hodProvidedDate")
	@NotNull
	@NotEmpty
	private Long hodProvidedDate = null;

	@JsonProperty("resolutionDate")
	@NotNull
	@NotEmpty
	private Long resolutionDate = null;

	@JsonProperty("paraWiseComments")
	@NotNull
	@NotEmpty
	private String paraWiseComments = null;

	@JsonProperty("referenceNo")
	@NotEmpty
	@NotNull
	private String referenceNo = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

	@JsonProperty("documents")
	private List<String> documents = null;

	@NotNull
	@NotEmpty
	@Size(min = 4, max = 128)
	private String tenantId;

}
