package org.egov.mseva.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Reference {
    private String mobileNumber;
    private String message;

}
