package org.egov.lams.contract;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.egov.lams.model.Agreement;
import org.egov.lams.model.Allottee;
import org.egov.lams.model.Asset;
import org.egov.lams.model.Boundary;
import org.egov.lams.model.City;
import org.egov.lams.model.DemandDetails;
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
public class AgreementDetailsEs {

	private String agreementNumber;
	private String tenantId;
	private String agreementId;
	private String ackNumber;
    private String action;

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
	private String tenderNumber;
	private Date tenderDate;
	private String councilNumber;
	private Date councilDate;
	Double bankGuaranteeAmount;
	private Date bankGuaranteeDate;
	Double securityDeposit;
	private Date securityDepositDate;
	private String natureOfAllotment;
	Double registrationFee;
	private String caseNumber;
	private Date commencementDate;
	private Date expiryDate;
	private String orderDetails;
	Double rent;
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
	private String createdBy;
	private Date createdDate;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String workFlowState;
	private BigDecimal arrearAmount;
	private BigDecimal arrearCollection;
	private BigDecimal currentAmount;
	private BigDecimal currentCollection;

	private CityEs city;
	private AssetEs asset;
	private AllotteeEs allottee;
	private List<DemandDetailsEs> demandDetails;

	public void setAgreement(Agreement agreement) {

		this.agreementNumber = agreement.getAgreementNumber();
		this.agreementId = agreement.getId().toString();
		this.tenantId = agreement.getTenantId();
		this.ackNumber = agreement.getAcknowledgementNumber();
		this.agreementDate = agreement.getAgreementDate();
	    this.action = agreement.getAction().toString();
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
		this.registrationFee = agreement.getRegistrationFee();
		this.caseNumber = agreement.getCaseNo();
		this.commencementDate = agreement.getCommencementDate();
		this.expiryDate = agreement.getExpiryDate();
		this.orderDetails = agreement.getOrderDetails();
		this.rent = agreement.getRent();
		this.tradeLicenseNumber = agreement.getTradelicenseNumber();
		this.paymentCycle = agreement.getPaymentCycle().toString();
		if(agreement.getRentIncrementMethod() != null)
		this.rentIncrementMethod = agreement.getRentIncrementMethod().getType();
		this.orderNumber = agreement.getOrderNumber();
		this.orderDate = agreement.getOrderDate();
		this.rrReadingNumber = agreement.getRrReadingNo();
		this.remarks = agreement.getRemarks();
		this.solvencyCertificateNo = agreement.getSolvencyCertificateNo();
		this.solvencyCertificateDate = agreement.getSolvencyCertificateDate();
		this.tinNumber = agreement.getTinNumber();
		this.createdBy = agreement.getCreatedBy();
		this.createdDate = agreement.getCreatedDate();
		this.lastModifiedBy = agreement.getLastmodifiedBy();
		this.lastModifiedDate = agreement.getLastmodifiedDate();
	}

	public void setAsset(Asset asset) {
		if(null != asset){
			this.asset = AssetEs.builder()
					.asset(asset.getId().toString())
					.assetCategory(asset.getCategory().getName())
					.assetName(asset.getName())
					.assetCode(asset.getCode())
					.assetDrno(asset.getLocationDetails().getDoorNo())
					.build();
		}
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
		if(null != allottee){
			this.allottee = AllotteeEs.builder()
					.allottee(allottee.getId().toString())
					.allotteeName(allottee.getName())
					.allotteeMobile(allottee.getMobileNumber())
					.allotteeAadhaarNo(allottee.getAadhaarNumber())
					.build();
		}
	}

	public void setCity(City city) {
		if(null != city){
			this.city = CityEs.builder()
					.cityGrade(city.getGrade())
					.cityName(city.getName())
					.cityCode(city.getCode())
					.districtName(city.getDistrictName())
					.regionName(city.getRegionName())
					.build();
		}
	}
	
	public void setRent(List<DemandDetails> demandDetails, Installment installment,String taxReason) {
		BigDecimal arrearAmount = BigDecimal.ZERO;
		BigDecimal arrearCollection = BigDecimal.ZERO;
		BigDecimal currentAmount = BigDecimal.ZERO;
		BigDecimal currentCollection = BigDecimal.ZERO;
		Date date = new Date();
		for (DemandDetails demandDetail : demandDetails) {
			if (demandDetail.getTaxReason().equalsIgnoreCase(taxReason)) {
				if (demandDetail.getTaxPeriod().equals(installment.getDescription())) {
					currentAmount = currentAmount.add(demandDetail.getTaxAmount());
					currentCollection = currentCollection.add(demandDetail.getCollectionAmount());
				} else if (date.after(demandDetail.getPeriodEndDate())) {
					arrearAmount = arrearAmount.add(demandDetail.getTaxAmount());
					arrearCollection = arrearCollection.add(demandDetail.getCollectionAmount());
				}
			}
		}
		this.arrearAmount=arrearAmount;
		this.arrearCollection=arrearCollection;
		this.currentAmount=currentAmount;
		this.currentCollection=currentCollection;

	}

}