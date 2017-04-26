package org.egov.pgr.read.web.contract;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
@Getter
@AllArgsConstructor
public class ReceivingCenterResponse {

	private ResponseInfo responseInfo;
    private List<ReceivingCenter> receivingCenters;

}
