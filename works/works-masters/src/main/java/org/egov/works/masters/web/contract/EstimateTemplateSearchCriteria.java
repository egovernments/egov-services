package org.egov.works.masters.web.contract;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by ramki on 7/11/17.
 */

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EstimateTemplateSearchCriteria {
    @NotNull
    private String tenantId;
    private List<String> ids;
    private String code;
    private String name;
    private String typeOfWork;
    private String subTypeOfWork;
    private Integer pageSize;
    private Integer pageNumber;
    private Boolean isUpdateUniqueCheck = Boolean.FALSE;
}
