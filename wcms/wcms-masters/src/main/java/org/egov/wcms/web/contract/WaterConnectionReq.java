package org.egov.wcms.web.contract;

import javax.validation.constraints.NotNull;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.wcms.model.Connection;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;


@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
public class WaterConnectionReq {
	
	@NotNull
	@JsonProperty("RequestInfo")
	private ResponseInfo requestInfo;
	
	@NotNull
	@JsonProperty("Connection")
	private Connection connection;

}
