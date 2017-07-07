package org.egov.commons.consumers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.commons.model.AuthenticatedUser;
import org.egov.commons.service.BusinessCategoryService;
import org.egov.commons.web.contract.BusinessCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.KafkaHeaders;
@Service
@Slf4j
public class BusinessCategoryConsumer {

	public static final Logger LOGGER = LoggerFactory.getLogger(BusinessCategoryConsumer.class);
	   @Autowired
	    private ObjectMapper objectMapper;
	

	@Autowired
	BusinessCategoryService businessCategoryService;

	@KafkaListener(topics = { "egov-common-business-category-create","egov-common-business-category-update" })
	public void processMessage(Map<String, Object> consumerRecord, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		log.debug("key:" + topic + ":" + "value:" + consumerRecord);
		try {

		org.egov.commons.model.BusinessCategory modelCategory = new org.egov.commons.model.BusinessCategory(
	objectMapper.convertValue(consumerRecord.get("BusinessCategoryInfo"),
	BusinessCategory.class), getUserInfo(objectMapper.convertValue(consumerRecord.get("RequestInfo"),RequestInfo.class)));
			if (topic.equals("egov-common-business-category-create")) 
			businessCategoryService.create(modelCategory);
			else if(topic.equals("egov-common-business-category-update"))
				businessCategoryService.update(modelCategory);
			
		} catch (Exception exception) {
			log.debug("processMessage:" + exception);
			throw exception;
		}
	}

	private AuthenticatedUser getUserInfo(RequestInfo requestInfo) {
		User user = requestInfo.getUserInfo();
		return AuthenticatedUser.builder().id(user.getId()).anonymousUser(false).emailId(user.getEmailId())
				.mobileNumber(user.getMobileNumber()).name(user.getName()).build();
	}
}
