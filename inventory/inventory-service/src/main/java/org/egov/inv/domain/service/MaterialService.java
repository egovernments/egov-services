package org.egov.inv.domain.service;

import org.egov.inv.domain.model.Material;
import org.egov.inv.domain.model.MaterialSearchRequest;
import org.egov.inv.domain.model.Pagination;
import org.egov.inv.domain.repository.MaterialRepository;
import org.egov.inv.domain.service.validator.MaterialRequestValidator;
import org.egov.inv.web.contract.MaterialRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.util.StringUtils.isEmpty;

@Service
public class MaterialService {

    public static final String SEQ_MATERIAL = "seq_material";
    private MaterialRepository materialRepository;
    private InventoryUtilityService inventoryUtilityService;
    private MaterialRequestValidator materialRequestValidator;


    @Autowired
    public MaterialService(MaterialRepository materialRepository,
                           InventoryUtilityService inventoryUtilityService,
                           MaterialRequestValidator materialRequestValidator) {
        this.materialRepository = materialRepository;
        this.inventoryUtilityService = inventoryUtilityService;
        this.materialRequestValidator = materialRequestValidator;
    }

    public List<Material> save(MaterialRequest materialRequest, String tenantId) {

        materialRequest.materials.forEach(material -> {
            materialRequestValidator.validate(material);
        });

        List<Long> materialIdList = inventoryUtilityService.getIdList(materialRequest.getMaterials().size(), SEQ_MATERIAL);

        for (int i = 0; i <= materialIdList.size() - 1; i++) {
            materialRequest.getMaterials().get(i)
                    .setId(materialIdList.get(i).toString());
            Long materialCodeSerialNo = getMaterialCodeSerialNo();
            materialCodeSerialNo++;
            materialRequest.getMaterials().get(i)
                    .setCode(materialRequest.getMaterials().get(i).getMaterialType().getCode() + "/" + materialCodeSerialNo);
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


    private Long getMaterialCodeSerialNo() {
        String latestMaterialCode = materialRepository.getLatestMaterialCode();
        if (!isEmpty(latestMaterialCode)) {
            String[] materialCode = latestMaterialCode.split("/");
            return Long.valueOf(materialCode[1]);
        } else return Long.valueOf(0);
    }
}
