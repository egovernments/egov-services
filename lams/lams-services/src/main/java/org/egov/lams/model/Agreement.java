package org.egov.lams.model;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.egov.lams.model.enums.NatureOfAllotment;
import org.egov.lams.model.enums.PaymentCycle;
import org.egov.lams.model.enums.Status;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Agreement {

	private Long id;

	@NotNull
	private String tenantId;
	private String agreementNumber;
	private String acknowledgementNumber;
	private String stateId;

	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date agreementDate;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date closeDate;
	@NotNull
	private Long timePeriod;
	private Allottee allottee;
	private Asset asset;
	private String tenderNumber;

	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date tenderDate;
	private String councilNumber;

	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date councilDate;
	private Double bankGuaranteeAmount;

	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date bankGuaranteeDate;
	@NotNull
	private Double securityDeposit;

	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date securityDepositDate;
	private Status status;

	@NotNull
	private NatureOfAllotment natureOfAllotment;
	private Double registrationFee;
	private String caseNo;

	@NotNull
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date commencementDate;

	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date expiryDate;
	private String orderDetails;

	@NotNull
	private Double rent;
	private String tradelicenseNumber;

	@NotNull
	private PaymentCycle paymentCycle;
	private RentIncrementType rentIncrementMethod;
	private String orderNo;

	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date orderDate;
	private String rrReadingNo;
	private String remarks;
	private String solvencyCertificateNo;

	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date solvencyCertificateDate;
	private String tinNumber;

	private List<Document> documents;
	private List<String> demands;
	private WorkFlowDetails workflowDetails;
	private Double goodWillAmount;
}
