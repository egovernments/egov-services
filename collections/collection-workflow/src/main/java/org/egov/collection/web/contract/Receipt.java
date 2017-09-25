package org.egov.collection.web.contract;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Data
@AllArgsConstructor
public class Receipt {

    private String id;

    private String tenantId;

    @NotNull
    @JsonProperty("Bill")
    private List<Bill> bill = new ArrayList<>();

    private Instrument instrument;

    @JsonProperty("WorkflowDetails")
    private WorkflowDetailsRequest workflowDetails;


}
