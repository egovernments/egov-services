package org.egov.domain.service;

import org.egov.domain.model.SevaRequest;
import org.egov.persistence.queue.MessageQueueRepository;
import org.springframework.stereotype.Service;
import org.trimou.util.ImmutableMap;

import java.util.Map;

@Service
public class SMSService {
	private static final String SMS_ENGLISH = "sms_en";
	private static final String NAME = "name";
	private static final String NUMBER = "number";
	private TemplateService templateService;
	private MessageQueueRepository messageQueueRepository;

	public SMSService(TemplateService templateService,
					  MessageQueueRepository messageQueueRepository) {
		this.templateService = templateService;
		this.messageQueueRepository = messageQueueRepository;
	}

	public void send(SevaRequest sevaRequest) {
        final String smsMessage = getSMSMessage(sevaRequest);
        final String mobileNumber = sevaRequest.getMobileNumber();
        messageQueueRepository.sendSMS(mobileNumber, smsMessage);
    }

    private String getSMSMessage(SevaRequest sevaRequest) {
        final Map<Object, Object> map = ImmutableMap.of(
			NAME, sevaRequest.getComplaintTypeName(),
			NUMBER, sevaRequest.getCrn()
		);
        return templateService.loadByName(SMS_ENGLISH, map);
    }
}
