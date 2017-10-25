package org.egov.inv.domain.repository;

import org.egov.inv.web.contract.MaterialRequest;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MaterialRepository {

    private LogAwareKafkaTemplate<String, Object> logAwareKafkaTemplate;

    private String materialsSaveTopicName;

    @Autowired
    public MaterialRepository(LogAwareKafkaTemplate logAwareKafkaTemplate,
                              @Value("${inv.materials.save.topic}") String materialsSaveTopicName) {
        this.logAwareKafkaTemplate = logAwareKafkaTemplate;
        this.materialsSaveTopicName = materialsSaveTopicName;
    }

    public void save(MaterialRequest materialRequest) {

        logAwareKafkaTemplate.send(materialsSaveTopicName, materialRequest);

    }
}
