package org.egov.web.indexer.enricher;

import java.util.List;

import org.egov.web.indexer.contract.ConnectionDocument;
import org.egov.web.indexer.contract.WaterConnectionReq;
import org.springframework.stereotype.Service;

@Service
public class TenantDocumentEnricher implements ConnectionDocumentEnricher {
/*
    private TenantRepository tenantRepository;

    public TenantDocumentEnricher(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }*/

    @Override
    public boolean matches(WaterConnectionReq waterConnectionRequest) {
        return true;
    }

    @Override
    public void enrich(WaterConnectionReq waterConnectionReques, List<ConnectionDocument> documents) {
    	// TODO
    }

}
