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
public class PaymentTerms {

    @JsonProperty("noOfDays")
    private Long noOfDays = null;

    @NotNull
    @Length(min = 1, max = 20, message = "Value of label shall be between 1 and 20")
    @JsonProperty("label")
    private String label = null;

}
