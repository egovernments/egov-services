package org.egov.audit.web.contract;

import java.util.Date;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Audit {

 	@JsonProperty("id")
	private Long id;
	
	@NotEmpty
 	@JsonProperty("channel")
	private String channel;
	
 	@JsonProperty("deviceId")
	private String deviceId;
	
 	@JsonProperty("consumerId")
	private Long consumerId;
 	
	@NotEmpty
 	@JsonProperty("eventName")
	private String eventName;
	
 	@JsonProperty("eventDate")
	private Date eventDate;
	
 	@JsonProperty("ipAddress")
	private String ipAddress;
}
