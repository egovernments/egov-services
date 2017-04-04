package org.egov.access.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
public class ActionSearchCriteria {
    private List<Long> roleIds;
    private List<Long> featureIds;
}
