package org.egov.pgr.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class User {
    private String mobileNumber;
    private String emailId;
    private String name;
    private Integer id;
}
