package org.egov.mr.web.contract;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.mr.model.MarriageDocumentType;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@Builder
public class MarriageDocTypeRequest {

	@JsonProperty("RequestInfo")
	@Valid
	private RequestInfo requestInfo;

	@JsonProperty("MarriageDocumentType")
	@Valid
	private List<MarriageDocumentType> marriageDocTypes = new ArrayList<MarriageDocumentType>();
}
