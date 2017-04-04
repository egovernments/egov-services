package org.egov.egf.master.index.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.egf.master.index.domain.model.RequestContext;
import org.egov.egf.master.index.persistence.queue.contract.BankContractRequest;
import org.egov.egf.master.index.persistence.repository.ElasticSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


public class IndexerListener {

	private static final String OBJECT_TYPE = "bank";

	@Autowired
	private ElasticSearchRepository elasticSearchRepository;

	@KafkaListener(id = "${kafka.topics.egov.index.id}", topics = "${kafka.topics.egov.index.name}", group = "${kafka.topics.egov.index.group}")
	public void listen(ConsumerRecord<String, BankContractRequest> record) {
		BankContractRequest sevaRequest = record.value();
		RequestContext.setId("" + sevaRequest.getBank().getId());
		elasticSearchRepository.index(OBJECT_TYPE, sevaRequest.getBank().getCode(), sevaRequest.getBank().getId());
	}

}
