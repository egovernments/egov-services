package org.egov.pgr.consumer;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.egov.common.contract.request.RequestInfo;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.pgr.contract.EmailRequest;
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
	
	@Value("${sms.notif.text.on.submission}")
	private String smsTextOnSubmission;
		
	@Value("${sms.notif.text.on.assignment}")
	private String smsTextOnAssignment;
	
	@Value("${sms.notif.text.on.reassignment}")
	private String smsTextOnReassignment;
	
	@Value("${sms.notif.text.on.resolution}")
	private String smsTextOnResolution;
	
	@Value("${sms.notif.text.on.rejection}")
	private String smsTextOnRejection;
		
	@Value("${sms.notif.text.on.reopen}")
	private String smsTextOnReopen;
	
	@Value("${sms.notif.default.text}")
	private String smsDefaultText;
	
	@Value("${sms.notif.text.on.comment}")
	private String smsTextOnComment;
	
	@Value("${sms.notif.default.text.on.comment}")
	private String smsDefaultTextOnComment;
		
		
	@Autowired
	private ServiceRequestRepository serviceRequestRepository;
		
	@Autowired
	private PGRUtils pGRUtils;
	
	@Autowired
	private GrievanceService grievanceService;
		
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
					if(isNotificationEnabled(actionInfo.getStatus(), serviceReqRequest.getRequestInfo().getUserInfo().getType(), actionInfo.getAction())) {
		    			if(isSMSNotificationEnabled) {
		    	    		SMSRequest smsRequest = prepareSMSRequest(service, actionInfo, serviceReqRequest.getRequestInfo());
		    	        	log.info("SMS: "+smsRequest.getMessage()+" | MOBILE: "+smsRequest.getMobileNumber());
			        		pGRProducer.push(smsNotifTopic, smsRequest);
		    			}
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
    	String message = null;
    	String serviceType = getServiceType(serviceReq, requestInfo);
		if(StringUtils.isEmpty(actionInfo.getStatus())) {
			if(!StringUtils.isEmpty(actionInfo.getComment())) {
				message = prepareMsgTextOnComment(serviceType, serviceReq, actionInfo, requestInfo);
			}
		}
		else {
			switch(actionInfo.getStatus()) {
			case WorkFlowConfigs.STATUS_OPENED:{
				message = prepareMsgTextOnSubmission(serviceType, serviceReq, actionInfo, date);
				break;
			}case WorkFlowConfigs.STATUS_ASSIGNED:{
				message = prepareMsgTextOnAssignment(serviceType, serviceReq, actionInfo, date, requestInfo);
				break;
			}case WorkFlowConfigs.STATUS_REJECTED:{
				message = prepareMsgTextOnRejection(serviceType, serviceReq, date);
				break;
			}case WorkFlowConfigs.STATUS_RESOLVED:{
				message = prepareMsgTextOnResolution(serviceType, serviceReq, date);
	    		break;
			}default:
				break;
			}
		}
    	return message;
    	
    }
    
    public String prepareMsgTextOnSubmission(String serviceType, Service serviceReq, ActionInfo actionInfo, String date) {
    	String text = null;
		if(null == serviceType) {
			text = smsDefaultText;
			if(null != actionInfo.getAction() && actionInfo.getAction().equals(WorkFlowConfigs.ACTION_REOPEN))
				text = text.replaceAll("<status>", "reopened");
			else
				text = text.replaceAll("<status>", "submitted");
			
			return text;
		}
		if(null != actionInfo.getAction() && actionInfo.getAction().equals(WorkFlowConfigs.ACTION_REOPEN)) {
			text = smsTextOnReopen;
			text = text.replace("<complaint type>", serviceType).replace("<date>", date);
		}else {
			text = smsTextOnSubmission;
			text = text.replace("<complaint type>", serviceType)
					.replace("<id>", serviceReq.getServiceRequestId()).replace("<date>", date);
		}
		return text;
  
	}
    
    public String prepareMsgTextOnAssignment(String serviceType, Service serviceReq, ActionInfo actionInfo, String date, RequestInfo requestInfo) {
    	String text = null;
		String employee = grievanceService.getEmployeeName(serviceReq.getTenantId(), actionInfo.getAssignee(), requestInfo);;
		String department = getDepartment(serviceReq, "code", requestInfo);
		String designation = getDesignation(serviceReq, "code", requestInfo);
		if(null == serviceType || null == department || null == employee || null == designation) {
			text = smsDefaultText;
			if(null != actionInfo.getAction() && actionInfo.getAction().equals(WorkFlowConfigs.ACTION_REASSIGN))
				text = text.replaceAll("<status>", "re-assigned");
			else
				text = text.replaceAll("<status>", "assigned");
			
			return text;
			
		}
		if(null != actionInfo.getAction() && actionInfo.getAction().equals(WorkFlowConfigs.ACTION_REASSIGN))
			text = smsTextOnAssignment;
		else
			text = smsTextOnReassignment;
		
		text = text.replace("<complaint_type>", serviceType).replace("<emp_name>", employee)
    						.replace("<emp_designation>", designation).replace("<emp_department>", department);
		
		return text;
	}
    
    public String prepareMsgTextOnRejection(String serviceType, Service serviceReq, String date) {
		String text = null;
    	if(null == serviceType) {
    		text = smsDefaultText;
    		text = text.replaceAll("<status>", "rejected");
    		return text;
		}
		String[] desc = null;
		if(null != serviceReq.getDescription()) {
			desc = serviceReq.getDescription().split("|");
			if(desc.length < 2) {
				text = smsDefaultText;
				text = text.replaceAll("<status>", "rejected");
	    		return text;
			}	
		}
		text = smsTextOnRejection;
		text = text.replace("<complaint_type>", serviceType).replace("<date>", date)
    				.replace("<reason>", desc[0]).replace("<additional_comments> ", desc[1]);
		return text;
	}
    
    public String prepareMsgTextOnResolution(String serviceType, Service serviceReq, String date) {
    	String text = null;
		if(null == serviceType) {
			text = smsDefaultText;
			text = text.replaceAll("<status>", "resolved");
			return text;
		}
		text = smsTextOnResolution;
		text = text.replace("<complaint_type>", serviceType).replace("<date>", date).replace("<app_link>", PGRConstants.WEB_APP_FEEDBACK_PAGE_LINK);
		return text;
	}
    
    
    public String prepareMsgTextOnComment(String serviceType, Service serviceReq, ActionInfo actionInfo, RequestInfo requestInfo) {
    	String text = null;
		String employee = grievanceService.getEmployeeName(serviceReq.getTenantId(), actionInfo.getAssignee(), requestInfo);;
		if(null == serviceType || null == employee) {			
			return smsDefaultTextOnComment;
		}
		text = smsTextOnComment;
		text = text.replace("<complaint_type>", serviceType).replace("<user_name>", employee)
							.replace("<id>", serviceReq.getServiceRequestId()).replace("<comment>", actionInfo.getComment());		
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
    
    public boolean isNotificationEnabled(String status, String comment, String action) {
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
				
    	if((null != comment && !comment.isEmpty()) && isCommentByEmpNotifEnaled) {
			isNotifEnabled = true;
    	}
		
		return isNotifEnabled;
    }
}
