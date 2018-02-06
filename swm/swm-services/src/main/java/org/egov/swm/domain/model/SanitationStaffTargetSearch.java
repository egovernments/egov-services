package org.egov.swm.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SanitationStaffTargetSearch extends SanitationStaffTarget {
    private String targetNos;
    private String routeCode;
    private String swmProcessCode;
    private String dumpingGroundCode;
    private String employeeCode;
    private Boolean validate;
    private String sortBy;
    private Integer pageSize;
    private Integer offset;
}