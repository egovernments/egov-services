package org.egov.demand.consumer;

import java.util.List;
import java.util.Map;

import org.egov.common.contract.request.RequestInfo;
import org.egov.demand.config.ApplicationProperties;
import org.egov.demand.helper.CollectionReceiptRequest;
import org.egov.demand.model.BillDetail.StatusEnum;
import org.egov.demand.model.DemandUpdateMisRequest;
import org.egov.demand.model.enums.Status;
import org.egov.demand.repository.BillRepository;
import org.egov.demand.service.BusinessServDetailService;
import org.egov.demand.service.DemandAdjustmentService;
import org.egov.demand.service.DemandService;
import org.egov.demand.service.GlCodeMasterService;
import org.egov.demand.service.ReceiptService;
import org.egov.demand.service.TaxHeadMasterService;
import org.egov.demand.service.TaxPeriodService;
import org.egov.demand.web.contract.*;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BillingServiceConsumer {

	@Autowired
	private ApplicationProperties applicationProperties;

	@Autowired
	private DemandService demandService;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private BillRepository billRepository;

	@Autowired
	private ReceiptService receiptService;

	@Autowired
	private DemandAdjustmentService demandAdjustmentService;

	@KafkaListener(topics = { "${kafka.topics.receipt.update.collecteReceipt}", "${kafka.topics.save.bill}",
			"${kafka.topics.update.bill}", "${kafka.topics.save.demand}", "${kafka.topics.update.demand}",
			"${kafka.topics.receipt.update.demand}", "${kafka.topics.receipt.cancel.name}" })
	public void processMessage(Map<String, Object> consumerRecord, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		
		log.debug("key:" + topic + ":" + "value:" + consumerRecord);

		try {

			if (applicationProperties.getCreateDemandTopic().equals(topic))
				demandService.save(objectMapper.convertValue(consumerRecord, DemandRequest.class));
			else if (applicationProperties.getUpdateDemandTopic().equals(topic))
				demandService.update(objectMapper.convertValue(consumerRecord, DemandRequest.class));
			else if (topic.equals(applicationProperties.getCreateBillTopic()))
				billRepository.saveBill(objectMapper.convertValue(consumerRecord, BillRequest.class));
			else if (applicationProperties.getUpdateDemandFromReceipt().equals(topic)) {

				CollectionReceiptRequest collectionReceiptRequest = objectMapper.convertValue(consumerRecord,
						CollectionReceiptRequest.class);
				RequestInfo requestInfo = collectionReceiptRequest.getRequestInfo();
				List<Receipt> receipts = collectionReceiptRequest.getReceipt();

				ReceiptRequest receiptRequest = ReceiptRequest.builder().receipt(receipts).requestInfo(requestInfo)
						.tenantId(collectionReceiptRequest.getTenantId()).build();
				log.debug("the receipt request is -------------------" + receiptRequest);

				receiptService.updateDemandFromReceipt(receiptRequest, StatusEnum.CREATED);
			}

			else if (applicationProperties.getReceiptCancellationTopic().equals(topic)) {
				demandAdjustmentService.adjustDemandOnReceiptCancellation(
						objectMapper.convertValue(consumerRecord, ReceiptRequest.class));
			}

		} catch (Exception exception) {
			log.error("processMessage:" + exception);
			throw new CustomException("EG_BS_CONSUMER_EXCEPTION", exception.getMessage());
		}
	}
}
