package org.egov.collection.consumer.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class Receipt {

    private String tenantId;

    private String transactionId;

    @Size(min = 1, max = 1)
    @JsonProperty("Bill")
    private List<Bill> bill = new ArrayList<>();

    private Instrument instrument;

}
