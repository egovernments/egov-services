package org.egov.lcms.models;

import java.util.List;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This object holds information about the parawise comments response
 */

public class ParaWiseCommentResponse {
	@JsonProperty("responseInfo")
	@Valid
	private ResponseInfo responseInfo = null;

	@JsonProperty("parawiseComments")
	@Valid
	private List<ParaWiseComment> parawiseComments = null;
}
