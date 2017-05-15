package org.egov.egf.listner.factory;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.json.JsonObject;

import org.egov.egf.domain.model.contract.RequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RequestInfoFactory {

	public static final Logger LOGGER = LoggerFactory.getLogger(RequestInfoFactory.class);

	public RequestInfo getRequestInfo(JsonObject requestInfoJSONObject, String tenantId) {
		final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

		Date ts = null;
		try {
			ts = isEmpty(requestInfoJSONObject.getString("ts")) ? null : sdf.parse(requestInfoJSONObject.getString("ts"));
		} catch (ParseException e) {
			e.printStackTrace();
			LOGGER.debug("Parse exception while parsing date : " + requestInfoJSONObject.getString("ts"));
		}

		RequestInfo requestInfo = RequestInfo.builder()
				.apiId(isEmpty(requestInfoJSONObject.getString("apiId")) ? null : requestInfoJSONObject.getString("apiId"))
				.ver(isEmpty(requestInfoJSONObject.getString("ver")) ? null : requestInfoJSONObject.getString("ver"))
				.ts(ts)
				.action(isEmpty(requestInfoJSONObject.getString("action")) ? null : requestInfoJSONObject.getString("action"))
				.did(isEmpty(requestInfoJSONObject.getString("did")) ? null : requestInfoJSONObject.getString("did"))
				.key(isEmpty(requestInfoJSONObject.getString("key")) ? null : requestInfoJSONObject.getString("key"))
				.msgId(isEmpty(requestInfoJSONObject.getString("msgId")) ? null : requestInfoJSONObject.getString("msgId"))
				.requesterId(isEmpty(requestInfoJSONObject.getString("requesterId")) ? null : requestInfoJSONObject.getString("requesterId"))
				.authToken(isEmpty(requestInfoJSONObject.getString("authToken")) ? null : requestInfoJSONObject.getString("authToken"))
				.tenantId(tenantId)
				.build();

		LOGGER.debug("requestInfo : " + requestInfo);
		return requestInfo;
	}

}
