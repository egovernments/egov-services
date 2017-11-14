package org.egov.inv.domain.service;

import io.swagger.model.*;
import org.egov.common.contract.request.RequestInfo;
import org.egov.inv.domain.repository.MaterialStoreESRepository;
import org.egov.inv.persistence.entity.MaterialStoreMappingEntity;
import org.egov.inv.persistence.repository.MaterialStoreMappingJdbcRepository;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.isEmpty;

@Service
public class MaterialStoreMappingService {

    public static final String INV_001 = "inv.001";
    public static final String STORE = "store";
    public static final String MATERIAL = "material";
    public static final String INV_005 = "inv.005";
    public static final String INV_006 = "INV.006";

    private LogAwareKafkaTemplate logAwareKafkaTemplate;

    private MaterialStoreMappingJdbcRepository materialStoreMappingJdbcRepository;

    private MaterialStoreESRepository materialESRepository;

    private InventoryUtilityService inventoryUtilityService;

    private StoreService storeService;

    private String createTopicName;

    private String updateTopicName;

    private String deleteTopicName;

    private boolean isESEnabled;

    private boolean isFinancialEnabled;

    @Autowired
    public MaterialStoreMappingService(LogAwareKafkaTemplate logAwareKafkaTemplate,
                                       MaterialStoreMappingJdbcRepository materialStoreMappingJdbcRepository,
                                       InventoryUtilityService inventoryUtilityService,
                                       StoreService storeService,
                                       MaterialStoreESRepository materialESRepository,
                                       @Value("${inv.materialstore.save.topic}") String createTopicName,
                                       @Value("${inv.materialstore.update.topic}") String updateTopicName,
                                       @Value("${inv.materialstore.delete.topic}") String deleteTopicName,
                                       @Value("${es.enabled}") boolean isESEnabled,
                                       @Value("${financial.enabled}") boolean isFinancialEnabled) {
        this.logAwareKafkaTemplate = logAwareKafkaTemplate;
        this.materialStoreMappingJdbcRepository = materialStoreMappingJdbcRepository;
        this.materialESRepository = materialESRepository;
        this.inventoryUtilityService = inventoryUtilityService;
        this.storeService = storeService;
        this.createTopicName = createTopicName;
        this.updateTopicName = updateTopicName;
        this.deleteTopicName = deleteTopicName;
        this.isESEnabled = isESEnabled;
        this.isFinancialEnabled = isFinancialEnabled;
    }


    public List<MaterialStoreMapping> create(MaterialStoreMappingRequest materialStoreMappingRequest, String tenantId) {

        materialStoreMappingRequest.getMaterialStoreMappings().stream()
                .forEach(materialStoreMapping -> {
                    materialStoreMapping.setAuditDetails(inventoryUtilityService.mapAuditDetails(materialStoreMappingRequest.getRequestInfo(), tenantId));
                    validateCreateRequest(tenantId, materialStoreMapping);
                    materialStoreMapping.setId(materialStoreMappingJdbcRepository.getSequence(materialStoreMapping));
                    buildMaterialStoreMapping(tenantId, materialStoreMapping);
                });

        return push(createTopicName, materialStoreMappingRequest);

    }

    public List<MaterialStoreMapping> update(MaterialStoreMappingRequest materialStoreMappingRequest, String tenantId) {
        List<MaterialStoreMapping> deleteStoreMappings = materialStoreMappingRequest.getMaterialStoreMappings().stream()
                .filter(materialStoreMapping ->
                        null != materialStoreMapping.getDelete() && materialStoreMapping.getDelete().equals(Boolean.TRUE))
                .collect(Collectors.toList());

        if (deleteStoreMappings.size() > 0) {
            deleteMaterialStore(materialStoreMappingRequest.getRequestInfo(), deleteStoreMappings, tenantId);
            materialStoreMappingRequest.getMaterialStoreMappings().remove(deleteStoreMappings);
        }

        materialStoreMappingRequest.getMaterialStoreMappings().stream()
                .forEach(materialStoreMapping -> {
                    materialStoreMapping.setAuditDetails(inventoryUtilityService.mapAuditDetails(materialStoreMappingRequest.getRequestInfo(), tenantId));
                    validateUpdateRequest(tenantId, materialStoreMapping, materialStoreMappingRequest.getRequestInfo());
                    buildMaterialStoreMapping(tenantId, materialStoreMapping);
                });

        return push(updateTopicName, materialStoreMappingRequest);

    }

    private void deleteMaterialStore(RequestInfo requestInfo, List<MaterialStoreMapping> materialStoreMappings, String tenantId) {

        materialStoreMappings.stream()
                .forEach(storeMapping -> storeMapping.setAuditDetails(inventoryUtilityService.
                        mapAuditDetails(requestInfo, tenantId))
                );

        MaterialStoreMappingRequest storeMappingRequest = MaterialStoreMappingRequest.builder()
                .requestInfo(requestInfo)
                .materialStoreMappings(materialStoreMappings)
                .build();

        push(deleteTopicName, storeMappingRequest);
    }

    public Pagination<MaterialStoreMapping> search(MaterialStoreMappingSearchRequest materialStoreMappingSearchRequest) {
        /*Pagination<MaterialStoreMapping> materialStoreMappingList = isESEnabled ? materialESRepository.search(materialStoreMappingSearchRequest) :
                materialStoreMappingJdbcRepository.search(materialStoreMappingSearchRequest);*/
        Pagination<MaterialStoreMapping> materialStoreMappingList = materialStoreMappingJdbcRepository.search(materialStoreMappingSearchRequest);
        materialStoreMappingList.getPagedData().stream()
                .forEach(materialStoreMapping ->
                        buildMaterialStoreMapping(materialStoreMappingSearchRequest.getTenantId(), materialStoreMapping));
        return materialStoreMappingList;
    }

    private List<MaterialStoreMapping> push(String topicName, MaterialStoreMappingRequest
            materialStoreMappingRequest) {

        logAwareKafkaTemplate.send(topicName, materialStoreMappingRequest);

        return materialStoreMappingRequest.getMaterialStoreMappings();
    }


    private void validateUpdateRequest(String tenantId, MaterialStoreMapping materialStoreMapping, RequestInfo requestInfo) {
        validateChartOfAccount(materialStoreMapping);

        int size = findMaterialStore(tenantId, materialStoreMapping).size();

        if (size > 0) {
            uniqueCheck(materialStoreMapping);
        } else {
            create(buildCreateRequest(materialStoreMapping, requestInfo), tenantId);
        }
    }

    private MaterialStoreMappingRequest buildCreateRequest(MaterialStoreMapping materialStoreMapping, RequestInfo requestInfo) {
        List<MaterialStoreMapping> materialStoreMappings = new ArrayList<>();
        materialStoreMappings.add(materialStoreMapping);
        return MaterialStoreMappingRequest.builder()
                .requestInfo(requestInfo)
                .materialStoreMappings(materialStoreMappings)
                .build();
    }

    private void validateCreateRequest(String tenantId, MaterialStoreMapping materialStoreMapping) {
        validateChartOfAccount(materialStoreMapping);
        getStore(materialStoreMapping.getStore().getCode(), tenantId);
        uniqueCheck(materialStoreMapping);
    }

    private void validateChartOfAccount(MaterialStoreMapping materialStoreMapping) {
        if (isFinancialEnabled && isEmpty(materialStoreMapping.getChartofAccount().getGlCode())) {
            throw new CustomException("inv.007", "Account Code is Mandatory ");
        }
    }

    private List<MaterialStoreMapping> findMaterialStore(String tenantId, MaterialStoreMapping materialStoreMapping) {
        MaterialStoreMappingSearchRequest materialStoreMappingSearchRequest = new MaterialStoreMappingSearchRequest();
        materialStoreMappingSearchRequest.setMaterial(materialStoreMapping.getMaterial().getCode());
        materialStoreMappingSearchRequest.setStore(materialStoreMapping.getStore().getCode());
        materialStoreMappingSearchRequest.setTenantId(tenantId);
        return materialStoreMappingJdbcRepository.search(materialStoreMappingSearchRequest).getPagedData();
    }

    private void buildMaterialStoreMapping(String tenantId, MaterialStoreMapping materialStoreMapping) {
        Store store = getStore(materialStoreMapping.getStore().getCode(), tenantId);
        materialStoreMapping.setStore(store);
    }


    private Store getStore(String storeCode, String tenantId) {

        StoreGetRequest storeGetRequest = getStoreGetRequest(storeCode, tenantId);

        Pagination<Store> store = storeService.search(storeGetRequest);

        if (null != store && store.getPagedData().size() > 0) {
            return store.getPagedData().get(0);
        } else {
            throw new CustomException(INV_005, "Store Not Found " + storeCode);
        }
    }

    private void uniqueCheck(MaterialStoreMapping materialStoreMapping) {
        if (!materialStoreMappingJdbcRepository.uniqueCheck(MATERIAL, STORE, new MaterialStoreMappingEntity().toEntity(materialStoreMapping))) {
            throw new CustomException(INV_001, "Combination of Code and Name Already Exists " + materialStoreMapping.getMaterial().getName()
                    + ", " + materialStoreMapping.getStore().getName());
        }
    }

    private StoreGetRequest getStoreGetRequest(String storeCode, String tenantId) {
        return StoreGetRequest.builder()
                .code(Arrays.asList(storeCode))
                .tenantId(tenantId)
                .build();
    }
}
