package org.egov.inv.domain.repository;

import org.egov.inv.domain.model.Pagination;
import org.egov.inv.domain.model.Store;
import org.egov.inv.persistence.repository.StoreJdbcRepository;
import org.egov.inv.web.contract.StoreGetRequest;
import org.egov.inv.web.contract.StoreRequest;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class StoreRepository {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Value("${egov.inv.store.save.topic}")
    private String createTopic;

    @Value("${egov.inv.store.update.topic}")
    private String updateTopic;

    @Autowired
    private StoreJdbcRepository storeJdbcRepository;

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

    public Pagination<Store> search(StoreGetRequest storeGetRequest) {
        return storeJdbcRepository.search(storeGetRequest);

    }

    public boolean checkStoreCodeExists(String code, String tenantId) {
        String selectQuery = storeJdbcRepository.checkCodeExistsQuery();
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("code", code);
        paramsMap.put("tenantid", tenantId);
        List<String> storeIds = namedParameterJdbcTemplate.queryForList(selectQuery, paramsMap, String.class);
        if (storeIds.isEmpty())
            return false;
        else
            return true;
    }

}
