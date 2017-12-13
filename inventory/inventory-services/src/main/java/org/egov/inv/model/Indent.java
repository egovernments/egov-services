package org.egov.inv.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This object holds information required for indent note and transfer indent.
 */
@ApiModel(description = "This object holds information required for indent note and transfer indent. ")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-12-04T09:19:39.812Z")

public class Indent   {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("action")
    private String action = null;

    @JsonProperty("issueStore")
    private Store issueStore = null;

    @JsonProperty("indentStore")
    private Store indentStore = null;

    @JsonProperty("indentDate")
    private Long indentDate = null;

    @JsonProperty("indentNumber")
    private String indentNumber = null;

    /**
     * There are 2 types of indent namely Indent Note and Transfer Indent
     */
    public enum IndentTypeEnum {
        INDENTNOTE("Indent"),

        TRANSFERINDENT("Transfer Indent");

        private String value;

        IndentTypeEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static IndentTypeEnum fromValue(String text) {
            for (IndentTypeEnum b : IndentTypeEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    @JsonProperty("indentType")
    private IndentTypeEnum indentType = null;

    /**
     * Using this field to search indents for purchase order or material issue creation.
     */
    public enum SearchPurposeEnum {
        PURCHASEORDER("PurchaseOrder"),

        ISSUEMATERIAL("IssueMaterial");

        private String value;

        SearchPurposeEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static SearchPurposeEnum fromValue(String text) {
            for (SearchPurposeEnum b : SearchPurposeEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    @JsonProperty("searchPurpose")
    private SearchPurposeEnum searchPurpose = null;

    /**
     * Consumption, RepairsAndMaintenance, Capital - applicable for Indent Note. MaterialTransferNote - applicable for Transfer Indent.
     */
    public enum IndentPurposeEnum {
        CONSUMPTION("Consumption"),

        REPAIRSANDMAINTENANCE("Repairs and Maintenance"),

        CAPITAL("Capital"),

        MATERIALTRANSFERNOTE("Material Transfer Note");

        private String value;

        IndentPurposeEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static IndentPurposeEnum fromValue(String text) {
            for (IndentPurposeEnum b : IndentPurposeEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    @JsonProperty("indentPurpose")
    private IndentPurposeEnum indentPurpose = null;

    /**
     * Gets or Sets inventoryType
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

    @JsonProperty("expectedDeliveryDate")
    private Long expectedDeliveryDate = null;

    @JsonProperty("materialHandOverTo")
    private String materialHandOverTo = null;

    @JsonProperty("narration")
    private String narration = null;

    /**
     * status of the Indent
     */
    public enum IndentStatusEnum {
        CREATED("CREATED"),

        APPROVED("APPROVED"),

        REJECTED("REJECTED"),

        CANCELED("CANCELED"),

        ISSUED("ISSUED");

        private String value;

        IndentStatusEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static IndentStatusEnum fromValue(String text) {
            for (IndentStatusEnum b : IndentStatusEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    @JsonProperty("indentStatus")
    private IndentStatusEnum indentStatus = null;

    @JsonProperty("indentDetails")
    private List<IndentDetail> indentDetails = new ArrayList<IndentDetail>();

    @JsonProperty("department")
    private Department department = null;

    @JsonProperty("totalIndentValue")
    private BigDecimal totalIndentValue = null;

    @JsonProperty("fileStoreId")
    private String fileStoreId = null;

    @JsonProperty("indentCreatedBy")
    private String indentCreatedBy = null;

    @JsonProperty("designation")
    private String designation = null;

    @JsonProperty("stateId")
    private Long stateId = null;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

    public Indent id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Unique Identifier of the Indent
     * @return id
     **/
    @ApiModelProperty(value = "Unique Identifier of the Indent ")


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Indent tenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * Tenant id of the Indent
     * @return tenantId
     **/
    @ApiModelProperty(value = "Tenant id of the Indent")

    @Size(min=4,max=128)
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Indent action(String action) {
        this.action = action;
        return this;
    }

    /**
     * Action to update Indent Details
     * @return action
     **/
    @ApiModelProperty(value = "Action to update Indent Details")

    @Size(max=50)
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Indent issueStore(Store issueStore) {
        this.issueStore = issueStore;
        return this;
    }

    /**
     * Applicable for both Indent Note and Transfer Indent. This field holds information of the intended issuing store.
     * @return issueStore
     **/
    @ApiModelProperty(value = "Applicable for both Indent Note and Transfer Indent. This field holds information of the intended issuing store. ")



    public Store getIssueStore() {
        return issueStore;
    }

    public void setIssueStore(Store issueStore) {
        this.issueStore = issueStore;
    }

    public Indent indentStore(Store indentStore) {
        this.indentStore = indentStore;
        return this;
    }

    /**
     * Applicable for Tranfer Indent Only.This field holds the value of store raising the indent in case of Transfer Indent.
     * @return indentStore
     **/
    @ApiModelProperty(required = true, value = "Applicable for Tranfer Indent Only.This field holds the value of store raising the indent in case of Transfer Indent. ")

    public Store getIndentStore() {
        return indentStore;
    }

    public void setIndentStore(Store indentStore) {
        this.indentStore = indentStore;
    }

    public Indent indentDate(Long indentDate) {
        this.indentDate = indentDate;
        return this;
    }

    /**
     * indent requisition date
     * @return indentDate
     **/
    @ApiModelProperty(required = true, value = "indent requisition date")
    @NotNull


    public Long getIndentDate() {
        return indentDate;
    }

    public void setIndentDate(Long indentDate) {
        this.indentDate = indentDate;
    }

    public Indent indentNumber(String indentNumber) {
        this.indentNumber = indentNumber;
        return this;
    }

    /**
     * indentNumber  Auto generated number, read only <ULB short code><Store Code><fin. Year><serial No.>
     * @return indentNumber
     **/
    @ApiModelProperty(readOnly = true, value = "indentNumber  Auto generated number, read only <ULB short code><Store Code><fin. Year><serial No.> ")

    @Size(max=64)
    public String getIndentNumber() {
        return indentNumber;
    }

    public void setIndentNumber(String indentNumber) {
        this.indentNumber = indentNumber;
    }

    public Indent indentType(IndentTypeEnum indentType) {
        this.indentType = indentType;
        return this;
    }

    /**
     * There are 2 types of indent namely Indent Note and Transfer Indent
     * @return indentType
     **/
    @ApiModelProperty(required = true, value = "There are 2 types of indent namely Indent Note and Transfer Indent ")


    public IndentTypeEnum getIndentType() {
        return indentType;
    }

    public void setIndentType(IndentTypeEnum indentType) {
        this.indentType = indentType;
    }

    public Indent searchPurpose(SearchPurposeEnum searchPurpose) {
        this.searchPurpose = searchPurpose;
        return this;
    }

    /**
     * Using this field to search indents for purchase order or material issue creation.
     * @return searchPurpose
     **/
    @ApiModelProperty(value = "Using this field to search indents for purchase order or material issue creation. ")


    public SearchPurposeEnum getSearchPurpose() {
        return searchPurpose;
    }

    public void setSearchPurpose(SearchPurposeEnum searchPurpose) {
        this.searchPurpose = searchPurpose;
    }

    public Indent indentPurpose(IndentPurposeEnum indentPurpose) {
        this.indentPurpose = indentPurpose;
        return this;
    }

    /**
     * Consumption, RepairsAndMaintenance, Capital - applicable for Indent Note. MaterialTransferNote - applicable for Transfer Indent.
     * @return indentPurpose
     **/
    @ApiModelProperty(required = true, value = "Consumption, RepairsAndMaintenance, Capital - applicable for Indent Note. MaterialTransferNote - applicable for Transfer Indent. ")
    @NotNull


    public IndentPurposeEnum getIndentPurpose() {
        return indentPurpose;
    }

    public void setIndentPurpose(IndentPurposeEnum indentPurpose) {
        this.indentPurpose = indentPurpose;
    }

    public Indent inventoryType(InventoryTypeEnum inventoryType) {
        this.inventoryType = inventoryType;
        return this;
    }

    /**
     * Get inventoryType
     * @return inventoryType
     **/
    @ApiModelProperty(value = "")


    public InventoryTypeEnum getInventoryType() {
        return inventoryType;
    }

    public void setInventoryType(InventoryTypeEnum inventoryType) {
        this.inventoryType = inventoryType;
    }

    public Indent expectedDeliveryDate(Long expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
        return this;
    }

    /**
     * Applicable only for Indent Note. Expected delivery date for Indent Note
     * @return expectedDeliveryDate
     **/
    @ApiModelProperty(value = "Applicable only for Indent Note. Expected delivery date for Indent Note ")


    public Long getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    public void setExpectedDeliveryDate(Long expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    public Indent materialHandOverTo(String materialHandOverTo) {
        this.materialHandOverTo = materialHandOverTo;
        return this;
    }

    /**
     * Material Handovered to person name
     * @return materialHandOverTo
     **/
    @ApiModelProperty(value = "Material Handovered to person name ")

    @Size(max=128)
    public String getMaterialHandOverTo() {
        return materialHandOverTo;
    }

    public void setMaterialHandOverTo(String materialHandOverTo) {
        this.materialHandOverTo = materialHandOverTo;
    }

    public Indent narration(String narration) {
        this.narration = narration;
        return this;
    }

    /**
     * description of the Indent
     * @return narration
     **/
    @ApiModelProperty(value = "description of the Indent ")

    @Size(max=1000)
    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public Indent indentStatus(IndentStatusEnum indentStatus) {
        this.indentStatus = indentStatus;
        return this;
    }

    /**
     * status of the Indent
     * @return indentStatus
     **/
    @ApiModelProperty(value = "status of the Indent ")


    public IndentStatusEnum getIndentStatus() {
        return indentStatus;
    }

    public void setIndentStatus(IndentStatusEnum indentStatus) {
        this.indentStatus = indentStatus;
    }

    public Indent indentDetails(List<IndentDetail> indentDetails) {
        this.indentDetails = indentDetails;
        return this;
    }

    public Indent addIndentDetailsItem(IndentDetail indentDetailsItem) {
        this.indentDetails.add(indentDetailsItem);
        return this;
    }

    /**
     * Get indentDetails
     * @return indentDetails
     **/
    @ApiModelProperty(required = true, value = "")
    @NotNull

    @Valid
    @Size(min=1,max=50)
    public List<IndentDetail> getIndentDetails() {
        return indentDetails;
    }

    public void setIndentDetails(List<IndentDetail> indentDetails) {
        this.indentDetails = indentDetails;
    }

    public Indent department(Department department) {
        this.department = department;
        return this;
    }

    /**
     * department of indenting store or direct department request for material. If store is selected then autopopulate department information and readonly field.
     * @return department
     **/
    @ApiModelProperty(value = "department of indenting store or direct department request for material. If store is selected then autopopulate department information and readonly field.")

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Indent totalIndentValue(BigDecimal totalIndentValue) {
        this.totalIndentValue = totalIndentValue;
        return this;
    }

    /**
     * totalIndentValue  denormalized value from Indent Material
     * @return totalIndentValue
     **/
    @ApiModelProperty(value = "totalIndentValue  denormalized value from Indent Material ")

    @Valid

    public BigDecimal getTotalIndentValue() {
        return totalIndentValue;
    }

    public void setTotalIndentValue(BigDecimal totalIndentValue) {
        this.totalIndentValue = totalIndentValue;
    }

    public Indent fileStoreId(String fileStoreId) {
        this.fileStoreId = fileStoreId;
        return this;
    }

    /**
     * fileStoreId  File Store id of the Indent Note/Transfer Indent Generated
     * @return fileStoreId
     **/
    @ApiModelProperty(value = "fileStoreId  File Store id of the Indent Note/Transfer Indent Generated ")


    public String getFileStoreId() {
        return fileStoreId;
    }

    public void setFileStoreId(String fileStoreId) {
        this.fileStoreId = fileStoreId;
    }

    public Indent indentCreatedBy(String indentCreatedBy) {
        this.indentCreatedBy = indentCreatedBy;
        return this;
    }

    /**
     * indent requested user information. If store incharge registering request on behalf of some one, enter indent request user name. Autopopulate login user name by default. Linking with employee master also possible.
     * @return indentCreatedBy
     **/
    @ApiModelProperty(value = "indent requested user information. If store incharge registering request on behalf of some one, enter indent request user name. Autopopulate login user name by default. Linking with employee master also possible. ")

    @Size(max=128)
    public String getIndentCreatedBy() {
        return indentCreatedBy;
    }

    public void setIndentCreatedBy(String indentCreatedBy) {
        this.indentCreatedBy = indentCreatedBy;
    }

    public Indent designation(String designation) {
        this.designation = designation;
        return this;
    }

    /**
     * Designation of the employee at the time of Indent Note/Transfer Indent creation. Autopopulate login user designation by default.
     * @return designation
     **/
    @ApiModelProperty(value = "Designation of the employee at the time of Indent Note/Transfer Indent creation. Autopopulate login user designation by default.")


    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Indent stateId(Long stateId) {
        this.stateId = stateId;
        return this;
    }

    /**
     * State id of the Material indent
     * @return stateId
     **/
    @ApiModelProperty(value = "State id of the Material indent ")


    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public Indent auditDetails(AuditDetails auditDetails) {
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
        Indent indent = (Indent) o;
        return Objects.equals(this.id, indent.id) &&
                Objects.equals(this.tenantId, indent.tenantId) &&
                Objects.equals(this.action, indent.action) &&
                Objects.equals(this.issueStore, indent.issueStore) &&
                Objects.equals(this.indentStore, indent.indentStore) &&
                Objects.equals(this.indentDate, indent.indentDate) &&
                Objects.equals(this.indentNumber, indent.indentNumber) &&
                Objects.equals(this.indentType, indent.indentType) &&
                Objects.equals(this.searchPurpose, indent.searchPurpose) &&
                Objects.equals(this.indentPurpose, indent.indentPurpose) &&
                Objects.equals(this.inventoryType, indent.inventoryType) &&
                Objects.equals(this.expectedDeliveryDate, indent.expectedDeliveryDate) &&
                Objects.equals(this.materialHandOverTo, indent.materialHandOverTo) &&
                Objects.equals(this.narration, indent.narration) &&
                Objects.equals(this.indentStatus, indent.indentStatus) &&
                Objects.equals(this.indentDetails, indent.indentDetails) &&
                Objects.equals(this.department, indent.department) &&
                Objects.equals(this.totalIndentValue, indent.totalIndentValue) &&
                Objects.equals(this.fileStoreId, indent.fileStoreId) &&
                Objects.equals(this.indentCreatedBy, indent.indentCreatedBy) &&
                Objects.equals(this.designation, indent.designation) &&
                Objects.equals(this.stateId, indent.stateId) &&
                Objects.equals(this.auditDetails, indent.auditDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantId, action, issueStore, indentStore, indentDate, indentNumber, indentType, searchPurpose, indentPurpose, inventoryType, expectedDeliveryDate, materialHandOverTo, narration, indentStatus, indentDetails, department, totalIndentValue, fileStoreId, indentCreatedBy, designation, stateId, auditDetails);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Indent {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
        sb.append("    action: ").append(toIndentedString(action)).append("\n");
        sb.append("    issueStore: ").append(toIndentedString(issueStore)).append("\n");
        sb.append("    indentStore: ").append(toIndentedString(indentStore)).append("\n");
        sb.append("    indentDate: ").append(toIndentedString(indentDate)).append("\n");
        sb.append("    indentNumber: ").append(toIndentedString(indentNumber)).append("\n");
        sb.append("    indentType: ").append(toIndentedString(indentType)).append("\n");
        sb.append("    searchPurpose: ").append(toIndentedString(searchPurpose)).append("\n");
        sb.append("    indentPurpose: ").append(toIndentedString(indentPurpose)).append("\n");
        sb.append("    inventoryType: ").append(toIndentedString(inventoryType)).append("\n");
        sb.append("    expectedDeliveryDate: ").append(toIndentedString(expectedDeliveryDate)).append("\n");
        sb.append("    materialHandOverTo: ").append(toIndentedString(materialHandOverTo)).append("\n");
        sb.append("    narration: ").append(toIndentedString(narration)).append("\n");
        sb.append("    indentStatus: ").append(toIndentedString(indentStatus)).append("\n");
        sb.append("    indentDetails: ").append(toIndentedString(indentDetails)).append("\n");
        sb.append("    department: ").append(toIndentedString(department)).append("\n");
        sb.append("    totalIndentValue: ").append(toIndentedString(totalIndentValue)).append("\n");
        sb.append("    fileStoreId: ").append(toIndentedString(fileStoreId)).append("\n");
        sb.append("    indentCreatedBy: ").append(toIndentedString(indentCreatedBy)).append("\n");
        sb.append("    designation: ").append(toIndentedString(designation)).append("\n");
        sb.append("    stateId: ").append(toIndentedString(stateId)).append("\n");
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

