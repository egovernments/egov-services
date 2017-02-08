package org.egov.pgr.domain.service;

import org.egov.pgr.domain.model.ComplaintTypeSearchCriteria;
import org.egov.pgr.persistence.entity.ComplaintType;
import org.egov.pgr.persistence.repository.ComplaintTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComplaintTypeService {

	private final ComplaintTypeRepository complaintTypeRepository;

	@Autowired
	public ComplaintTypeService(final ComplaintTypeRepository complaintTypeRepository) {
		this.complaintTypeRepository = complaintTypeRepository;
	}

	public ComplaintType getComplaintType(String complaintTypeCode) {
        return complaintTypeRepository.getComplaintType(complaintTypeCode);
    }

    public List<ComplaintType> findByCriteria(ComplaintTypeSearchCriteria searchCriteria) {
        searchCriteria.validate();
        return findComplaintTypesByCriteria(searchCriteria);
    }

    private List<ComplaintType> findComplaintTypesByCriteria(ComplaintTypeSearchCriteria searchCriteria) {
        if (searchCriteria.isCategorySearch()) {
            return complaintTypeRepository.findActiveComplaintTypesByCategory(searchCriteria.getCategoryId());
        } else if (searchCriteria.isFrequencySearch()) {
            return complaintTypeRepository.getFrequentlyFiledComplaints(searchCriteria.getCount());
        }
        return null;
    }
}
