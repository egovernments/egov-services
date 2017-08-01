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

    private String tenantId;

    @NotNull
    @JsonProperty("Bill")
    private List<Bill> bill = new ArrayList<>();


}
