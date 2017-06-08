package org.egov.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * This object holds type of documents to be uploaded during the transaction for each application type.
 * Author : Narendra
 */

public class DocumentType   {
	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("name")
	private String name = null;

	/**
	 * Application type.
	 */
	public enum ApplicationEnum {
		CREATE("CREATE"),

		RENEWAL("RENEWAL"),

		EVICTION("EVICTION"),

		CANCEL("CANCEL");

		private String value;

		ApplicationEnum(String value) {
			this.value = value;
		}

		@Override
		@JsonValue
		public String toString() {
			return String.valueOf(value);
		}

		@JsonCreator
		public static ApplicationEnum fromValue(String text) {
			for (ApplicationEnum b : ApplicationEnum.values()) {
				if (String.valueOf(b.value).equals(text)) {
					return b;
				}
			}
			return null;
		}
	}

	@JsonProperty("application")
	private ApplicationEnum application = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

	public DocumentType(Long id, String name, ApplicationEnum application, AuditDetails auditDetails) {
		super();
		this.id = id;
		this.name = name;
		this.application = application;
		this.auditDetails = auditDetails;
	}

	public DocumentType() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ApplicationEnum getApplication() {
		return application;
	}

	public void setApplication(ApplicationEnum application) {
		this.application = application;
	}

	public AuditDetails getAuditDetails() {
		return auditDetails;
	}

	public void setAuditDetails(AuditDetails auditDetails) {
		this.auditDetails = auditDetails;
	}

	@Override
	public String toString() {
		return "DocumentType [id=" + id + ", name=" + name + ", application=" + application + ", auditDetails="
				+ auditDetails + ", getId()=" + getId() + ", getName()=" + getName() + ", getApplication()="
				+ getApplication() + ", getAuditDetails()=" + getAuditDetails() + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((application == null) ? 0 : application.hashCode());
		result = prime * result + ((auditDetails == null) ? 0 : auditDetails.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		DocumentType other = (DocumentType) obj;
		if (application != other.application)
			return false;
		if (auditDetails == null) {
			if (other.auditDetails != null)
				return false;
		} else if (!auditDetails.equals(other.auditDetails))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}


}

