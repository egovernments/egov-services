package org.egov.works.measurementbook.persistence.helper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.works.measurementbook.web.contract.LOAMeasurementSheet;
import org.egov.works.measurementbook.web.contract.MBMeasurementSheet;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MBMeasurementSheetHelper {

    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

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

    @JsonProperty("measurementBookDetail")
    private String measurementBookDetail = null;

    @JsonProperty("loaMeasurementSheet")
    private String loaMeasurementSheet = null;

    public MBMeasurementSheet toDomain() {
        MBMeasurementSheet mbMeasurementSheet = new MBMeasurementSheet();
        mbMeasurementSheet.setTenantId(this.tenantId);
        mbMeasurementSheet.setId(this.id);
        mbMeasurementSheet.setRemarks(this.remarks);
        mbMeasurementSheet.setNumber(this.number);
        mbMeasurementSheet.setLength(this.length);
        mbMeasurementSheet.setWidth(this.width);
        mbMeasurementSheet.setDepthOrHeight(this.depthOrHeight);
        mbMeasurementSheet.setQuantity(this.quantity);
        mbMeasurementSheet.setMeasurementBookDetail(this.measurementBookDetail);
        mbMeasurementSheet.setLoaMeasurementSheet(new LOAMeasurementSheet());
        mbMeasurementSheet.getLoaMeasurementSheet().setId(this.loaMeasurementSheet);
        mbMeasurementSheet.setMultiplier(this.multiplier);
        return mbMeasurementSheet;
    }
}
