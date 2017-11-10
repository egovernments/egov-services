package org.egov.lcms.notification.model;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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

	private String code;

	@JsonProperty("parawiseCommentsAskedDate")
	@NotNull
	private Long parawiseCommentsAskedDate = null;

	@JsonProperty("parawiseCommentsReceivedDate")
	@NotNull
	private Long parawiseCommentsReceivedDate = null;

	@JsonProperty("hodProvidedDate")
	@NotNull
	private Long hodProvidedDate = null;

	@JsonProperty("resolutionDate")
	@NotNull
	private Long resolutionDate = null;

	@JsonProperty("paraWiseComments")
	@NotNull
	private String paraWiseComments = null;


	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

	@JsonProperty("documents")
	private List<Document> documents = null;

	@NotNull
	@Size(min = 4, max = 128)
	private String tenantId;
}
