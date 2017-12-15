package org.egov.dataupload.model;


import java.util.List;

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
public class ConsumerRequest {
	
	@JsonProperty("UploaderRequest")
	public UploaderRequest uploaderRequest;
		
}