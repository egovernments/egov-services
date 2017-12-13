package org.egov.dataupload.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ProcessMetaData {
	
	@JsonProperty("Message")
	public String message;
	
	@JsonProperty("Job Id")
	public String jobId;
	
	@JsonProperty("FinalFileStoreId")
	public String finalfileStoreId;
	
	@JsonProperty("localFilePath")
	public String localFilePath;

}