package org.egov.property.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ResponseInfo  {

	private String appId;

	private String ver;

	private String ts;

	private String msgId;

	private String resMsgId;

	private String  status;
}
