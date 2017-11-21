package org.egov.pa.web.contract;

import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.pa.model.DocumentTypeContract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentTypeResponse {

	private ResponseInfo responseInfo;

	private List<DocumentTypeContract> documentTypes;
}