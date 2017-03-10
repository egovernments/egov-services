package org.egov.egf.persistence.queue.contract;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseInfo {

	private String resMsgId = null;
	
	private String status = null;

	private String apiId = null;

	private String ver = null;

	private Date ts = null;

	private String key = null;

	private String tenantId = null;

}
