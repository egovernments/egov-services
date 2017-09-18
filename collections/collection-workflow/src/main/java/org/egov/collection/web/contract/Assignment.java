package org.egov.collection.web.contract;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class Assignment {

    private Long id;

    @NotNull
    private Long position;

    private Long fund;

    private Long functionary;

    private Long function;

    @NotNull
    private Long department;

    @NotNull
    private Long designation;
}
