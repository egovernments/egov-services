package org.egov.inv.domain.repository;

import io.swagger.model.MaterialRequest;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MaterialRepository {

    private LogAwareKafkaTemplate<String, Object> logAwareKafkaTemplate;

    private String materialSaveTopicName;

    private String materialUpdateTopicName;


    @Autowired
    public MaterialRepository(LogAwareKafkaTemplate logAwareKafkaTemplate,
                              @Value("${inv.materials.save.topic}") String materialSaveTopicName,
                              @Value("${inv.materials.update.topic}") String materialsUpdateTopicName) {
        this.logAwareKafkaTemplate = logAwareKafkaTemplate;
        this.materialSaveTopicName = materialSaveTopicName;
        this.materialUpdateTopicName = materialsUpdateTopicName;
    }

    public void save(MaterialRequest materialRequest) {

        logAwareKafkaTemplate.send(materialSaveTopicName, materialRequest);

    }

    public void update(MaterialRequest materialRequest) {

        logAwareKafkaTemplate.send(materialUpdateTopicName, materialRequest);

    }

}
