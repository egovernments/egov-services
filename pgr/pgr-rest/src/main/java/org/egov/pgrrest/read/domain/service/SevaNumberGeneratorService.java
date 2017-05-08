package org.egov.pgrrest.read.domain.service;

import org.egov.pgrrest.read.persistence.repository.CrnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SevaNumberGeneratorService {

    private CrnRepository crnRepository;

    @Autowired
    public SevaNumberGeneratorService(CrnRepository crnRepository) {
        this.crnRepository = crnRepository;
    }

    public String generate() {
        return crnRepository.getCrn().getValue();
    }
}
