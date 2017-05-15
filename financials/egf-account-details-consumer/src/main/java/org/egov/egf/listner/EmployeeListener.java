package org.egov.egf.listner;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.egf.domain.model.contract.RequestInfo;
import org.egov.egf.listner.factory.RequestInfoFactory;
import org.egov.egf.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class EmployeeListener {

	public static final Logger LOGGER = LoggerFactory.getLogger(EmployeeListener.class);

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private RequestInfoFactory requestInfoFactory;

	@KafkaListener(topics = "${kafka.topics.employee.savedb.name}")
	public void listen(ConsumerRecord<String, String> record) {
		LOGGER.info("key : " + record.key() + "\t\t" + "value : " + record.value());

		JsonReader jsonReader = Json.createReader(new StringReader(record.value()));
		JsonObject jsonObject = jsonReader.readObject();
		jsonReader.close();
		JsonObject requestInfoJSONObject = jsonObject.getJsonObject("RequestInfo");
		LOGGER.info("requestInfoJSONObject : " + requestInfoJSONObject);
		JsonObject employeeJSONObject = jsonObject.getJsonObject("Employee");
		LOGGER.info("employeeJSONObject : " + employeeJSONObject);
		String tenantId = employeeJSONObject.isNull("tenantId") ? null : employeeJSONObject.getString("tenantId");
		LOGGER.info("tenantId : " + tenantId);

		RequestInfo requestInfo = requestInfoFactory.getRequestInfo(requestInfoJSONObject, tenantId);

		// FIXME : Parse id in long once egf-masters update their AccountDetailKey Contract & table schema.
		employeeService.processRequest(employeeJSONObject.getInt("id"), tenantId, requestInfo);
	}

}