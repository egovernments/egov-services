package org.egov.pgr.rest.web.model;

import java.io.Serializable;
import java.util.List;

import org.springframework.web.bind.annotation.RequestHeader;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Services implements Serializable {

    private static final long serialVersionUID = 2615995768080662713L;

    @SerializedName("Services")
    @Expose
    private List<Service> services;

    @SerializedName("request_header")
    @Expose
    private RequestHeader requestHeader;

    @SerializedName("jurisdiction_id")
    @Expose
    private String jurisdictionId;

    public List<Service> getServices() {
        return services;
    }

    public void setServices(final List<Service> services) {
        this.services = services;
    }

    public RequestHeader getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(final RequestHeader requestHeader) {
        this.requestHeader = requestHeader;
    }

    public String getJurisdictionId() {
        return jurisdictionId;
    }

    public void setJurisdictionId(final String jurisdictionId) {
        this.jurisdictionId = jurisdictionId;
    }
}