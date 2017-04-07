package org.egov.lams.contract;

import java.util.Date;

import org.egov.lams.model.Agreement;
import org.egov.lams.model.Allottee;
import org.egov.lams.model.Asset;
import org.egov.lams.model.City;
import org.egov.lams.model.Location;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AgreementDetails {

	private String agreementNumber;
	private String ackNumber;
	private String cityGrade;
	private String cityName;
	private String districtName;
	private String cityCode;
	private String regionName;
	private String adminWardName;
	private String revenueWard;
	private String revZoneName;
	private String revenueBlock;
	private String locationName;
	private Date agreementDate;
	private String status;
	private String allotteeName;
	private String allotteeMobile;
	private String allotteeAadhaarNo;
	private String assetCategory;
	private String assetCode;
	private String assetName;
	private String assetDrno;
	private String tenderNumber;
	private Date tenderDate;
	private String councilNumber;
	private Date councilDate;
	Double bankGuaranteeAmount;
	private Date bankGuaranteeDate;
	Double securityDeposit;
	private Date securityDepositDate;
	private String natureOfAllotment;
	Double registrationFeetype;
	private String caseNumber;
	private Date commencementDate;
	private Date expiryDate;
	private String orderDetails;
	Double renttype;
	private String tradeLicenseNumber;
	private String paymentCycle;
	private String rentIncrementMethod;
	private String orderNumber;
	private Date orderDate;
	private String rrReadingNumber;
	private String remarks;
	private String solvencyCertificateNo;
	private Date solvencyCertificateDate;
	private String tinNumber;
	private String workFlowState;

	public void setAgreement(Agreement agreement) {

		this.agreementNumber = agreement.getAgreementNumber();
		this.ackNumber= agreement.getAcknowledgementNumber();
		this.agreementDate = agreement.getAgreementDate();
		this.tenderNumber = agreement.getTenderNumber();
		this.tenderDate = agreement.getTenderDate();
		this.councilNumber = agreement.getCouncilNumber();
		this.councilDate = agreement.getCouncilDate();
		this.bankGuaranteeAmount = agreement.getBankGuaranteeAmount();
		this.bankGuaranteeDate = agreement.getBankGuaranteeDate();
		this.securityDeposit = agreement.getSecurityDeposit();
		this.securityDepositDate = agreement.getSecurityDepositDate();
		this.status = agreement.getStatus().toString();
		this.natureOfAllotment = agreement.getNatureOfAllotment().toString();
		this.registrationFeetype = agreement.getRegistrationFee();
		this.caseNumber = agreement.getCaseNo();
		this.commencementDate = agreement.getCommencementDate();
		this.expiryDate = agreement.getExpiryDate();
		this.orderDetails = agreement.getOrderDetails();
		// FIX ME renttype name
		this.renttype = agreement.getRent();
		this.tradeLicenseNumber = agreement.getTradelicenseNumber();
		this.paymentCycle = agreement.getPaymentCycle().toString();
		// FIX ME change rentincrement type to int
		this.rentIncrementMethod = agreement.getRentIncrementMethod().getType();
		this.orderNumber = agreement.getOrderNo();
		this.orderDate = agreement.getOrderDate();
		this.rrReadingNumber = agreement.getRrReadingNo();
		this.remarks = agreement.getRemarks();
		this.solvencyCertificateNo = agreement.getSolvencyCertificateNo();
		this.solvencyCertificateDate = agreement.getSolvencyCertificateDate();
		this.tinNumber = agreement.getTinNumber();
	}

	public void setAsset(Asset asset) {

		// FIXME asset
		//TODO change all ward and zone number to name using boundary calls
		Location location = asset.getLocationDetails();
		//this.revZoneName = location.getZone().toString();
		//this.adminWardName=location.getElectionWard().toString();
		this.assetName = asset.getName();
		this.assetCode = asset.getCode();
		//this.revenueBlock = location.getBlock().toString();
		//this.assetDrno = asset.getDoorNo();
		//this.revenueWard = location.getElectionWard().toString();
		this.assetCategory = asset.getCategory().getName();

	}

	public void setAllottee(Allottee allottee) {

		// allottee
		this.allotteeName = allottee.getName();
		// FIX ME check data type
		this.allotteeMobile = allottee.getMobileNumber().toString();
		this.allotteeAadhaarNo = allottee.getAadhaarNumber();
	}

	public void setCity(City city) {

		this.cityGrade = city.getGrade();
		this.cityName = city.getName();
		this.districtName = city.getDistrictName();
		this.cityCode = city.getCode();
		this.regionName = city.getRegionName();

	}

}
