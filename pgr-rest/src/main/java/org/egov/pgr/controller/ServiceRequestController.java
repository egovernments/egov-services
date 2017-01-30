package org.egov.pgr.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.egov.pgr.builder.User;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.entity.enums.ReceivingMode;
import org.egov.pgr.model.*;
import org.egov.pgr.model.Error;
import org.egov.pgr.producer.GrievanceProducer;
import org.egov.pgr.rest.numbergeneration.SevaNumberGeneratorImpl;
import org.egov.pgr.rest.validate.ValidateSevaRequest;
import org.egov.pgr.service.ComplaintService;
import org.egov.pgr.service.ComplaintStatusService;
import org.egov.pgr.service.ComplaintTypeService;
import org.egov.pgr.validators.FieldErrorDTO;
import org.egov.pgr.validators.SevaRequestValidator;
import org.egov.pgr.validators.ValidationErrorDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = {"/service_request"})
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

    @Autowired
    private GrievanceProducer kafkaProducer;

    private ResponseInfo resInfo = null;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(new SevaRequestValidator());
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ServiceRequestRes createServiceRequest(@RequestParam String jurisdiction_id,
                                                  @Valid @RequestBody SevaRequest request) throws Exception {
        try {
            User user = null;
            RequestInfo requestInfo = request.getRequestInfo();
            String token = requestInfo.getAuthToken();
            if (token != null) user = retrieveUser(token);
            if (user != null) {
                ServiceRequest serviceRequest = request.getServiceRequest();
                serviceRequest.setFirstName(user.getName());
                serviceRequest.setPhone(user.getMobileNumber());
                serviceRequest.setEmail(user.getEmailId());
            }
            String newComplaintId = sevaNumberGeneratorImpl.generate();
            request.getServiceRequest().setCrn(newComplaintId);
            request.getRequestInfo().setAction("create");
            kafkaProducer.sendMessage(jurisdiction_id + ".mseva.validated", request);
            resInfo = new ResponseInfo(requestInfo.getApiId(), requestInfo.getVer(), new Date().toString(), "uief87324", requestInfo.getMsgId(), "true");
            ServiceRequestRes serviceRequestResponse = new ServiceRequestRes();
            serviceRequestResponse.setResposneInfo(resInfo);
            serviceRequestResponse.getServiceRequests().add(request.getServiceRequest());
            return serviceRequestResponse;
        } catch (Exception exception) {
            throw exception;
        }
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity handle(MethodArgumentNotValidException exception) {
        ValidationErrorDTO dto = new ValidationErrorDTO();
        dto.addFieldErrors(exception.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError -> new FieldErrorDTO(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList()));

        return new ResponseEntity(dto, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity processFieldErrors(List<FieldError> fieldErrors) {
        ValidationErrorDTO dto = new ValidationErrorDTO();

        for (FieldError fieldError : fieldErrors) {
            //TODO - Consider resolving localized version of the message
            String localizedErrorMessage = fieldError.getDefaultMessage();
            dto.addFieldError(fieldError.getField(), localizedErrorMessage);
        }

        return new ResponseEntity(dto, HttpStatus.BAD_REQUEST);
    }

    private User retrieveUser(String token) {
        //TODO - Externalize the url to the user details
        String url = String.format("http://localhost:8080/user/details?access_token=%s", token);
        return new RestTemplate().getForObject(url, User.class);
    }

    @RequestMapping(value = "/requests/kafka", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseInfo createServiceRequestWithKafka(@RequestParam String jurisdiction_id,
                                                      @RequestBody SevaRequest request) throws Exception {
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

    private void pushValidatedRequests(SevaRequest sevaRequest, String topic)
            throws JsonProcessingException {
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

        Future<RecordMetadata> future = producer.send(new ProducerRecord<String, String>(topic,
                sevaRequest.getServiceRequest().getCrn(), mapper.writeValueAsString(sevaRequest)));
        System.err.println(future.toString());
        System.out.println("------------- RECORD PUSHED TO VALIDATED TOPIC -----------");
        producer.close();
    }

//    public void assignedRequestsReceiver() {
//        Properties props = new Properties();
//        props.put("bootstrap.servers", "localhost:9092");
//        props.put("group.id", "assigned");
//        props.put("enable.auto.commit", "true");
//        props.put("auto.commit.interval.ms", "10000");
//        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//        KafkaConsumer<String, String> savedRequests = new KafkaConsumer<>(props);
//        savedRequests.subscribe(Arrays.asList("ap.public.mseva.assigned"));
//        while (true) {
//            ConsumerRecords<String, String> records = savedRequests.poll(5000);
//            System.err.println("******** polling assignedRequestsReceiver at time " + new Date().toString());
//            for (ConsumerRecord<String, String> record : records) {
//
//                ObjectMapper mapper = new ObjectMapper();
//                SevaRequest request;
//                try {
//                    request = mapper.readValue(record.value(), SevaRequest.class);
//
//                    System.err.println(
//                            "---------------- Received form topic  ap.public.mseva.assigned -------------------------");
//                } catch (JsonParseException e) {
//                    e.printStackTrace();
//                } catch (JsonMappingException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }


    private Complaint toComplaint(SevaRequest request) {
        Complaint complaint = new Complaint();
        ServiceRequest serviceRequest = request.getServiceRequest();
        BeanUtils.copyProperties(serviceRequest, complaint);
//		Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
//		String name = StringUtils.EMPTY;
//		if (Objects.nonNull(authUser)) {
//			SecureUser secureUser = (SecureUser) authUser.getPrincipal();
//			if (Objects.isNull(secureUser))
//				name = "ANONYMOUS";
//			else
//				name = secureUser.getUserType().name();
//		}
//		if (name.equals("CITIZEN") || name.equals("EMPLOYEE")) {
//			User user = userService.getUserById(Long.valueOf(request.getRequestInfo().getRequesterId()));
//			complaint.getComplainant().setUserDetail(user);
//		} else {
        complaint.getComplainant().setName(request.getServiceRequest().getFirstName());
        complaint.getComplainant().setMobile(request.getServiceRequest().getPhone());
        complaint.getComplainant().setEmail(request.getServiceRequest().getEmail());
//		}

        List<AttributeValue> values = serviceRequest.getValues();
//		if (Objects.nonNull(request.getServiceRequest().getCrossHierarchyId())
//				&& Long.parseLong(request.getServiceRequest().getCrossHierarchyId()) > 0) {
//			final Long locationId = Long.parseLong(request.getServiceRequest().getCrossHierarchyId());
////			final CrossHierarchy crosshierarchy = crossHierarchyService.findById(locationId);
//			complaint.setLocation(crosshierarchy.getParent());
//			complaint.setChildLocation(crosshierarchy.getChild());
//			complaint.getChildLocation().setParent(crosshierarchy.getChild().getParent());
//		}
//		if (Objects.nonNull(request.getServiceRequest().getComplaintTypeCode())) {
//			final ComplaintType complaintType = complaintTypeService
//					.findBy(Long.valueOf(request.getServiceRequest().getComplaintTypeCode()));
//			complaint.setComplaintType(complaintType);
//		}

        complaint.setReceivingMode(ReceivingMode.MOBILE);
//		String locationText = complaint.getLocation().getLocalName() + " " + complaint.getChildLocation().getName();
//		request.getServiceRequest().getValues().add(new AttributeValue("location_text", locationText));

        return complaint;

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