package org.egov.works.masters.web.contract;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
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
    private Boolean pmc;
    private String correspondenceAddress;
    private String mobileNumber;
    private String emailId;
    private String statusLike;
}
