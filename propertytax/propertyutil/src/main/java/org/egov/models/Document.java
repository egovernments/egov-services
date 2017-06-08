package org.egov.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This object holds list of documents attached during the transaciton for a agreement
 * Author : Narendra
 */


public class Document   {
	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("documentType")
	private DocumentType documentType = null;

	@JsonProperty("fileStore")
	private String fileStore = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

	public Document(Long id, DocumentType documentType, String fileStore, AuditDetails auditDetails) {
		super();
		this.id = id;
		this.documentType = documentType;
		this.fileStore = fileStore;
		this.auditDetails = auditDetails;
	}

	public Document() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DocumentType getDocumentType() {
		return documentType;
	}

	public void setDocumentType(DocumentType documentType) {
		this.documentType = documentType;
	}

	public String getFileStore() {
		return fileStore;
	}

	public void setFileStore(String fileStore) {
		this.fileStore = fileStore;
	}

	public AuditDetails getAuditDetails() {
		return auditDetails;
	}

	public void setAuditDetails(AuditDetails auditDetails) {
		this.auditDetails = auditDetails;
	}

	@Override
	public String toString() {
		return "Document [id=" + id + ", documentType=" + documentType + ", fileStore=" + fileStore + ", auditDetails="
				+ auditDetails + ", getId()=" + getId() + ", getDocumentType()=" + getDocumentType()
				+ ", getFileStore()=" + getFileStore() + ", getAuditDetails()=" + getAuditDetails() + ", getClass()="
				+ getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((auditDetails == null) ? 0 : auditDetails.hashCode());
		result = prime * result + ((documentType == null) ? 0 : documentType.hashCode());
		result = prime * result + ((fileStore == null) ? 0 : fileStore.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Document other = (Document) obj;
		if (auditDetails == null) {
			if (other.auditDetails != null)
				return false;
		} else if (!auditDetails.equals(other.auditDetails))
			return false;
		if (documentType == null) {
			if (other.documentType != null)
				return false;
		} else if (!documentType.equals(other.documentType))
			return false;
		if (fileStore == null) {
			if (other.fileStore != null)
				return false;
		} else if (!fileStore.equals(other.fileStore))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


}

