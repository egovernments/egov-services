package org.egov.swm.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RefillingPumpStationSearch extends RefillingPumpStation {
    private String codes;
    private String locationCode;
    private String typeOfFuelCode;
    private String typeOfPumpCode;
    private String sortBy;
    private Integer pageSize;
    private Integer offset;
}