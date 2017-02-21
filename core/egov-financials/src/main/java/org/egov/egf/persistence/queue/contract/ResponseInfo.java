package org.egov.egf.persistence.queue.contract;

import java.util.Date;

import lombok.Data;

@Data
public class ResponseInfo {

	private String resMsgId = null;
	
	private String status = null;

	private String apiId = null;

	private String ver = null;

	private Date ts = null;

	private String key = null;

	private String tenantId = null;

}
