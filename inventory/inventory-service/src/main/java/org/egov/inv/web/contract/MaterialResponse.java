package org.egov.inv.web.contract;

import lombok.*;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.inv.domain.model.Material;
import org.egov.inv.domain.model.Pagination;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MaterialResponse {

    public ResponseInfo responseInfo;

    public List<Material> materials;

    private Pagination page;

}
