package org.egov.lams.contract;

import java.util.ArrayList;
import java.util.List;
import org.egov.lams.model.DocumentType;
import org.egov.lams.model.ResponseInfo;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentTypeResponse {

	@JsonProperty("ResposneInfo")
	private ResponseInfo resposneInfo = null;

	@JsonProperty("documentTypes")
	private List<DocumentType> documentTypes = new ArrayList<DocumentType>();
}
