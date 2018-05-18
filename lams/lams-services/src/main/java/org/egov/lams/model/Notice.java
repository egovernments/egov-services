package org.egov.lams.model;

import java.util.Date;

import lombok.*;

import org.egov.lams.model.enums.Status;
import org.egov.lams.util.AmountInWordUtil;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;


/**
 * Notice
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Notice   {

  @JsonProperty("id")
  private Long id = null;
  
  @JsonProperty("tenantId")
  @NotNull
  private String tenantId = null;
  
  @JsonProperty("noticeNo")
  private String noticeNo = null;

  @JsonProperty("noticeDate")
  @JsonFormat(pattern = "dd/MM/yyyy")
  private Date noticeDate = null;

  @JsonProperty("agreementNumber")
  private String agreementNumber = null;

  @JsonProperty("assetCategory")
  @NotNull
  private Long assetCategory = null;

  @JsonProperty("acknowledgementNumber")
  private String acknowledgementNumber = null;

  @JsonProperty("assetNo")
  @NotNull
  private Long assetNo = null;

  @JsonProperty("allotteeName")
  private String allotteeName = null;

  @JsonProperty("allotteeAddress")
  private String allotteeAddress = null;

  @JsonProperty("allotteeMobileNumber")
  private String allotteeMobileNumber = null;

  @JsonProperty("agreementPeriod")
  private Long agreementPeriod = null;

  @JsonProperty("commencementDate")
  @JsonFormat(pattern = "dd/MM/yyyy")
  private Date commencementDate = null;

  @JsonProperty("templateVersion")
  private String templateVersion = null;
  
  @JsonProperty("rentInWord")
  private String rentInWord = null;

  @JsonProperty("expiryDate")
  @JsonFormat(pattern = "dd/MM/yyyy")
  private Date expiryDate = null;

  @JsonProperty("rent")
  private Double rent = null;

  @JsonProperty("securityDeposit")
  private Double securityDeposit = null;
  
  @JsonProperty("securityDepositInWord")
  private String securityDepositInWord = null;

  @JsonProperty("commissionerName")
  private String commissionerName = null;

  @JsonProperty("locality")
  private Long locality = null;

  @JsonProperty("street")
  private Long street = null;

  @JsonProperty("zone")
  private Long zone = null;

  @JsonProperty("ward")
  private Long ward = null;

  @JsonProperty("block")
  private Long block = null;

  @JsonProperty("electionward")
  private Long electionward = null;
  
  @JsonProperty("doorNo")
  private String doorNo = null;

	@JsonProperty("status")
	private Status status;

	@JsonProperty("noticeType")
	private String noticeType;

  @NotNull
  private String fileStore;
  
  public void toNotice(Agreement agreement){
	  this.acknowledgementNumber = agreement.getAcknowledgementNumber();
	  this.agreementNumber = agreement.getAgreementNumber();
	  this.agreementPeriod = agreement.getTimePeriod();
	  this.allotteeAddress = agreement.getAllottee().getAddress();
	  this.allotteeMobileNumber = agreement.getAllottee().getMobileNumber();
	  this.allotteeName = agreement.getAllottee().getName();
	  this.assetCategory = agreement.getAsset().getCategory().getId();
	  this.assetNo = agreement.getAsset().getId();
	  this.block = agreement.getAsset().getLocationDetails().getBlock();
	  this.commencementDate = agreement.getCommencementDate();
	  //TODO this.commissionerName = agreement.get
	  
	  this.electionward = agreement.getAsset().getLocationDetails().getElectionWard();
	  this.expiryDate = agreement.getExpiryDate();
	  this.locality = agreement.getAsset().getLocationDetails().getLocality();
	  this.rent = agreement.getRent();
	  this.rentInWord = AmountInWordUtil.amountInWords(agreement.getRent());
	  this.securityDeposit = agreement.getSecurityDeposit();
	  this.securityDepositInWord = AmountInWordUtil.amountInWords(agreement.getSecurityDeposit());
	  this.street = agreement.getAsset().getLocationDetails().getStreet();
	  this.ward = agreement.getAsset().getLocationDetails().getRevenueWard();
	  this.zone = agreement.getAsset().getLocationDetails().getZone();
	  this.doorNo = agreement.getAsset().getLocationDetails().getDoorNo();
	  this.noticeType = agreement.getAction().toString();
	  this.noticeNo = agreement.getNoticeNumber();
	  this.noticeDate = agreement.getAgreementDate();
  }

}

