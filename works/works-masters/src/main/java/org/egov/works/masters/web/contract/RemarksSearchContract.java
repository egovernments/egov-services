package org.egov.works.masters.web.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RemarksSearchContract {

    @NotNull
    private String tenantId;

    private Integer pageSize;

    private Integer pageNumber;

    private String sortBy;

    private List<String> ids;

    private String typeOfDocument;

    private String remarksType;

    private String remarksDescription;
}
