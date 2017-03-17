package org.egov.web.indexer.consumer;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.eis.index.model.EmployeeIndex;
import org.egov.eis.model.Employee;
import org.egov.web.indexer.adaptor.EmployeeAdapter;
import org.egov.web.indexer.repository.ElasticSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class EmployeeListener {

	private static final String INDEX_NAME = "employees";
	private static final String INDEX_TYPE = "employee";

	private ElasticSearchRepository elasticSearchRepository;
	private EmployeeAdapter employeeAdapter;


	@Autowired
	public EmployeeListener(ElasticSearchRepository elasticSearchRepository, EmployeeAdapter employeeAdapter) {
		this.elasticSearchRepository = elasticSearchRepository;
		
		this.employeeAdapter = employeeAdapter;

	}

	@KafkaListener(id = "${kafka.topics.egov.index.id}", topics = "${kafka.topics.egov.index.name}", group = "${kafka.topics.egov.index.group}")
	public void listen(ConsumerRecord<String, Employee> record) {
		Employee employee = record.value();
		
		//if (employeeRequest.getServiceRequest() != null && !employeeRequest.getServiceRequest().getValues().isEmpty()
			//	&& employeeRequest.getServiceRequest().getValues().get("complaintStatus").equalsIgnoreCase("REGISTERED"))
		//{
		EmployeeIndex employeeIndex = employeeAdapter.indexOnCreate(employee);
		elasticSearchRepository.index(INDEX_NAME, INDEX_TYPE, employeeIndex.getEmployeeDetails().getEmployeeId(), employeeIndex);
		//}

	}
}