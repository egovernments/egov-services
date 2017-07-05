package org.egov.collection.indexer.enricher;

import org.egov.collection.indexer.contract.Receipt;
import org.egov.collection.indexer.contract.ReceiptRequest;
import org.egov.collection.indexer.repository.contract.ReceiptRequestDocument;
import org.springframework.stereotype.Service;

@Service
public class ReceiptDocumentEnricher implements ReceiptRequestDocumentEnricher {
    @Override
    public boolean matches(ReceiptRequest receiptRequest) {
        return true;
    }

    @Override
    public void enrich(ReceiptRequest receiptRequest, ReceiptRequestDocument document) {
        Receipt receipt = receiptRequest.getReceipt();
        document.setTenantId(receipt.getTenantId());
        document.setAdvanceAmount(receipt.getAdvanceAmount());
        document.setReceiptDate(receipt.getReceiptDate());
        document.setConsumerCode(receipt.getConsumerCode());
        document.setReceiptNumber(receipt.getReceiptNumber());
        document.setArrearAmount(receipt.getArrearAmount());
        document.setArrearCess(receipt.getArrearCess());
        document.setBillingService(receipt.getBillingService());
        document.setChannel(receipt.getChannel());
        document.setCityCode(receipt.getCityCode());
        document.setCityGrade(receipt.getCityGrade());
        document.setCityName(receipt.getCityName());
        document.setConsumerName(receipt.getConsumerName());
        document.setConsumerType(receipt.getConsumerType());

    }
}
