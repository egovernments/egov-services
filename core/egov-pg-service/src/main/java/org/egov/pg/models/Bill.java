package org.egov.pg.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Bill {
    //TODO some of the fields are mandatory in yml, lets discuss billdetail and billaccountdetail also for more clarity
    private String id;

    private String payeeName;

    private String payeeAddress;

    private String payeeEmail;

    private Boolean isActive;

    private Boolean isCancelled;

    private String paidBy;

    @JsonProperty("billDetails")
    private List<BillDetail> billDetails = new ArrayList<>(); //for billing-service

    private String tenantId;

    private String mobileNumber;

}

