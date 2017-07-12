package org.egov.collection.indexer.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Receipt {

    private String tenantId;

    private String instrumentType;

    private String instrumentHeader;

    @JsonProperty("BillWrapper")
    private BillWrapper billInfoWrapper;

}
