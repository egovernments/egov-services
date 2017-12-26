package org.egov.egf.voucher.domain.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.egov.egf.master.web.contract.ChartOfAccountContract;
import org.egov.egf.master.web.contract.FunctionContract;

public class Deduction {
    private String id;
    /**
     * orderId refers to the order in which account heads are created. This
     * field is used to send back the result in same order as created.
     */
    private Integer orderId;
    /** 
     * chartOfAccount is the account head.
     * This account should have mapped to recovery,
     * ie. deduction will consider only those account codes which are mapped in recovery master
     */
    @NotNull
    private ChartOfAccountContract chartOfAccount;
   
   /**
    * deductionAmount is the amount deducted from source.
    * 
    */
    @NotNull
    @Min(value = 0)
    @Max(value = 999999999)
    private BigDecimal deductionAmount;
    /**
     * remitedAmount is the amount paid .
     * if partial payment done the remitedAmount will be less than deductionAmount
     */
    
    private BigDecimal remitedAmount;
      
    private FunctionContract function;
    /**
     * deductionDetails will provide subledger wise details of the deduction.
     * These details will be present only if the recovery account head/code is control code
     */
   // @DrillDownTable
    private Set<DeductionDetail> deductionDetails = new HashSet<DeductionDetail>();
}
