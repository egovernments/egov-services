package org.egov.egf.bill.web.contract;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.egov.common.web.contract.AuditableContract;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@JsonPropertyOrder({ "id","type","subType","key","description"})
public class ChecklistContract extends AuditableContract {
    /**
     * id is the unique identifier
     */
    @Size(max = 50)
    private String id;
    /**
     * type refers to the name of the entities. If the check list is required for Bill then type will be billRegister
     * 
     */
    @Size(max = 50, min = 1)
    private String type;
    /**
     * subType refers to the different types of  entities. If the check list is required for Bill of type expenseBill 
     * then subtype value is expensebill. 
     * this is unique combination type and subtype is unique
     * 
     */
    
    @Size(max = 50, min = 1)
    private String subType;
    /**
     * key is the content of the check list . 
     * exmaple. 1. Passport copy attached
     * 		2. Ration card copy attached etc
     */
    @Size(max = 250, min = 3)
    private String key;
 
  
    /**
     * description is further detailed discription of the check list values
     * 
     */
    @Size(max = 250, min = 3)
    private String description;
}