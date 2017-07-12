package org.egov.collection.indexer.service;

import org.egov.collection.indexer.contract.*;
import org.egov.collection.indexer.enricher.ReceiptRequestDocumentEnricher;
import org.egov.collection.indexer.repository.contract.ReceiptRequestDocument;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentService {
    private List<ReceiptRequestDocumentEnricher> documentEnrichers;

    public DocumentService(List<ReceiptRequestDocumentEnricher> documentEnrichers) {
        this.documentEnrichers = documentEnrichers;
    }

    public List<ReceiptRequestDocument> enrich(ReceiptRequest receiptRequest) {
        final List<ReceiptRequestDocument> documents = new ArrayList<ReceiptRequestDocument>();
        final Receipt receipt = receiptRequest.getReceipt();
        BillWrapper billWrapper = receipt.getBillInfoWrapper();
        List<BillDetailsWrapper> billDetails = billWrapper.getBillDetailsWrapper();
        Bill bill = billWrapper.getBillInfo();
        for(BillDetailsWrapper billDetail:billDetails) {
            ReceiptRequestDocument document = new ReceiptRequestDocument();
            document.setTenantId(billDetail.getBillDetails().getTenantId());
            document.setPaymentMode(receipt.getInstrumentType());
            document.setConsumerName(bill.getPayeeName());
            document.setConsumerType(billDetail.getBillDetails().getConsumerType());
            document.setConsumerCode(billDetail.getBillDetails().getConsumerCode());
            document.setReceiptNumber(billDetail.getReceiptNumber());
            document.setReceiptDate(billDetail.getReceiptDate().toString());
            document.setChannel(billDetail.getChannel());
            document.setPaymentMode(billDetail.getCollectionType());
            document.setTotalAmount(billDetail.getBillDetails().getTotalAmount());
            documents.add(document);
        }
        documentEnrichers.stream()
            .filter(enricher -> enricher.matches(receiptRequest))
            .forEach(documentEnricher -> documentEnricher.enrich(receiptRequest, documents));
        return documents;
    }
}
