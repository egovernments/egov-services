package org.egov.web.indexer.contract;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class City {
    // TODO : remove default values once the dependant rest service is ready.
    private String name;
    private String code ;
    private String districtCode ;
    private String districtName ;
    private String grade ;
    private String domainURL;
    private String regionName ;
}
