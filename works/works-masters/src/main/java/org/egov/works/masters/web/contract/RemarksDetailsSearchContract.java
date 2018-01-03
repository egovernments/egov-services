package org.egov.works.masters.web.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by parvati on 2/1/18.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RemarksDetailsSearchContract {
    @NotNull
    private String tenantId;

    private Integer pageSize;

    private Integer pageNumber;

    private String sortBy;

    private List<String> ids;

    private List<String> remarks;
}
