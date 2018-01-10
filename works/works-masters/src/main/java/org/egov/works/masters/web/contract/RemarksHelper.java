package org.egov.works.masters.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RemarksHelper {

    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("typeOfDocument")
    private String typeOfDocument = null;

    @JsonProperty("remarksType")
    private String remarksType = null;

    @JsonProperty("deleted")
    private Boolean deleted = false;

    public Remarks toDomain() {
        Remarks remarks = new Remarks();
        remarks.setId(this.id);
        remarks.setTenantId(this.tenantId);
        TypeOfDocument typeOfDocument = new TypeOfDocument();
        typeOfDocument.setName(this.typeOfDocument);
        remarks.setTypeOfDocument(typeOfDocument);
        RemarksType remarksType = new RemarksType();
        remarksType.setName(this.remarksType);
        remarks.remarksType(remarksType);
        remarks.setDeleted(this.deleted);
        return remarks;
    }

}
