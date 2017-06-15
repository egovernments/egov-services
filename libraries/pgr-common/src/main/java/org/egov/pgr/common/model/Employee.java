package org.egov.pgr.common.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Employee {
    private String email;
    private String name;
    private String mobileNumber;
    private Long primaryPosition;
    private Long primaryDesignation;
}
