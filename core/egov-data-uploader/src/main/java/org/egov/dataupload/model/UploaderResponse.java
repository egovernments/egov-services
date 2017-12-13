package org.egov.dataupload.model;

import org.egov.common.contract.response.ResponseInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import groovy.transform.ToString;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UploaderResponse {
	
	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo;
	
	@JsonProperty("ProccessMetadata")
	private ProcessMetaData proccessMetadata;

}