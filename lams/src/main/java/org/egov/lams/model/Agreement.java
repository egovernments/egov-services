package org.egov.lams.model;

import java.util.Date;

import org.egov.lams.model.enums.NatureOfAllotment;
import org.egov.lams.model.enums.PaymentCycle;
import org.egov.lams.model.enums.Status;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Agreement {

	private Long id;
	private String agreementNumber;

	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date agreementDate;
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
	private Double securityDeposit;

	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date securityDepositDate;
	private Status status;
	private NatureOfAllotment natureOfAllotment;
	private Double registrationFee;
	private String caseNo;

	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date commencementDate;

	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date expiryDate;
	private String orderDetails;
	private Double rent;
	private String tradelicenseNumber;
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
}
