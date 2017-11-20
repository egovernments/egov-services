package org.egov.web.indexer.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.web.indexer.contract.SevaRequest;
import org.egov.web.indexer.repository.ElasticSearchRepository;
import org.egov.web.indexer.repository.contract.ServiceRequestDocument;
import org.egov.web.indexer.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

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
    public void listen(HashMap<String, Object> sevaRequestMap) throws UnsupportedEncodingException {
        SevaRequest sevaRequest = objectMapper.convertValue(sevaRequestMap, SevaRequest.class);
        final ServiceRequestDocument document = documentService.enrich(sevaRequest);
        elasticSearchRepository.index(document);
    }
}
