package io.swagger.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * This object holds the material store mapping details information.
 */
@ApiModel(description = "This object holds the material store mapping details information.   ")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-02T16:27:56.269+05:30")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MaterialStoreMapping {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("material")
    private String material = null;

    @JsonProperty("store")
    private String store = null;

    @JsonProperty("chartofAccount")
    private ChartofAccount chartofAccount = null;

    @JsonProperty("active")
    private Boolean active = null;

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
    @ApiModelProperty(value = "Unique Identifier of the Material Store Mapping       ")


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MaterialStoreMapping material(String material) {
        this.material = material;
        return this;
    }

    /**
     * code of material
     *
     * @return material
     **/
    @ApiModelProperty(value = "code of material ")


    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public MaterialStoreMapping store(String store) {
        this.store = store;
        return this;
    }

    /**
     * code of store
     *
     * @return store
     **/
    @ApiModelProperty(required = true, value = "code of store ")
    @NotNull


    public String getStore() {
        return store;
    }

    public void setStore(String store) {
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

    @Valid

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


    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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

    @Valid

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
        MaterialStoreMapping materialStoreMapping = (MaterialStoreMapping) o;
        return Objects.equals(this.id, materialStoreMapping.id) &&
                Objects.equals(this.material, materialStoreMapping.material) &&
                Objects.equals(this.store, materialStoreMapping.store) &&
                Objects.equals(this.chartofAccount, materialStoreMapping.chartofAccount) &&
                Objects.equals(this.active, materialStoreMapping.active) &&
                Objects.equals(this.auditDetails, materialStoreMapping.auditDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, material, store, chartofAccount, active, auditDetails);
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

