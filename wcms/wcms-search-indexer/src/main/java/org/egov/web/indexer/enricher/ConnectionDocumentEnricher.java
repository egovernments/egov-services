package org.egov.web.indexer.enricher;

import java.util.List;

import org.egov.web.indexer.contract.ConnectionDocument;
import org.egov.web.indexer.contract.WaterConnectionReq;

public interface ConnectionDocumentEnricher {
    boolean matches(WaterConnectionReq waterConnectionRequest);
    void enrich(WaterConnectionReq waterConnectionReques, List<ConnectionDocument> documents);
}
