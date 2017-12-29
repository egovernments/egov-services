package org.egov.lams.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
public class AgreementInfo {

	private Long id;
	private String agreementNumber;
	private String acknowledgementNumber;

	private String allotteeName;
	private String doorno;
	private String locality;
	private String revenueward;
	private String electionward;
	private String tradelicenseNumber;
	private String tenderNumber;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date tenderDate;
	private Double bankGuaranteeAmount;

	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date bankGuaranteeDate;
	private Double securityDeposit;

	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date securityDepositDate;
	private Double goodWillAmount;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date commencementDate;
	private Long timePeriod;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date expiryDate;
	private String action;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date agreementDate;
	private String assetCategory;
	private String status;
	private String natureOfAllotment;
	private Double rent;
	private Double balance;
	private String paymentCycle;
	private String source;

}
