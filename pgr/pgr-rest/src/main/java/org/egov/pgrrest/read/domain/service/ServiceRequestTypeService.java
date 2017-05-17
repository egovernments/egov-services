package org.egov.pgrrest.read.domain.service;

import org.egov.pgrrest.common.entity.ServiceType;
import org.egov.pgrrest.read.domain.model.ServiceTypeSearchCriteria;
import org.egov.pgrrest.read.persistence.repository.ComplaintTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceRequestTypeService {

    private final ComplaintTypeRepository complaintTypeRepository;

    @Autowired
    public ServiceRequestTypeService(final ComplaintTypeRepository complaintTypeRepository) {
        this.complaintTypeRepository = complaintTypeRepository;
    }

    public ServiceType getComplaintType(String complaintTypeCode, String tenantId) {
        return complaintTypeRepository.getComplaintType(complaintTypeCode, tenantId);
    }

    public List<ServiceType> findByCriteria(ServiceTypeSearchCriteria searchCriteria) {
        searchCriteria.validate();
        return findComplaintTypesByCriteria(searchCriteria);
    }

    private List<ServiceType> findComplaintTypesByCriteria(ServiceTypeSearchCriteria searchCriteria) {
        if (searchCriteria.isCategorySearch()) {
            return complaintTypeRepository.findActiveComplaintTypesByCategoryAndTenantId(searchCriteria.getCategoryId(),
                    searchCriteria.getTenantId());
        } else if (searchCriteria.isFrequencySearch()) {
            return complaintTypeRepository.getFrequentlyFiledComplaints(searchCriteria.getCount(),
                    searchCriteria.getTenantId());
        } else if (searchCriteria.isReturnAll()) {
            return complaintTypeRepository.getAllComplaintTypes();
        }
        return null;
    }
}
