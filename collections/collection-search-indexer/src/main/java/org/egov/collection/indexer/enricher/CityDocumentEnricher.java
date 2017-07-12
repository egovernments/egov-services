package org.egov.collection.indexer.enricher;


import org.egov.collection.indexer.contract.City;
import org.egov.collection.indexer.contract.Receipt;
import org.egov.collection.indexer.contract.ReceiptRequest;
import org.egov.collection.indexer.repository.TenantRepository;
import org.egov.collection.indexer.repository.contract.ReceiptRequestDocument;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityDocumentEnricher implements ReceiptRequestDocumentEnricher {

    private TenantRepository tenantRepository;

    public CityDocumentEnricher(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    @Override
    public boolean matches(ReceiptRequest receiptRequest) {
        return true;
    }

    @Override
    public void enrich(ReceiptRequest receiptRequest, List<ReceiptRequestDocument> documents) {
        final Receipt request = receiptRequest.getReceipt();
        City city = tenantRepository.fetchTenantByCode(request.getTenantId());
        if (city == null) {
            return;
        }
        for(ReceiptRequestDocument document : documents) {
            document.setDistrictName(city.getDistrictName());
            document.setCityName(city.getName());
            document.setRegionName(city.getRegionName());
        }
    }

}
