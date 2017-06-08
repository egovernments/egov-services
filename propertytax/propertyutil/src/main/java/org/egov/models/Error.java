package org.egov.models;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Error object will be returned as a part of reponse body in conjunction with ResponseInfo as part of ErrorResponse whenever the request processing status in the ResponseInfo is FAILED. HTTP return in this scenario will usually be HTTP 400.
 */


public class Error   {
	@JsonProperty("code")
	@NotNull
	private String code = null;

	@JsonProperty("message")
	@NotNull
	private String message = null;

	@JsonProperty("description")
	private String description = null;

	@JsonProperty("params")
	private Map<String,String> fileds = new HashMap<String,String>();

	public Error code(String code) {
		this.code = code;
		return this;
	}

	/**
	 * Error Code will be module specific error label/code to identiffy the error. All modules should also publish the Error codes with their specific localized values in localization service to ensure clients can print locale specific error messages. Example for error code would be User.NotFound to indicate User Not Found by User/Authentication service. All services must declare their possible Error Codes with brief description in the error response section of their API path.
	 * @return code
	 **/

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Error message(String message) {
		this.message = message;
		return this;
	}

	/**
	 * English locale message of the error code. Clients should make a separate call to get the other locale description if configured with the service. Clients may choose to cache these locale specific messages to enhance performance with a reasonable TTL (May be defined by the localization service based on tenant + module combination).
	 * @return message
	 **/


	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Error description(String description) {
		this.description = description;
		return this;
	}

	/**
	 * Optional long description of the error to help clients take remedial action. This will not be available as part of localization service.
	 * @return description
	 **/
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Map<String, String> getFileds() {
		return fileds;
	}

	public void setFileds(Map<String, String> fileds) {
		this.fileds = fileds;
	}

   


	public Error(String code, String message, String description, Map<String, String> fileds) {
		super();
		this.code = code;
		this.message = message;
		this.description = description;
		this.fileds = fileds;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((fileds == null) ? 0 : fileds.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
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
		Error other = (Error) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (fileds == null) {
			if (other.fileds != null)
				return false;
		} else if (!fileds.equals(other.fileds))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Error [code=" + code + ", message=" + message + ", description=" + description + ", fileds=" + fileds + "]";
	}
}

