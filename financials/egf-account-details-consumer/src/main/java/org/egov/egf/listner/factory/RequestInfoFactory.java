package org.egov.egf.listner.factory;

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
			ts = requestInfoJSONObject.isNull("ts") ? null : sdf.parse(requestInfoJSONObject.getString("ts"));
		} catch (ParseException e) {
			e.printStackTrace();
			LOGGER.debug("Parse exception while parsing date : " + requestInfoJSONObject.getString("ts"));
		}

		RequestInfo requestInfo = RequestInfo.builder()
				.apiId(requestInfoJSONObject.isNull("apiId") ? null : requestInfoJSONObject.getString("apiId"))
				.ver(requestInfoJSONObject.isNull("ver") ? null : requestInfoJSONObject.getString("ver"))
				.ts(ts)
				.action(requestInfoJSONObject.isNull("action") ? null : requestInfoJSONObject.getString("action"))
				.did(requestInfoJSONObject.isNull("did") ? null : requestInfoJSONObject.getString("did"))
				.key(requestInfoJSONObject.isNull("key") ? null : requestInfoJSONObject.getString("key"))
				.msgId(requestInfoJSONObject.isNull("msgId") ? null : requestInfoJSONObject.getString("msgId"))
				.requesterId(requestInfoJSONObject.isNull("requesterId") ? null : requestInfoJSONObject.getString("requesterId"))
				.authToken(requestInfoJSONObject.isNull("authToken") ? null : requestInfoJSONObject.getString("authToken"))
				.tenantId(tenantId)
				.build();

		LOGGER.debug("requestInfo : " + requestInfo);
		return requestInfo;
	}

}
