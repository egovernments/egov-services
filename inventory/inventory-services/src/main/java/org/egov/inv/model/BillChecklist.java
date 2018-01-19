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
public class BillChecklist {

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
     * id is the unique identifier
     */
    private String id;

    /**
     * bill is the id of the bill
     */
    private String bill;

    /**
     * checklist is the id of the check list
     */
    private Checklist checklist;

    /**
     * checklistValue is the value entered for the checklist
     */
    private String checklistValue;

    /**
     * Get auditDetails
     *
     * @return auditDetails
     **/
    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

}
