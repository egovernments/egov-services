package org.egov.pt.calculator.web.models.demand;

import java.util.List;

import org.egov.pt.calculator.web.models.property.AuditDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bill {
	
	public Bill(Bill bill) {

		this.id = bill.id;
		this.isActive = bill.isActive;
		this.tenantId = bill.tenantId;
		this.payeeName = bill.payeeName;
		this.payeeEmail = bill.payeeEmail;
		this.isCancelled = bill.isCancelled;
		this.auditDetails = bill.auditDetails;
		this.billDetails = bill.billDetails;
		this.payeeAddress = bill.payeeAddress;
	}
	//TODO some of the fields are mandatory in yml, lets discuss billdetail and billaccountdetail also for more clarity
	private String id;

	private String payeeName;

	private String payeeAddress;
	
	private String mobileNumber;

	private String payeeEmail;

	private Boolean isActive;

	private Boolean isCancelled;

	private List<BillDetail> billDetails;

	private String tenantId;
	
	private AuditDetails auditDetails;
}