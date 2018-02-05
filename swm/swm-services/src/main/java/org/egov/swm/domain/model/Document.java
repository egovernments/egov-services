package org.egov.swm.domain.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Document {

    @Length(min = 1, max = 256, message = "Value of tenantId shall be between 1 and 256")
    @JsonProperty("id")
    private String id = null;

    @NotNull
    @Length(min = 1, max = 256, message = "Value of tenantId shall be between 1 and 256")
    @JsonProperty("tenantId")
    private String tenantId = null;

    @Length(min = 0, max = 256, message = "Value of name shall be between 0 and 256")
    @JsonProperty("name")
    private String name = null;

    @NotNull
    @Length(min = 1, max = 256, message = "Value of refCode shall be between 1 and 256")
    @JsonProperty("refCode")
    private String refCode = null;

    @NotNull
    @JsonProperty("fileStoreId")
    private String fileStoreId = null;

    @Length(min = 0, max = 1024, message = "Value of name shall be between 0 and 1024")
    @JsonProperty("comments")
    private String comments;

    @JsonProperty("auditDetails")
    @Valid
    private AuditDetails auditDetails = null;

}
