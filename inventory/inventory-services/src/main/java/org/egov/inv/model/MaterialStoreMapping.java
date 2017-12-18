package org.egov.inv.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * This object holds the material store mapping details information.
 */
@ApiModel(description = "This object holds the material store mapping details information. ")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-14T07:04:08.897Z")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MaterialStoreMapping {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("material")
    private Material material = null;

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

    public MaterialStoreMapping id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Unique Identifier of the Material Store Mapping
     *
     * @return id
     **/
    @ApiModelProperty(value = "Unique Identifier of the Material Store Mapping ")


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MaterialStoreMapping material(Material material) {
        this.material = material;
        return this;
    }

    /**
     * Get material
     *
     * @return material
     **/
    @ApiModelProperty(required = true, value = "")
    @NotNull

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public MaterialStoreMapping store(Store store) {
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

    public MaterialStoreMapping chartofAccount(ChartofAccount chartofAccount) {
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

    public MaterialStoreMapping active(Boolean active) {
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

    public MaterialStoreMapping delete(Boolean delete) {
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

    public MaterialStoreMapping tenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * Tenant id of the Material Store
     *
     * @return tenantId
     **/
    @ApiModelProperty(required = true, value = "Tenant id of the Material Store")
    @NotNull

    @Size(min = 4, max = 128)
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public MaterialStoreMapping auditDetails(AuditDetails auditDetails) {
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
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MaterialStoreMapping materialStoreMapping = (MaterialStoreMapping) o;
        return Objects.equals(this.id, materialStoreMapping.id) &&
                Objects.equals(this.material, materialStoreMapping.material) &&
                Objects.equals(this.store, materialStoreMapping.store) &&
                Objects.equals(this.chartofAccount, materialStoreMapping.chartofAccount) &&
                Objects.equals(this.active, materialStoreMapping.active) &&
                Objects.equals(this.delete, materialStoreMapping.delete) &&
                Objects.equals(this.tenantId, materialStoreMapping.tenantId) &&
                Objects.equals(this.auditDetails, materialStoreMapping.auditDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, material, store, chartofAccount, active, delete, tenantId, auditDetails);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class MaterialStoreMapping {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    material: ").append(toIndentedString(material)).append("\n");
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
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
