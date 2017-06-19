package org.egov.pgr.common.repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.egov.common.contract.response.ResponseInfo;
import org.springframework.util.CollectionUtils;

import java.util.List;

@AllArgsConstructor
@Getter
class DesignationResponse {

	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo;

	@JsonProperty("Designation")
	private List<Designation> designation;

    public org.egov.pgr.common.model.Designation toDomain() {
        if (CollectionUtils.isEmpty(designation)) {
            return null;
        }
        return new org.egov.pgr.common.model.Designation(designation.get(0).getName());
    }
}