package org.egov.inv.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * This object holds the material type store mapping details information.
 */
@ApiModel(description = "This object holds the material type store mapping details information. ")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-12-14T09:31:14.044Z")

public class MaterialTypeStoreMapping {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("materialType")
    private MaterialType materialType = null;

    @JsonProperty("store")
    private Store store = null;

    @JsonProperty("chartofAccount")
    private ChartofAccount chartofAccount = null;

    @JsonProperty("active")
    private Boolean active = null;

    @JsonProperty("delete")
    private Boolean delete = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

    public MaterialTypeStoreMapping id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Unique Identifier of the Material Type Store Mapping
     *
     * @return id
     **/
    @ApiModelProperty(value = "Unique Identifier of the Material Type Store Mapping ")


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MaterialTypeStoreMapping materialType(MaterialType materialType) {
        this.materialType = materialType;
        return this;
    }

    /**
     * Get materialType
     *
     * @return materialType
     **/
    @ApiModelProperty(required = true, value = "")
    @NotNull

    public MaterialType getMaterialType() {
        return materialType;
    }

    public void setMaterialType(MaterialType materialType) {
        this.materialType = materialType;
    }

    public MaterialTypeStoreMapping store(Store store) {
        this.store = store;
        return this;
    }

    /**
     * Get store
     *
     * @return store
     **/
    @ApiModelProperty(required = true, value = "")
    @NotNull

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public MaterialTypeStoreMapping chartofAccount(ChartofAccount chartofAccount) {
        this.chartofAccount = chartofAccount;
        return this;
    }

    /**
     * Get chartofAccount
     *
     * @return chartofAccount
     **/
    @ApiModelProperty(value = "")

    public ChartofAccount getChartofAccount() {
        return chartofAccount;
    }

    public void setChartofAccount(ChartofAccount chartofAccount) {
        this.chartofAccount = chartofAccount;
    }

    public MaterialTypeStoreMapping active(Boolean active) {
        this.active = active;
        return this;
    }

    /**
     * active or inactive flag of mapping
     *
     * @return active
     **/
    @ApiModelProperty(value = "active or inactive flag of mapping ")


    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public MaterialTypeStoreMapping delete(Boolean delete) {
        this.delete = delete;
        return this;
    }

    /**
     * delete flag of mapping used during deleting the mapping.
     *
     * @return delete
     **/
    @ApiModelProperty(value = "delete flag of mapping used during deleting the mapping. ")


    public Boolean getDelete() {
        return delete;
    }

    public void setDelete(Boolean delete) {
        this.delete = delete;
    }

    public MaterialTypeStoreMapping tenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * Tenant id of the Material Type Store
     *
     * @return tenantId
     **/
    @ApiModelProperty(required = true, value = "Tenant id of the Material Type Store")
    @NotNull

    @Size(min = 4, max = 128)
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public MaterialTypeStoreMapping auditDetails(AuditDetails auditDetails) {
        this.auditDetails = auditDetails;
        return this;
    }

    /**
     * Get auditDetails
     *
     * @return auditDetails
     **/
    @ApiModelProperty(value = "")

    public AuditDetails getAuditDetails() {
        return auditDetails;
    }

    public void setAuditDetails(AuditDetails auditDetails) {
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
        MaterialTypeStoreMapping materialTypeStoreMapping = (MaterialTypeStoreMapping) o;
        return Objects.equals(this.id, materialTypeStoreMapping.id) &&
                Objects.equals(this.materialType, materialTypeStoreMapping.materialType) &&
                Objects.equals(this.store, materialTypeStoreMapping.store) &&
                Objects.equals(this.chartofAccount, materialTypeStoreMapping.chartofAccount) &&
                Objects.equals(this.active, materialTypeStoreMapping.active) &&
                Objects.equals(this.delete, materialTypeStoreMapping.delete) &&
                Objects.equals(this.tenantId, materialTypeStoreMapping.tenantId) &&
                Objects.equals(this.auditDetails, materialTypeStoreMapping.auditDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, materialType, store, chartofAccount, active, delete, tenantId, auditDetails);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class MaterialTypeStoreMapping {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    materialType: ").append(toIndentedString(materialType)).append("\n");
        sb.append("    store: ").append(toIndentedString(store)).append("\n");
        sb.append("    chartofAccount: ").append(toIndentedString(chartofAccount)).append("\n");
        sb.append("    active: ").append(toIndentedString(active)).append("\n");
        sb.append("    delete: ").append(toIndentedString(delete)).append("\n");
        sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
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

