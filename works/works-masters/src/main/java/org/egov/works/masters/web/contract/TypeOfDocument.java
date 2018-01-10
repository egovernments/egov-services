package org.egov.works.masters.web.contract;

import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * An Object that holds object of TypeOfDocument
 */
@ApiModel(description = "An Object that holds object of TypeOfDocument")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-01-10T13:23:45.489Z")

public class TypeOfDocument {
    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("name")
    private String name = null;

    public TypeOfDocument tenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * Tenant id of the Type Of Document
     * @return tenantId
     **/
    @ApiModelProperty(required = true, value = "Tenant id of the Type Of Document")
    @NotNull

    @Size(min = 2, max = 128)
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public TypeOfDocument name(String name) {
        this.name = name;
        return this;
    }

    /**
     * name of Type Of Document
     * @return name
     **/
    @ApiModelProperty(required = true, value = "name of Type Of Document")
    @NotNull

    @Size(min = 1, max = 100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TypeOfDocument typeOfDocument = (TypeOfDocument) o;
        return Objects.equals(this.tenantId, typeOfDocument.tenantId) &&
                Objects.equals(this.name, typeOfDocument.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tenantId, name);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class TypeOfDocument {\n");

        sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
