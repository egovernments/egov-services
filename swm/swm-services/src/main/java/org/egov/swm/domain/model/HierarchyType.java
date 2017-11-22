package org.egov.swm.domain.model;

import java.time.LocalDate;

import javax.validation.Valid;

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
public class HierarchyType {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("code")
    private String code = null;

    @Valid
    @JsonProperty("name")
    private LocalDate name = null;

    @Valid
    @JsonProperty("loalname")
    private LocalDate loalname = null;

}
