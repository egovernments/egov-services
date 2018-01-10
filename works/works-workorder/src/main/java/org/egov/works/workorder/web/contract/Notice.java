package org.egov.works.workorder.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * An Object that holds the basic data of Notice
 */
@ApiModel(description = "An Object that holds the basic data of Notice")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-30T11:45:24.744Z")

public class Notice {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;
    
    @JsonProperty("noticeNumber")
    private String noticeNumber = null;

    @JsonProperty("letterOfAcceptance")
    private LetterOfAcceptance letterOfAcceptance = null;

    @JsonProperty("noticeDetails")
    private List<NoticeDetail> noticeDetails = new ArrayList<NoticeDetail>();

    @JsonProperty("closingLine")
    private String closingLine = null;

    @JsonProperty("daysOfReply")
    private Integer daysOfReply = null;
    
    @JsonProperty("status")
    private NoticeStatus status;

    @JsonProperty("documentDetails")
    private List<DocumentDetail> documentDetails = null;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;
    
    @JsonProperty("workFlowDetails")
    private WorkFlowDetails workFlowDetails = null;
    
    @JsonProperty("deleted")
    private Boolean deleted = false;

    public Notice id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Unique Identifier of the Notice
     *
     * @return id
     **/
    @ApiModelProperty(value = "Unique Identifier of the Notice")


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Notice tenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * Tenant id of the Notice
     *
     * @return tenantId
     **/
    @ApiModelProperty(required = true, value = "Tenant id of the Notice")
    @NotNull

    @Size(min = 2, max = 128)
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getNoticeNumber() {
		return noticeNumber;
	}

	public void setNoticeNumber(String noticeNumber) {
		this.noticeNumber = noticeNumber;
	}

	public Notice letterOfAcceptance(LetterOfAcceptance letterOfAcceptance) {
        this.letterOfAcceptance = letterOfAcceptance;
        return this;
    }

    /**
     * Reference of LOA
     *
     * @return letterOfAcceptance
     **/
    @ApiModelProperty(required = true, value = "Reference of LOA")
    @NotNull

//    @Valid

    public LetterOfAcceptance getLetterOfAcceptance() {
        return letterOfAcceptance;
    }

    public void setLetterOfAcceptance(LetterOfAcceptance letterOfAcceptance) {
        this.letterOfAcceptance = letterOfAcceptance;
    }

    public Notice noticeDetails(List<NoticeDetail> noticeDetails) {
        this.noticeDetails = noticeDetails;
        return this;
    }

    public Notice addNoticeDetailsItem(NoticeDetail noticeDetailsItem) {
        this.noticeDetails.add(noticeDetailsItem);
        return this;
    }

    /**
     * Array of Notice Detail
     *
     * @return noticeDetails
     **/
    @ApiModelProperty(required = true, value = "Array of Notice Detail")
    @NotNull

    @Valid
    @Size(min = 1)
    public List<NoticeDetail> getNoticeDetails() {
        return noticeDetails;
    }

    public void setNoticeDetails(List<NoticeDetail> noticeDetails) {
        this.noticeDetails = noticeDetails;
    }

    public Notice closingLine(String closingLine) {
        this.closingLine = closingLine;
        return this;
    }

    /**
     * Closing Line for the work
     *
     * @return closingLine
     **/
    @ApiModelProperty(value = "Closing Line for the work")

    @Pattern(regexp = "[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]+")
    public String getClosingLine() {
        return closingLine;
    }

    public void setClosingLine(String closingLine) {
        this.closingLine = closingLine;
    }

    public Notice daysOfReply(Integer daysOfReply) {
        this.daysOfReply = daysOfReply;
        return this;
    }

    /**
     * The number of Days for reply to Notice
     *
     * @return daysOfReply
     **/
    @ApiModelProperty(value = "The number of Days for reply to Notice")


    public Integer getDaysOfReply() {
        return daysOfReply;
    }

    public void setDaysOfReply(Integer daysOfReply) {
        this.daysOfReply = daysOfReply;
    }

    public NoticeStatus getStatus() {
		return status;
	}

	public void setStatus(NoticeStatus status) {
		this.status = status;
	}

	public Notice documentDetails(List<DocumentDetail> documentDetails) {
        this.documentDetails = documentDetails;
        return this;
    }

    public Notice addDocumentDetailsItem(DocumentDetail documentDetailsItem) {
        if (this.documentDetails == null) {
            this.documentDetails = new ArrayList<DocumentDetail>();
        }
        this.documentDetails.add(documentDetailsItem);
        return this;
    }

    /**
     * Array of document details
     *
     * @return documentDetails
     **/
    @ApiModelProperty(value = "Array of document details")

    @Valid

    public List<DocumentDetail> getDocumentDetails() {
        return documentDetails;
    }

    public void setDocumentDetails(List<DocumentDetail> documentDetails) {
        this.documentDetails = documentDetails;
    }

    public Notice auditDetails(AuditDetails auditDetails) {
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


    public WorkFlowDetails getWorkFlowDetails() {
		return workFlowDetails;
	}

	public void setWorkFlowDetails(WorkFlowDetails workFlowDetails) {
		this.workFlowDetails = workFlowDetails;
	}

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
        Notice notice = (Notice) o;
        return Objects.equals(this.id, notice.id) &&
                Objects.equals(this.tenantId, notice.tenantId) &&
                Objects.equals(this.letterOfAcceptance, notice.letterOfAcceptance) &&
                Objects.equals(this.noticeDetails, notice.noticeDetails) &&
                Objects.equals(this.closingLine, notice.closingLine) &&
                Objects.equals(this.daysOfReply, notice.daysOfReply) &&
                Objects.equals(this.documentDetails, notice.documentDetails) &&
                Objects.equals(this.auditDetails, notice.auditDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantId, letterOfAcceptance, noticeDetails, closingLine, daysOfReply, documentDetails, auditDetails);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Notice {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
        sb.append("    letterOfAcceptance: ").append(toIndentedString(letterOfAcceptance)).append("\n");
        sb.append("    noticeDetails: ").append(toIndentedString(noticeDetails)).append("\n");
        sb.append("    closingLine: ").append(toIndentedString(closingLine)).append("\n");
        sb.append("    daysOfReply: ").append(toIndentedString(daysOfReply)).append("\n");
        sb.append("    documentDetails: ").append(toIndentedString(documentDetails)).append("\n");
        sb.append("    auditDetails: ").append(toIndentedString(auditDetails)).append("\n");
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

