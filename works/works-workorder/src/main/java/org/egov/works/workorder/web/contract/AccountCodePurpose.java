package org.egov.works.workorder.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 *
 */
@ApiModel(description = "")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-30T11:45:24.744Z")

public class AccountCodePurpose {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("auditDetails")
    private Auditable auditDetails = null;

    public AccountCodePurpose id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Unique Identifier of the AccountCodePurpose
     *
     * @return id
     **/
    @ApiModelProperty(value = "Unique Identifier of the AccountCodePurpose ")


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AccountCodePurpose name(String name) {
        this.name = name;
        return this;
    }

    /**
     * name of the AccountCodePurpose
     *
     * @return name
     **/
    @ApiModelProperty(required = true, value = "name of the AccountCodePurpose ")
    @NotNull

    @Size(min = 3, max = 256)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AccountCodePurpose auditDetails(Auditable auditDetails) {
        this.auditDetails = auditDetails;
        return this;
    }

    /**
     * Get auditDetails
     *
     * @return auditDetails
     **/
    @ApiModelProperty(value = "")

    @Valid

    public Auditable getAuditDetails() {
        return auditDetails;
    }

    public void setAuditDetails(Auditable auditDetails) {
        this.auditDetails = auditDetails;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AccountCodePurpose accountCodePurpose = (AccountCodePurpose) o;
        return Objects.equals(this.id, accountCodePurpose.id) &&
                Objects.equals(this.name, accountCodePurpose.name) &&
                Objects.equals(this.auditDetails, accountCodePurpose.auditDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, auditDetails);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AccountCodePurpose {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    auditDetails: ").append(toIndentedString(auditDetails)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}

