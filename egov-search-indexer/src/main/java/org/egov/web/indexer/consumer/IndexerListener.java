package org.egov.web.indexer.consumer;

import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.web.indexer.adaptor.ComplaintAdapter;
import org.egov.web.indexer.config.IndexerPropertiesManager;
import org.egov.web.indexer.contract.SevaRequest;
import org.egov.web.indexer.models.ComplaintIndex;
import org.egov.web.indexer.service.AssignmentService;
import org.egov.web.indexer.service.BoundaryService;
import org.egov.web.indexer.service.CityService;
import org.egov.web.indexer.service.ComplaintTypeService;
import org.egov.web.indexer.service.ElasticSearchIndexerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import com.fasterxml.jackson.databind.ObjectMapper;

public class IndexerListener {

	public static final String OBJECT_TYPE_COMPLAINT = "complaint";

	@Autowired
	private ElasticSearchIndexerService elasticSearchIndexerService;
	@Autowired
	private IndexerPropertiesManager propertiesManager;
	@Autowired
	private BoundaryService boundaryService;
	@Autowired
	private ComplaintTypeService complaintTypeService;
	@Autowired
	private CityService cityService;
	@Autowired
	private AssignmentService assignmentService;

	/**
	 * A key/value pair to be received from Kafka. Format of Value map:
	 * {"RequestInfo":{},"ServiceRequest":{"serviceRequestId":"somecrn","status"
	 * :true,"statusNotes":"COMPLETED", "values": {"locationId":"172",
	 * "childLocationId":"176"}}}
	 */
	@KafkaListener(id = "${kafka.topics.egov.index.id}", topics = "${kafka.topics.egov.index.name}", group = "${kafka.topics.egov.index.group}")
	public void listen(ConsumerRecord<String, Map> record) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			SevaRequest sevaReq = objectMapper.convertValue(record.value(), SevaRequest.class);
			ComplaintAdapter complaintAdapter = new ComplaintAdapter(propertiesManager, boundaryService,
					complaintTypeService, cityService, assignmentService);
			ComplaintIndex complaintIndex = complaintAdapter.index(sevaReq.getServiceRequest());
			elasticSearchIndexerService.indexObject(OBJECT_TYPE_COMPLAINT, complaintIndex.getCrn(), complaintIndex);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
