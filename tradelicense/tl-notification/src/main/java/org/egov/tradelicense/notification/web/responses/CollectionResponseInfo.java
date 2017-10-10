package org.egov.tradelicense.notification.web.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CollectionResponseInfo {

	private String resMsgId = null;

	private String status = null;

	private String apiId = null;

	private String ver = null;

	private String ts = null;

	private String key = null;

	private String tenantId = null;
}