package org.egov.pgrrest.read.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.egov.common.contract.response.ResponseInfo;

@Getter
@AllArgsConstructor
public class UpdateEligibiltyResponse {

    private ResponseInfo responseInfo;

    @JsonProperty("isUpdateAllowed")
    private boolean updateAllowed;
}
