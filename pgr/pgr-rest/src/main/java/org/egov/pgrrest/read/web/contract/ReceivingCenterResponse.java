package org.egov.pgrrest.read.web.contract;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.egov.common.contract.response.ResponseInfo;

@Getter
@AllArgsConstructor
public class ReceivingCenterResponse {

	private ResponseInfo responseInfo;
    private List<ReceivingCenter> receivingCenters;

}
