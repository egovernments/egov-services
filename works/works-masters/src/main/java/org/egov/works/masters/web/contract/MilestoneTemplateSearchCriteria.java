package org.egov.works.masters.web.contract;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by ramki on 15/12/17.
 */

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MilestoneTemplateSearchCriteria {
    @NotNull
    private String tenantId;
    private List<String> ids;
    private List<String> codes;
    private String name;
    private Boolean active;
    private String typeOfWork;
    private String subTypeOfWork;
    private Integer pageSize;
    private Integer pageNumber;
    private Boolean isUpdateUniqueCheck = Boolean.FALSE;
}
