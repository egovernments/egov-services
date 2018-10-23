package org.egov.collection.web.contract;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.egov.collection.model.AuditDetails;
import org.egov.collection.model.Instrument;

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

    @NotNull
    private String tenantId;

    private String transactionId;

    @NotNull
    @Size(min = 1, max = 1)
    @Valid
    @JsonProperty("Bill")
    private List<Bill> bill = new ArrayList<>();

    private AuditDetails auditDetails;

    @Valid
    private Instrument instrument;

}
