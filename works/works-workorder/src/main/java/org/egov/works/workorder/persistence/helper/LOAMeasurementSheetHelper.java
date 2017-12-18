package org.egov.works.workorder.persistence.helper;

import java.math.BigDecimal;

import org.egov.works.workorder.web.contract.LOAMeasurementSheet;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LOAMeasurementSheetHelper {

    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

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

    @JsonProperty("loaActivity")
    private String loaActivity = null;

    @JsonProperty("estimateMeasurementSheet")
    private String estimateMeasurementSheet = null;

    public LOAMeasurementSheet toDomain() {
        LOAMeasurementSheet loaMeasurementSheet = new LOAMeasurementSheet();
        loaMeasurementSheet.setId(this.id);
        loaMeasurementSheet.setTenantId(this.tenantId);
        loaMeasurementSheet.setNumber(this.number);
        loaMeasurementSheet.setLength(this.length);
        loaMeasurementSheet.setWidth(this.width);
        loaMeasurementSheet.setDepthOrHeight(this.depthOrHeight);
        loaMeasurementSheet.setQuantity(this.quantity);
        loaMeasurementSheet.setLoaActivity(this.loaActivity);
        loaMeasurementSheet.setEstimateMeasurementSheet(this.estimateMeasurementSheet);
        loaMeasurementSheet.setMultiplier(this.multiplier);
        return loaMeasurementSheet;

    }
}
