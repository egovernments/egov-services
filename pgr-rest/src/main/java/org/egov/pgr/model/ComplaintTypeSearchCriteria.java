package org.egov.pgr.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.egov.pgr.exception.InvalidComplaintTypeSearchException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Value
@Builder
@AllArgsConstructor
public class ComplaintTypeSearchCriteria {

    private static final List<String> KNOWN_SEARCH_TYPES = Arrays.asList("category", "frequency");
    private static final int DEFAULT_FREQUENCY_COUNT = 5;
    private static final String INVALID_KNOWN_TYPE = "Invalid search type";
    private static final String CATEGORY_ID_IS_NOT_PRESENT = "Category id is not present";
    private String complaintTypeSearch;
    private Integer count;
    private Long categoryId;

    public ComplaintTypeSearch getComplaintTypeSearch() {
        return ComplaintTypeSearch.valueOf(complaintTypeSearch.toUpperCase());
    }

    public int getCount() {
        return count == null ? DEFAULT_FREQUENCY_COUNT : count;
    }

    public void isValid() {
        validateSearchType();
        validateCategorySearchType();
    }

    private void validateCategorySearchType() {
        if (getComplaintTypeSearch() == ComplaintTypeSearch.CATEGORY && Objects.isNull(categoryId)) {
            throw new InvalidComplaintTypeSearchException(CATEGORY_ID_IS_NOT_PRESENT);
        }
    }

    private void validateSearchType() {
        if (KNOWN_SEARCH_TYPES.indexOf(complaintTypeSearch.toLowerCase()) < 0) {
            throw new InvalidComplaintTypeSearchException(INVALID_KNOWN_TYPE);
        }
    }

    public boolean isCategorySearch() {
        return getComplaintTypeSearch() == ComplaintTypeSearch.CATEGORY;
    }

    public boolean isFrequencySearch() {
        return getComplaintTypeSearch() == ComplaintTypeSearch.FREQUENCY;
    }
}


