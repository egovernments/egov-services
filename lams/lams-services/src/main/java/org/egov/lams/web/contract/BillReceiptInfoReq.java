package org.egov.lams.web.contract;

import java.util.ArrayList;
import java.util.List;

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

	private List<BillReceiptInfo> billReceiptInfo = new ArrayList<>();

}
