package org.egov.pgr.consumer;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.egov.common.contract.request.RequestInfo;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.pgr.contract.EmailRequest;
import org.egov.pgr.contract.RequestInfoWrapper;
import org.egov.pgr.contract.SMSRequest;
import org.egov.pgr.contract.ServiceRequest;
import org.egov.pgr.model.ActionInfo;
import org.egov.pgr.model.Service;
import org.egov.pgr.producer.PGRProducer;
import org.egov.pgr.repository.ServiceRequestRepository;
import org.egov.pgr.service.GrievanceService;
import org.egov.pgr.utils.PGRConstants;
import org.egov.pgr.utils.PGRUtils;
import org.egov.pgr.utils.WorkFlowConfigs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

@org.springframework.stereotype.Service
@Slf4j
public class PGRNotificationConsumer {
	
	@Autowired
	private PGRProducer pGRProducer;
	
	@Value("${egov.hr.employee.host}")
	private String hrEmployeeHost;
	
	@Value("${egov.hr.employee.v2.search.endpoint}")
	private String hrEmployeeV2SearchEndpoint;
	
	@Value("${kafka.topics.notification.sms}")
	private String smsNotifTopic;
	
	@Value("${kafka.topics.notification.email}")
	private String emailNotifTopic;
	
	@Value("${text.for.sms.notification}")
	private String textForNotif;
	
	@Value("${text.for.subject.email.notif}")
	private String subjectForEmail;
		
	@Value("${notification.sms.enabled}")
	private Boolean isSMSNotificationEnabled;
	
	@Value("${notification.email.enabled}")
	private Boolean isEmailNotificationEnabled;
	
	@Value("${open.complaint.enabled}")
	private Boolean isOpenComplaintNotifEnabled;
	
	@Value("${assign.complaint.enabled}")
	private Boolean isAssignNotifEnabled;
		
	@Value("${reject.complaint.enabled}")
	private Boolean isRejectedNotifEnabled;
	
	@Value("${resolve.complaint.enabled}")
	private Boolean isResolveNotificationEnabled;
	
	@Value("${close.complaint.enabled}")
	private Boolean isCloseNotifEnabled;
	
	@Value("${reassign.complaint.enabled}")
	private Boolean isReassignNotifEnaled;
	
	@Value("${reopen.complaint.enabled}")
	private Boolean isReopenNotifEnaled;
	
	@Value("${comment.by.employee.notif.enabled}")
	private Boolean isCommentByEmpNotifEnaled;
	
	@Value("${email.template.path}")
	private String emailTemplatePath;
	
	@Value("${date.format.notification}")
	private String notificationDateFormat;
	
		
		
	@Autowired
	private ServiceRequestRepository serviceRequestRepository;
		
	@Autowired
	private PGRUtils pGRUtils;
	
	@Autowired
	private GrievanceService grievanceService;
	
	private static Map<String, Map<String, String>> localizedMessageMap = new HashMap<>();
		
    @KafkaListener(topics = {"${kafka.topics.notification.complaint}"})
    
	public void listen(final HashMap<String, Object> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		ObjectMapper mapper = new ObjectMapper();
		ServiceRequest serviceReqRequest = new ServiceRequest();
		try{
			log.info("Consuming record: "+record);
			serviceReqRequest = mapper.convertValue(record, ServiceRequest.class);
		}catch(final Exception e){
			log.error("Error while listening to value: "+record+" on topic: "+topic+": "+e.getMessage());
		}
		process(serviceReqRequest);		
	}
    
    public void process(ServiceRequest serviceReqRequest) {
		if(!CollectionUtils.isEmpty(serviceReqRequest.getActionInfo())) {
			for(ActionInfo actionInfo: serviceReqRequest.getActionInfo()) {
				if(null != actionInfo && (null != actionInfo.getStatus() || null != actionInfo.getComment())) {
					Service service = serviceReqRequest.getServices().get(serviceReqRequest.getActionInfo().indexOf(actionInfo));
					if(isNotificationEnabled(actionInfo.getStatus(), serviceReqRequest.getRequestInfo().getUserInfo().getType(), actionInfo.getComment(), actionInfo.getAction())) {
		    			if(isSMSNotificationEnabled) {
		    	    		SMSRequest smsRequest = prepareSMSRequest(service, actionInfo, serviceReqRequest.getRequestInfo());
		    	    		if(null == smsRequest) {
		    	    			log.info("Messages from localization couldn't be fetched!");
		    	    			continue;
		    	    		}
		    	        	log.info("SMS: "+smsRequest.getMessage()+" | MOBILE: "+smsRequest.getMobileNumber());
			        		pGRProducer.push(smsNotifTopic, smsRequest);
		    			}
		    			//Not enabled for now - email notifications to be part of next version of PGR.
		    			if(isEmailNotificationEnabled && (null != service.getEmail() && !service.getEmail().isEmpty())) {
		    					EmailRequest emailRequest = prepareEmailRequest(service, actionInfo);
		    		        	log.info("EMAIL: "+emailRequest.getBody()+"| SUBJECT: "+emailRequest.getSubject()+"| ID: "+emailRequest.getEmail());
				        		pGRProducer.push(emailNotifTopic, emailRequest);
		    			}
		    		}else {
		    			log.info("Notification disabled for this case!");
		    			continue;
		    		}
				}else {
					log.info("No Action!");
					continue;
				}
			}
		}
    }
    
    public SMSRequest prepareSMSRequest(Service serviceReq, ActionInfo actionInfo, RequestInfo requestInfo) {
		String phone = serviceReq.getPhone();
		String message = getMessageForSMS(serviceReq, actionInfo, requestInfo);
		if(null == message)
			return null;
		return SMSRequest.builder().mobileNumber(phone).message(message).build();
    }
    
    public EmailRequest prepareEmailRequest(Service serviceReq, ActionInfo actionInfo) {
		String email = serviceReq.getEmail();
		StringBuilder subject = new StringBuilder();
		String body = getBodyAndSubForEmail(serviceReq, actionInfo, subject);
		return EmailRequest.builder().email(email).subject(subject.toString()).body(body)
				.isHTML(true).build();
    }
    
    public String getBodyAndSubForEmail(Service serviceReq, ActionInfo actionInfo, StringBuilder subject) {
    	Map<String, Object> map = new HashMap<>();
        VelocityEngine ve = new VelocityEngine();
        ve.init();
        VelocityContext context = new VelocityContext();
    	map.put("name", serviceReq.getFirstName());
    	map.put("id", serviceReq.getServiceRequestId());
		switch(actionInfo.getStatus()) {
	    case WorkFlowConfigs.STATUS_OPENED:{
        	map.put("status", "registered");
    		break;
		}case WorkFlowConfigs.STATUS_ASSIGNED:{
        	map.put("status", "assigned to Mr."+actionInfo.getAssignee());
    		break;
		}case WorkFlowConfigs.STATUS_RESOLVED:{
        	map.put("status", "resolved on "+new Date(serviceReq.getAuditDetails().getCreatedTime()).toString());
    		break;
		}case WorkFlowConfigs.STATUS_REJECTED:{
        	map.put("status", "rejected on "+new Date(serviceReq.getAuditDetails().getCreatedTime()).toString());
    		break;
		}case WorkFlowConfigs.STATUS_CLOSED:{
        	map.put("status", "resolved on "+new Date(serviceReq.getAuditDetails().getCreatedTime()).toString());
    		break;
		}default:
			break;
		}
		
        context.put("params", map);
        Template t = ve.getTemplate(emailTemplatePath);
        StringWriter writer = new StringWriter();
        t.merge(context, writer);
    	String message = writer.toString();
    	String subjectText = subjectForEmail;
    	subject.append(subjectText.replace("<id>", serviceReq.getServiceRequestId()));
    	
    	return message;    	
    }
    
    public String getMessageForSMS(Service serviceReq, ActionInfo actionInfo, RequestInfo requestInfo) {
    	SimpleDateFormat dateFormat = new SimpleDateFormat(notificationDateFormat);
    	String date = dateFormat.format(new Date());
    	String tenantId = serviceReq.getTenantId().split("[.]")[0]; //localization values are for now state-level.
    	String locale = null;
    	try {
    		locale = requestInfo.getMsgId().split(",")[1]; //Conventionally locale is sent in the first index of msgid.
    	}catch(Exception e) {
    		log.info("No locale found in the request, falling back to default locale: en_IN");
    		locale = "en_IN";
    	}
    	if(StringUtils.isEmpty(locale)) {
    		log.info("No locale found in the request, falling back to default locale: en_IN");
    		locale = "en_IN";
    	}
    	if(null == localizedMessageMap.get(locale+"|"+tenantId)) //static map that saves code-message pair against locale | tenantId.
    		getLocalisedMessages(requestInfo, tenantId, locale, PGRConstants.LOCALIZATION_MODULE_NAME);
    	Map<String, String> messageMap = localizedMessageMap.get(locale+"|"+tenantId);
    	if(null == messageMap)
    		return null;
    	String message = null;
    	String serviceType = getServiceType(serviceReq, requestInfo);
		if(StringUtils.isEmpty(actionInfo.getStatus())) {
			if(!StringUtils.isEmpty(actionInfo.getComment())) {
				message = prepareMsgTextOnComment(serviceType, serviceReq, actionInfo, requestInfo, messageMap);
			}
		}
		else {
			switch(actionInfo.getStatus()) {
			case WorkFlowConfigs.STATUS_OPENED:{
				message = prepareMsgTextOnSubmission(serviceType, serviceReq, actionInfo, date, messageMap);
				break;
			}case WorkFlowConfigs.STATUS_ASSIGNED:{
				message = prepareMsgTextOnAssignment(serviceType, serviceReq, actionInfo, requestInfo, messageMap);
				break;
			}case WorkFlowConfigs.STATUS_REJECTED:{
				message = prepareMsgTextOnRejection(serviceType, serviceReq, date, messageMap);
				break;
			}case WorkFlowConfigs.STATUS_RESOLVED:{
				message = prepareMsgTextOnResolution(serviceType, date, messageMap);
	    		break;
			}default:
				break;
			}
		}
    	return message;
    	
    }
    
    public String prepareMsgTextOnSubmission(String serviceType, Service serviceReq, ActionInfo actionInfo, String date, Map<String, String> messageMap) {
    	String text = null;
		if(null == serviceType) {
			text = messageMap.get(PGRConstants.LOCALIZATION_CODE_DEFAULT);
			if(null != actionInfo.getAction() && actionInfo.getAction().equals(WorkFlowConfigs.ACTION_REOPEN))
				text = text.replaceAll(PGRConstants.SMS_NOTIFICATION_STATUS_KEY, "reopened");
			else
				text = text.replaceAll(PGRConstants.SMS_NOTIFICATION_STATUS_KEY, "submitted");
			
			return text;
		}
		if(null != actionInfo.getAction() && actionInfo.getAction().equals(WorkFlowConfigs.ACTION_REOPEN)) {
			text = messageMap.get(PGRConstants.LOCALIZATION_CODE_REOPEN);
			text = text.replace(PGRConstants.SMS_NOTIFICATION_COMPLAINT_TYPE_KEY, serviceType).replace(PGRConstants.SMS_NOTIFICATION_DATE_KEY, date);
		}else {
			text = messageMap.get(PGRConstants.LOCALIZATION_CODE_SUBMIT);
			text = text.replace(PGRConstants.SMS_NOTIFICATION_COMPLAINT_TYPE_KEY, serviceType)
					.replace(PGRConstants.SMS_NOTIFICATION_ID_KEY, serviceReq.getServiceRequestId()).replace(PGRConstants.SMS_NOTIFICATION_DATE_KEY, date);
		}
		return text;
  
	}
    
    public String prepareMsgTextOnAssignment(String serviceType, Service serviceReq, ActionInfo actionInfo, RequestInfo requestInfo, Map<String, String> messageMap) {
    	String text = null; 
    	Boolean fallingToDefaultCase = false;
    	Map<String, String> employeeDetails = getEmployeeDetails(serviceReq.getTenantId(), actionInfo.getAssignee(), requestInfo);
    	if(null == employeeDetails) {
    		fallingToDefaultCase = true;
    	}else {
    		if(null == serviceType || null == employeeDetails.get("name") || 
    					null == employeeDetails.get("department") || null == employeeDetails.get("designation"))
        		fallingToDefaultCase = true;
    	}
    	if(fallingToDefaultCase) {
			text = messageMap.get(PGRConstants.LOCALIZATION_CODE_DEFAULT);
			if(null != actionInfo.getAction() && actionInfo.getAction().equals(WorkFlowConfigs.ACTION_REASSIGN))
				text = text.replaceAll(PGRConstants.SMS_NOTIFICATION_STATUS_KEY, "re-assigned");
			else
				text = text.replaceAll(PGRConstants.SMS_NOTIFICATION_STATUS_KEY, "assigned");
			
			return text;
    	}
		String department = getDepartment(serviceReq, employeeDetails.get("department"), requestInfo);
		String designation = getDesignation(serviceReq, employeeDetails.get("designation"), requestInfo);
		if((null != department && !department.isEmpty()) && (null != designation && !designation.isEmpty())) {
			if(null != actionInfo.getAction() && actionInfo.getAction().equals(WorkFlowConfigs.ACTION_REASSIGN))
				text = messageMap.get(PGRConstants.LOCALIZATION_CODE_ASSIGN);
			else
				text = messageMap.get(PGRConstants.LOCALIZATION_CODE_REASSIGN);
			text = text.replace(PGRConstants.SMS_NOTIFICATION_COMPLAINT_TYPE_KEY, serviceType).replace(PGRConstants.SMS_NOTIFICATION_EMP_NAME_KEY, employeeDetails.get("name"))
	    						.replace(PGRConstants.SMS_NOTIFICATION_EMP_DESIGNATION_KEY, designation).replace(PGRConstants.SMS_NOTIFICATION_EMP_DEPT_KEY, department);
		}		
		return text;
	}
    
    public String prepareMsgTextOnRejection(String serviceType, Service serviceReq, String date, Map<String, String> messageMap) {
		String text = null;
    	if(null == serviceType) {
			text = messageMap.get(PGRConstants.LOCALIZATION_CODE_DEFAULT);
    		text = text.replaceAll(PGRConstants.SMS_NOTIFICATION_STATUS_KEY, "rejected");
    		return text;
		}
		String[] desc = null;
		if(null != serviceReq.getDescription()) {
			desc = serviceReq.getDescription().split("[|]");
			if(desc.length < 2) {
				text = messageMap.get(PGRConstants.LOCALIZATION_CODE_DEFAULT);
				text = text.replaceAll(PGRConstants.SMS_NOTIFICATION_STATUS_KEY, "rejected");
	    		return text;
			}	
		}
		text = messageMap.get(PGRConstants.LOCALIZATION_CODE_REJECT);
		text = text.replace(PGRConstants.SMS_NOTIFICATION_COMPLAINT_TYPE_KEY, serviceType).replace(PGRConstants.SMS_NOTIFICATION_DATE_KEY, date)
    				.replace(PGRConstants.SMS_NOTIFICATION_REASON_FOR_REOPEN_KEY, desc[0]).replace(PGRConstants.SMS_NOTIFICATION_ADDITIONAL_COMMENT_KEY, desc[1]);
		return text;
	}
    
    public String prepareMsgTextOnResolution(String serviceType, String date, Map<String, String> messageMap) {
    	String text = null;
		if(null == serviceType) {
			text = messageMap.get(PGRConstants.LOCALIZATION_CODE_DEFAULT);
			text = text.replaceAll(PGRConstants.SMS_NOTIFICATION_STATUS_KEY, "resolved");
			return text;
		}
		text = messageMap.get(PGRConstants.LOCALIZATION_CODE_RESOLVE);
		text = text.replace(PGRConstants.SMS_NOTIFICATION_COMPLAINT_TYPE_KEY, serviceType)
						.replace(PGRConstants.SMS_NOTIFICATION_DATE_KEY, date).replace(PGRConstants.SMS_NOTIFICATION_APP_LINK_KEY, PGRConstants.WEB_APP_FEEDBACK_PAGE_LINK);
		return text;
	}
    
    
    public String prepareMsgTextOnComment(String serviceType, Service serviceReq, ActionInfo actionInfo, RequestInfo requestInfo, Map<String, String> messageMap) {
    	String text = null;
    	Map<String, String> employeeDetails = getEmployeeDetails(serviceReq.getTenantId(), actionInfo.getAssignee(), requestInfo);
		if(null == serviceType || null == employeeDetails.get("name")) {			
			return messageMap.get(PGRConstants.LOCALIZATION_CODE_COMMENT_DEFAULT);
		}
		text = messageMap.get(PGRConstants.LOCALIZATION_CODE_COMMENT);
		text = text.replace(PGRConstants.SMS_NOTIFICATION_COMPLAINT_TYPE_KEY, serviceType).replace(PGRConstants.SMS_NOTIFICATION_EMP_NAME_KEY, employeeDetails.get("name"))
							.replace(PGRConstants.SMS_NOTIFICATION_ID_KEY, serviceReq.getServiceRequestId()).replace(PGRConstants.SMS_NOTIFICATION_COMMENT_KEY, actionInfo.getComment());		
		return text;
	}
    
    public String getServiceType(Service serviceReq, RequestInfo requestInfo) {
		StringBuilder uri = new StringBuilder();
		MdmsCriteriaReq mdmsCriteriaReq = pGRUtils.prepareSearchRequestForServiceType(uri, serviceReq.getTenantId(),
				serviceReq.getServiceCode(), requestInfo);
		List<String> serviceTypes = null;
		try {
			Object result = serviceRequestRepository.fetchResult(uri, mdmsCriteriaReq);
			log.info("service definition name result: "+result);
			serviceTypes = JsonPath.read(result, PGRConstants.JSONPATH_SERVICE_CODES);
			if(null == serviceTypes || serviceTypes.isEmpty())
				return null;
		}catch(Exception e) {
			return null;
		}
		
    	return serviceTypes.get(0);
    }
    
	public Map<String, String> getEmployeeDetails(String tenantId, String id, RequestInfo requestInfo) {
		StringBuilder uri = new StringBuilder();
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		uri.append(hrEmployeeHost).append(hrEmployeeV2SearchEndpoint).append("?id="+id).append("&tenantId="+tenantId);
		Object response = null;
		log.debug("Employee: " + response);
		Map<String, String> employeeDetails = new HashMap<>();
		try {
			response = serviceRequestRepository.fetchResult(uri, requestInfoWrapper);
			if (null == response) {
				return null;
			}
			log.debug("Employee: " + response);
			employeeDetails.put("name", JsonPath.read(response, PGRConstants.EMPLOYEE_NAME_JSONPATH));
			employeeDetails.put("department", JsonPath.read(response, PGRConstants.EMPLOYEE_DEPTCODE_JSONPATH));
			employeeDetails.put("designation", JsonPath.read(response, PGRConstants.EMPLOYEE_DESGCODE_JSONPATH));

		} catch (Exception e) {
			log.error("Exception: " + e);
			return null;
		}
		return employeeDetails;
	}
    
    public String getDepartment(Service serviceReq, String code, RequestInfo requestInfo) {
		StringBuilder uri = new StringBuilder();
		MdmsCriteriaReq mdmsCriteriaReq = pGRUtils.prepareMdMsRequestForDept(uri, serviceReq.getTenantId(), code, requestInfo);
		List<String> departmemts = null;
		try {
			Object result = serviceRequestRepository.fetchResult(uri, mdmsCriteriaReq);
			log.info("Dept result: "+result);
			departmemts = JsonPath.read(result, PGRConstants.JSONPATH_DEPARTMENTS);
			if(null == departmemts || departmemts.isEmpty())
				return null;
		}catch(Exception e) {
			return null;
		}
		
    	return departmemts.get(0);
    }
    
    public String getDesignation(Service serviceReq, String code, RequestInfo requestInfo) {
		StringBuilder uri = new StringBuilder();
		MdmsCriteriaReq mdmsCriteriaReq = pGRUtils.prepareMdMsRequestForDesignation(uri, serviceReq.getTenantId(), code, requestInfo);
		List<String> designations = null;
		try {
			Object result = serviceRequestRepository.fetchResult(uri, mdmsCriteriaReq);
			log.info("Designation result: "+result);
			designations = JsonPath.read(result, PGRConstants.JSONPATH_DESIGNATIONS);
			if(null == designations || designations.isEmpty())
				return null;
		}catch(Exception e) {
			return null;
		}
		
    	return designations.get(0);
    }
    
    public void getLocalisedMessages(RequestInfo requestInfo, String tenantId, String locale, String module){
    	Map<String, String> mapOfCodesAndMessages = new HashMap<>();
    	StringBuilder uri = new StringBuilder();
    	RequestInfoWrapper requestInfoWrapper = pGRUtils.prepareRequestForLocalization(uri, requestInfo, locale, tenantId, module);
    	List<String> codes = null;
    	List<String> messages = null;
    	Object result = null;
		try {
			result = serviceRequestRepository.fetchResult(uri, requestInfoWrapper);
			log.info("localization result: "+result);
		    codes = JsonPath.read(result, "$.messages.*.code");
			messages = JsonPath.read(result, "$.messages.*.message");
		}catch(Exception e) {
			log.error("Exception while fetching from localization: " + e);
		}
		if(null != result) {
			for(int i = 0; i < codes.size(); i++) {
				mapOfCodesAndMessages.put(codes.get(i), messages.get(i));
			}
		localizedMessageMap.put(locale+"|"+tenantId, mapOfCodesAndMessages);
		}
    }
    
    public boolean isNotificationEnabled(String status, String userType, String comment, String action) {
    	boolean isNotifEnabled = false;
    	if(!StringUtils.isEmpty(status)) {
			switch(status) {
			case WorkFlowConfigs.STATUS_OPENED:{
				if(action.equals(WorkFlowConfigs.ACTION_REOPEN)) {
					if(isReopenNotifEnaled) {
						isNotifEnabled = true;
					}
				}else {
					if(isOpenComplaintNotifEnabled) {
						isNotifEnabled = true;
					} 
				}
				break;
			}case WorkFlowConfigs.STATUS_ASSIGNED:{
				if(action.equals(WorkFlowConfigs.ACTION_REASSIGN)) {
					if(isReassignNotifEnaled) {
						isNotifEnabled = true;
					}
				}else {
					if(isAssignNotifEnabled) {
						isNotifEnabled = true;
					}
				}
				break;
			}case WorkFlowConfigs.STATUS_REJECTED:{
				if(isRejectedNotifEnabled) {
					isNotifEnabled = true;
				}
				break;
			}case WorkFlowConfigs.STATUS_CLOSED:{
				if(isCloseNotifEnabled) {
					isNotifEnabled = true;
				}
				break;
			}case WorkFlowConfigs.STATUS_RESOLVED:{
				if(isResolveNotificationEnabled) {
					isNotifEnabled = true;
				}
				break;
			}default:
				break;
			}
    	}
				
    	if((null != comment && !comment.isEmpty()) && isCommentByEmpNotifEnaled && userType.equalsIgnoreCase("EMPLOYEE")) {
			isNotifEnabled = true;
    	}
		
		return isNotifEnabled;
    }
}
