package org.egov.collection.notification.consumer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.collection.model.Instrument;
import org.egov.collection.producer.CollectionProducer;
import org.egov.collection.web.contract.Bill;
import org.egov.collection.web.contract.BillDetail;
import org.egov.collection.web.contract.Receipt;
import org.egov.collection.web.contract.ReceiptReq;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;

@Service
@Slf4j
@Data
public class NotificationConsumer {
	
	@Value("${coll.notification.ui.host}")
	private String uiHost;
	
	@Value("${coll.notification.ui.redirect.url}")
	private String uiRedirectUrl;
	
	@Value("${egov.localization.host}")
	private String localizationHost;
	
	@Value("${egov.localization.search.endpoint}")
	private String localizationEndpoint;
	
	@Value("${coll.notification.fallback.locale}")
	private String fallBackLocale;
	
	@Value("${kafka.topics.notification.sms}")
	private String smsTopic;
	
	@Value("${kafka.topics.notification.sms.key}")
	private String smsTopickey;
	
    @Autowired
    private ObjectMapper objectMapper;
		
	@Autowired
	private CollectionProducer producer;
	
	@Autowired
	private RestTemplate restTemplate;
	
	private static final String COLLECTION_LOCALIZATION_MODULE = "collection-services";
	public static final String PAYMENT_MSG_LOCALIZATION_CODE = "coll.notif.payment.receipt.link";

	private static final String BUSINESSSERVICE_LOCALIZATION_MODULE = "rainmaker-uc";
	public static final String BUSINESSSERVICELOCALIZATION_CODE_PREFIX = "BILLINGSERVICE_BUSINESSSERVICE_";

	
	public static final String LOCALIZATION_CODES_JSONPATH = "$.messages.*.code";
	public static final String LOCALIZATION_MSGS_JSONPATH = "$.messages.*.message";

	
    @KafkaListener(topics = { "${kafka.topics.payment.receiptlink.name}" })
	public void listen(HashMap<String, Object> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		try {
			ReceiptReq req = objectMapper.convertValue(record, ReceiptReq.class);
			sendNotification(req);
		}catch(Exception e) {
			log.error("Exception while reading from the queue: ", e);
		}
	}
	
	
	private void sendNotification(ReceiptReq receiptReq) {
		Receipt receipt = receiptReq.getReceipt().get(0); //currently we dont support mutliple receipts.
		for(Bill bill: receipt.getBill()) {
			for(BillDetail detail: bill.getBillDetails()) {
				String phNo = bill.getMobileNumber();
				String message = buildSmsBody(receipt.getInstrument(), bill, detail, receiptReq.getRequestInfo());
				if(!StringUtils.isEmpty(message)) {
					Map<String, Object> request = new HashMap<>();
					request.put("mobileNumber", phNo);
					request.put("message", message);
					
					producer.producer(smsTopic, smsTopickey, request);
				}else {
					log.error("No message configured!");
				}
			}
		}
		
	}
	
	private String buildSmsBody(Instrument instrument, Bill bill, BillDetail billDetail, RequestInfo requestInfo) {
		String content = fetchContentFromLocalization(requestInfo, billDetail.getTenantId(), COLLECTION_LOCALIZATION_MODULE, PAYMENT_MSG_LOCALIZATION_CODE);
		String message = null;
		if(!StringUtils.isEmpty(content)) {
			StringBuilder link = new StringBuilder();
			link.append(uiHost).append("/otpLogin?mobileNo=").append(bill.getMobileNumber()).append("&redirectTo=")
					.append(uiRedirectUrl).append("&params=").append(billDetail.getTenantId() + "," + billDetail.getReceiptNumber());
			
			content = content.replaceAll("<rcpt_link>", link.toString());
			String taxName = fetchContentFromLocalization(requestInfo, billDetail.getTenantId(), BUSINESSSERVICE_LOCALIZATION_MODULE, 
												BUSINESSSERVICELOCALIZATION_CODE_PREFIX + billDetail.getBusinessService());
			if(StringUtils.isEmpty(taxName))
				taxName = "Adhoc Tax";
			content = content.replaceAll("<tax_name>", taxName);
			content = content.replaceAll("<fin_year>", fetchFinYear(billDetail.getFromPeriod(), billDetail.getToPeriod()));
			content = content.replaceAll("<rcpt_no>", billDetail.getReceiptNumber());
			content = content.replaceAll("<amount_paid>", instrument.getAmount().toString());
			
			message = content;
		}
		return message;
	}
	
	private String fetchContentFromLocalization(RequestInfo requestInfo, String tenantId, String module, String code) {
		String message = null;
		List<String> codes = new ArrayList<>();
		List<String> messages = new ArrayList<>();
		Object result = null;
		String locale = requestInfo.getMsgId().split("|")[1]; // Conventionally locale is sent in the first index of msgid split by |
		if(StringUtils.isEmpty(locale))
			locale = fallBackLocale;
		StringBuilder uri = new StringBuilder();
		uri.append(localizationHost).append(localizationEndpoint);
		uri.append("?tenantId=").append(tenantId.split("\\.")[0]).append("&locale=").append(locale).append("&module=").append(module);
		Map<String, Object> request = new HashMap<>();
		request.put("RequestInfo", requestInfo);
		try {
			result = restTemplate.postForObject(uri.toString(), request, Map.class);
			codes = JsonPath.read(result, LOCALIZATION_CODES_JSONPATH);
			messages = JsonPath.read(result, LOCALIZATION_MSGS_JSONPATH);
		} catch (Exception e) {
			log.error("Exception while fetching from localization: " + e);
		}
		if (null != result) {
			for (int i = 0; i < codes.size(); i++) {
				if(codes.get(i).equals(code)) message = messages.get(i);
			}
		}
		return message;
	}
	
	
	private String fetchFinYear(Long fromPeriod, Long toPeriod) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(fromPeriod);
		int fromYear = Calendar.YEAR;
		calendar.setTimeInMillis(toPeriod);
		int toYear = Calendar.YEAR;
		
		return fromYear + "-" + (toYear % 1000);

	}
	

}
