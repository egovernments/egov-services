package org.egov.asset.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class VoucherAccountCodeDetails {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("glcode")
    private String glcode;

    @JsonProperty("debitAmount")
    private BigDecimal debitAmount = BigDecimal.ZERO;

    @JsonProperty("creditAmount")
    private BigDecimal creditAmount = BigDecimal.ZERO;

    @JsonProperty("function")
    private Function function;

    @JsonProperty("subledgerDetails")
    private List<LedgerDetail> subledgerDetails = new ArrayList<>();

}
