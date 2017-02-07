package org.egov.pgr.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.egov.pgr.exception.InvalidComplaintSearchException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Value
@Builder
public class ComplaintTypeSearchCriteria {

    private static final List<String> KNOWN_TYPES = Arrays.asList("category", "frequency");
    private static final long DEFAULT_COUNT = 5L;
    private static final String INVALID_KNOWN_TYPE = "Invalid search type";
    private static final String CATEGORY_ID_IS_NOT_PRESENT = "Category id is not present";
    private String complaintTypeSearch;
    private Long count;
    private Long categoryId;

    public ComplaintTypeSearch getComplaintTypeSearch() {
        return ComplaintTypeSearch.valueOf(complaintTypeSearch.toUpperCase());
    }

    public Long getCount() {
        return count == null ? Long.valueOf(DEFAULT_COUNT) : count;
    }

    public void isValid() {
        if (KNOWN_TYPES.indexOf(complaintTypeSearch.toLowerCase()) < 0) {
            throw new InvalidComplaintSearchException(INVALID_KNOWN_TYPE);
        }

        if (getComplaintTypeSearch() == ComplaintTypeSearch.CATEGORY && Objects.isNull(categoryId)) {
            throw new InvalidComplaintSearchException(CATEGORY_ID_IS_NOT_PRESENT);
        }
    }
}


