package org.egov.inv.domain.service;

import io.swagger.model.Material;
import io.swagger.model.MaterialRequest;
import org.egov.inv.domain.repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterialService {

    public static final String SEQ_MATERIAL = "seq_material";
    public static final String SEQ_SERIAL_NO = "seq_material_code_serial_no";
    private MaterialRepository materialRepository;
    private InventoryUtilityService inventoryUtilityService;


    @Autowired
    public MaterialService(MaterialRepository materialRepository,
                           InventoryUtilityService inventoryUtilityService) {
        this.materialRepository = materialRepository;
        this.inventoryUtilityService = inventoryUtilityService;
    }

    public List<Material> save(MaterialRequest materialRequest, String tenantId) {

        materialRequest.getMaterials().forEach(material -> {
            material.setAuditDetails(inventoryUtilityService.mapAuditDetails(materialRequest.getRequestInfo(), tenantId));
        });

        List<Long> materialIdList = inventoryUtilityService.getIdList(materialRequest.getMaterials().size(), SEQ_MATERIAL);
        List<Long> materialCodeSerialNo = inventoryUtilityService.getIdList(materialRequest.getMaterials().size(), SEQ_SERIAL_NO);
        for (int i = 0; i <= materialIdList.size() - 1; i++) {
            materialRequest.getMaterials().get(i)
                    .setId(materialIdList.get(i).toString());
            materialRequest.getMaterials().get(i)
                    .setCode(materialRequest.getMaterials().get(i).getMaterialType().getCode() + "/" + materialCodeSerialNo.get(i));
        }

        materialRepository.save(materialRequest);

        return materialRequest.getMaterials();
    }

    public List<Material> update(MaterialRequest materialRequest, String tenantId) {

        materialRequest.getMaterials().stream()
                .forEach(material -> {
                    material.setAuditDetails(
                            inventoryUtilityService.mapAuditDetailsForUpdate(materialRequest.getRequestInfo(), tenantId));
                });

        materialRepository.update(materialRequest);

        return materialRequest.getMaterials();
    }

}
