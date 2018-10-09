package org.egov.collection.consumer.model;

import java.util.List;

import org.egov.common.contract.request.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptRequest {

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;

    @JsonProperty("Receipt")
    private List<Receipt> receipt = null;

}
