package org.egov.pgr.model;

import java.util.Date;

public class SevaSearchCriteria {
    private String serviceRequestId;
    private String serviceCode;
    private Date startDate;
    private Date endDate;
    private String status;
    private Date lastModifiedDatetime;

    public SevaSearchCriteria(String serviceRequestId, String serviceCode,
                              Date startDate, Date endDate, String status,
                              Date lastModifiedDatetime) {
        this.serviceRequestId = serviceRequestId;
        this.serviceCode = serviceCode;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.lastModifiedDatetime = lastModifiedDatetime;
    }

    public String getServiceRequestId() {
        return serviceRequestId;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getStatus() {
        return status;
    }

    public Date getLastModifiedDatetime() {
        return lastModifiedDatetime;
    }
}
