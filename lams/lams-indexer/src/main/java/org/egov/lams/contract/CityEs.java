package org.egov.lams.contract;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CityEs {

    private String cityGrade;
    private String cityName;
    private String districtName;
    private String cityCode;
    private String regionName;
}
