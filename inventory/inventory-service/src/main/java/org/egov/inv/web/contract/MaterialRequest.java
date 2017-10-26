package org.egov.inv.web.contract;

import lombok.*;
import org.egov.common.contract.request.RequestInfo;
import org.egov.inv.domain.model.Material;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MaterialRequest {

    public RequestInfo requestInfo;


    public List<Material> materials;
}
