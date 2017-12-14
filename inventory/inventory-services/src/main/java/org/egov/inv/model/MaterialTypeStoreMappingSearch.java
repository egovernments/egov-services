package org.egov.inv.model;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MaterialTypeStoreMappingSearch {

    private List<String> ids;

    private String materialType;

    private String store;

    private Boolean active;

    private String glCode;

    private String sortBy;

    private Integer pageSize;

    private Integer offset;

    @NotNull
    private String tenantId;
}
