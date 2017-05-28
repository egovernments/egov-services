package org.egov.web.indexer.enricher;

import org.egov.web.indexer.contract.City;
import org.egov.web.indexer.contract.ServiceRequest;
import org.egov.web.indexer.contract.ServiceType;
import org.egov.web.indexer.contract.SevaRequest;
import org.egov.web.indexer.repository.TenantRepository;
import org.egov.web.indexer.repository.contract.ServiceRequestDocument;
import org.springframework.stereotype.Service;

@Service
public class CityDocumentEnricher implements ServiceRequestDocumentEnricher {

    private TenantRepository tenantRepository;

    public CityDocumentEnricher(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    @Override
    public boolean matches(ServiceType serviceType, SevaRequest sevaRequest) {
        return true;
    }

    @Override
    public void enrich(ServiceType serviceType, SevaRequest sevaRequest, ServiceRequestDocument document) {
        final ServiceRequest serviceRequest = sevaRequest.getServiceRequest();
        City city = tenantRepository.fetchTenantByCode(serviceRequest.getTenantId());
        if (city == null) {
            return;
        }
        document.setCityCode(city.getCode());
        document.setCityDistrictCode(city.getDistrictCode());
        document.setCityDistrictName(city.getDistrictName());
        document.setCityGrade(city.getUlbGrade());
        document.setCityName(city.getName());
        document.setCityRegionName(city.getRegionName());
    }

}
