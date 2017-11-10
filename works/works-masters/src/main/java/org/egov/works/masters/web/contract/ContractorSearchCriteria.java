package org.egov.works.masters.web.contract;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ContractorSearchCriteria {
    @NotNull
    private String tenantId;
    private List<String> ids;
    private String code;
    private String name;
    private List<String> contractClass;
    private List<String> statuses;
    private Integer pageSize;
    private Integer pageNumber;
}
