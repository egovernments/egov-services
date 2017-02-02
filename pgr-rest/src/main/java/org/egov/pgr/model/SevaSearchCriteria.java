package org.egov.pgr.model;

import java.util.Date;

public class SevaSearchCriteria {
    private String service_request_id;
    private String service_code;
    private Date start_date;
    private Date end_date;
    private String status;
    private Date last_modified_datetime;

    public SevaSearchCriteria(String service_request_id, String service_code,
                              Date start_date, Date end_date, String status,
                              Date last_modified_datetime){
        this.service_request_id = service_request_id;
        this.service_code = service_code;
        this.start_date = start_date;
        this.end_date = end_date;
        this.status = status;
        this.last_modified_datetime = last_modified_datetime;
    }

    public String getService_request_id() {
        return service_request_id;
    }

    public String getService_code() {
        return service_code;
    }

    public Date getStart_date() {
        return start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public String getStatus() {
        return status;
    }

    public Date getLast_modified_datetime() {
        return last_modified_datetime;
    }
}
