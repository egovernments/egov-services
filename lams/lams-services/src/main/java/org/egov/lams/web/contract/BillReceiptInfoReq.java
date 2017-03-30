package org.egov.lams.web.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BillReceiptInfoReq {
	private RequestInfo requestInfo = null;

	private BillReceiptReq billReceiptInfo;

}
