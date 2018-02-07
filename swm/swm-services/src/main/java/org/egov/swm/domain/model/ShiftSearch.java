package org.egov.swm.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShiftSearch extends Shift {
    private String codes;
    private String shiftTypeCode;
    private String departmentCode;
    private String designationCode;
    private String sortBy;
    private Integer pageSize;
    private Integer offset;
}