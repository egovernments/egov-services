package org.egov.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * ResponseInfo
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ResponseInfo {

	private String apiId;

	private String ver;

	private String ts;

	private String resMsgId;

	private String msgId;

	@NonNull
	private String status;

}
