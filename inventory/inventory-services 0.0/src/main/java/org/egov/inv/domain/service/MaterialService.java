package org.egov.inv.domain.service;

import io.swagger.model.*;
import org.egov.inv.domain.repository.MaterialRepository;
import org.egov.inv.persistense.repository.MaterialJdbcRepository;
import org.egov.inv.persistense.repository.entity.MaterialEntity;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.StringUtils.isEmpty;

@Service
public class MaterialService {

    public static final String SEQ_MATERIAL = "seq_material";
    public static final String SEQ_SERIAL_NO = "seq_material_code_serial_no";
    private MaterialRepository materialRepository;
    private MaterialJdbcRepository materialJdbcRepository;
    private InventoryUtilityService inventoryUtilityService;
    private MaterialStoreMappingService materialStoreMappingService;


    @Autowired
    public MaterialService(MaterialRepository materialRepository,
                           InventoryUtilityService inventoryUtilityService,
                           MaterialStoreMappingService materialStoreMappingService,
                           MaterialJdbcRepository materialJdbcRepository) {
        this.materialRepository = materialRepository;
        this.inventoryUtilityService = inventoryUtilityService;
        this.materialStoreMappingService = materialStoreMappingService;
        this.materialJdbcRepository = materialJdbcRepository;
    }

    public List<Material> save(MaterialRequest materialRequest, String tenantId) {

        List<MaterialStoreMapping> materialStoreMappings = new ArrayList<>();

        List<Long> materialIdList = inventoryUtilityService.getIdList(materialRequest.getMaterials().size(), SEQ_MATERIAL);

        List<Long> materialCodeSerialNo = inventoryUtilityService.getIdList(materialRequest.getMaterials().size(), SEQ_SERIAL_NO);

        for (int i = 0; i <= materialIdList.size() - 1; i++) {
            materialRequest.getMaterials().get(i)
                    .setId(materialIdList.get(i).toString());
            materialRequest.getMaterials().get(i)
                    .setCode(materialRequest.getMaterials().get(i).getMaterialType().getCode() + "/" + materialCodeSerialNo.get(i));
            materialRequest.getMaterials().get(i).setAuditDetails(inventoryUtilityService.mapAuditDetails(materialRequest.getRequestInfo(), tenantId));
            materialStoreMappings = buildMaterialStoreMapping(materialRequest.getMaterials().get(i).getCode(), materialRequest.getMaterials().get(i).getStoreMapping());
            uniqueCheck(materialRequest.getMaterials().get(i));
            validateAsset(materialRequest.getMaterials().get(i));
        }

        materialRepository.save(materialRequest);

        materialStoreMappingService.create(buildMaterialStoreRequest(materialRequest.getRequestInfo(), materialStoreMappings), tenantId);

        return materialRequest.getMaterials();
    }

    public List<Material> update(MaterialRequest materialRequest, String tenantId) {
        List<MaterialStoreMapping> materialStoreMappings = new ArrayList<>();

        for (Material material : materialRequest.getMaterials()) {
            material.setAuditDetails(
                    inventoryUtilityService.mapAuditDetailsForUpdate(materialRequest.getRequestInfo(), tenantId));
            materialStoreMappings = buildMaterialStoreMapping(material.getCode(), material.getStoreMapping());
            uniqueCheck(material);
            validateAsset(material);
        }

        materialRepository.update(materialRequest);

        materialStoreMappingService.update(buildMaterialStoreRequest(materialRequest.getRequestInfo(), materialStoreMappings), tenantId);

        return materialRequest.getMaterials();
    }

    public Pagination<Material> search(MaterialSearchRequest materialSearchRequest) {

        Pagination<Material> searchMaterial = materialRepository.search(materialSearchRequest);

        for (Material material : searchMaterial.getPagedData()) {
            List<StoreMapping> storeMappings = new ArrayList<>();

            MaterialStoreMappingSearchRequest materialStoreMappingSearchRequest = MaterialStoreMappingSearchRequest.builder()
                    .material(material.getCode())
                    .tenantId(material.getAuditDetails().getTenantId())
                    .build();

            Pagination<MaterialStoreMapping> materialStoreMappings = materialStoreMappingService.search(materialStoreMappingSearchRequest);

            materialStoreMappings.getPagedData().stream().forEach(materialStoreMapping -> {
                        StoreMapping storeMapping = StoreMapping.builder()
                                .id(materialStoreMapping.getId())
                                .chartofAccount(materialStoreMapping.getChartofAccount())
                                .active(materialStoreMapping.getActive())
                                .store(materialStoreMapping.getStore())
                                .auditDetails(materialStoreMapping.getAuditDetails())
                                .build();
                        storeMappings.add(storeMapping);
                    }
            );
            material.setStoreMapping(storeMappings);
        }

        return searchMaterial;
    }

    private List<MaterialStoreMapping> buildMaterialStoreMapping(String materialCode, List<StoreMapping> storeMappings) {
        List<MaterialStoreMapping> materialStoreMappings = new ArrayList<>();

        storeMappings.stream().forEach(
                storeMapping -> {
                    MaterialStoreMapping materialStoreMapping = MaterialStoreMapping.builder()
                            .id(storeMapping.getId())
                            .material(buildMaterial(materialCode))
                            .store(storeMapping.getStore())
                            .chartofAccount(storeMapping.getChartofAccount())
                            .active(storeMapping.getActive())
                            .auditDetails(storeMapping.getAuditDetails())
                            .delete(storeMapping.getDelete())
                            .build();

                    materialStoreMappings.add(materialStoreMapping);
                }
        );
        return materialStoreMappings;
    }

    private Material buildMaterial(String materialCode) {
        return Material
                .builder()
                .code(materialCode)
                .build();
    }

    private MaterialStoreMappingRequest buildMaterialStoreRequest(org.egov.common.contract.request.RequestInfo requestInfo,
                                                                  List<MaterialStoreMapping> materialStoreMappings) {
        return MaterialStoreMappingRequest.builder()
                .requestInfo(requestInfo)
                .materialStoreMappings(materialStoreMappings)
                .build();
    }

    private void validateAsset(Material material) {
        if (!isEmpty(material.getInventoryType()) &&
                material.getInventoryType().toString().equalsIgnoreCase("Asset")
                && (null == material.getAssetCategory() ||
                (null != material.getAssetCategory() && isEmpty(material.getAssetCategory().getCode())))) {
            throw new CustomException("inv.0012", "Asset Category is mandatory when Inventory type  is Asset");
        }
    }

    private void uniqueCheck(Material material) {

        if (!materialJdbcRepository.uniqueCheck("name", new MaterialEntity().toEntity(material))) {
            throw new CustomException("inv.010", "material name already exists " + material.getName());
        }
        if (!materialJdbcRepository.uniqueCheck("name", "code", new MaterialEntity().toEntity(material))) {
            throw new CustomException("inv.0011", "Combination of Code and Name Already Exists " + material.getName()
                    + ", " + material.getCode());
        }
    }

}
