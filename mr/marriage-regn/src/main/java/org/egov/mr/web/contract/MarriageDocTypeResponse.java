package org.egov.mr.web.contract;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.mr.model.MarriageDocumentType;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@Builder
public class MarriageDocTypeResponse {
	private ResponseInfo responseInfo;

	private List<MarriageDocumentType> marriageDocTypes = new ArrayList<MarriageDocumentType>();
}
