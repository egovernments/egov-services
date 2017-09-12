package org.egov.asset.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
public class Voucher {

    private Long id;

    @NotNull
    private String type;

    @NotNull
    private String name;

    private String description;

    private String voucherNumber;

    @NotNull
    @JsonFormat(pattern = "dd-MM-yyyy")
    private String voucherDate;

    private Fund fund;

    private FiscalPeriod fiscalPeriod;

    private String status;

    private Long originalVhId;

    private Long refVhId;

    private Long moduleId;

    private String cgvn;

    @NotNull
    private Long department;

    private String source;

    private Scheme scheme;

    private SubScheme subScheme;

    private Functionary functionary;

    private FundSource fundsource;

    private List<VoucherAccountCodeDetails> ledgers = new ArrayList<>();

}
