package org.egov.inv.domain.service;

import org.egov.common.DomainService;
import org.egov.common.Pagination;
import org.egov.inv.model.*;
import org.egov.inv.persistence.repository.SupplierAdvanceRequisitionJdbcRepository;
import org.egov.inv.persistence.repository.SupplierBillAdvanceAdjusmentJdbcRepository;
import org.egov.inv.persistence.repository.SupplierBillJdbcRepository;
import org.egov.inv.persistence.repository.SupplierBillReceiptJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SupplierBillService extends DomainService {

    @Value("${inv.supplierbill.save.topic}")
    private String saveTopic;

    @Value("${inv.supplierbill.save.key}")
    private String savekey;

    @Autowired
    private SupplierBillJdbcRepository supplierBillJdbcRepository;

    @Autowired
    private SupplierBillReceiptJdbcRepository supplierBillReceiptJdbcRepository;

    @Autowired
    private SupplierBillAdvanceAdjusmentJdbcRepository supplierBillAdvanceAdjusmentJdbcRepository;

    @Autowired
    private SupplierAdvanceRequisitionJdbcRepository supplierAdvanceRequisitionJdbcRepository;

    public SupplierBillResponse create(SupplierBillRequest supplierBillRequest, String tenantId) {
        List<SupplierBill> supplierBillList = supplierBillRequest.getSupplierBills();
        for (SupplierBill supplierBill : supplierBillList) {
            //Set Supplier Bill Id
            supplierBill.setId(supplierBillJdbcRepository.getSequence("seq_supplierbill"));

            for (SupplierBillReceipt supplierBillReceipt : supplierBill.getSupplierBillReceipts()) {
                //Set Supplier Bill Receipt Id
                supplierBillReceipt.setId(supplierBillJdbcRepository.getSequence("seq_supplierbillreceipt"));
                supplierBillReceipt.setSupplierBill(supplierBill.getId());
            }

            for (SupplierBillAdvanceAdjustment supplierBillAdvanceAdjustment : supplierBill.getSupplierBillAdvanceAdjustments()) {
                //Set Supplier Bill Advance Adjustment Id
                supplierBillAdvanceAdjustment.setId(supplierBillJdbcRepository.getSequence("seq_supplierbilladvanceadjustment"));
                supplierBillAdvanceAdjustment.supplierBill(supplierBill.getId());
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

    public SupplierBillResponse search(SupplierBillSearch supplierBillSearch) {

        SupplierBillResponse supplierBillResponse = new SupplierBillResponse();

        Pagination<SupplierBill> supplierBills = supplierBillJdbcRepository.search(supplierBillSearch);

        if (supplierBills.getPagedData().size() > 0) {
            for (SupplierBill supplierBill : supplierBills.getPagedData()) {

                //fetch supplier bill receipts
                SupplierBillReceiptSearch supplierBillReceiptSearch = SupplierBillReceiptSearch.builder()
                        .supplierBill(supplierBill.getId())
                        .build();

                Pagination<SupplierBillReceipt> billReceiptPagination = supplierBillReceiptJdbcRepository.search(supplierBillReceiptSearch);

                if (billReceiptPagination.getPagedData().size() > 0) {
                    supplierBill.setSupplierBillReceipts(billReceiptPagination.getPagedData());
                } else {
                    supplierBill.setSupplierBillReceipts(Collections.EMPTY_LIST);
                }

                //fetch supplier bill advance adjustments
                SupplierBillAdvanceAdjustmentSearch supplierBillAdvanceAdjustmentSearch = SupplierBillAdvanceAdjustmentSearch.builder()
                        .supplierBill(supplierBill.getId())
                        .build();

                Pagination<SupplierBillAdvanceAdjustment> advanceAdjustmentPagination = supplierBillAdvanceAdjusmentJdbcRepository.search(supplierBillAdvanceAdjustmentSearch);

                if (advanceAdjustmentPagination.getPagedData().size() > 0) {

                    supplierBill.setSupplierBillAdvanceAdjustments(advanceAdjustmentPagination.getPagedData());

                } else {
                    supplierBill.setSupplierBillAdvanceAdjustments(Collections.EMPTY_LIST);
                }

            }
            return supplierBillResponse.
                    responseInfo(null)
                    .supplierBills(supplierBills.getPagedData());

        } else return supplierBillResponse.
                responseInfo(null)
                .supplierBills(Collections.EMPTY_LIST);


    }
}
