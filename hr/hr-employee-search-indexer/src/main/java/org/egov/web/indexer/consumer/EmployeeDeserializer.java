package org.egov.web.indexer.consumer;

import org.egov.eis.model.Employee;
import org.springframework.kafka.support.serializer.JsonDeserializer;

public class EmployeeDeserializer extends JsonDeserializer<Employee> {


	public EmployeeDeserializer() {
		super(Employee.class);
	}

}


