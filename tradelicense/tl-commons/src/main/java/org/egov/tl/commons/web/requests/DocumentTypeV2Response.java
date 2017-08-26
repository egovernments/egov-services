package org.egov.tl.commons.web.requests;

import java.util.List;

import org.egov.tl.commons.web.contract.DocumentTypeContract;
import org.egov.tl.commons.web.contract.ResponseInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class describe the set of fields contained in DocumentTypeResponse
 * 
 * 
 * @author Shubham Pratap Singh
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentTypeV2Response {

	private ResponseInfo responseInfo;

	private List<DocumentTypeContract> documentTypes;
}