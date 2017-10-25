package org.egov.inv.domain.service;

import org.egov.inv.domain.repository.MaterialRepository;
import org.egov.inv.web.contract.MaterialRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MaterialService {

    private MaterialRepository materialRepository;

    @Autowired
    public MaterialService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    public void save(MaterialRequest materialRequest) {
        materialRepository.save(materialRequest);
    }

}
