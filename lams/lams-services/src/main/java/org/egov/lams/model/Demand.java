package org.egov.lams.model;

import java.util.List;

public class Demand {
	private String id;
	private String installment;
	private String moduleName;
	private List<DemandDetails> demandDetails;
	private List<PaymentInfo> paymentInfo;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getInstallment() {
		return installment;
	}

	public void setInstallment(String installment) {
		this.installment = installment;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public List<DemandDetails> getDemandDetails() {
		return demandDetails;
	}

	public void setDemandDetails(List<DemandDetails> demandDetails) {
		this.demandDetails = demandDetails;
	}

	public List<PaymentInfo> getPaymentInfo() {
		return paymentInfo;
	}

	public void setPaymentInfo(List<PaymentInfo> paymentInfo) {
		this.paymentInfo = paymentInfo;
	}

}
