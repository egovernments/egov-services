package org.egov.pgr.web.locationassignment.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.egov.pgr.web.locationassignment.model.ServiceRequestReq;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/pgr/locationassignment")
public class LocationAssignmentController {
	public void locationAssignedRequestsReceiver() {
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("group.id", "notifications");
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "10000");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		KafkaConsumer<String, String> validatedRequests = new KafkaConsumer<>(props);
		validatedRequests.subscribe(Arrays.asList("ap.public.mseva.locationassigned"));
		while (true) {
			ConsumerRecords<String, String> records = validatedRequests.poll(5000);
			System.err.println("******** polling locationAssignedRequestsReceiver at time " + new Date().toString());
			for (ConsumerRecord<String, String> record : records) {
				ObjectMapper mapper = new ObjectMapper();
				ServiceRequestReq request;
				try {
					request = mapper.readValue(record.value(), ServiceRequestReq.class);
					if(request.getRequestInfo().getRequesterId() != null || request.getRequestInfo().getRequesterId() != StringUtils.EMPTY){
						System.out.println("---------------- Location Assigned to Complaint --------------");
						System.out.println("---------------- Received form topic  ap.public.mseva.locationassigned -------------------------");
						pushAssignedRequests(request, "ap.public.mseva.assigned");
						System.out.println("---------------- Pushing to topic ap.public.mseva.assigned -------------------------");
					}
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

	private void pushAssignedRequests(ServiceRequestReq request, String topic) throws JsonProcessingException {
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
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