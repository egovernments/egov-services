package io.swagger.model;

import java.util.List;

import org.egov.common.contract.response.ResponseInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class IdGenerationResponse {

	private ResponseInfo responseInfo;

	private List<IdResponse> idResponses;

}
