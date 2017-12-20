package org.egov.works.qualitycontrol.persistence.helper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.works.qualitycontrol.web.contract.QualityTestingDetail;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QualityTestingDetailHelper {

    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("qualityTesting")
    private String qualityTesting = null;

    @JsonProperty("materialName")
    private String materialName = null;

    @JsonProperty("testName")
    private String testName = null;

    @JsonProperty("resultUnit")
    private String resultUnit = null;

    @JsonProperty("minimumValue")
    private BigDecimal minimumValue = null;

    @JsonProperty("maximumValue")
    private BigDecimal maximumValue = null;

    @JsonProperty("hodRemarks")
    private String hodRemarks = null;

    @JsonProperty("coRemarks")
    private String coRemarks = null;

    public QualityTestingDetail toDomain() {
        QualityTestingDetail qualityTestingDetail = new QualityTestingDetail();
        qualityTestingDetail.setId(this.id);
        return qualityTestingDetail;
    }
}
