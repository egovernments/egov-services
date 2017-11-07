package org.egov.inv.domain.service;

import io.swagger.model.*;
import org.egov.inv.domain.repository.MaterialStoreESRepository;
import org.egov.inv.persistence.entity.MaterialStoreMappingEntity;
import org.egov.inv.persistence.repository.MaterialStoreMappingJdbcRepository;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.util.StringUtils.isEmpty;

@Service
public class MaterialStoreMappingService {

    public static final String INV_001 = "inv.001";
    public static final String STORE = "store";
    public static final String MATERIAL = "material";
    public static final String INV_005 = "inv.005";
    public static final String INV_003 = "INV.003";

    private LogAwareKafkaTemplate logAwareKafkaTemplate;

    private MaterialStoreMappingJdbcRepository materialStoreMappingJdbcRepository;

    private MaterialStoreESRepository materialESRepository;

    private InventoryUtilityService inventoryUtilityService;

    private StoreService storeService;

    private String createTopicName;

    private String updateTopicName;

    private boolean isESEnabled;


    @Autowired
    public MaterialStoreMappingService(LogAwareKafkaTemplate logAwareKafkaTemplate,
                                       MaterialStoreMappingJdbcRepository materialStoreMappingJdbcRepository,
                                       InventoryUtilityService inventoryUtilityService,
                                       StoreService storeService,
                                       MaterialStoreESRepository materialESRepository,
                                       @Value("${inv.materialstore.save.topic}") String createTopicName,
                                       @Value("${inv.materialstore.update.topic}") String updateTopicName,
                                       @Value("${es.enabled}") boolean isESEnabled) {
        this.logAwareKafkaTemplate = logAwareKafkaTemplate;
        this.materialStoreMappingJdbcRepository = materialStoreMappingJdbcRepository;
        this.materialESRepository = materialESRepository;
        this.inventoryUtilityService = inventoryUtilityService;
        this.storeService = storeService;
        this.createTopicName = createTopicName;
        this.updateTopicName = updateTopicName;
        this.isESEnabled = isESEnabled;
    }


    public List<MaterialStoreMapping> create(MaterialStoreMappingRequest materialStoreMappingRequest, String tenantId) {
        materialStoreMappingRequest.getMaterialStoreMappings().stream()
                .forEach(materialStoreMapping -> {
                    materialStoreMapping.setAuditDetails(inventoryUtilityService.mapAuditDetails(materialStoreMappingRequest.getRequestInfo(), tenantId));
                    uniqueCheck(materialStoreMapping);
                    materialStoreMapping.setId(materialStoreMappingJdbcRepository.getSequence(materialStoreMapping));
                });

        return push(createTopicName, materialStoreMappingRequest);

    }

    public List<MaterialStoreMapping> update(MaterialStoreMappingRequest materialStoreMappingRequest, String tenantId) {
        materialStoreMappingRequest.getMaterialStoreMappings().stream()
                .forEach(materialStoreMapping -> {
                    materialStoreMapping.setAuditDetails(inventoryUtilityService.mapAuditDetails(materialStoreMappingRequest.getRequestInfo(), tenantId));
                    validateUpdateRequest(tenantId, materialStoreMapping);
                });
        return push(updateTopicName, materialStoreMappingRequest);

    }

    public Pagination<MaterialStoreMapping> search(MaterialStoreMappingSearchRequest materialStoreMappingSearchRequest) {
        return isESEnabled ? materialESRepository.search(materialStoreMappingSearchRequest) :
                materialStoreMappingJdbcRepository.search(materialStoreMappingSearchRequest);
    }

    private List<MaterialStoreMapping> push(String topicName, MaterialStoreMappingRequest
            materialStoreMappingRequest) {

        logAwareKafkaTemplate.send(topicName, materialStoreMappingRequest);

        return materialStoreMappingRequest.getMaterialStoreMappings();
    }


    private void validateUpdateRequest(String tenantId, MaterialStoreMapping materialStoreMapping) {
        if (isEmpty(materialStoreMapping.getId())) {
            throw new CustomException("id", "Id is not present");
        }

        int size = findMaterialStore(tenantId, materialStoreMapping).size();

        if (size > 0) {
            uniqueCheck(materialStoreMapping);
        } else
            throw new CustomException(INV_003, "Material Store Mapping Not Found");
    }


    private List<MaterialStoreMapping> findMaterialStore(String tenantId, MaterialStoreMapping materialStoreMapping) {
        MaterialStoreMappingSearchRequest materialStoreMappingSearchRequest = new MaterialStoreMappingSearchRequest();
        materialStoreMappingSearchRequest.setMaterial(materialStoreMapping.getMaterial().getCode());
        materialStoreMappingSearchRequest.setStore(materialStoreMapping.getStore().getCode());
        materialStoreMappingSearchRequest.setTenantId(tenantId);
        return materialStoreMappingJdbcRepository.search(materialStoreMappingSearchRequest).getPagedData();
    }

    private void buildStoreMapping(String tenantId, MaterialStoreMapping materialStoreMapping) {
        Store store = getStore(materialStoreMapping.getStore().getCode(), tenantId);
        materialStoreMapping.getStore().setCode(store.getCode());
        materialStoreMapping.getStore().setDepartment(store.getDepartment());
    }

    private Store getStore(String storeCode, String tenantId) {

        StoreGetRequest storeGetRequest = getStoreGetRequest(storeCode, tenantId);

        Pagination<Store> store = storeService.search(storeGetRequest);

        if (store.getPagedData().size() > 0) {
            return store.getPagedData().get(0);
        } else {
            throw new CustomException(INV_005, "Store Not Found");
        }
    }

    private void uniqueCheck(MaterialStoreMapping materialStoreMapping) {
        if (!materialStoreMappingJdbcRepository.uniqueCheck(MATERIAL, STORE, new MaterialStoreMappingEntity().toEntity(materialStoreMapping))) {
            throw new CustomException(INV_001, "Combination of Code and Name Already Exists ");
        }
    }

    private StoreGetRequest getStoreGetRequest(String storeCode, String tenantId) {
        return StoreGetRequest.builder()
                .code(storeCode)
                .tenantId(tenantId)
                .build();
    }
}
