package org.egov.pgr.controller;

import java.util.Date;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.entity.enums.ReceivingMode;
import org.egov.pgr.model.Error;
import org.egov.pgr.model.ErrorRes;
import org.egov.pgr.model.RequestInfo;
import org.egov.pgr.model.ResponseInfo;
import org.egov.pgr.model.ServiceRequest;
import org.egov.pgr.model.ServiceRequestReq;
import org.egov.pgr.model.ServiceRequestRes;
import org.egov.pgr.rest.numbergeneration.SevaNumberGeneratorImpl;
import org.egov.pgr.rest.validate.ValidateSevaRequest;
import org.egov.pgr.service.ComplaintService;
import org.egov.pgr.service.ComplaintStatusService;
import org.egov.pgr.service.ComplaintTypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value = { "/v1", "/a1" })
public class ServiceRequestController {

	@Autowired
	private ComplaintTypeService complaintTypeService;

	@Autowired
	private ComplaintStatusService complaintStatusService;

	@Autowired
	private ComplaintService complaintService;

	@Autowired
	private SevaNumberGeneratorImpl sevaNumberGeneratorImpl;
	
	@Autowired
	private ValidateSevaRequest validateSevaRequest;

	private ResponseInfo resInfo = null;

	@RequestMapping(value = "/requests", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceRequestRes createServiceRequest(@RequestParam String jurisdiction_id,
			@RequestBody ServiceRequestReq request) throws Exception {
		try {
			RequestInfo requestInfo = request.getRequestInfo();
			ServiceRequest serviceRequest = request.getServiceRequest();
			resInfo = new ResponseInfo(requestInfo.getApiId(), requestInfo.getVer(), new Date().toString(), "uief87324",
					requestInfo.getMsgId(), "true");
			    request.getServiceRequest().setCrn(sevaNumberGeneratorImpl.generate());
				Complaint complaint = new Complaint();
				BeanUtils.copyProperties(serviceRequest, complaint);
					complaint.getComplainant().setName(serviceRequest.getFirstName());
					complaint.getComplainant().setMobile(serviceRequest.getPhone());
					complaint.getComplainant().setEmail(serviceRequest.getEmail());
				complaint.setLocation(5L);	
				complaint.setChildLocation(172L);	
				if (Objects.nonNull(serviceRequest.getComplaintTypeCode())) {
					final ComplaintType complaintType = complaintTypeService
							.findBy(Long.valueOf(serviceRequest.getComplaintTypeCode()));
					complaint.setComplaintType(complaintType);
				}
				complaint.setReceivingMode(ReceivingMode.MOBILE);
				Complaint savedComplaint = complaintService.createComplaint(complaint);
				serviceRequest.setCrn(savedComplaint.getCrn());
				ServiceRequestRes serviceRequestResponse = new ServiceRequestRes();
				ResponseInfo responseInfo = resInfo;
				serviceRequestResponse.setResposneInfo(responseInfo);
				serviceRequestResponse.getServiceRequests().add(serviceRequest);
				return serviceRequestResponse;
			
		} catch (Exception exception) {
			throw exception;
		}
	}
	
	@RequestMapping(value = "/requests/kafka", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseInfo createServiceRequestWithKafka(@RequestParam String jurisdiction_id,
			@RequestBody ServiceRequestReq request) throws Exception {
		try {
			if (validateSevaRequest.validate(request)) {
				// ----------------------------- auto generated seva id
				// ------------------------------
				request.getServiceRequest().setCrn(sevaNumberGeneratorImpl.generate());
				// ----------------------------- auto generated seva id ------------------------------
				// ---------------------------- Push to queue with topic
				// ----------------------------
				pushValidatedRequests(request, jurisdiction_id + ".mseva.validated");
				// ---------------------------- Push to queue with topic
				// ----------------------------
				ResponseInfo responseInfo = resInfo;
				responseInfo.setStatus(
						"Your Complaint with crn: " + request.getServiceRequest().getCrn() + " Is under process");
				return responseInfo;
			} else {
				// Error handling part to be done.
				throw new Exception();
			}
		} catch (Exception exception) {
			throw exception;
		}
	}
	
	private void pushValidatedRequests(ServiceRequestReq serviceRequestReq, String topic)
			throws JsonProcessingException {
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

		Future<RecordMetadata> future = producer.send(new ProducerRecord<String, String>(topic,
				serviceRequestReq.getServiceRequest().getCrn(), mapper.writeValueAsString(serviceRequestReq)));
		System.err.println(future.toString());
		producer.close();
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorRes> handleError(Exception ex) {
		ex.printStackTrace();
		ErrorRes response = new ErrorRes();
		response.setResposneInfo(resInfo);
		Error error = new Error();
		error.setCode(400);
		error.setDescription("General Server Error");
		response.setError(error);
		return new ResponseEntity<ErrorRes>(response, HttpStatus.BAD_REQUEST);
	}
}