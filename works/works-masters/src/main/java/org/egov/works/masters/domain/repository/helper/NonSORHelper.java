package org.egov.works.masters.domain.repository.helper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.works.masters.web.contract.NonSOR;
import org.egov.works.masters.web.contract.UOM;

/**
 * Created by ramki on 7/11/17.
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NonSORHelper {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("description")
    private String description = null;

    @JsonProperty("uom")
    private String uom = null;

    @JsonProperty("createdBy")
    private String createdBy = null;

    @JsonProperty("lastModifiedBy")
    private String lastModifiedBy = null;

    @JsonProperty("createdTime")
    private Long createdTime = null;

    @JsonProperty("lastModifiedTime")
    private Long lastModifiedTime = null;

    public NonSOR toDomain() {
        NonSOR nonSOR = new NonSOR();
        nonSOR.setId(this.id);
        nonSOR.setTenantId(this.tenantId);
        nonSOR.setDescription(this.description);
        nonSOR.setUom(new UOM());
        nonSOR.getUom().setCode(this.uom);
        return nonSOR;
    }
}
