package org.egov.pgr.rest.web.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.Future;

import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.egov.infra.admin.master.entity.CrossHierarchy;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.CrossHierarchyService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.config.security.authentication.SecureUser;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.entity.enums.ComplaintStatus;
import org.egov.pgr.entity.enums.ReceivingMode;
import org.egov.pgr.rest.web.location.assign.AssignSevaRequestLocation;
import org.egov.pgr.rest.web.model.AttributeValue;
import org.egov.pgr.rest.web.model.Error;
import org.egov.pgr.rest.web.model.ErrorRes;
import org.egov.pgr.rest.web.model.RequestInfo;
import org.egov.pgr.rest.web.model.ResponseInfo;
import org.egov.pgr.rest.web.model.ServiceRequest;
import org.egov.pgr.rest.web.model.ServiceRequestReq;
import org.egov.pgr.rest.web.model.ServiceRequestRes;
import org.egov.pgr.rest.web.numbergeneration.SevaNumberGeneratorImpl;
import org.egov.pgr.rest.web.validate.ValidateSevaRequest;
import org.egov.pgr.service.ComplaintRouterService;
import org.egov.pgr.service.ComplaintService;
import org.egov.pgr.service.ComplaintStatusService;
import org.egov.pgr.service.ComplaintTypeService;
import org.egov.pgr.service.es.ComplaintIndexService;
import org.egov.pims.commons.Position;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value = { "/v1", "/a1" })
public class ServiceRequestController {

	@Autowired
	private CrossHierarchyService crossHierarchyService;

	@Autowired
	private ComplaintTypeService complaintTypeService;

	@Autowired
	private ComplaintStatusService complaintStatusService;

	@Autowired
	private UserService userService;

	@Autowired
	private ComplaintService complaintService;

	@Autowired
	private ValidateSevaRequest validateSevaRequest;

	@Autowired
	private SevaNumberGeneratorImpl sevaNumberGeneratorImpl;

	@Autowired
	private AssignSevaRequestLocation assignSevaRequestLocation;

	@Autowired
	private ComplaintRouterService complaintRouterService;

	@Autowired
	private ComplaintIndexService complaintIndexService;

	private ResponseInfo resInfo = null;

	@RequestMapping(value = "/requests", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseInfo createServiceRequest(@RequestParam String jurisdiction_id,
			@RequestBody ServiceRequestReq request) throws Exception {
		try {
			RequestInfo requestInfo = request.getRequestInfo();
			resInfo = new ResponseInfo(requestInfo.getApiId(), requestInfo.getVer(), new Date().toString(), "uief87324",
					requestInfo.getMsgId(), "true");
			Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
			String name = StringUtils.EMPTY;
			if (Objects.nonNull(authUser)) {
				SecureUser secureUser = (SecureUser) authUser.getPrincipal();
				if (Objects.isNull(secureUser))
					name = "ANONYMOUS";
				else
					name = secureUser.getUserType().name();
			}

			if (validateSevaRequest.validate(request)) {
				// ----------------------------- auto generated seva id
				// ------------------------------
				request.getServiceRequest().setCrn(sevaNumberGeneratorImpl.generate());
				// ----------------------------- auto generated seva id
				// ------------------------------

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

	@RequestMapping(value = "/requests/{service_request_id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceRequestRes updateServiceRequest(@PathVariable String service_request_id,
			@RequestParam String jurisdiction_id, @RequestBody ServiceRequestReq request) throws Exception {
		try {
			RequestInfo requestInfo = request.getRequestInfo();
			resInfo = new ResponseInfo(requestInfo.getApiId(), requestInfo.getVer(), new DateTime().toString(),
					"7gduf46t3erhg", requestInfo.getMsgId(), "true");
			if (request.validate()) {
				ServiceRequest serviceRequest = request.getServiceRequest();
				Complaint complaint = complaintService.getComplaintByCRN(service_request_id);
				Date escalationDate = complaint.getEscalationDate();
				if (Objects.nonNull(complaint)) {
					BeanUtils.copyProperties(serviceRequest, complaint);
					complaint.setEscalationDate(escalationDate);
					User user = userService.getUserByUsername(requestInfo.getRequesterId());
					complaint.getComplainant().setUserDetail(user);
					complaint.getComplainant().setName(user.getName());
					complaint.getComplainant().setMobile(user.getMobileNumber());
					complaint.getComplainant().setEmail(user.getEmailId());
					complaint.setStatus(complaintStatusService.getByName(serviceRequest.getStatusDetails().name()));

					if (Objects.nonNull(serviceRequest.getCrossHierarchyId())
							&& Long.parseLong(serviceRequest.getCrossHierarchyId()) > 0) {
						final Long locationId = Long.parseLong(serviceRequest.getCrossHierarchyId());
						final CrossHierarchy crosshierarchy = crossHierarchyService.findById(locationId);
						complaint.setLocation(crosshierarchy.getParent());
						complaint.setChildLocation(crosshierarchy.getChild());
						complaint.getChildLocation().setParent(crosshierarchy.getChild().getParent());
					}
					if (Objects.nonNull(serviceRequest.getComplaintTypeCode())) {
						final ComplaintType complaintType = complaintTypeService
								.findByCode(serviceRequest.getComplaintTypeCode());
						complaint.setComplaintType(complaintType);
					}
					complaint.setReceivingMode(ReceivingMode.WEBSITE);
					Complaint savedComplaint = complaintService.update(complaint, serviceRequest.getApprovalPosition(),
							serviceRequest.getApprovalComment());
					String locationText = complaint.getLocation().getLocalName() + " "
							+ complaint.getChildLocation().getName();
					serviceRequest.getValues().add(new AttributeValue("location_text", locationText));
					serviceRequest.setEscalationDate(savedComplaint.getEscalationDate());
					serviceRequest.setLastModifiedDate(savedComplaint.getLastModifiedDate());

					ServiceRequestRes serviceRequestResponse = new ServiceRequestRes();
					ResponseInfo responseInfo = resInfo;
					serviceRequestResponse.setResposneInfo(responseInfo);
					serviceRequestResponse.getServiceRequests().add(serviceRequest);

					return serviceRequestResponse;
				} else
					throw new Exception();

			} else {
				// Error handling part to be done.
				throw new Exception();
			}
		} catch (Exception exception) {
			throw exception;
		}
	}

	@RequestMapping(value = "/requests", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceRequestRes updateServiceRequest(@RequestParam String jurisdiction_id,
			@RequestParam String service_code, @RequestParam String status, @RequestParam String service_request_id,
			@RequestParam String api_id, @RequestParam String ver, @RequestParam String ts,
			@RequestParam String start_date, @RequestParam String creater_id) throws Exception {
		try {
			List<Complaint> complaintsList = complaintService.getBySearchInputs(service_code, status,
					service_request_id, start_date, creater_id);
			ServiceRequestRes serviceRequestResponse = new ServiceRequestRes();
			ResponseInfo responseInfo = new ResponseInfo(api_id, ver, new DateTime().toString(), "7gduf46t3erhg", "",
					"true");
			serviceRequestResponse.setResposneInfo(responseInfo);

			for (Complaint complaint : complaintsList) {
				ServiceRequest serviceRequest = new ServiceRequest();
				BeanUtils.copyProperties(complaint, serviceRequest);
				String locationText = complaint.getLocation().getLocalName() + " "
						+ complaint.getChildLocation().getName();
				serviceRequest.getValues().add(new AttributeValue("location_text", locationText));
				serviceRequest.setEscalationDate(complaint.getEscalationDate());
				serviceRequest.setLastModifiedDate(complaint.getLastModifiedDate());
				serviceRequest.setCreatedDate(complaint.getCreatedDate());
				serviceRequest.setStatusDetails(ComplaintStatus.valueOf(complaint.getStatus().getName()));
				serviceRequest.setComplaintTypeName(complaint.getComplaintType().getName());
				serviceRequest.setComplaintTypeCode(complaint.getComplaintType().getCode());
				serviceRequest.setComplaintTypeId(complaint.getComplaintType().getId().toString());
				serviceRequest.setFirstName(complaint.getComplainant().getName());
				serviceRequest.setPhone(complaint.getComplainant().getMobile());
				serviceRequest.setEmail(complaint.getComplainant().getEmail());
				serviceRequest.setCrossHierarchyId(complaint.getLocation().getId().toString());
				serviceRequestResponse.getServiceRequests().add(serviceRequest);
			}
			return serviceRequestResponse;
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

	private void pushAssignedRequests(ServiceRequestReq request, String topic) throws JsonProcessingException {
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

	private void pushSavedRequests(String crn, String topic) throws JsonProcessingException {
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

		producer.send(new ProducerRecord<String, String>(topic, crn, crn));
		producer.close();
	}

	@RequestMapping(value = "/receive-validated-requests", method = RequestMethod.GET)
	public void validatedRequestsReceiver(@RequestParam String jurisdiction_id) {

		Properties props = new Properties();
		props.put("bootstrap.servers", "kafka:9092");
		props.put("group.id", "notifications");
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "10000");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		KafkaConsumer<String, String> validatedRequests = new KafkaConsumer<>(props);
		validatedRequests.subscribe(Arrays.asList(jurisdiction_id + ".mseva.validated"));
		while (true) {
			ConsumerRecords<String, String> records = validatedRequests.poll(5000);
			System.err.println("******** polling validatedRequestsReceiver at time " + new Date().toString());
			for (ConsumerRecord<String, String> record : records) {
				ObjectMapper mapper = new ObjectMapper();
				ServiceRequestReq request;
				try {
					request = mapper.readValue(record.value(), ServiceRequestReq.class);
					if (assignSevaRequestLocation.assign(request.getServiceRequest())) {
						pushAssignedRequests(request, jurisdiction_id + ".mseva.locationassigned");
					} else {
						pushAssignedRequests(request, jurisdiction_id + ".mseva.locationassignfailed");
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

	@RequestMapping(value = "/receive-location-assigned-requests", method = RequestMethod.GET)
	public void locationAssignedRequestsReceiver(@RequestParam String jurisdiction_id) {
		Properties props = new Properties();
		props.put("bootstrap.servers", "kafka:9092");
		props.put("group.id", "notifications");
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "10000");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		KafkaConsumer<String, String> validatedRequests = new KafkaConsumer<>(props);
		validatedRequests.subscribe(Arrays.asList(jurisdiction_id + ".mseva.locationassigned"));
		while (true) {
			ConsumerRecords<String, String> records = validatedRequests.poll(5000);
			System.err.println("******** polling locationAssignedRequestsReceiver at time " + new Date().toString());
			for (ConsumerRecord<String, String> record : records) {
				ObjectMapper mapper = new ObjectMapper();
				ServiceRequestReq request;
				try {
					request = mapper.readValue(record.value(), ServiceRequestReq.class);
					pushAssignedRequests(request, jurisdiction_id + ".mseva.assigned");
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

	@RequestMapping(value = "/receive-assigned-requests", method = RequestMethod.GET)
	public void assignedRequestsReceiver(@RequestParam String jurisdiction_id) {

		Properties props = new Properties();
		props.put("bootstrap.servers", "kafka:9092");
		props.put("group.id", "notifications");
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "10000");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		KafkaConsumer<String, String> validatedRequests = new KafkaConsumer<>(props);
		validatedRequests.subscribe(Arrays.asList(jurisdiction_id + ".mseva.assigned"));
		while (true) {
			ConsumerRecords<String, String> records = validatedRequests.poll(5000);
			System.err.println("******* polling assignedRequestsReceiver at time " + new Date().toString());
			for (ConsumerRecord<String, String> record : records) {
				ObjectMapper mapper = new ObjectMapper();
				Complaint complaint;
				ServiceRequestReq request;
				try {
					request = mapper.readValue(record.value(), ServiceRequestReq.class);
					complaint = toComplaint(request);
					complaint = assignComplaint(complaint);
					Complaint savedComplaint = complaintService.createComplaint(complaint);
					pushSavedRequests(savedComplaint.getCrn(), jurisdiction_id + ".mseva.saved");

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

	@RequestMapping(value = "/receive-success-requests", method = RequestMethod.GET)
	public void successRequestsReceiver(@RequestParam String jurisdiction_id) {

		Properties props = new Properties();
		props.put("bootstrap.servers", "kafka:9092");
		props.put("group.id", "notifications");
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "10000");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		KafkaConsumer<String, String> validatedRequests = new KafkaConsumer<>(props);
		validatedRequests.subscribe(Arrays.asList(jurisdiction_id + ".mseva.indexed"));
		while (true) {
			ConsumerRecords<String, String> records = validatedRequests.poll(5000);
			System.err.println("******* polling assignedRequestsReceiver at time " + new Date().toString());
			for (ConsumerRecord<String, String> record : records) {
				ObjectMapper mapper = new ObjectMapper();
				Complaint complaint;
				ServiceRequestReq request;
				try {
					request = mapper.readValue(record.value(), ServiceRequestReq.class);
					complaint = toComplaint(request);
					complaint = assignComplaint(complaint);
					Complaint savedComplaint = complaintService.createComplaint(complaint);

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

	private Complaint assignComplaint(Complaint complaint) {
		final Position assignee = complaintRouterService.getAssignee(complaint);
		complaint.setAssignee(assignee);
		return complaint;
	}

	private Complaint toComplaint(ServiceRequestReq request) {
		Complaint complaint = new Complaint();
		BeanUtils.copyProperties(request.getServiceRequest(), complaint);
		Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
		String name = StringUtils.EMPTY;
		if (Objects.nonNull(authUser)) {
			SecureUser secureUser = (SecureUser) authUser.getPrincipal();
			if (Objects.isNull(secureUser))
				name = "ANONYMOUS";
			else
				name = secureUser.getUserType().name();
		}
		if (name.equals("CITIZEN") || name.equals("EMPLOYEE")) {
			User user = userService.getUserById(Long.valueOf(request.getRequestInfo().getRequesterId()));
			complaint.getComplainant().setUserDetail(user);
		} else {
			complaint.getComplainant().setName(request.getServiceRequest().getFirstName());
			complaint.getComplainant().setMobile(request.getServiceRequest().getPhone());
			complaint.getComplainant().setEmail(request.getServiceRequest().getEmail());
		}
		if (Objects.nonNull(request.getServiceRequest().getCrossHierarchyId())
				&& Long.parseLong(request.getServiceRequest().getCrossHierarchyId()) > 0) {
			final Long locationId = Long.parseLong(request.getServiceRequest().getCrossHierarchyId());
			final CrossHierarchy crosshierarchy = crossHierarchyService.findById(locationId);
			complaint.setLocation(crosshierarchy.getParent());
			complaint.setChildLocation(crosshierarchy.getChild());
			complaint.getChildLocation().setParent(crosshierarchy.getChild().getParent());
		}
		if (Objects.nonNull(request.getServiceRequest().getComplaintTypeCode())) {
			final ComplaintType complaintType = complaintTypeService
					.findBy(Long.valueOf(request.getServiceRequest().getComplaintTypeCode()));
			complaint.setComplaintType(complaintType);
		}

		complaint.setReceivingMode(ReceivingMode.MOBILE);
		String locationText = complaint.getLocation().getLocalName() + " " + complaint.getChildLocation().getName();
		request.getServiceRequest().getValues().add(new AttributeValue("location_text", locationText));

		return complaint;
	}

	@RequestMapping(value = "/receive-saved-requests", method = RequestMethod.GET)
	public void savedRequestsReceiver(@RequestParam String jurisdiction_id) {
		Properties props = new Properties();
		props.put("bootstrap.servers", "kafka:9092");
		props.put("group.id", "notifications");
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "10000");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		KafkaConsumer<String, String> savedRequests = new KafkaConsumer<>(props);
		savedRequests.subscribe(Arrays.asList(jurisdiction_id + ".mseva.saved"));
		while (true) {
			ConsumerRecords<String, String> records = savedRequests.poll(5000);
			System.err.println("******** polling savedRequestsReceiver at time " + new Date().toString());
			for (ConsumerRecord<String, String> record : records) {
				Complaint complaint = complaintService.getComplaintByCRN(record.value());
				// complaintIndexService.createComplaintIndex(complaint);
			}
		}
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