package org.egov.pgr.read.web.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ReceivingCenter {
	private Long id;
	private String name;
	private boolean crnRequired;
	private Long orderNo;
	private String tenantId;

	public ReceivingCenter(org.egov.pgr.read.domain.model.ReceivingCenter receivingCenter) {
		this.id = receivingCenter.getId();
		this.name = receivingCenter.getName();
		this.orderNo = receivingCenter.getOrderNo();
		this.tenantId = receivingCenter.getTenantId();
		this.crnRequired = receivingCenter.isCrnRequired();
	}
}
