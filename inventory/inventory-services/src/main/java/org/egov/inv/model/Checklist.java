package org.egov.inv.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Checklist {

    /**
     * tenantId Unique Identifier of the tenant, Like AP, AP.Kurnool etc. represents the client for which the transaction is
     * created.
     *
     * @return tenantId
     **/
    @NotNull
    @Size(min = 0, max = 256)
    @JsonProperty("tenantId")
    private String tenantId = null;

    /**
     * code is the unique identifier
     *
     * @return code
     **/
    @Size(max = 50)
    private String code;

    /**
     * type refers to the name of the entities. If the check list is required for Bill then type will be billRegister
     */
    @Size(max = 50, min = 1)
    private String type;

    /**
     * subType refers to the different types of entities. If the check list is required for Bill of type expenseBill then subtype
     * value is expensebill. this is unique combination type and subtype is unique
     */
    @Size(max = 50, min = 1)
    private String subType;

    /**
     * key is the content of the check list . exmaple. 1. Passport copy attached 2. Ration card copy attached etc
     */
    @Size(max = 250, min = 3)
    private String key;

    /**
     * description is further detailed discription of the check list values
     */
    @Size(max = 250, min = 3)
    private String description;

    /**
     * Get auditDetails
     *
     * @return auditDetails
     **/
    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;
}
