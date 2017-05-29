package org.egov.demand.web.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class BillSearchCriteria {
	private Long billId;
	private Long demandId;
	private String history = null;
	private String cancelled = null;
	private String billType = null;
	private String consumerCode = null;

}
