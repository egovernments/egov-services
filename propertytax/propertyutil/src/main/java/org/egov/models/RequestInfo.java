package org.egov.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * RequestInfo
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RequestInfo {
	@NonNull
	private String apiId;

	@NonNull
	private String ver;

	private String ts;

	private String did;

	private String key;

	private String msgId;

	private String requesterId;

	private String authToken;

}
