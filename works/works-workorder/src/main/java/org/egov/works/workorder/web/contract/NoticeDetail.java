package org.egov.works.workorder.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * An Object holds the basic data for a Notice Detail
 */
@ApiModel(description = "An Object holds the basic data for a Notice Detail")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-30T11:45:24.744Z")

public class NoticeDetail {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("notice")
    private String notice = null;

    @JsonProperty("remarks")
    private String remarks = null;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

    @JsonProperty("deleted")
    private Boolean deleted = false;

    public NoticeDetail id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Unique Identifier of the Notice Detail
     *
     * @return id
     **/
    @ApiModelProperty(value = "Unique Identifier of the Notice Detail")


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public NoticeDetail tenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * Tenant id of the Notice Detail
     *
     * @return tenantId
     **/
    @ApiModelProperty(required = true, value = "Tenant id of the Notice Detail")
    @NotNull

    @Size(min = 2, max = 128)
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public NoticeDetail notice(String notice) {
        this.notice = notice;
        return this;
    }

    /**
     * Reference of Notice
     *
     * @return notice
     **/
    @ApiModelProperty(required = true, value = "Reference of Notice")
//    @NotNull


    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public NoticeDetail remarks(String remarks) {
        this.remarks = remarks;
        return this;
    }

    /**
     * Remarks for the Notice.
     *
     * @return remarks
     **/
    @ApiModelProperty(value = "Remarks for the Notice.")

    @Pattern(regexp = "[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]+")
    @Size(min = 1, max = 1024)
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public NoticeDetail auditDetails(AuditDetails auditDetails) {
        this.auditDetails = auditDetails;
        return this;
    }

    /**
     * Get auditDetails
     *
     * @return auditDetails
     **/
    @ApiModelProperty(value = "")

    @Valid

    public AuditDetails getAuditDetails() {
        return auditDetails;
    }

    public void setAuditDetails(AuditDetails auditDetails) {
        this.auditDetails = auditDetails;
    }

    public NoticeDetail deleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    /**
     * Boolean value to identify whether the object is deleted or not from UI.
     *
     * @return deleted
     **/
    @ApiModelProperty(value = "Boolean value to identify whether the object is deleted or not from UI.")


    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NoticeDetail noticeDetail = (NoticeDetail) o;
        return Objects.equals(this.id, noticeDetail.id) &&
                Objects.equals(this.tenantId, noticeDetail.tenantId) &&
                Objects.equals(this.notice, noticeDetail.notice) &&
                Objects.equals(this.remarks, noticeDetail.remarks) &&
                Objects.equals(this.auditDetails, noticeDetail.auditDetails) &&
                Objects.equals(this.deleted, noticeDetail.deleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantId, notice, remarks, auditDetails, deleted);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class NoticeDetail {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
        sb.append("    notice: ").append(toIndentedString(notice)).append("\n");
        sb.append("    remarks: ").append(toIndentedString(remarks)).append("\n");
        sb.append("    auditDetails: ").append(toIndentedString(auditDetails)).append("\n");
        sb.append("    deleted: ").append(toIndentedString(deleted)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}

