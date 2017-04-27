package org.egov.lams.web.contract;

import java.util.ArrayList;
import java.util.List;

import org.egov.lams.model.DocumentType;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentTypeResponse {

	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo = null;

	@JsonProperty("documentTypes")
	private List<DocumentType> documentTypes = new ArrayList<>();
}
