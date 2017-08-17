package org.egov.web.indexer.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.web.indexer.contract.Connection;
import org.egov.web.indexer.contract.ConnectionDocument;
import org.egov.web.indexer.contract.WaterConnectionReq;
import org.egov.web.indexer.enricher.ConnectionDocumentEnricher;
import org.egov.web.indexer.repository.ConnectionRepository;
import org.springframework.stereotype.Service;

@Service
public class DocumentService {
    private List<ConnectionDocumentEnricher> documentEnrichers;
    private ConnectionRepository connectionRepository;

    public DocumentService(List<ConnectionDocumentEnricher> documentEnrichers,
    		ConnectionRepository connectionRepository) {
        this.documentEnrichers = documentEnrichers;
        this.connectionRepository = connectionRepository;
    }

    public List<ConnectionDocument> enrich(WaterConnectionReq waterConnectionReq) {
        final List<ConnectionDocument> documents = new ArrayList<>();
        final Connection connection= waterConnectionReq.getConnection();
        final List<Connection> connectionList = connectionRepository
            .fetchConnectionByAcknowledgement(connection.getAcknowledgementNumber(), connection.getTenantId());
        
        for(Connection waterConnection: connectionList) {
            ConnectionDocument document = new ConnectionDocument();
            document.setAcknowledgementNumber(waterConnection.getAcknowledgementNumber());
            document.setApplicationType(waterConnection.getApplicationType());
            document.setBillingType(waterConnection.getBillingType());
            document.setCategoryId(waterConnection.getCategoryId());
            document.setConnectionId(waterConnection.getId());
            document.setConnectionStatus(waterConnection.getStatus());
            document.setConnectionType(waterConnection.getConnectionType());
            document.setConsumerNumber(waterConnection.getConsumerNumber());
            document.setPipesizeId(waterConnection.getPipesizeId());
            document.setPropertyIdentifier(waterConnection.getPropertyIdentifier());
            document.setSourceTypeId(waterConnection.getSourceTypeId());
            document.setStateId(waterConnection.getStateId());
            document.setStatus(waterConnection.getStatus());
            document.setSupplyTypeId(waterConnection.getSupplyTypeId());
            document.setTenantId(waterConnection.getTenantId());
            document.setWaterTreatmentId(waterConnection.getWaterTreatmentId());
            documents.add(document);
        }
        
        documentEnrichers.stream()
            .filter(enricher -> enricher.matches(waterConnectionReq))
            .forEach(documentEnricher -> documentEnricher.enrich(waterConnectionReq, documents));
        return documents;
    }
}
