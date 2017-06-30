package org.egov.collection.web.errorhandlers;

import java.util.List;

import org.egov.collection.web.contract.ResponseInfo;
import org.egov.common.contract.response.ErrorField;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class ErrorResponse {

	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo;

	@JsonProperty("Error")
	private Error error;

	@JsonIgnore
	public List<ErrorField> getErrorFields() {
		return error.getErrorFields();
	}

}
