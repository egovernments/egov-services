package org.egov.web.indexer.contract;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Boundary {

    private Long id;
    private String name;
    private Float longitude;
    private Float latitude;
    private Long boundaryNum;
}
