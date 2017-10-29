package org.egov.inv.persistense.repository;

import io.swagger.model.Store;
import io.swagger.model.StoreRequest;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true)
public class StoreRepository {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Value("${inv.store.save.topic}")
    private String createTopic;

    @Value("${inv.store.update.topic}")
    private String updateTopic;


    @Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    public List<Store> create(StoreRequest storeRequest) {
        kafkaTemplate.send(createTopic, storeRequest);
        return storeRequest.getStores();
    }


}
