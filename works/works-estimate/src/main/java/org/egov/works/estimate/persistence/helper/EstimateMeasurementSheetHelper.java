package org.egov.works.estimate.persistence.helper;

import java.math.BigDecimal;

import org.egov.works.estimate.web.contract.AuditDetails;
import org.egov.works.estimate.web.contract.EstimateMeasurementSheet;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * An Object holds the basic data of Estimate Measurement Sheet
 */
@ApiModel(description = "An Object holds the basic data of Estimate Measurement Sheet")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-30T10:26:20.111Z")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EstimateMeasurementSheetHelper {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("slNo")
    private Integer slNo = null;

    @JsonProperty("identifier")
    private String identifier = null;

    @JsonProperty("remarks")
    private String remarks = null;

    @JsonProperty("number")
    private BigDecimal number = null;

    @JsonProperty("multiplier")
    private BigDecimal multiplier = null;

    @JsonProperty("length")
    private BigDecimal length = null;

    @JsonProperty("width")
    private BigDecimal width = null;

    @JsonProperty("depthOrHeight")
    private BigDecimal depthOrHeight = null;

    @JsonProperty("quantity")
    private BigDecimal quantity = null;

    @JsonProperty("estimateActivity")
    private String estimateMeasurementSheet = null;

    @JsonProperty("parent")
    private String parent = null;

    @JsonProperty("createdBy")
    private String createdBy = null;

    @JsonProperty("lastModifiedBy")
    private String lastModifiedBy = null;

    @JsonProperty("createdTime")
    private Long createdTime = null;

    @JsonProperty("lastModifiedTime")
    private Long lastModifiedTime = null;

    public EstimateMeasurementSheet toDomain() {

		final EstimateMeasurementSheet estimateMeasurementSheet = new EstimateMeasurementSheet();
		estimateMeasurementSheet.setAuditDetails(new AuditDetails());
		estimateMeasurementSheet.getAuditDetails().setCreatedBy(this.createdBy);
		estimateMeasurementSheet.getAuditDetails().setCreatedTime(this.createdTime);
		estimateMeasurementSheet.getAuditDetails().setLastModifiedBy(this.lastModifiedBy);
		estimateMeasurementSheet.getAuditDetails().setLastModifiedTime(this.lastModifiedTime);
		estimateMeasurementSheet.setId(this.id);
		estimateMeasurementSheet.setTenantId(this.tenantId);
		estimateMeasurementSheet.setDepthOrHeight(this.depthOrHeight);
		estimateMeasurementSheet.setIdentifier(this.identifier);
		estimateMeasurementSheet.setLength(this.length);
		estimateMeasurementSheet.setNumber(this.number);
		estimateMeasurementSheet.setParent(this.parent);
		estimateMeasurementSheet.setQuantity(this.quantity);
		estimateMeasurementSheet.setRemarks(this.remarks);
		estimateMeasurementSheet.setSlNo(this.slNo);
		estimateMeasurementSheet.setWidth(this.width);
		estimateMeasurementSheet.setMultiplier(this.multiplier);
		return estimateMeasurementSheet;
	}

}
