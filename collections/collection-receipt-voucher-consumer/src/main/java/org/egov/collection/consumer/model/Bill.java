package org.egov.collection.consumer.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Bill {

    private String id;

    private String payeeName;

    private String payeeAddress;

    private String payeeEmail;

    private Boolean isActive;

    private Boolean isCancelled;

    private String paidBy;

    @JsonProperty("billDetails")
    private List<BillDetail> billDetails = new ArrayList<>(); // for billing-service

    private String tenantId;

    private String mobileNumber;

}
