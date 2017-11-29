package org.egov.works.measurementbook.persistence.helper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.works.measurementbook.web.contract.ContractorBill;
import org.egov.works.measurementbook.web.contract.MBContractorBills;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MBContractorBillHelper {

    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("measurementBook")
    private String measurementBook = null;

    @JsonProperty("contractorBill")
    private String contractorBill = null;

    public MBContractorBills toDomain() {
        MBContractorBills mbContractorBills = new MBContractorBills();
        mbContractorBills.setId(this.id);
        mbContractorBills.setTenantId(this.tenantId);
        mbContractorBills.setMeasurementBook(this.measurementBook);
        mbContractorBills.setContractorBill(new ContractorBill());//No propeties generated in contractorbill
        return mbContractorBills;
    }

}


