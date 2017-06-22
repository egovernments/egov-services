package org.egov.pgrrest.read.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.egov.pgrrest.read.domain.exception.InvalidServiceTypeSearchException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Value
@Builder
@AllArgsConstructor
public class ServiceTypeSearchCriteria {

    private static final List<String> KNOWN_SEARCH_TYPES = Arrays.asList("category", "frequency","all");
    private static final int DEFAULT_FREQUENCY_COUNT = 5;
    private static final String INVALID_KNOWN_TYPE = "Invalid search type";
    private static final String CATEGORY_ID_IS_NOT_PRESENT = "Category id is not present";
    private String complaintTypeSearch;
    private Integer count;
    private Long categoryId;
    private String tenantId;

    public ServiceRequestTypeSearch getComplaintTypeSearch() {
        return ServiceRequestTypeSearch.valueOf(complaintTypeSearch.toUpperCase());
    }

    public int getCount() {
        return count == null ? DEFAULT_FREQUENCY_COUNT : count;
    }

    public void validate() {
        validateSearchType();
        validateCategorySearchType();
    }

    private void validateCategorySearchType() {
        if (getComplaintTypeSearch() == ServiceRequestTypeSearch.CATEGORY && Objects.isNull(categoryId)) {
            throw new InvalidServiceTypeSearchException(CATEGORY_ID_IS_NOT_PRESENT);
        }
    }

    private void validateSearchType() {
        if (KNOWN_SEARCH_TYPES.indexOf(complaintTypeSearch.toLowerCase()) < 0) {
            throw new InvalidServiceTypeSearchException(INVALID_KNOWN_TYPE);
        }
    }

    public boolean isCategorySearch() {
        return getComplaintTypeSearch() == ServiceRequestTypeSearch.CATEGORY;
    }

    public boolean isFrequencySearch() {
        return getComplaintTypeSearch() == ServiceRequestTypeSearch.FREQUENCY;
    }
    
    public boolean isReturnAll(){
    	return getComplaintTypeSearch() == ServiceRequestTypeSearch.ALL;
    }
}


