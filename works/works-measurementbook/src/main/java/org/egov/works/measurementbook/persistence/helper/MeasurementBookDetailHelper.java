package org.egov.works.measurementbook.persistence.helper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.works.measurementbook.web.contract.AuditDetails;
import org.egov.works.measurementbook.web.contract.LOAActivity;
import org.egov.works.measurementbook.web.contract.MBMeasurementSheet;
import org.egov.works.measurementbook.web.contract.MeasurementBookDetail;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MeasurementBookDetailHelper {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("measurementBook")
    private String measurementBook = null;

    @JsonProperty("loaActivity")
    private String loaActivity = null;

    @JsonProperty("quantity")
    private Double quantity = null;

    @JsonProperty("rate")
    private Double rate = null;

    @JsonProperty("remarks")
    private String remarks = null;

    @JsonProperty("amount")
    private BigDecimal amount = null;

    @JsonProperty("measurementSheets")
    private List<MBMeasurementSheet> measurementSheets = null;

    @JsonProperty("partRate")
    private BigDecimal partRate = null;

    @JsonProperty("reducedRate")
    private BigDecimal reducedRate = null;

    public MeasurementBookDetail toDomain() {
        MeasurementBookDetail measurementBookDetail = new MeasurementBookDetail();
        measurementBookDetail.setId(this.id);
        measurementBookDetail.setTenantId(this.tenantId);
        measurementBookDetail.setMeasurementBook(this.measurementBook);
        measurementBookDetail.setLoaActivity(new LOAActivity());
        measurementBookDetail.getLoaActivity().setId(this.loaActivity);
        measurementBookDetail.setQuantity(this.quantity);
        measurementBookDetail.setRate(this.rate);
        measurementBookDetail.setRemarks(this.remarks);
        measurementBookDetail.setAmount(this.amount);
        measurementBookDetail.setPartRate(this.partRate);
        measurementBookDetail.setReducedRate(this.reducedRate);
        return measurementBookDetail;
    }

}
