package org.egov.collection.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.egov.common.contract.response.ResponseInfo;

import java.util.List;

@Getter
@AllArgsConstructor
public class IdGenerationResponse {

    private ResponseInfo responseInfo;

    private List<IdResponse> idResponses;

}
