package org.egov.web.indexer.contract;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class City {
    // TODO : remove default values once the dependant rest service is ready.
    private String name = "Kurnool";
    private String code = "KC";
    private String districtCode = "KC01";
    private String districtName = "Kurnool District";
    private String grade = "Grade A";
    private String domainURL = "Localhost";
    private String regionName = "Kurnool Region";
}
