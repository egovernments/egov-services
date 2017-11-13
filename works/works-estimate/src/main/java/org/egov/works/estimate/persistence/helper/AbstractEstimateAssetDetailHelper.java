package org.egov.works.estimate.persistence.helper;

import org.egov.works.estimate.web.contract.AbstractEstimateAssetDetail;
import org.egov.works.estimate.web.contract.Asset;
import org.egov.works.estimate.web.contract.AssetPresentCondition;
import org.egov.works.estimate.web.contract.AuditDetails;
import org.egov.works.estimate.web.contract.LandAssetPresentCondition;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * An Object that holds the basic data of Technical Sanction for Detailed
 * Estimate
 */
@ApiModel(description = "An Object that holds the basic data of Technical Sanction for Detailed Estimate")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-10T07:36:50.343Z")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AbstractEstimateAssetDetailHelper {

	@JsonProperty("id")
	private String id = null;

	@JsonProperty("tenantId")
	private String tenantId = null;

	@JsonProperty("abstractEstimate")
	private String abstractEstimate = null;

	@JsonProperty("asset")
	private String asset = null;

	@JsonProperty("assetCondition")
	private String assetCondition = null;

	@JsonProperty("assetRemarks")
	private String assetRemarks = null;

	@JsonProperty("landAsset")
	private String landAsset = null;

	@JsonProperty("landAssetCondition")
	private String landAssetCondition = null;

	@JsonProperty("constructionArea")
	private Double constructionArea = null;

	@JsonProperty("createdBy")
	private String createdBy = null;

	@JsonProperty("lastModifiedBy")
	private String lastModifiedBy = null;

	@JsonProperty("createdTime")
	private Long createdTime = null;

	@JsonProperty("lastModifiedTime")
	private Long lastModifiedTime = null;

	public AbstractEstimateAssetDetail toDomain() {

		final AbstractEstimateAssetDetail abstractEstimateAssetDetail = new AbstractEstimateAssetDetail();
		abstractEstimateAssetDetail.setId(this.id);
		abstractEstimateAssetDetail.setTenantId(this.tenantId);
		abstractEstimateAssetDetail.setAbstractEstimate(this.abstractEstimate);
		abstractEstimateAssetDetail.setAsset(new Asset());
		abstractEstimateAssetDetail.getAsset().setCode(this.asset);
		abstractEstimateAssetDetail.setAssetCondition(new AssetPresentCondition());
		abstractEstimateAssetDetail.getAssetCondition().setName(this.assetCondition);
		abstractEstimateAssetDetail.setAssetRemarks(this.assetRemarks);
		abstractEstimateAssetDetail.setLandAsset(this.landAsset);
		abstractEstimateAssetDetail.setLandAssetCondition(new LandAssetPresentCondition());
		abstractEstimateAssetDetail.getLandAssetCondition().setName(this.landAssetCondition);
		abstractEstimateAssetDetail.setConstructionArea(this.constructionArea);
		abstractEstimateAssetDetail.setAuditDetails(new AuditDetails());
		abstractEstimateAssetDetail.getAuditDetails().setCreatedBy(this.createdBy);
		abstractEstimateAssetDetail.getAuditDetails().setCreatedTime(this.createdTime);
		abstractEstimateAssetDetail.getAuditDetails().setLastModifiedBy(this.lastModifiedBy);
		abstractEstimateAssetDetail.getAuditDetails().setLastModifiedTime(this.lastModifiedTime);
		return abstractEstimateAssetDetail;

	}
}
