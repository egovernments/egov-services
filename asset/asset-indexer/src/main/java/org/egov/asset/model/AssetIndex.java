package org.egov.asset.model;

import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
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
	private String assetAttributes;

	public void setAssetData(Asset asset) {
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
		String property = null;
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.setSerializationInclusion(Include.NON_NULL);
			Asset asset2 = new Asset();
			asset2.setAssetAttributes(asset.getAssetAttributes());
			property = objectMapper.writeValueAsString(asset2);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
		this.assetAttributes = property;
		setAssetDepartment(asset.getDepartment());
	}

	public void setAssetDepartment(Department department) {

		this.departmentId = department.getId();
		this.departmentName = department.getName();
		this.departmentCode = department.getCode();

	}

	public void setAssetLocation(Location location, Map<Long, Boundary> map) {

		this.locality = location.getLocality();
		Boundary locationBoundary = map.get(location.getLocality());
		if (locationBoundary != null)
			this.localityName = locationBoundary.getName();

		this.zone = location.getZone();
		Boundary zoneBoundary = map.get(location.getZone());
		if (zoneBoundary != null)
			this.zoneName = map.get(location.getZone()).getName();

		this.revenueWard = location.getRevenueWard();
		Boundary revenueWardBoundary = map.get(location.getRevenueWard());
		if (revenueWardBoundary != null)
			this.revenueWardName = map.get(location.getRevenueWard()).getName();

		this.block = location.getBlock();
		Boundary blockBoundary = map.get(location.getBlock());
		if (blockBoundary != null)
			this.blockName = map.get(location.getBlock()).getName();

		this.street = location.getStreet();
		Boundary streetBoundary = map.get(location.getStreet());
		if (streetBoundary != null)
			this.streetName = map.get(location.getStreet()).getName();

		this.electionWard = location.getElectionWard();
		Boundary electionWardBoundary = map.get(location.getElectionWard());
		if (electionWardBoundary != null)
			this.electionWardName = map.get(location.getElectionWard()).getName();

		this.doorNo = location.getDoorNo();
		this.pinCode = location.getPinCode();

	}

}
