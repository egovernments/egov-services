package org.egov.inv.domain.repository;

import io.swagger.model.Material;
import io.swagger.model.MaterialRequest;
import io.swagger.model.MaterialSearchRequest;
import io.swagger.model.Pagination;
import org.egov.inv.persistense.repository.MaterialJdbcRepository;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MaterialRepository {

    private LogAwareKafkaTemplate<String, Object> logAwareKafkaTemplate;

    private String materialSaveTopicName;

    private String materialUpdateTopicName;

    private MaterialJdbcRepository materialJdbcRepository;

    @Autowired
    public MaterialRepository(LogAwareKafkaTemplate logAwareKafkaTemplate,
                              @Value("${inv.materials.save.topic}") String materialSaveTopicName,
                              @Value("${inv.materials.update.topic}") String materialsUpdateTopicName,
                              MaterialJdbcRepository materialJdbcRepository) {
        this.logAwareKafkaTemplate = logAwareKafkaTemplate;
        this.materialSaveTopicName = materialSaveTopicName;
        this.materialUpdateTopicName = materialsUpdateTopicName;
        this.materialJdbcRepository = materialJdbcRepository;
    }

    public void save(MaterialRequest materialRequest) {

        logAwareKafkaTemplate.send(materialSaveTopicName, materialRequest);

    }

    public void update(MaterialRequest materialRequest) {

        logAwareKafkaTemplate.send(materialUpdateTopicName, materialRequest);

    }

    public Pagination<Material> search(MaterialSearchRequest materialSearchRequest) {
        return materialJdbcRepository.search(materialSearchRequest);
    }

}
