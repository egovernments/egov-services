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
public class DefaultersInfo {

	private Long id;
	private String agreementNumber;
	private String acknowledgementNumber;

	private String doorno;
	private Double securityDeposit;
	private Double goodWillAmount;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date commencementDate;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date expiryDate;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date agreementDate;
	private Long timePeriod;
	private String action;
	private String status;
	private String assetName;
	private String assetCode;
	private Long assetCategory;
	private String categoryName;
	private Long locality;
	private Long street;
	private Long zone;
	private Long revenueWard;
	private Long block;
	private Long electionward;
	private String paymentCycle;
	private String source;

	private String allotteeName;
	private String mobileNumber;
	private Double totalAmount;
	private Double totalCollection;
	private Double totalBalance;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date lastPaid;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date installmentFromDate;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date installmentToDate;
	private String installment;
	private String tenantId;

}
