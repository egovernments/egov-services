package org.egov.hr.emp.indexer.consumer;

import org.egov.hr.emp.indexer.model.Employee;
import org.springframework.kafka.support.serializer.JsonDeserializer;

public class EmployeeDeserializer extends JsonDeserializer<Employee> {


	public EmployeeDeserializer() {
		super(Employee.class);
	}

}


