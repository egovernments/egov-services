package org.egov.works.commons.domain.model;

import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * An Object that hold Document Details (Uploaded files/documents) for all the
 * objects
 */
@ApiModel(description = "An Object that hold Document Details (Uploaded files/documents) for all the objects")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-24T13:08:31.335Z")

public class DocumentDetail {
	@JsonProperty("id")
	private String id = null;

	@JsonProperty("objectId")
	private String objectId = null;

	@JsonProperty("objectType")
	private String objectType = null;

	@JsonProperty("fileStore")
	private String fileStore = null;

	@JsonProperty("tenantId")
	private String tenantId = null;

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

	@Size(min = 4, max = 128)
	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		DocumentDetail documentDetail = (DocumentDetail) o;
		return Objects.equals(this.id, documentDetail.id) && Objects.equals(this.objectId, documentDetail.objectId)
				&& Objects.equals(this.objectType, documentDetail.objectType)
				&& Objects.equals(this.fileStore, documentDetail.fileStore)
				&& Objects.equals(this.tenantId, documentDetail.tenantId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, objectId, objectType, fileStore, tenantId);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class DocumentDetail {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    objectId: ").append(toIndentedString(objectId)).append("\n");
		sb.append("    objectType: ").append(toIndentedString(objectType)).append("\n");
		sb.append("    fileStore: ").append(toIndentedString(fileStore)).append("\n");
		sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}
