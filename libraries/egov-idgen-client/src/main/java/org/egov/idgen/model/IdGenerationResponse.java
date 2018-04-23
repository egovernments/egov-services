package org.egov.idgen.model;

import java.util.List;

import org.egov.common.contract.response.ResponseInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdGenerationResponse {

	private ResponseInfo responseInfo;

	private List<IdResponse> idResponses;

}
