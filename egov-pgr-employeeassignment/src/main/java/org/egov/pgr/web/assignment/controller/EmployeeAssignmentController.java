package org.egov.pgr.web.assignment.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.egov.pgr.web.assignment.model.AttributeValue;
import org.egov.pgr.web.assignment.model.ServiceRequest;
import org.egov.pgr.web.assignment.model.ServiceRequestReq;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class EmployeeAssignmentController {

	public void locationAssignedRequestsReceiver() {
		Properties props = new Properties();
		props.put("bootstrap.servers", "kafka:9092");
		props.put("group.id", "locationassigned");
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "10000");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		KafkaConsumer<String, String> savedRequests = new KafkaConsumer<>(props);
		savedRequests.subscribe(Arrays.asList("ap.public.mseva.locationassigned"));
		while (true) {
			ConsumerRecords<String, String> records = savedRequests.poll(5000);
			System.err.println("******** polling locationAssignedRequestsReceiver at time " + new Date().toString());
			for (ConsumerRecord<String, String> record : records) {

				ObjectMapper mapper = new ObjectMapper();
				ServiceRequestReq request;
				try {
					request = mapper.readValue(record.value(), ServiceRequestReq.class);
					ServiceRequest serviceRequest = request.getServiceRequest();
			        List<AttributeValue> attributesList = serviceRequest.getValues();
					attributesList.add(new AttributeValue("assignee", "2"));
					serviceRequest.setValues(attributesList);
					request.setServiceRequest(serviceRequest);
					System.err.println(
							"---------------- Received form topic  ap.public.mseva.locationassigned -------------------------");
					System.err.println("---------------- Employee Assigned to Complaint --------------");
					pushValidatedRequests(request, "ap.public.mseva.assigned");
					System.err.println(
							"---------------- Pushing to topic ap.public.mseva.assigned -------------------------");
				} catch (JsonParseException e) {
					e.printStackTrace();
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
	}

	private void pushValidatedRequests(ServiceRequestReq request, String topic) throws JsonProcessingException {
		Properties props = new Properties();
		props.put("bootstrap.servers", "kafka:9092");
		props.put("acks", "all");
		props.put("retries", 0);
		props.put("batch.size", 1);
		props.put("linger.ms", 1);
		props.put("buffer.memory", 33554432);
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		Producer producer = new KafkaProducer<>(props);
		ObjectMapper mapper = new ObjectMapper();

		producer.send(new ProducerRecord<String, String>(topic, request.getServiceRequest().getCrn(),
				mapper.writeValueAsString(request)));
		producer.close();
	}
}