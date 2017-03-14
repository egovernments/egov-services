package org.egov.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    public Integer id;
    public String userName;
    public String name;
    public String type;
    public String mobileNumber;
    public String emailId;
    public List<Role> roles;
}
