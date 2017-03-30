package org.egov.pgr.read.domain.service;

import org.egov.pgr.read.persistence.repository.CrnRepository;
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
