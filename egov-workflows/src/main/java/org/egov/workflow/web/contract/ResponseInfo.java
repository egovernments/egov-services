package org.egov.workflow.web.contract;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Setter
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ResponseInfo {

	private String apiId = null;

	private String ver = null;

	private String ts = null;

	private String resMsgId = null;

	private String msgId = null;

	private String status = null;

	public ResponseInfo apiId(final String apiId) {
		this.apiId = apiId;
		return this;
	}

}
