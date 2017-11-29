package org.egov.works.measurementbook.persistence.helper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.works.measurementbook.web.contract.*;

import java.math.BigDecimal;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MeasurementBookHelper {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("mbRefNo")
    private String mbRefNo = null;

    @JsonProperty("contractorComments")
    private String contractorComments = null;

    @JsonProperty("mbDate")
    private Long mbDate = null;

    @JsonProperty("mbIssuedDate")
    private Long mbIssuedDate = null;

    @JsonProperty("mbAbstract")
    private String mbAbstract = null;

    @JsonProperty("fromPageNo")
    private Integer fromPageNo = null;

    @JsonProperty("toPageNo")
    private Integer toPageNo = null;

    @JsonProperty("letterOfAcceptanceEstimate")
    private String letterOfAcceptanceEstimate = null;

    @JsonProperty("status")
    private String status = null;

    @JsonProperty("isLegacyMB")
    private Boolean isLegacyMB = null;

    @JsonProperty("mbAmount")
    private BigDecimal mbAmount = null;

    @JsonProperty("approvedDate")
    private Long approvedDate = null;

    @JsonProperty("stateId")
    private String stateId = null;

    @JsonProperty("cancellationReason")
    private String cancellationReason = null;

    @JsonProperty("cancellationRemarks")
    private String cancellationRemarks = null;

    public MeasurementBook toDomain() {
        MeasurementBook mb = new MeasurementBook();
        mb.setTenantId(this.tenantId);
        mb.setId(this.id);
        mb.fromPageNo(this.fromPageNo);
        mb.setToPageNo(this.toPageNo);
        mb.setMbRefNo(this.mbRefNo);
        mb.contractorComments(this.contractorComments);
        mb.setMbAbstract(this.mbAbstract);
        mb.setMbDate(this.mbDate);
        mb.setMbIssuedDate(this.mbIssuedDate);
        mb.setLetterOfAcceptanceEstimate(new LetterOfAcceptanceEstimate());
        mb.getLetterOfAcceptanceEstimate().setId(this.letterOfAcceptanceEstimate);
        mb.setApprovedDate(this.approvedDate);
        mb.stateId(this.stateId);
        mb.status(MeasurementBookStatus.valueOf(this.status));
        mb.setIsLegacyMB(this.isLegacyMB);
        mb.setMbAmount(this.mbAmount);
        mb.setCancellationRemarks(this.cancellationRemarks);
        mb.setCancellationReason(this.cancellationReason);
        return mb;

    }


}
