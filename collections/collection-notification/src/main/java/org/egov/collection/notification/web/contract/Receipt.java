package org.egov.collection.notification.web.contract;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.common.contract.request.RequestInfo;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Data
@AllArgsConstructor
public class Receipt {

    private RequestInfo requestInfo;

    private String tenantId;
    private String receiptNumber;
    private String mobileNumber;
    private String payeeName;
    private BigDecimal amountPaid;
    private String payeeEmail;
    private String consumerCode;
    private String businessService;

    @NotNull
    @JsonProperty("Bill")
    private List<Bill> bill = new ArrayList<>();


}
