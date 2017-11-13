package org.egov.works.estimate.persistence.helper;

import org.egov.works.estimate.web.contract.Asset;
import org.egov.works.estimate.web.contract.AssetsForEstimate;
import org.egov.works.estimate.web.contract.AuditDetails;
import org.egov.works.estimate.web.contract.DetailedEstimate;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * An Object holds the basic data for Assets for Estimate
 */
@ApiModel(description = "An Object holds the basic data for Assets for Estimate")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-28T12:22:31.360Z")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AssetsForEstimateHelper {
	@JsonProperty("id")
	private String id = null;

	@JsonProperty("tenantId")
	private String tenantId = null;

	@JsonProperty("asset")
	private String asset = null;

	@JsonProperty("detailedEstimate")
	private String detailedEstimate = null;

	@JsonProperty("createdBy")
	private String createdBy = null;

	@JsonProperty("lastModifiedBy")
	private String lastModifiedBy = null;

	@JsonProperty("createdTime")
	private Long createdTime = null;

	@JsonProperty("lastModifiedTime")
	private Long lastModifiedTime = null;

	public AssetsForEstimate toDomain() {

		final AssetsForEstimate assetsForEstimate = new AssetsForEstimate();
		assetsForEstimate.setAuditDetails(new AuditDetails());
		assetsForEstimate.getAuditDetails().setCreatedBy(this.createdBy);
		assetsForEstimate.getAuditDetails().setCreatedTime(this.createdTime);
		assetsForEstimate.getAuditDetails().setLastModifiedBy(this.lastModifiedBy);
		assetsForEstimate.getAuditDetails().setLastModifiedTime(this.lastModifiedTime);
		assetsForEstimate.setId(this.id);
		assetsForEstimate.setTenantId(this.tenantId);
		assetsForEstimate.setDetailedEstimate(this.detailedEstimate);

		Asset asset = new Asset();
		asset.setCode(this.asset);
		assetsForEstimate.setAsset(asset);
		return assetsForEstimate;
	}

}
