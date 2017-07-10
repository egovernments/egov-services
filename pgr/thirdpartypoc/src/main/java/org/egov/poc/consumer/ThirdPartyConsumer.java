package org.egov.poc.consumer;

import org.egov.poc.contract.SevaRequest;
import org.egov.poc.contract.ThirdPartyRecord;
import org.egov.poc.repository.ThirdPartyRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class ThirdPartyConsumer {

    private ThirdPartyRepository thirdPartyRepository;

    public ThirdPartyConsumer(ThirdPartyRepository thirdPartyRepository) {
        this.thirdPartyRepository = thirdPartyRepository;
    }

    @KafkaListener(id = "${kafka.topics.poc.external.id}",
            topics = {"${kafka.topics.poc.external.name}"},
            group = "${spring.kafka.consumer.group-id}")
    public void process(HashMap<String, Object> sevaRequestMap) {
        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        ThirdPartyRecord record = ThirdPartyRecord.builder()
                .crn(sevaRequest.getCrn())
                .serviceCode(sevaRequest.getServiceCode())
                .status(sevaRequest.getStatus())
                .tenantId(sevaRequest.getTenantId())
                .url("http://tpt/foo")
                .build();

        record.processStatus();

        thirdPartyRepository.save(record);
    }
}
