package io.swagger.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This object holds the material information.
 */
@ApiModel(description = "This object holds the material information.   ")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-11T10:04:38.711Z")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Material   {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("code")
    private String code = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("description")
    private String description = null;

    @JsonProperty("oldCode")
    private String oldCode = null;

    @JsonProperty("materialType")
    private MaterialType materialType = null;

    @JsonProperty("baseUom")
    private Uom baseUom = null;

    /**
     * inventory type of the Material
     */
    public enum InventoryTypeEnum {
        ASSET("Asset"),

        CONSUMABLE("Consumable");

        private String value;

        InventoryTypeEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static InventoryTypeEnum fromValue(String text) {
            for (InventoryTypeEnum b : InventoryTypeEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    @JsonProperty("inventoryType")
    private InventoryTypeEnum inventoryType = null;

    /**
     * status of the Material
     */
    public enum StatusEnum {
        ACTIVE("Active"),

        INACTIVE("Inactive"),

        WITHDRAWN("Withdrawn"),

        OBSOLETE("Obsolete");

        private String value;

        StatusEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static StatusEnum fromValue(String text) {
            for (StatusEnum b : StatusEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    @JsonProperty("status")
    private StatusEnum status = null;

    @JsonProperty("inActiveDate")
    private Long inActiveDate = null;

    @JsonProperty("purchaseUom")
    private Uom purchaseUom = null;

    @JsonProperty("expenseAccount")
    private ChartofAccount expenseAccount = null;

    @JsonProperty("minQuantity")
    private BigDecimal minQuantity = null;

    @JsonProperty("maxQuantity")
    private BigDecimal maxQuantity = null;

    @JsonProperty("stockingUom")
    private Uom stockingUom = null;

    /**
     * material class of the Material
     */
    public enum MaterialClassEnum {
        HIGHUSAGE("HighUsage"),

        MEDIUMUSAGE("MediumUsage"),

        LOWUSAGE("LowUsage");

        private String value;

        MaterialClassEnum(String value) {
            this.value = value;
        }

        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static MaterialClassEnum fromValue(String text) {
            for (MaterialClassEnum b : MaterialClassEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    @JsonProperty("materialClass")
    private MaterialClassEnum materialClass = null;

    @JsonProperty("reorderLevel")
    private BigDecimal reorderLevel = null;

    @JsonProperty("reorderQuantity")
    private BigDecimal reorderQuantity = null;

    @JsonProperty("lotControl")
    private Boolean lotControl = null;

    @JsonProperty("shelfLifeControl")
    private Boolean shelfLifeControl = null;

    @JsonProperty("serialNumber")
    private Boolean serialNumber = null;

    @JsonProperty("model")
    private String model = null;

    @JsonProperty("manufacturePartNo")
    private String manufacturePartNo = null;

    @JsonProperty("techincalSpecs")
    private String techincalSpecs = null;

    @JsonProperty("termsOfDelivery")
    private String termsOfDelivery = null;

    @JsonProperty("scrapable")
    private Boolean scrapable = null;

    @JsonProperty("assetCategory")
    private AssetCategory assetCategory = null;

    @JsonProperty("storeMapping")
    private List<StoreMapping> storeMapping = null;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

    public Material id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Unique Identifier of the Material
     * @return id
     **/
    @ApiModelProperty(value = "Unique Identifier of the Material ")


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Material tenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * Tenant id of the Material
     * @return tenantId
     **/
    @ApiModelProperty(value = "Tenant id of the Material")

    @Size(min=4,max=128)
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Material code(String code) {
        this.code = code;
        return this;
    }

    /**
     * code of the Material
     * @return code
     **/
    @ApiModelProperty(value = "code of the Material ")

    @Size(min=5,max=50)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Material name(String name) {
        this.name = name;
        return this;
    }

    /**
     * name of the Material
     * @return name
     **/
    @ApiModelProperty(required = true, value = "name of the Material ")
    @NotNull

    @Size(min=5,max=50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Material description(String description) {
        this.description = description;
        return this;
    }

    /**
     * description of the Material
     * @return description
     **/
    @ApiModelProperty(required = true, value = "description of the Material ")
    @NotNull

    @Size(max=1000)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Material oldCode(String oldCode) {
        this.oldCode = oldCode;
        return this;
    }

    /**
     * unique old code of the Material
     * @return oldCode
     **/
    @ApiModelProperty(value = "unique old code of the Material ")

    @Size(max=50)
    public String getOldCode() {
        return oldCode;
    }

    public void setOldCode(String oldCode) {
        this.oldCode = oldCode;
    }

    public Material materialType(MaterialType materialType) {
        this.materialType = materialType;
        return this;
    }

    /**
     * Get materialType
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

    public Material baseUom(Uom baseUom) {
        this.baseUom = baseUom;
        return this;
    }

    /**
     * Get baseUom
     * @return baseUom
     **/
    @ApiModelProperty(required = true, value = "")
    @NotNull

    public Uom getBaseUom() {
        return baseUom;
    }

    public void setBaseUom(Uom baseUom) {
        this.baseUom = baseUom;
    }

    public Material inventoryType(InventoryTypeEnum inventoryType) {
        this.inventoryType = inventoryType;
        return this;
    }

    /**
     * inventory type of the Material
     * @return inventoryType
     **/
    @ApiModelProperty(value = "inventory type of the Material ")

    @NotNull
    public InventoryTypeEnum getInventoryType() {
        return inventoryType;
    }

    public void setInventoryType(InventoryTypeEnum inventoryType) {
        this.inventoryType = inventoryType;
    }

    public Material status(StatusEnum status) {
        this.status = status;
        return this;
    }

    /**
     * status of the Material
     * @return status
     **/
    @ApiModelProperty(value = "status of the Material ")


    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public Material inActiveDate(Long inActiveDate) {
        this.inActiveDate = inActiveDate;
        return this;
    }

    /**
     * inactive date of the Material
     * @return inActiveDate
     **/
    @ApiModelProperty(value = "inactive date of the Material ")


    public Long getInActiveDate() {
        return inActiveDate;
    }

    public void setInActiveDate(Long inActiveDate) {
        this.inActiveDate = inActiveDate;
    }

    public Material purchaseUom(Uom purchaseUom) {
        this.purchaseUom = purchaseUom;
        return this;
    }

    /**
     * Get purchaseUom
     * @return purchaseUom
     **/
    @ApiModelProperty(required = true, value = "")
    @NotNull

    public Uom getPurchaseUom() {
        return purchaseUom;
    }

    public void setPurchaseUom(Uom purchaseUom) {
        this.purchaseUom = purchaseUom;
    }

    public Material expenseAccount(ChartofAccount expenseAccount) {
        this.expenseAccount = expenseAccount;
        return this;
    }

    /**
     * Expense account code is mandatory , if inventory type is consumable
     * @return expenseAccount
     **/
    @ApiModelProperty(value = "Expense account code is mandatory , if inventory type is consumable")

    public ChartofAccount getExpenseAccount() {
        return expenseAccount;
    }

    public void setExpenseAccount(ChartofAccount expenseAccount) {
        this.expenseAccount = expenseAccount;
    }

    public Material minQuantity(BigDecimal minQuantity) {
        this.minQuantity = minQuantity;
        return this;
    }

    /**
     * min quantity of the Material
     * @return minQuantity
     **/
    @ApiModelProperty(required = true, value = "min quantity of the Material ")
    @NotNull

    @Valid

    public BigDecimal getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(BigDecimal minQuantity) {
        this.minQuantity = minQuantity;
    }

    public Material maxQuantity(BigDecimal maxQuantity) {
        this.maxQuantity = maxQuantity;
        return this;
    }

    /**
     * max quantity of the Material
     * @return maxQuantity
     **/
    @ApiModelProperty(required = true, value = "max quantity of the Material ")
    @NotNull

    @Valid

    public BigDecimal getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(BigDecimal maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public Material stockingUom(Uom stockingUom) {
        this.stockingUom = stockingUom;
        return this;
    }

    /**
     * Get stockingUom
     * @return stockingUom
     **/
    @ApiModelProperty(required = true, value = "")
    @NotNull

    public Uom getStockingUom() {
        return stockingUom;
    }

    public void setStockingUom(Uom stockingUom) {
        this.stockingUom = stockingUom;
    }

    public Material materialClass(MaterialClassEnum materialClass) {
        this.materialClass = materialClass;
        return this;
    }

    /**
     * material class of the Material
     * @return materialClass
     **/
    @ApiModelProperty(required = true, value = "material class of the Material ")
    @NotNull


    public MaterialClassEnum getMaterialClass() {
        return materialClass;
    }

    public void setMaterialClass(MaterialClassEnum materialClass) {
        this.materialClass = materialClass;
    }

    public Material reorderLevel(BigDecimal reorderLevel) {
        this.reorderLevel = reorderLevel;
        return this;
    }

    /**
     * reorder level of the Material
     * @return reorderLevel
     **/
    @ApiModelProperty(required = true, value = "reorder level of the Material ")
    @NotNull

    @Valid

    public BigDecimal getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(BigDecimal reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public Material reorderQuantity(BigDecimal reorderQuantity) {
        this.reorderQuantity = reorderQuantity;
        return this;
    }

    /**
     * reorder quantity of the Material
     * @return reorderQuantity
     **/
    @ApiModelProperty(required = true, value = "reorder quantity of the Material ")
    @NotNull

    @Valid

    public BigDecimal getReorderQuantity() {
        return reorderQuantity;
    }

    public void setReorderQuantity(BigDecimal reorderQuantity) {
        this.reorderQuantity = reorderQuantity;
    }

    public Material lotControl(Boolean lotControl) {
        this.lotControl = lotControl;
        return this;
    }

    /**
     * LOT Control for Material.
     * @return lotControl
     **/
    @ApiModelProperty(value = "LOT Control for Material.      ")

    public Boolean getLotControl() {
        return lotControl;
    }

    public void setLotControl(Boolean lotControl) {
        this.lotControl = lotControl;
    }

    public Material shelfLifeControl(Boolean shelfLifeControl) {
        this.shelfLifeControl = shelfLifeControl;
        return this;
    }

    /**
     * Shelf Life Control for Material.
     * @return shelfLifeControl
     **/
    @ApiModelProperty(value = "Shelf Life Control for Material.     ")


    public Boolean getShelfLifeControl() {
        return shelfLifeControl;
    }

    public void setShelfLifeControl(Boolean shelfLifeControl) {
        this.shelfLifeControl = shelfLifeControl;
    }

    public Material serialNumber(Boolean serialNumber) {
        this.serialNumber = serialNumber;
        return this;
    }

    /**
     * serial Number for Material.
     * @return serialNumber
     **/
    @ApiModelProperty(value = "serial Number for Material.                                 ")


    public Boolean getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Boolean serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Material model(String model) {
        this.model = model;
        return this;
    }

    /**
     * model of the Material
     * @return model
     **/
    @ApiModelProperty(value = "model of the Material ")


    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Material manufacturePartNo(String manufacturePartNo) {
        this.manufacturePartNo = manufacturePartNo;
        return this;
    }

    /**
     * manufacture part no of the Material
     * @return manufacturePartNo
     **/
    @ApiModelProperty(value = "manufacture part no of the Material ")


    public String getManufacturePartNo() {
        return manufacturePartNo;
    }

    public void setManufacturePartNo(String manufacturePartNo) {
        this.manufacturePartNo = manufacturePartNo;
    }

    public Material techincalSpecs(String techincalSpecs) {
        this.techincalSpecs = techincalSpecs;
        return this;
    }

    /**
     * techincal specs of the Material
     * @return techincalSpecs
     **/
    @ApiModelProperty(value = "techincal specs of the Material ")


    public String getTechincalSpecs() {
        return techincalSpecs;
    }

    public void setTechincalSpecs(String techincalSpecs) {
        this.techincalSpecs = techincalSpecs;
    }

    public Material termsOfDelivery(String termsOfDelivery) {
        this.termsOfDelivery = termsOfDelivery;
        return this;
    }

    /**
     * terms of delivery of the Material
     * @return termsOfDelivery
     **/
    @ApiModelProperty(value = "terms of delivery of the Material ")


    public String getTermsOfDelivery() {
        return termsOfDelivery;
    }

    public void setTermsOfDelivery(String termsOfDelivery) {
        this.termsOfDelivery = termsOfDelivery;
    }

    public Material scrapable(Boolean scrapable) {
        this.scrapable = scrapable;
        return this;
    }

    /**
     * Material is scrapable or not.
     * @return scrapable
     **/
    @ApiModelProperty(value = "Material is scrapable or not. ")


    public Boolean getScrapable() {
        return scrapable;
    }

    public void setScrapable(Boolean scrapable) {
        this.scrapable = scrapable;
    }

    public Material assetCategory(AssetCategory assetCategory) {
        this.assetCategory = assetCategory;
        return this;
    }

    /**
     * Asset category is mandatory if the inventory type is asset
     * @return assetCategory
     **/
    @ApiModelProperty(value = "Asset category is mandatory if the inventory type is asset")

    public AssetCategory getAssetCategory() {
        return assetCategory;
    }

    public void setAssetCategory(AssetCategory assetCategory) {
        this.assetCategory = assetCategory;
    }

    public Material storeMapping(List<StoreMapping> storeMapping) {
        this.storeMapping = storeMapping;
        return this;
    }

    public Material addStoreMappingItem(StoreMapping storeMappingItem) {
        if (this.storeMapping == null) {
            this.storeMapping = new ArrayList<StoreMapping>();
        }
        this.storeMapping.add(storeMappingItem);
        return this;
    }

    /**
     * Get storeMapping
     * @return storeMapping
     **/
    @ApiModelProperty(value = "")

    public List<StoreMapping> getStoreMapping() {
        return storeMapping;
    }

    public void setStoreMapping(List<StoreMapping> storeMapping) {
        this.storeMapping = storeMapping;
    }

    public Material auditDetails(AuditDetails auditDetails) {
        this.auditDetails = auditDetails;
        return this;
    }

    /**
     * Get auditDetails
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
        Material material = (Material) o;
        return Objects.equals(this.id, material.id) &&
                Objects.equals(this.tenantId, material.tenantId) &&
                Objects.equals(this.code, material.code) &&
                Objects.equals(this.name, material.name) &&
                Objects.equals(this.description, material.description) &&
                Objects.equals(this.oldCode, material.oldCode) &&
                Objects.equals(this.materialType, material.materialType) &&
                Objects.equals(this.baseUom, material.baseUom) &&
                Objects.equals(this.inventoryType, material.inventoryType) &&
                Objects.equals(this.status, material.status) &&
                Objects.equals(this.inActiveDate, material.inActiveDate) &&
                Objects.equals(this.purchaseUom, material.purchaseUom) &&
                Objects.equals(this.expenseAccount, material.expenseAccount) &&
                Objects.equals(this.minQuantity, material.minQuantity) &&
                Objects.equals(this.maxQuantity, material.maxQuantity) &&
                Objects.equals(this.stockingUom, material.stockingUom) &&
                Objects.equals(this.materialClass, material.materialClass) &&
                Objects.equals(this.reorderLevel, material.reorderLevel) &&
                Objects.equals(this.reorderQuantity, material.reorderQuantity) &&
                Objects.equals(this.lotControl, material.lotControl) &&
                Objects.equals(this.shelfLifeControl, material.shelfLifeControl) &&
                Objects.equals(this.serialNumber, material.serialNumber) &&
                Objects.equals(this.model, material.model) &&
                Objects.equals(this.manufacturePartNo, material.manufacturePartNo) &&
                Objects.equals(this.techincalSpecs, material.techincalSpecs) &&
                Objects.equals(this.termsOfDelivery, material.termsOfDelivery) &&
                Objects.equals(this.scrapable, material.scrapable) &&
                Objects.equals(this.assetCategory, material.assetCategory) &&
                Objects.equals(this.storeMapping, material.storeMapping) &&
                Objects.equals(this.auditDetails, material.auditDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantId, code, name, description, oldCode, materialType, baseUom, inventoryType, status, inActiveDate, purchaseUom, expenseAccount, minQuantity, maxQuantity, stockingUom, materialClass, reorderLevel, reorderQuantity, lotControl, shelfLifeControl, serialNumber, model, manufacturePartNo, techincalSpecs, termsOfDelivery, scrapable, assetCategory, storeMapping, auditDetails);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Material {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
        sb.append("    code: ").append(toIndentedString(code)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    oldCode: ").append(toIndentedString(oldCode)).append("\n");
        sb.append("    materialType: ").append(toIndentedString(materialType)).append("\n");
        sb.append("    baseUom: ").append(toIndentedString(baseUom)).append("\n");
        sb.append("    inventoryType: ").append(toIndentedString(inventoryType)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    inActiveDate: ").append(toIndentedString(inActiveDate)).append("\n");
        sb.append("    purchaseUom: ").append(toIndentedString(purchaseUom)).append("\n");
        sb.append("    expenseAccount: ").append(toIndentedString(expenseAccount)).append("\n");
        sb.append("    minQuantity: ").append(toIndentedString(minQuantity)).append("\n");
        sb.append("    maxQuantity: ").append(toIndentedString(maxQuantity)).append("\n");
        sb.append("    stockingUom: ").append(toIndentedString(stockingUom)).append("\n");
        sb.append("    materialClass: ").append(toIndentedString(materialClass)).append("\n");
        sb.append("    reorderLevel: ").append(toIndentedString(reorderLevel)).append("\n");
        sb.append("    reorderQuantity: ").append(toIndentedString(reorderQuantity)).append("\n");
        sb.append("    lotControl: ").append(toIndentedString(lotControl)).append("\n");
        sb.append("    shelfLifeControl: ").append(toIndentedString(shelfLifeControl)).append("\n");
        sb.append("    serialNumber: ").append(toIndentedString(serialNumber)).append("\n");
        sb.append("    model: ").append(toIndentedString(model)).append("\n");
        sb.append("    manufacturePartNo: ").append(toIndentedString(manufacturePartNo)).append("\n");
        sb.append("    techincalSpecs: ").append(toIndentedString(techincalSpecs)).append("\n");
        sb.append("    termsOfDelivery: ").append(toIndentedString(termsOfDelivery)).append("\n");
        sb.append("    scrapable: ").append(toIndentedString(scrapable)).append("\n");
        sb.append("    assetCategory: ").append(toIndentedString(assetCategory)).append("\n");
        sb.append("    storeMapping: ").append(toIndentedString(storeMapping)).append("\n");
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
