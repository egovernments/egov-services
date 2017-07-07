package org.egov.commons.consumers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.commons.model.AuthenticatedUser;
import org.egov.commons.model.BusinessAccountSubLedgerDetails;
import org.egov.commons.model.BusinessDetails;
import org.egov.commons.service.BusinessCategoryService;
import org.egov.commons.service.BusinessDetailsService;
import org.egov.commons.web.contract.BusinessAccountDetails;
import org.egov.commons.web.contract.BusinessAccountSubLedger;
import org.egov.commons.web.contract.BusinessDetailsRequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BusinessDetailsConsumer {

	public static final Logger LOGGER = LoggerFactory.getLogger(BusinessDetailsConsumer.class);
	   @Autowired
	    private ObjectMapper objectMapper;
	   @Autowired
		BusinessCategoryService businessCategoryService;
	

	@Autowired
	BusinessDetailsService businessDetailsService;

	@KafkaListener(topics = { "egov-common-business-details-create","egov-common-business-details-update" })
	public void processMessage(Map<String, Object> consumerRecord, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		log.debug("key:" + topic + ":" + "value:" + consumerRecord);
		try {
		
	        BusinessDetailsRequestInfo detailsInfo=objectMapper.convertValue(consumerRecord.get("BusinessDetailsInfo"),BusinessDetailsRequestInfo.class);
	        org.egov.commons.model.BusinessCategory modelCategory = businessCategoryService.getBusinessCategoryByIdAndTenantId
	        		(detailsInfo.getBusinessCategory(), detailsInfo.getTenantId());
	        BusinessDetails modelDetails = new BusinessDetails(detailsInfo, modelCategory,getUserInfo(objectMapper.convertValue
	        		(consumerRecord.get("RequestInfo"),RequestInfo.class)));
	    	List<BusinessAccountDetails> listContractAccountDetails = detailsInfo.getAccountDetails();
			List<org.egov.commons.model.BusinessAccountDetails> listModelAccountDetails = new ArrayList<>();
			List<BusinessAccountSubLedger> contractListOfSubledgers = detailsInfo.getSubledgerDetails();
			List<BusinessAccountSubLedgerDetails> listModelAccountSubledger = new ArrayList<>();
			if (topic.equals("egov-common-business-details-create")) {
				for (BusinessAccountDetails details : listContractAccountDetails) {
					listModelAccountDetails
							.add(new org.egov.commons.model.BusinessAccountDetails(details, modelDetails, false));
				}
				for (BusinessAccountSubLedger subledger : contractListOfSubledgers) {
					listModelAccountSubledger.add(new BusinessAccountSubLedgerDetails(subledger, modelDetails, false));
				}
			businessDetailsService.createBusinessDetails(modelDetails,listModelAccountDetails,listModelAccountSubledger);
			}
			else if(topic.equals("egov-common-business-details-update")){
				for (BusinessAccountDetails details : listContractAccountDetails) {
					listModelAccountDetails.add(new org.egov.commons.model.BusinessAccountDetails(details, modelDetails, true));
				}
				for (BusinessAccountSubLedger subledger : contractListOfSubledgers) {
					listModelAccountSubledger.add(new BusinessAccountSubLedgerDetails(subledger, modelDetails, true));
				}
				businessDetailsService.updateBusinessDetails(modelDetails, listModelAccountDetails, listModelAccountSubledger);
			}
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

