package org.egov.tl.commons.web.requests;

import java.util.List;

import javax.validation.Valid;

import org.egov.tl.commons.web.contract.DocumentTypeContract;
import org.egov.tl.commons.web.contract.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class describe the set of fields contained in DocumentTypeRequest
 * 
 * @author Shubham pratap Singh
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentTypeV2Request {

	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;

	@Valid
	private List<DocumentTypeContract> documentTypes;
}