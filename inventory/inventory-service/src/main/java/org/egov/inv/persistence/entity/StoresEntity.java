package org.egov.inv.persistence.entity;



import org.egov.inv.domain.model.AuditDetails;
import org.egov.inv.domain.model.Department;
import org.egov.inv.domain.model.Employee;
import org.egov.inv.domain.model.Store;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StoresEntity {

   private String id = null;
   
    private String code = null;
    
    private String name = null;
    
    private String description = null;
    
    private String departmentCode = null;
    
    private String storeInChargeCode = null;
    
    private String billingAddress = null;
    
    private String deliveryAddress = null;
    
    private String contactnumber1 = null;
    
    private String contactnumber2 = null;
    
    private String emailid = null;
    
    private Boolean isCentralStore = null;
    
    private Boolean active = null;
    
    private String tenantId = null;
    
    private String createdBy = null;
    
    private Long createdDate = null;
    
    private String lastmodifiedBy = null;
    
    private Long lastmodifiedDate = null;
    
    public Store toDomain(){
        Store store = new Store();
        store.setId(id);
        store.setCode(code);
        store.setName(name);
        store.setDescription(description);
        store.setActive(active);
        store.setBillingAddress(billingAddress);
        store.setDeliveryAddress(deliveryAddress);
        store.setContactNo1(contactnumber1);
        store.setContactNo2(contactnumber2);
        store.setEmail(emailid);
        store.setIsCentralStore(isCentralStore);
        AuditDetails auditDetails = new AuditDetails();
        auditDetails.setCreatedBy(createdBy);
        auditDetails.setCreatedTime(createdDate);
        auditDetails.setLastModifiedBy(lastmodifiedBy);
        auditDetails.setLastModifiedTime(lastmodifiedDate);
        store.setAuditDetails(auditDetails);
        Department department = new Department();
        department.setCode(departmentCode);
        store.setDepartment(department);
        Employee employee = new Employee();
        employee.setCode(storeInChargeCode);
        store.setStoreInCharge(employee);
        return store;
        
    }
    
}
