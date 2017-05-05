package org.egov.asset.model;

import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class AssetIndex {

	private String tenantId;
	private Long assetId;
	private String assetName;
	private String assetCode;

	private Long departmentId;
	private String departmentName;
	private String departmentCode;

	private Long assetCategoryId;
	private String assetCategoryName;
	private String assetCategoryCode;
	private String assetCategoryType;
	private Long assetCategoryparentId;
	private String assetCategoryparentName;
	private String depreciationMethod;

	private String assetDetails;
	private String modeOfAcquisition;
	private String status;
	private String description;
	private Date dateOfCreation;

	private Long locality;
	private String localityName;
	private Long zone;
	private String zoneName;
	private Long revenueWard;
	private String revenueWardName;
	private Long block;
	private String blockName;
	private Long street;
	private String streetName;
	private Long electionWard;
	private String electionWardName;
	private String doorNo;
	private Long pinCode;

	private String remarks;
	private String length;
	private String width;
	private String totalArea;
	private String properties;

	public void setAssetData(Asset asset, AssetIndex assetIndex) {
		this.tenantId = asset.getTenantId();
		this.assetId = asset.getId();
		this.assetName = asset.getName();
		this.assetCode = asset.getCode();

		AssetCategory assetCategory = asset.getAssetCategory();
		this.assetCategoryId = assetCategory.getId();
		this.assetCategoryName = assetCategory.getName();
		this.assetCategoryCode = assetCategory.getCode();
		this.assetCategoryparentId = assetCategory.getParent();

		// Todo parent name
		if (assetCategory.getDepreciationMethod() != null)
			this.depreciationMethod = assetCategory.getDepreciationMethod().toString();

		if (asset.getModeOfAcquisition() != null)
			this.modeOfAcquisition = asset.getModeOfAcquisition().toString();

		if (asset.getStatus() != null)
			this.status = asset.getStatus().toString();

		this.assetDetails = asset.getAssetDetails();
		this.description = asset.getDescription();
		this.dateOfCreation = asset.getDateOfCreation();
		this.remarks = asset.getRemarks();
		this.length = asset.getLength();
		this.width = asset.getWidth();
		this.totalArea = asset.getTotalArea();

	//FIXME property is removed
		/*	String property = null;
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			property = objectMapper.writeValueAsString(asset.getProperties());
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.properties = property;*/
	}

	public void setAssetDepartment(Department department) {

		this.departmentId = department.getId();
		this.departmentName = department.getName();
		this.departmentCode = department.getCode();

	}

	public void setAssetLocation(Location location, Map<Long, Boundary> map) {

		this.locality = location.getLocality();
		this.localityName = map.get(location.getLocality()).getName();
		this.zone = location.getZone();
		this.zoneName = map.get(location.getZone()).getName();
		this.revenueWard = location.getRevenueWard();
		this.revenueWardName = map.get(location.getRevenueWard()).getName();
		this.block = location.getBlock();
		this.blockName = map.get(location.getBlock()).getName();
		this.street = location.getStreet();
		this.streetName = map.get(location.getStreet()).getName();
		this.electionWard = location.getElectionWard();
		this.electionWardName = map.get(location.getElectionWard()).getName();
		this.doorNo = location.getDoorNo();
		this.pinCode = location.getPinCode();

	}

}
