package org.egov.egf.bill.web.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.egov.common.web.contract.AuditableContract;
import org.egov.egf.bill.domain.model.BillRegister;
import org.egov.egf.bill.domain.model.Checklist;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@JsonPropertyOrder({ "id","bill","checklist","checklistValue"})
public class BillChecklistContract extends AuditableContract {

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
