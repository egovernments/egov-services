package org.egov.models;

import java.util.List;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentTypeRequest {
	
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;

	@JsonProperty("documentType")
	@Valid
	private List<DocumentType> documentType;
}
