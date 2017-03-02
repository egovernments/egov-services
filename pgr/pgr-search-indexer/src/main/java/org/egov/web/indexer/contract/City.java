package org.egov.web.indexer.contract;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class City {
	private String id;
    private String name;
    private String code ;
    private String districtCode ;
    private String districtName ;
    private String grade ;
    private String domainURL;
    private String regionName ;
}
