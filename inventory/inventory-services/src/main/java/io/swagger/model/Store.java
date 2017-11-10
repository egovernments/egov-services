/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package io.swagger.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * This object holds the store information.
 */
@ApiModel(description = "This object holds the store information.   ")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-09T09:33:03.094Z")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Store {
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

    @JsonProperty("department")
    private Department department = null;

    @JsonProperty("officeLocation")
    private Location officeLocation = null;

    @JsonProperty("billingAddress")
    private String billingAddress = null;

    @JsonProperty("deliveryAddress")
    private String deliveryAddress = null;

    @JsonProperty("contactNo1")
    private String contactNo1 = null;

    @JsonProperty("contactNo2")
    private String contactNo2 = null;

    @JsonProperty("email")
    private String email = null;

    @JsonProperty("storeInCharge")
    private Employee storeInCharge = null;

    @JsonProperty("isCentralStore")
    private Boolean isCentralStore = null;

    @JsonProperty("active")
    private Boolean active = null;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

    public Store id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Unique Identifier of the Store
     *
     * @return id
     **/
    @ApiModelProperty(value = "Unique Identifier of the Store ")

    @Size(max = 32)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Store tenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * Tenant id of the Store
     *
     * @return tenantId
     **/
    @ApiModelProperty(value = "Tenant id of the Store")

    @Size(min = 4, max = 128)
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Store code(String code) {
        this.code = code;
        return this;
    }

    /**
     * code of the Store
     *
     * @return code
     **/
    @ApiModelProperty(required = true, value = "code of the Store ")
    @NotNull

    @Pattern(regexp = "^[a-zA-Z0-9]*$")
    @Size(min = 5, max = 50)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Store name(String name) {
        this.name = name;
        return this;
    }

    /**
     * name of the Store
     *
     * @return name
     **/
    @ApiModelProperty(required = true, value = "name of the Store ")
    @NotNull

    @Pattern(regexp = "^[a-zA-Z ]*$")
    @Size(min = 5, max = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Store description(String description) {
        this.description = description;
        return this;
    }

    /**
     * description of the Store
     *
     * @return description
     **/
    @ApiModelProperty(required = true, value = "description of the Store ")
    @NotNull

    @Pattern(regexp = "^[a-zA-Z0-9 ]*$")
    @Size(max = 1000)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Store department(Department department) {
        this.department = department;
        return this;
    }

    /**
     * Get department
     *
     * @return department
     **/
    @ApiModelProperty(required = true, value = "")
    @NotNull

    @Valid

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Store officeLocation(Location officeLocation) {
        this.officeLocation = officeLocation;
        return this;
    }

    /**
     * Get officeLocation
     *
     * @return officeLocation
     **/
    @ApiModelProperty(value = "")

    @Valid

    public Location getOfficeLocation() {
        return officeLocation;
    }

    public void setOfficeLocation(Location officeLocation) {
        this.officeLocation = officeLocation;
    }

    public Store billingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
        return this;
    }

    /**
     * billing address of the Store
     *
     * @return billingAddress
     **/
    @ApiModelProperty(required = true, value = "billing address of the Store ")
    @NotNull

    @Pattern(regexp = "^[a-zA-Z0-9 ]*$")
    @Size(max = 1000)
    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public Store deliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
        return this;
    }

    /**
     * delivery address of the Store
     *
     * @return deliveryAddress
     **/
    @ApiModelProperty(required = true, value = "delivery address of the Store ")
    @NotNull

    @Pattern(regexp = "^[a-zA-Z0-9 ]*$")
    @Size(max = 1000)
    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public Store contactNo1(String contactNo1) {
        this.contactNo1 = contactNo1;
        return this;
    }

    /**
     * contact no1 of the Store
     *
     * @return contactNo1
     **/
    @ApiModelProperty(value = "contact no1 of the Store ")

    @Pattern(regexp = "^[0-9]*$")
    @Size(max = 10)
    public String getContactNo1() {
        return contactNo1;
    }

    public void setContactNo1(String contactNo1) {
        this.contactNo1 = contactNo1;
    }

    public Store contactNo2(String contactNo2) {
        this.contactNo2 = contactNo2;
        return this;
    }

    /**
     * contact no2 of the Store
     *
     * @return contactNo2
     **/
    @ApiModelProperty(value = "contact no2 of the Store ")

    @Pattern(regexp = "^[0-9]*$")
    public String getContactNo2() {
        return contactNo2;
    }

    public void setContactNo2(String contactNo2) {
        this.contactNo2 = contactNo2;
    }

    public Store email(String email) {
        this.email = email;
        return this;
    }

    /**
     * email of the Store
     *
     * @return email
     **/
    @ApiModelProperty(value = "email of the Store ")

    @Pattern(regexp = "^$|([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$")
    @Size(max = 100)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Store storeInCharge(Employee storeInCharge) {
        this.storeInCharge = storeInCharge;
        return this;
    }

    /**
     * Get storeInCharge
     *
     * @return storeInCharge
     **/
    @ApiModelProperty(value = "")
    @NotNull
    @Valid

    public Employee getStoreInCharge() {
        return storeInCharge;
    }

    public void setStoreInCharge(Employee storeInCharge) {
        this.storeInCharge = storeInCharge;
    }

    public Store isCentralStore(Boolean isCentralStore) {
        this.isCentralStore = isCentralStore;
        return this;
    }

    /**
     * is central store of the Store
     *
     * @return isCentralStore
     **/
    @ApiModelProperty(value = "is central store of the Store ")

    public Boolean getIsCentralStore() {
        return isCentralStore;
    }

    public void setIsCentralStore(Boolean isCentralStore) {
        this.isCentralStore = isCentralStore;
    }

    public Store active(Boolean active) {
        this.active = active;
        return this;
    }

    /**
     * Whether Store is Active or not. If the value is TRUE, then Store is
     * active,If the value is FALSE then Store is inactive,Default value is TRUE
     *
     * @return active
     **/
    @ApiModelProperty(value = "Whether Store is Active or not. If the value is TRUE, then Store is active,If the value is FALSE then Store is inactive,Default value is TRUE ")

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Store auditDetails(AuditDetails auditDetails) {
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
        Store store = (Store) o;
        return Objects.equals(this.id, store.id)
                && Objects.equals(this.tenantId, store.tenantId)
                && Objects.equals(this.code, store.code)
                && Objects.equals(this.name, store.name)
                && Objects.equals(this.description, store.description)
                && Objects.equals(this.department, store.department)
                && Objects.equals(this.officeLocation, store.officeLocation)
                && Objects.equals(this.billingAddress, store.billingAddress)
                && Objects.equals(this.deliveryAddress, store.deliveryAddress)
                && Objects.equals(this.contactNo1, store.contactNo1)
                && Objects.equals(this.contactNo2, store.contactNo2)
                && Objects.equals(this.email, store.email)
                && Objects.equals(this.storeInCharge, store.storeInCharge)
                && Objects.equals(this.isCentralStore, store.isCentralStore)
                && Objects.equals(this.active, store.active)
                && Objects.equals(this.auditDetails, store.auditDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantId, code, name, description, department,
                officeLocation, billingAddress, deliveryAddress, contactNo1,
                contactNo2, email, storeInCharge, isCentralStore, active,
                auditDetails);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Store {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    tenantId: ").append(toIndentedString(tenantId))
                .append("\n");
        sb.append("    code: ").append(toIndentedString(code)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    description: ").append(toIndentedString(description))
                .append("\n");
        sb.append("    department: ").append(toIndentedString(department))
                .append("\n");
        sb.append("    officeLocation: ")
                .append(toIndentedString(officeLocation)).append("\n");
        sb.append("    billingAddress: ")
                .append(toIndentedString(billingAddress)).append("\n");
        sb.append("    deliveryAddress: ")
                .append(toIndentedString(deliveryAddress)).append("\n");
        sb.append("    contactNo1: ").append(toIndentedString(contactNo1))
                .append("\n");
        sb.append("    contactNo2: ").append(toIndentedString(contactNo2))
                .append("\n");
        sb.append("    email: ").append(toIndentedString(email)).append("\n");
        sb.append("    storeInCharge: ").append(toIndentedString(storeInCharge))
                .append("\n");
        sb.append("    isCentralStore: ")
                .append(toIndentedString(isCentralStore)).append("\n");
        sb.append("    active: ").append(toIndentedString(active)).append("\n");
        sb.append("    auditDetails: ").append(toIndentedString(auditDetails))
                .append("\n");
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

