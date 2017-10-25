package org.egov.inv.domain.service;

import org.egov.inv.domain.model.Material;
import org.egov.inv.domain.model.MaterialSearchRequest;
import org.egov.inv.domain.model.Pagination;
import org.egov.inv.domain.repository.MaterialRepository;
import org.egov.inv.web.contract.MaterialRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterialService {

    private MaterialRepository materialRepository;

    private InventoryUtilityService inventoryUtilityService;

    public static final String SEQ_MATERIAL = "seq_material";


    @Autowired
    public MaterialService(MaterialRepository materialRepository,
                           InventoryUtilityService inventoryUtilityService) {
        this.materialRepository = materialRepository;
        this.inventoryUtilityService = inventoryUtilityService;
    }

    public List<Material> save(MaterialRequest materialRequest, String tenantId) {

        List<Long> materialIdList = inventoryUtilityService.getIdList(materialRequest.getMaterials().size(), SEQ_MATERIAL);

        for (int i = 0; i <= materialIdList.size() - 1; i++) {
            materialRequest.getMaterials().get(i)
                    .setId(materialIdList.get(i).toString());
            materialRequest.getMaterials().get(i)
                    .setCode(materialRequest.getMaterials().get(i).getMaterialType().getCode() + "/" + materialIdList.get(i).toString());
            materialRequest.getMaterials().get(i).
                    setAuditDetails(inventoryUtilityService.mapAuditDetails(materialRequest.getRequestInfo(), tenantId));
        }

        materialRepository.save(materialRequest);

        return materialRequest.getMaterials();
    }

    public List<Material> update(MaterialRequest materialRequest, String tenantId) {

        materialRequest.getMaterials().stream()
                .forEach(material -> material.setAuditDetails(
                        inventoryUtilityService.mapAuditDetailsForUpdate(materialRequest.getRequestInfo(), tenantId)
                ));

        materialRepository.update(materialRequest);

        return materialRequest.getMaterials();
    }

    public Pagination<Material> search(MaterialSearchRequest materialSearchRequest) {
        return materialRepository.search(materialSearchRequest);
    }
}
