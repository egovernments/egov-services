package org.egov.lams.contract;

import java.util.Date;
import java.util.Map;

import org.egov.lams.model.Agreement;
import org.egov.lams.model.Allottee;
import org.egov.lams.model.Asset;
import org.egov.lams.model.Boundary;
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
	private String agreementId;
	private String ackNumber;
	private String cityGrade;
	private String cityName;
	private String districtName;
	private String cityCode;
	private String regionName;

	private String adminWardName;
	private String adminWard;

	private String revenueWardName;
	private String revenueWard;

	private String revZoneName;
	private String revZone;

	private String revenueBlockName;
	private String revenueBlock;

	private String locationName;
	private String location;

	private Date agreementDate;
	private String status;
	private String allottee;
	private String allotteeName;
	private String allotteeMobile;
	private String allotteeAadhaarNo;
	
	private String asset;
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
		this.ackNumber = agreement.getAcknowledgementNumber();
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
		this.renttype = agreement.getRent();
		this.tradeLicenseNumber = agreement.getTradelicenseNumber();
		this.paymentCycle = agreement.getPaymentCycle().toString();
		this.rentIncrementMethod = agreement.getRentIncrementMethod().getType();
		this.orderNumber = agreement.getOrderNumber();
		this.orderDate = agreement.getOrderDate();
		this.rrReadingNumber = agreement.getRrReadingNo();
		this.remarks = agreement.getRemarks();
		this.solvencyCertificateNo = agreement.getSolvencyCertificateNo();
		this.solvencyCertificateDate = agreement.getSolvencyCertificateDate();
		this.tinNumber = agreement.getTinNumber();
	}

	public void setAsset(Asset asset) {
		
		this.asset = asset.getId().toString();
		this.assetName = asset.getName();
		this.assetCode = asset.getCode();
		this.assetCategory = asset.getCategory().getName();
		this.assetDrno = asset.getLocationDetails().getDoorNo();

	}

	public void setBoundaryDetails(Location location, Map<Long, Boundary> boundaryMap) {

		if (location.getZone() != null) {
			this.revZone = location.getZone().toString();
			this.revZoneName = boundaryMap.get(location.getZone()).getName();
		}
		if (location.getLocality() != null) {
			this.location = location.getLocality().toString();
			this.locationName = boundaryMap.get(location.getLocality()).getLocalName();
		}
		if (location.getElectionWard() != null) {
			this.adminWard = location.getElectionWard().toString();
			this.adminWardName = boundaryMap.get(location.getElectionWard()).getName();
		}
		if (location.getBlock() != null) {
			this.revenueBlock = location.getBlock().toString();
			this.revenueBlockName = boundaryMap.get(location.getBlock()).getName();
		}
		if (location.getRevenueWard() != null) {
			this.revenueWard = location.getRevenueWard().toString();
			this.revenueWardName = boundaryMap.get(location.getRevenueWard()).getName();
		}
	}

	public void setAllottee(Allottee allottee) {
		
		this.allottee = allottee.getId().toString();
		this.allotteeName = allottee.getName();
		this.allotteeMobile = allottee.getMobileNumber();
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
