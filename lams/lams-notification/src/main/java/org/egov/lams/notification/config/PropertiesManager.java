package org.egov.lams.notification.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Setter
@Getter
public class PropertiesManager {

	@Autowired
	private Environment environment;

	@Value("${kafka.topics.notification.sms.name}")
	private String smsNotificationTopic;
	
	@Value("${kafka.topics.notification.email.name}")
	private String emailNotificationTopic;
	
	@Value("${kafka.topics.notification.sms.key}")
	private String smsNotificationTopicKey;

	@Value("${lams.create.notification.msg}")
	private String createNotificationMessage;

	@Value("${lams.create.approval.msg}")
	public String createApproveMessage;

	@Value("${lams.create.rejected.msg}")
	public String createRejectMessage;
	
	@Value("${lams.evict.notification.msg}")
	private String evictNotificationMessage;

	@Value("${lams.evict.approval.msg}")
	public String evictApproveMessage;

	@Value("${lams.evict.rejected.msg}")
	public String evictRejectMessage;
	
	@Value("${lams.create.subject}")
	public String createSubject;
	
	@Value("${lams.evict.subject}")
	public String evictSubject;
	
	@Value("${lams.cancel.subject}")
	public String cancelSubject;
	
	@Value("${egov.services.asset_service.hostname}")
	 private String assetApiHostUrl;
	 
	 @Value("${egov.services.asset_service.searchpath}")
	 private String assetApiSearchPath;
	
	 @Value("${egov.services.allottee_service.hostname}")
	 private String allotteeApiHostUrl;
	 
	 @Value("${egov.services.allottee_service.searchpath}")
	 private String allotteeApiSearchPath;
	 
	 @Value("${egov.services.tenant.host}")
	 private String tenantServiceHostName;
	 
	 

}
