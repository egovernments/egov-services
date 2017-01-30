package org.egov.pgr.builder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    private String mobileNumber;
    private String emailId;
    private String name;
    private Integer id;

    public User() {

    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getEmailId() {
        return emailId;
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }
}
