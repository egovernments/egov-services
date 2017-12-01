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
 * An Object that hold Document Details (Uploaded files/documents) for all the objects
 */
@ApiModel(description = "An Object that hold Document Details (Uploaded files/documents) for all the objects")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-30T11:45:24.744Z")

public class DocumentDetail {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("objectId")
    private String objectId = null;

    @JsonProperty("objectType")
    private String objectType = null;

    @JsonProperty("fileStore")
    private String fileStore = null;

    @JsonProperty("documentType")
    private DocumentType documentType = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("latitude")
    private Integer latitude = null;

    @JsonProperty("longitude")
    private Integer longitude = null;

    @JsonProperty("description")
    private String description = null;

    @JsonProperty("dateOfCapture")
    private Long dateOfCapture = null;

    @JsonProperty("workProgress")
    private WorkProgress workProgress = null;

    @JsonProperty("deleted")
    private Boolean deleted = false;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

    public DocumentDetail id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Unique Identifier of the Document Detail
     *
     * @return id
     **/
    @ApiModelProperty(value = "Unique Identifier of the Document Detail")


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DocumentDetail objectId(String objectId) {
        this.objectId = objectId;
        return this;
    }

    /**
     * Object id of the Document for which documents are attached.
     *
     * @return objectId
     **/
    @ApiModelProperty(required = true, value = "Object id of the Document for which documents are attached.")
    @NotNull

    @Pattern(regexp = "[a-zA-Z0-9-\\\\]+")
    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public DocumentDetail objectType(String objectType) {
        this.objectType = objectType;
        return this;
    }

    /**
     * Object type of the document for which documents are attached.
     *
     * @return objectType
     **/
    @ApiModelProperty(required = true, value = "Object type of the document for which documents are attached.")
    @NotNull

    @Pattern(regexp = "[a-zA-Z0-9\\s\\.,]+")
    @Size(min = 1, max = 100)
    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public DocumentDetail fileStore(String fileStore) {
        this.fileStore = fileStore;
        return this;
    }

    /**
     * Filestore reference of the document which is attached
     *
     * @return fileStore
     **/
    @ApiModelProperty(required = true, value = "Filestore reference of the document which is attached")
    @NotNull

    @Size(min = 1, max = 256)
    public String getFileStore() {
        return fileStore;
    }

    public void setFileStore(String fileStore) {
        this.fileStore = fileStore;
    }

    public DocumentDetail documentType(DocumentType documentType) {
        this.documentType = documentType;
        return this;
    }

    /**
     * DocumentType Enum for the Document details
     *
     * @return documentType
     **/
    @ApiModelProperty(value = "DocumentType Enum for the Document details")

    @Valid

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public DocumentDetail tenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * Tenant Id of the Document detail
     *
     * @return tenantId
     **/
    @ApiModelProperty(required = true, value = "Tenant Id of the Document detail")
    @NotNull

    @Size(min = 2, max = 128)
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public DocumentDetail latitude(Integer latitude) {
        this.latitude = latitude;
        return this;
    }

    /**
     * Latitude of the photograph taken
     *
     * @return latitude
     **/
    @ApiModelProperty(value = "Latitude of the photograph taken")


    public Integer getLatitude() {
        return latitude;
    }

    public void setLatitude(Integer latitude) {
        this.latitude = latitude;
    }

    public DocumentDetail longitude(Integer longitude) {
        this.longitude = longitude;
        return this;
    }

    /**
     * Longitude of the photograph taken
     *
     * @return longitude
     **/
    @ApiModelProperty(value = "Longitude of the photograph taken")


    public Integer getLongitude() {
        return longitude;
    }

    public void setLongitude(Integer longitude) {
        this.longitude = longitude;
    }

    public DocumentDetail description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Description of the photograph
     *
     * @return description
     **/
    @ApiModelProperty(value = "Description of the photograph")

    @Pattern(regexp = "[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]+")
    @Size(max = 1024)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DocumentDetail dateOfCapture(Long dateOfCapture) {
        this.dateOfCapture = dateOfCapture;
        return this;
    }

    /**
     * Epoch time of when photo has captured
     *
     * @return dateOfCapture
     **/
    @ApiModelProperty(value = "Epoch time of when photo has captured")


    public Long getDateOfCapture() {
        return dateOfCapture;
    }

    public void setDateOfCapture(Long dateOfCapture) {
        this.dateOfCapture = dateOfCapture;
    }

    public DocumentDetail workProgress(WorkProgress workProgress) {
        this.workProgress = workProgress;
        return this;
    }

    /**
     * WorkProgress Enum for Photos captured
     *
     * @return workProgress
     **/
    @ApiModelProperty(value = "WorkProgress Enum for Photos captured")

    @Valid

    public WorkProgress getWorkProgress() {
        return workProgress;
    }

    public void setWorkProgress(WorkProgress workProgress) {
        this.workProgress = workProgress;
    }

    public DocumentDetail deleted(Boolean deleted) {
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

    public DocumentDetail auditDetails(AuditDetails auditDetails) {
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


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DocumentDetail documentDetail = (DocumentDetail) o;
        return Objects.equals(this.id, documentDetail.id) &&
                Objects.equals(this.objectId, documentDetail.objectId) &&
                Objects.equals(this.objectType, documentDetail.objectType) &&
                Objects.equals(this.fileStore, documentDetail.fileStore) &&
                Objects.equals(this.documentType, documentDetail.documentType) &&
                Objects.equals(this.tenantId, documentDetail.tenantId) &&
                Objects.equals(this.latitude, documentDetail.latitude) &&
                Objects.equals(this.longitude, documentDetail.longitude) &&
                Objects.equals(this.description, documentDetail.description) &&
                Objects.equals(this.dateOfCapture, documentDetail.dateOfCapture) &&
                Objects.equals(this.workProgress, documentDetail.workProgress) &&
                Objects.equals(this.deleted, documentDetail.deleted) &&
                Objects.equals(this.auditDetails, documentDetail.auditDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, objectId, objectType, fileStore, documentType, tenantId, latitude, longitude, description, dateOfCapture, workProgress, deleted, auditDetails);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class DocumentDetail {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    objectId: ").append(toIndentedString(objectId)).append("\n");
        sb.append("    objectType: ").append(toIndentedString(objectType)).append("\n");
        sb.append("    fileStore: ").append(toIndentedString(fileStore)).append("\n");
        sb.append("    documentType: ").append(toIndentedString(documentType)).append("\n");
        sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
        sb.append("    latitude: ").append(toIndentedString(latitude)).append("\n");
        sb.append("    longitude: ").append(toIndentedString(longitude)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    dateOfCapture: ").append(toIndentedString(dateOfCapture)).append("\n");
        sb.append("    workProgress: ").append(toIndentedString(workProgress)).append("\n");
        sb.append("    deleted: ").append(toIndentedString(deleted)).append("\n");
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

