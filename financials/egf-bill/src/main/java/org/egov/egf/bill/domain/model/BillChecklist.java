package org.egov.egf.bill.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.egov.common.domain.model.Auditable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillChecklist extends Auditable {
    
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
