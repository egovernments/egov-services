package org.egov.inv.domain.repository;

import io.swagger.model.PriceList;
import io.swagger.model.PriceListRequest;
import io.swagger.model.PriceListSearchRequest;
import io.swagger.model.Pagination;
import org.egov.inv.persistense.repository.PriceListJdbcRepository;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PriceListRepository {

    private LogAwareKafkaTemplate<String, Object> logAwareKafkaTemplate;

    private String priceListSaveTopicName;

    private String priceListUpdateTopicName;

    private PriceListJdbcRepository priceListJdbcRepository;

    @Autowired
    public PriceListRepository(LogAwareKafkaTemplate logAwareKafkaTemplate,
                              @Value("${inv.pricelist.save.topic}") String priceListSaveTopicName,
                              @Value("${inv.pricelist.update.topic}") String priceListsUpdateTopicName,
                              PriceListJdbcRepository priceListJdbcRepository) {
        this.logAwareKafkaTemplate = logAwareKafkaTemplate;
        this.priceListSaveTopicName = priceListSaveTopicName;
        this.priceListUpdateTopicName = priceListsUpdateTopicName;
        this.priceListJdbcRepository = priceListJdbcRepository;
    }

    public void save(PriceListRequest priceListRequest) {

        logAwareKafkaTemplate.send(priceListSaveTopicName, priceListRequest);

    }

    public void update(PriceListRequest priceListRequest) {

        logAwareKafkaTemplate.send(priceListUpdateTopicName, priceListRequest);

    }

    public Pagination<PriceList> search(PriceListSearchRequest priceListSearchRequest) {
        return priceListJdbcRepository.search(priceListSearchRequest);
    }

}
