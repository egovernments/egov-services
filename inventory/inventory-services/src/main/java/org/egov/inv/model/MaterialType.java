package org.egov.inv.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * This object holds the material type information.
 */
@ApiModel(description = "This object holds the material type information.   ")
@javax.annotation.Generated(value = "org.egov.inv.codegen.languages.SpringCodegen", date = "2017-11-08T13:51:07.770Z")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialType {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("code")
    private String code = null;

    @JsonProperty("parent")
    private String parent = null;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

    public MaterialType id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Unique Identifier of the Material Type
     *
     * @return id
     **/
    @ApiModelProperty(value = "Unique Identifier of the Material Type ")


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MaterialType tenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * Tenant id of the Material Type
     *
     * @return tenantId
     **/
    @ApiModelProperty(value = "Tenant id of the Material Type")

    @Size(min = 4, max = 128)
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public MaterialType name(String name) {
        this.name = name;
        return this;
    }

    /**
     * name of the MaterialType
     *
     * @return name
     **/
    @ApiModelProperty(value = "name of the MaterialType ")


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MaterialType code(String code) {
        this.code = code;
        return this;
    }

    /**
     * code of the MaterialType
     *
     * @return code
     **/
    @ApiModelProperty(value = "code of the MaterialType ")


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public MaterialType parent(String parent) {
        this.parent = parent;
        return this;
    }

    /**
     * parent of the MaterialType
     *
     * @return parent
     **/
    @ApiModelProperty(value = "parent of the MaterialType ")


    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public MaterialType auditDetails(AuditDetails auditDetails) {
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

    public AuditDetails getAuditDetails() {
        return auditDetails;
    }

    public void setAuditDetails(AuditDetails auditDetails) {
        this.auditDetails = auditDetails;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MaterialType materialType = (MaterialType) o;
        return Objects.equals(this.id, materialType.id) &&
                Objects.equals(this.tenantId, materialType.tenantId) &&
                Objects.equals(this.name, materialType.name) &&
                Objects.equals(this.code, materialType.code) &&
                Objects.equals(this.parent, materialType.parent) &&
                Objects.equals(this.auditDetails, materialType.auditDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantId, name, code, parent, auditDetails);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class MaterialType {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    code: ").append(toIndentedString(code)).append("\n");
        sb.append("    parent: ").append(toIndentedString(parent)).append("\n");
        sb.append("    auditDetails: ").append(toIndentedString(auditDetails)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}

