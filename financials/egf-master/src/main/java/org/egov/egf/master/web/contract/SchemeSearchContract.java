package org.egov.egf.master.web.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class SchemeSearchContract extends SchemeContract {
    private String sortBy;
    private Integer pageSize=10;
    private Integer offset;
}