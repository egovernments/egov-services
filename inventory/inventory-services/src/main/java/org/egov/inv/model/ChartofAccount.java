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
 * This object hold the chart of account information.
 */
@ApiModel(description = "This object hold the chart of account information.   ")
@javax.annotation.Generated(value = "org.egov.inv.codegen.languages.SpringCodegen", date = "2017-11-08T13:51:07.770Z")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChartofAccount {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("glCode")
    private String glCode = null;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

    public ChartofAccount id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Unique Identifier of the Chart of Account
     *
     * @return id
     **/
    @ApiModelProperty(value = "Unique Identifier of the Chart of Account ")


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ChartofAccount tenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * Tenant id of the Chart of Account
     *
     * @return tenantId
     **/
    @ApiModelProperty(value = "Tenant id of the Chart of Account")

    @Size(min = 4, max = 128)
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public ChartofAccount name(String name) {
        this.name = name;
        return this;
    }

    /**
     * name of the ChartofAccount
     *
     * @return name
     **/
    @ApiModelProperty(value = "name of the ChartofAccount ")


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ChartofAccount glCode(String glCode) {
        this.glCode = glCode;
        return this;
    }

    /**
     * gl code of the ChartofAccount
     *
     * @return glCode
     **/
    @ApiModelProperty(value = "gl code of the ChartofAccount ")


    public String getGlCode() {
        return glCode;
    }

    public void setGlCode(String glCode) {
        this.glCode = glCode;
    }

    public ChartofAccount auditDetails(AuditDetails auditDetails) {
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
        ChartofAccount chartofAccount = (ChartofAccount) o;
        return Objects.equals(this.id, chartofAccount.id) &&
                Objects.equals(this.tenantId, chartofAccount.tenantId) &&
                Objects.equals(this.name, chartofAccount.name) &&
                Objects.equals(this.glCode, chartofAccount.glCode) &&
                Objects.equals(this.auditDetails, chartofAccount.auditDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantId, name, glCode, auditDetails);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ChartofAccount {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    glCode: ").append(toIndentedString(glCode)).append("\n");
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

