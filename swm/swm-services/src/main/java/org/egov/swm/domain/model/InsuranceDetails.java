package org.egov.swm.domain.model;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InsuranceDetails {

    @NotNull
    @Length(min = 1, max = 256)
    @JsonProperty("insuranceNumber")
    private String insuranceNumber = null;

    @NotNull
    @JsonProperty("insuranceValidityDate")
    private Long insuranceValidityDate = null;

    @JsonProperty("insuranceDocument")
    private Document insuranceDocument = null;

}
