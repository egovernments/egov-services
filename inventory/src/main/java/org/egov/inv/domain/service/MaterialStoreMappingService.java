package org.egov.inv.domain.service;

import io.swagger.model.MaterialStoreMapping;
import io.swagger.model.MaterialStoreMappingRequest;
import io.swagger.model.MaterialStoreMappingSearchRequest;
import io.swagger.model.Pagination;
import org.egov.inv.persistence.entity.MaterialStoreMappingEntity;
import org.egov.inv.persistence.repository.MaterialStoreMappingJdbcRepository;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;

@Service
public class MaterialStoreMappingService {

    private LogAwareKafkaTemplate logAwareKafkaTemplate;

    private MaterialStoreMappingJdbcRepository materialStoreMappingJdbcRepository;

    private InventoryUtilityService inventoryUtilityService;

    private String createTopicName;

    private String updateTopicName;

    public MaterialStoreMappingService(LogAwareKafkaTemplate logAwareKafkaTemplate,
                                       MaterialStoreMappingJdbcRepository materialStoreMappingJdbcRepository,
                                       InventoryUtilityService inventoryUtilityService,
                                       @Value("${inv.materialstore.save.topic}") String createTopicName,
                                       @Value("${inv.materialstore.update.topic}") String updateTopicName) {
        this.logAwareKafkaTemplate = logAwareKafkaTemplate;
        this.materialStoreMappingJdbcRepository = materialStoreMappingJdbcRepository;
        this.inventoryUtilityService = inventoryUtilityService;
        this.createTopicName = createTopicName;
        this.updateTopicName = updateTopicName;
    }


    public List<MaterialStoreMapping> create(MaterialStoreMappingRequest materialStoreMappingRequest, String tenantId, BindingResult errors) {
        materialStoreMappingRequest.getMaterialStoreMappings().stream()
                .forEach(materialStoreMapping -> {
                    materialStoreMapping.setAuditDetails(inventoryUtilityService.mapAuditDetails(materialStoreMappingRequest.getRequestInfo(), tenantId));
                    if (!materialStoreMappingJdbcRepository.uniqueCheck("material", "store", new MaterialStoreMappingEntity().toEntity(materialStoreMapping))) {
                        throw new RuntimeException("material and store combination already exists");
                    }
                    materialStoreMapping.setId(materialStoreMappingJdbcRepository.getSequence(materialStoreMapping));
                });
        return push(createTopicName, materialStoreMappingRequest);

    }

    public List<MaterialStoreMapping> update(MaterialStoreMappingRequest materialStoreMappingRequest, String tenantId, BindingResult errors) {
        materialStoreMappingRequest.getMaterialStoreMappings().stream()
                .forEach(materialStoreMapping -> {
                    materialStoreMapping.setAuditDetails(inventoryUtilityService.mapAuditDetails(materialStoreMappingRequest.getRequestInfo(), tenantId));
                    if (!materialStoreMappingJdbcRepository.uniqueCheck("material", "store", new MaterialStoreMappingEntity().toEntity(materialStoreMapping))) {
                        throw new RuntimeException("material and store combination already exists");
                    }
                });
        return push(updateTopicName, materialStoreMappingRequest);

    }

    private List<MaterialStoreMapping> push(String topicName, MaterialStoreMappingRequest
            materialStoreMappingRequest) {

        logAwareKafkaTemplate.send(topicName, materialStoreMappingRequest);

        return materialStoreMappingRequest.getMaterialStoreMappings();
    }

    public Pagination<MaterialStoreMapping> search(MaterialStoreMappingSearchRequest materialStoreMappingSearchRequest) {
        return materialStoreMappingJdbcRepository.search(materialStoreMappingSearchRequest);
    }

}
