package org.egov.egf.bill.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillChecklist {
    
	/**
     * id is the unique identifier
     */
    private String id;
    
    /**
     * bill is the id of the bill
     */
    private BillRegister bill;
    
    /**
     *checklist is the id of the check list 
     */
    private Checklist checklist;
    
    /**
     * checklistValue is the value entered for the checklist
     */
    private String checklistValue;
    
}
