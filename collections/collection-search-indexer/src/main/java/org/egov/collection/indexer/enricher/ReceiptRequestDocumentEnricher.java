package org.egov.collection.indexer.enricher;

import org.egov.collection.indexer.contract.ReceiptRequest;
import org.egov.collection.indexer.repository.contract.ReceiptRequestDocument;

import java.util.List;

public interface ReceiptRequestDocumentEnricher {

    boolean matches(ReceiptRequest receiptRequest);
    void enrich(ReceiptRequest receiptRequest, List<ReceiptRequestDocument> document);
}
