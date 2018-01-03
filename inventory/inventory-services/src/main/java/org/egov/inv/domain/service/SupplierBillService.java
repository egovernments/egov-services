package org.egov.inv.domain.service;

import org.egov.common.DomainService;
import org.egov.inv.model.*;
import org.egov.inv.persistence.repository.SupplierBillJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierBillService extends DomainService {

    @Value("${inv.supplierbill.save.topic}")
    private String saveTopic;

    @Value("${inv.supplierbill.save.key}")
    private String savekey;

    @Autowired
    private SupplierBillJdbcRepository supplierBillJdbcRepository;

    public SupplierBillResponse create(SupplierBillRequest supplierBillRequest, String tenantId) {
        List<SupplierBill> supplierBillList = supplierBillRequest.getSupplierBills();
        for (SupplierBill supplierBill : supplierBillList) {
            //Set Supplier Bill Id
            supplierBill.setId(supplierBillJdbcRepository.getSequence("seq_supplierbill"));

            //Set Supplier Bill Receipt Id
            for (SupplierBillReceipt supplierBillReceipt : supplierBill.getSupplierBillReceipts()) {
                supplierBillReceipt.setId(supplierBillJdbcRepository.getSequence("seq_supplierbillreceipt"));
            }

            //Set Supplier Bill Advance Adjustment Id
            for (SupplierBillAdvanceAdjustment supplierBillAdvanceAdjustment : supplierBill.getSupplierBillAdvanceAdjustments()) {
                supplierBillAdvanceAdjustment.setId(supplierBillJdbcRepository.getSequence("seq_supplierbilladvanceadjustment"));

                //Set Supplier Advance Requisition Id
                supplierBillAdvanceAdjustment.getSupplierAdvanceRequisition().setId(supplierBillJdbcRepository.getSequence("seq_supplieradvancerequisition"));
            }

        }

        kafkaQue.send(saveTopic, savekey, supplierBillRequest);

        SupplierBillResponse supplierBillResponse = new SupplierBillResponse();
        return supplierBillResponse.
                responseInfo(null)
                .supplierBills(supplierBillRequest.getSupplierBills());
    }


    public SupplierBillResponse update(SupplierBillRequest supplierBillRequest, String tenantId) {

        kafkaQue.send(saveTopic, savekey, supplierBillRequest);

        SupplierBillResponse supplierBillResponse = new SupplierBillResponse();
        return supplierBillResponse.
                responseInfo(null)
                .supplierBills(supplierBillRequest.getSupplierBills());
    }
}
