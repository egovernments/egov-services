package org.egov.swm.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SourceSegregationSearch extends SourceSegregation {
    private String codes;
    private String dumpingGroundCode;
    private String ulbCode;
    private String sortBy;
    private Integer pageSize;
    private Integer offset;
}