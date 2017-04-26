package org.egov.pgr.read.web.contract;

import java.util.List;
import org.egov.pgr.read.web.contract.ResponseInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
@Getter
@AllArgsConstructor
public class ReceivingModeResponse {

	private ResponseInfo responseInfo;
	private List<ReceivingMode> receivingModes;

}
