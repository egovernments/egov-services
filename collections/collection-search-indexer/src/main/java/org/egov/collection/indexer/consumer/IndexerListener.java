package org.egov.collection.indexer.consumer;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.collection.indexer.contract.ReceiptRequest;
import org.egov.collection.indexer.repository.ElasticSearchRepository;
import org.egov.collection.indexer.repository.contract.ReceiptRequestDocument;
import org.egov.collection.indexer.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class IndexerListener {


    private ElasticSearchRepository elasticSearchRepository;
    private ObjectMapper objectMapper;
    private DocumentService documentService;

    @Autowired
    public IndexerListener(ElasticSearchRepository elasticSearchRepository,
                           ObjectMapper objectMapper,
                           DocumentService documentService) {
        this.elasticSearchRepository = elasticSearchRepository;
        this.objectMapper = objectMapper;
        this.documentService = documentService;
    }

    @KafkaListener(topics = "${kafka.topics.egov.index.name}")
    public void listen(HashMap<String, Object> receiptRequestMap) {
        ReceiptRequest receiptRequest = objectMapper.convertValue(receiptRequestMap, ReceiptRequest.class);
        final List<ReceiptRequestDocument> documents = documentService.enrich(receiptRequest);
        elasticSearchRepository.index(documents);
    }
}
