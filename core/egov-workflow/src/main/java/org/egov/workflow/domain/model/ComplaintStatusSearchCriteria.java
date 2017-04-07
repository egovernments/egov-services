package org.egov.workflow.domain.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class ComplaintStatusSearchCriteria {

    private String complaintStatusName;
    private List<Long> roles;
}
