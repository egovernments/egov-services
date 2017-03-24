package org.egov.asset.indexer.consumers;

import java.io.IOException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.asset.contract.AssetDetails;
import org.egov.asset.contract.AssetRequest;
import org.egov.asset.indexer.adaptor.AssetAdaptor;
import org.egov.asset.indexer.repository.ElasticSearchRepository;
import org.egov.asset.model.Asset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SaveAssetConsumer {

	public static final Logger LOGGER = LoggerFactory.getLogger(SaveAssetConsumer.class);

	@Autowired
	private ElasticSearchRepository elasticSearchRepository;

	@Autowired
	private AssetAdaptor assetAdaptor;

	@KafkaListener(containerFactory = "kafkaListenerContainerFactory", topics = { "save-asset-db", "update-asset-db" })
	public void listen(ConsumerRecord<String, String> record) {

		LOGGER.info("key:" + record.key() + ":" + "value:" + record.value());

		LOGGER.debug(" Record topic is -->" + record.topic());

		if (record.topic().equals("save-asset-db") || record.topic().equals("asset-update-db")) {
			AssetRequest assetRequest = getAssetRequestFromJson(record.value());
			if (assetRequest == null)
				return;

			AssetDetails assetDetails = assetAdaptor.getIndexRecord(assetRequest.getAsset(),
					assetRequest.getRequestInfo());
			elasticSearchRepository.saveAsset(assetDetails);
		}
	}

	private AssetRequest getAssetRequestFromJson(String assetRequestJson) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(assetRequestJson, AssetRequest.class);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return null;
		}
	}
}