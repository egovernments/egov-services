package org.egov.works.workorder.persistence.helper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.egov.works.workorder.web.contract.AuditDetails;
import org.egov.works.workorder.web.contract.LOAMeasurementSheet;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder
public class LOAMeasurementSheetHelper {

    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("number")
    private BigDecimal number = null;

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
        loaMeasurementSheet.setNo(this.number);
        loaMeasurementSheet.setLength(this.length);
        loaMeasurementSheet.setWidth(this.width);
        loaMeasurementSheet.setDepthOrHeight(this.depthOrHeight);
        loaMeasurementSheet.setQuantity(this.quantity);
        loaMeasurementSheet.setLoaActivity(this.loaActivity);
        loaMeasurementSheet.setEstimateMeasurementSheet(this.estimateMeasurementSheet);
        return loaMeasurementSheet;

    }
}
