package org.egov.common.web.contract;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.egov.common.domain.model.User;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class RequestInfo {

	private String apiId;

	private String ver;

	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
	private Date ts;

	private String action;

	private String did;

	private String key;

	private String msgId;

	private String requesterId;

	private String authToken;

	private String tenantId;

	@NotNull
	private User userInfo;
}
