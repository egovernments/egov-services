package org.egov.eis.broker;


import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.eis.model.Employee;
import org.egov.eis.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import com.fasterxml.jackson.databind.ObjectMapper;

public class EmployeeSaveConsumer {

	public static final Logger LOGGER = LoggerFactory.getLogger(EmployeeSaveConsumer.class);

	@Autowired
	private EmployeeService employeeService;
	
/*	@Value("${kafka.topics.employee.savedb.name}")
	String employeeSaveTopic;*/

	@KafkaListener(containerFactory = "kafkaListenerContainerFactory",topics = "employee-save-db")
	public void listen(ConsumerRecord<String, String> record) {
		LOGGER.info("key:"+ record.key() +":"+ "value:" +record.value());
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			employeeService.saveEmployee(objectMapper.readValue(record.value(), Employee.class));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
