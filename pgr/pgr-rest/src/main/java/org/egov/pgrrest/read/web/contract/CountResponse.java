package org.egov.pgrrest.read.web.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.egov.common.contract.response.ResponseInfo;

@Getter
@AllArgsConstructor
public class CountResponse {

    private ResponseInfo responseInfo;
    private Long count;
}
