package org.egov.works.services.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OfflineStatusHelper {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("objectType")
    private String objectType = null;

    @JsonProperty("objectNumber")
    private String objectNumber = null;

    @JsonProperty("objectDate")
    private Long objectDate = null;

    @JsonProperty("status")
    private String status = null;

    @JsonProperty("statusDate")
    private Long statusDate = null;

    @JsonProperty("remarks")
    private String remarks = null;

    @JsonProperty("referenceNumber")
    private String referenceNumber = null;

    @JsonProperty("deleted")
    private Boolean deleted = false;

    public OfflineStatus toDomain() {
        OfflineStatus offlineStatus = new OfflineStatus();
        offlineStatus.setId(this.id);
        offlineStatus.setObjectNumber(this.objectNumber);
        offlineStatus.setObjectType(this.objectType);
        offlineStatus.setObjectDate(this.objectDate);
        offlineStatus.setStatusDate(this.statusDate);
        WorksStatus status = new WorksStatus();
        status.setCode(this.status);
        offlineStatus.setStatus(status);
        offlineStatus.setReferenceNumber(this.referenceNumber);
        offlineStatus.setRemarks(this.remarks);
        return offlineStatus;
    }

}
