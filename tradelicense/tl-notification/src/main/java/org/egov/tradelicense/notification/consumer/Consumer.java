package org.egov.tradelicense.notification.consumer;

import java.util.Map;

import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.contract.TradeLicenseIndexerContract;
import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tl.commons.web.requests.TradeLicenseIndexerRequest;
import org.egov.tl.commons.web.requests.TradeLicenseRequest;
import org.egov.tl.commons.web.response.LicenseStatusResponse;
import org.egov.tradelicense.notification.config.PropertiesManager;
import org.egov.tradelicense.notification.enums.NewLicenseStatus;
import org.egov.tradelicense.notification.service.NotificationService;
import org.egov.tradelicense.notification.web.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * This is Consumer class
 * 
 * @author Shubham
 *
 */
@Service
@Slf4j
public class Consumer {

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	NotificationService notificationService;

	@Autowired
	KafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	StatusRepository statusRepository;

	@Autowired
	ObjectMapper objectMapper;

	/**
	 * This is receive method for consuming record from Kafka server
	 * 
	 * @param consumerRecord
	 */

	@KafkaListener(topics = { "#{propertiesManager.getTradeLicenseValidatedTopic()}",
			"#{propertiesManager.getTradeLicensePersistedTopic()}" })
	public void receive(Map<String, Object> mastersMap) {

		if (mastersMap.get("tradelicense-new-create") != null) {

			TradeLicenseRequest request = objectMapper.convertValue(mastersMap.get("tradelicense-new-create"),
					TradeLicenseRequest.class);

			notificationService.licenseNewCreationAcknowledgement(request);
		}
		if (mastersMap.get("tradelicense-new-update") != null) {

			TradeLicenseRequest request = objectMapper.convertValue(mastersMap.get("tradelicense-new-update"),
					TradeLicenseRequest.class);

			notificationService.processNewLicenseUpdateAcknowledgement(request);
		}
		if (mastersMap.get("tradelicense-persisted") != null) {

			TradeLicenseIndexerRequest request = objectMapper.convertValue(mastersMap.get("tradelicense-persisted"),
					TradeLicenseIndexerRequest.class);

			RequestInfo requestInfo = request.getRequestInfo();
			RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
			requestInfoWrapper.setRequestInfo(requestInfo);

			for (TradeLicenseIndexerContract tradeLicenseIndexerContract : request.getLicenses()) {

				if (tradeLicenseIndexerContract.getApplications() != null
						&& tradeLicenseIndexerContract.getApplications().size() > 0) {

					String applicationStatus = tradeLicenseIndexerContract.getApplications().get(0).getStatus();
					LicenseStatusResponse currentStatus = null;

					if (applicationStatus != null && !applicationStatus.isEmpty()) {

						currentStatus = statusRepository.findByCodes(tradeLicenseIndexerContract.getTenantId(),
								applicationStatus, requestInfoWrapper);
					}

					if (null != currentStatus && !currentStatus.getLicenseStatuses().isEmpty()) {

						if (currentStatus.getLicenseStatuses().size() > 0) {

							String statusCode = currentStatus.getLicenseStatuses().get(0).getCode();

							if (statusCode != null
									&& statusCode.equalsIgnoreCase(NewLicenseStatus.LICENSE_FEE_PAID.getName())) {

								notificationService.licenseFeePaidAcknowledgement(tradeLicenseIndexerContract,
										requestInfo);

							}
						}
					}
				}
			}
		}
	}
}
