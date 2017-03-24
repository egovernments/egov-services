package org.egov.asset.contract;

import java.util.Date;

import org.egov.asset.model.Asset;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class AssetDetails {
	private String assetName;
	private String assetCode;
	private String assetDetails;
	private String modeOfAcquisition;
	private String status;
	private String description;
	private Date dateOfCreation;
	private String remarks;
	private String length;
	private String width;
	private String totalArea;
	private String properties;
	private String departmentName;
	private String departmentCode;
	private String zone;
	private String revenueWard;
	private String locality;
	private String block;
	private String street;
	private String electionWard;
	private String doorNo;
	private String pinCode;
	private String assetCategoryName;
	private String assetCategoryCode;
	private String assetCategoryType;
	private String parent;
	private String depreciationMethod;
	private String assetAccount;
	private String accumulatedDepreciationAccount;
	private String revaluationReserveAccount;
	private String depreciationExpenseAccount;
	private String unitOfMeasurement;
	private String depreciationRate;
	private String customFields;
	
	public void setAsset(Asset asset) {
		if (asset == null)
			return;
		this.assetName = asset.getName();
		this.assetCode = asset.getCode();
		this.assetDetails = asset.getAssetDetails();
		this.modeOfAcquisition = asset.getModeOfAcquisition().toString();
		this.status = asset.getStatus().toString();
		this.description = asset.getDescription();
		this.dateOfCreation = asset.getDateOfCreation();
		this.remarks = asset.getRemarks();
		this.length = asset.getLength();
		this.width = asset.getWidth();
		this.totalArea = asset.getTotalArea();
		this.properties = getJsonString(asset.getProperties());
		this.departmentName = asset.getDepartment().getName();
		this.departmentCode = asset.getDepartment().getCode();
		// FIXME zone etc needs to come from boundary service
		this.zone = asset.getLocationDetails().getZone().toString();
		this.revenueWard = asset.getLocationDetails().getRevenueWard().toString();
		this.locality = asset.getLocationDetails().getLocality().toString();
		this.block = asset.getLocationDetails().getBlock().toString();
		this.pinCode = asset.getLocationDetails().getPinCode().toString();
		this.street = asset.getLocationDetails().getStreet().toString();
		this.electionWard = asset.getLocationDetails().getElectionWard().toString();
		this.doorNo = asset.getLocationDetails().getDoorNo();

	}

	private String getJsonString(Object obj) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void setAssetCategory(AssetCategory assetCategory) {

		if (assetCategory == null)
			return;

		this.assetCategoryName = (assetCategory.getName() == null ? "" : assetCategory.getName());
		this.assetCategoryCode = (assetCategory.getId() == null ? "" : assetCategory.getId().toString());
		this.assetCategoryType = (assetCategory.getAssetCategoryType() == null ? ""
				: assetCategory.getAssetCategoryType().toString());
		this.parent = (assetCategory.getParent() == null ? "" : assetCategory.getParent().toString());
		this.depreciationMethod = (assetCategory.getDepreciationMethod() == null ? ""
				: assetCategory.getDepreciationMethod().toString());
		this.assetAccount = (assetCategory.getAssetAccount() == null ? "" : assetCategory.getAssetAccount().toString());
		this.unitOfMeasurement = (assetCategory.getUnitOfMeasurement() == null ? ""
				: assetCategory.getUnitOfMeasurement().toString());
		this.depreciationRate = (assetCategory.getDepreciationRate() == null ? ""
				: assetCategory.getDepreciationRate().toString());

		this.customFields = (assetCategory.getCustomFields() == null ? "" : assetCategory.getCustomFields().toString());
		this.accumulatedDepreciationAccount = (assetCategory.getAccumulatedDepreciationAccount() == null ? ""
				: assetCategory.getAccumulatedDepreciationAccount().toString());
		this.revaluationReserveAccount = (assetCategory.getRevaluationReserveAccount() == null ? ""
				: assetCategory.getRevaluationReserveAccount().toString());
		this.depreciationExpenseAccount = (assetCategory.getDepreciationExpenseAccount() == null ? ""
				: assetCategory.getDepreciationExpenseAccount().toString());

	}

}