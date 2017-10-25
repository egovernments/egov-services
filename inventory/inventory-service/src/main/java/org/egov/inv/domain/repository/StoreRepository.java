package org.egov.inv.domain.repository;

import java.util.List;

import org.egov.inv.domain.model.Store;
import org.egov.inv.web.contract.StoreRequest;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
public class StoreRepository {
    
    @Value("${egov.inv.store.save.topic}")
    private  String createTopic;
    
    @Value("${egov.inv.store.update.topic}")
    private String updateTopic;
    
    @Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    public List<Store> create(StoreRequest storeRequest) {
        kafkaTemplate.send(createTopic, storeRequest);
        return storeRequest.getStores();
    }

    public List<Store> update(StoreRequest storeRequest) {
        kafkaTemplate.send(updateTopic, storeRequest);
        return storeRequest.getStores();
    }

}
