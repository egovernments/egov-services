package org.egov.pgr.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class SevaSearchCriteria {
    private String serviceRequestId;
    private String serviceCode;
    private Date startDate;
    private Date endDate;
    private String status;
    private Date lastModifiedDatetime;
}
