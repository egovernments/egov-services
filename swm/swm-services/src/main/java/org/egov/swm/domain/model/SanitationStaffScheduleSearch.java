package org.egov.swm.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SanitationStaffScheduleSearch extends SanitationStaffSchedule {
    private String transactionNos;
    private String targetNo;
    private String shiftCode;
    private String sortBy;
    private Integer pageSize;
    private Integer offset;
}