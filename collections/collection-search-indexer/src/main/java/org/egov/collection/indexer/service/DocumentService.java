package org.egov.collection.indexer.service;

import org.egov.collection.indexer.contract.Receipt;
import org.egov.collection.indexer.contract.ReceiptRequest;
import org.egov.collection.indexer.enricher.ReceiptRequestDocumentEnricher;
import org.egov.collection.indexer.repository.contract.ReceiptRequestDocument;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentService {
    private List<ReceiptRequestDocumentEnricher> documentEnrichers;

    public DocumentService(List<ReceiptRequestDocumentEnricher> documentEnrichers) {
        this.documentEnrichers = documentEnrichers;
    }

    public ReceiptRequestDocument enrich(ReceiptRequest receiptRequest) {
        final ReceiptRequestDocument document = new ReceiptRequestDocument();
        final Receipt receipt = receiptRequest.getReceipt();
        documentEnrichers.stream()
            .filter(enricher -> enricher.matches(receiptRequest))
            .forEach(documentEnricher -> documentEnricher.enrich(receiptRequest, document));
        return document;
    }
}
