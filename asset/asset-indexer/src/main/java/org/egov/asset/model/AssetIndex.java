package org.egov.asset.model;

import java.math.BigDecimal;
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
	private Double assetCategoryDepreciationRate;
	private String depreciationMethod;
	private String assetCategoryVersion;

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

	private Boolean enableYearWiseDepreciation;
	private Double depreciationRate;
	private String yearWiseDepreciation;
	private BigDecimal grossValue;
	private BigDecimal accumulatedDepreciation;
	private Long assetReference;
	private String version;

	private String cityName;
	private String localName;
	private String districtCode;
	private String districtName;
	private String regionName;
	private String ulbGrade;

	public void setAssetData(final Asset asset) {
		tenantId = asset.getTenantId();
		assetId = asset.getId();
		assetName = asset.getName();
		assetCode = asset.getCode();
		grossValue = asset.getGrossValue();
		accumulatedDepreciation = asset.getAccumulatedDepreciation();
		assetReference = asset.getAssetReference();
		

		final AssetCategory assetCategory = asset.getAssetCategory();
		assetCategoryId = assetCategory.getId();
		assetCategoryName = assetCategory.getName();
		assetCategoryCode = assetCategory.getCode();
		assetCategoryparentId = assetCategory.getParent();
		assetCategoryDepreciationRate = assetCategory.getDepreciationRate();

		if (assetCategory.getDepreciationMethod() != null)
			depreciationMethod = assetCategory.getDepreciationMethod().toString();

		if (asset.getModeOfAcquisition() != null)
			modeOfAcquisition = asset.getModeOfAcquisition().toString();

		if (asset.getStatus() != null)
			status = asset.getStatus();

		assetDetails = asset.getAssetDetails();
		description = asset.getDescription();
		dateOfCreation = asset.getDateOfCreation();
		remarks = asset.getRemarks();
		length = asset.getLength();
		width = asset.getWidth();
		totalArea = asset.getTotalArea();
		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		try {
			final Asset asset2 = new Asset();
			asset2.setAssetAttributes(asset.getAssetAttributes());
			assetAttributes = objectMapper.writeValueAsString(asset2);
		} catch (final JsonProcessingException e) {
			throw new RuntimeException(e);
		}
		setAssetDepartment(asset.getDepartment());

		enableYearWiseDepreciation = asset.getEnableYearWiseDepreciation();
		depreciationRate = asset.getDepreciationRate();

		try {
			yearWiseDepreciation = objectMapper.writeValueAsString(asset.getYearWiseDepreciation());
		} catch (final JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	public void setAssetDepartment(final Department department) {
		departmentId = department.getId();
		departmentName = department.getName();
		departmentCode = department.getCode();
	}

	public void setAssetLocation(final Location location, final Map<Long, Boundary> map) {
		locality = location.getLocality();
		final Boundary locationBoundary = map.get(location.getLocality());
		if (locationBoundary != null)
			localityName = locationBoundary.getName();

		zone = location.getZone();
		final Boundary zoneBoundary = map.get(location.getZone());
		if (zoneBoundary != null)
			zoneName = map.get(location.getZone()).getName();

		revenueWard = location.getRevenueWard();
		final Boundary revenueWardBoundary = map.get(location.getRevenueWard());
		if (revenueWardBoundary != null)
			revenueWardName = map.get(location.getRevenueWard()).getName();

		block = location.getBlock();
		final Boundary blockBoundary = map.get(location.getBlock());
		if (blockBoundary != null)
			blockName = map.get(location.getBlock()).getName();

		street = location.getStreet();
		final Boundary streetBoundary = map.get(location.getStreet());
		if (streetBoundary != null)
			streetName = map.get(location.getStreet()).getName();

		electionWard = location.getElectionWard();
		final Boundary electionWardBoundary = map.get(location.getElectionWard());
		if (electionWardBoundary != null)
			electionWardName = map.get(location.getElectionWard()).getName();

		doorNo = location.getDoorNo();
		pinCode = location.getPinCode();
	}

}
