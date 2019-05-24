package org.egov.receipt.consumer.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.receipt.consumer.entity.VoucherIntegrationLog;
import org.egov.receipt.consumer.model.FinanceMdmsModel;
import org.egov.receipt.consumer.model.ReceiptReq;
import org.egov.receipt.consumer.model.VoucherResponse;
import org.egov.receipt.consumer.repository.VoucherIntegartionLogRepository;
import org.egov.reciept.consumer.config.PropertiesManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EgfKafkaListener {
	@Autowired
    private ObjectMapper objectMapper;
	@Autowired
	private VoucherService voucherService;
	@Autowired
	private ReceiptService receiptService;
	@Autowired
	private InstrumentService instrumentService;
	@Autowired
	private VoucherIntegartionLogRepository voucherIntegartionLogRepository;
	@Autowired
	private PropertiesManager manager;
	
	public static final Logger LOGGER = LoggerFactory.getLogger(EgfKafkaListener.class);
	private static final String RECEIPT_TYPE = "Receipt";
	private static String voucherNumber = "";
	
	@KafkaListener(topics = {"${egov.collection.receipt.voucher.save.topic}","${egov.collection.receipt.voucher.cancel.topic}"})	
    public void process(ConsumerRecord<String, String> record) {
        VoucherResponse voucherResponse = null;
        ReceiptReq request = null;
        FinanceMdmsModel finSerMdms = new FinanceMdmsModel();
        try {
        	String topic = record.topic();
        	request = objectMapper.readValue(record.value(), ReceiptReq.class);
        	LOGGER.info("topic : {} ,  request : {}", topic, request);
        	if(topic.equals(manager.getVoucherCreateTopic())){
        		if (voucherService.isVoucherCreationEnabled(request, finSerMdms)) {
        			voucherResponse = voucherService.createReceiptVoucher(request, finSerMdms);
        			voucherNumber = voucherResponse.getVouchers().get(0).getVoucherNumber();
        			receiptService.updateReceipt(request, voucherResponse);
        			instrumentService.createInstrument(request, voucherResponse);
        			this.getBackupToDB(request,Status.SUCCESS,"Voucher created successfully with voucher number : "+voucherNumber);
        		}else{
        			//Todo : Status should be different
        			this.getBackupToDB(request,Status.FAILED,"Voucher Creation is not enabled for business service code : "+request.getReceipt().get(0).getBill().get(0).getBillDetails().get(0).getBusinessService());
        		}
        	}else if(topic.equals(manager.getVoucherCancelTopic())){
                String voucherNumber = request.getReceipt().get(0).getBill().get(0).getBillDetails().get(0).getVoucherHeader();
                if(voucherService.isVoucherExists(request)){
                	voucherService.cancelReceiptVoucher(request);
                	instrumentService.cancelInstrument(request.getReceipt().get(0),request.getRequestInfo());
     			   	this.getBackupToDB(request,Status.SUCCESS,"Voucher number : "+voucherNumber+" is CANCELLED successfully!");
                }else{
                	this.getBackupToDB(request,Status.FAILED,"Voucher is not found for voucherNumber : "+voucherNumber);
                }
        	}
        } catch (Exception e) {
        	this.getBackupToDB(request,Status.FAILED,e.getMessage());
       		LOGGER.error(e.getMessage());
        }
    }
	
	/**
	 * function use to take a backup to DB after success/failure of voucher creation process.
	 */
	private void getBackupToDB(ReceiptReq request,Status status, String description){
		try {
			VoucherIntegrationLog voucherIntegrationLog = new VoucherIntegrationLog();
			voucherIntegrationLog.setStatus(status.toString());
			voucherIntegrationLog.setDescription(description);
			this.prepareVoucherIntegrationLog(voucherIntegrationLog, request);
			voucherIntegartionLogRepository.saveVoucherIntegrationLog(voucherIntegrationLog);			
		} catch (Exception e) {
			LOGGER.error("ERROR occurred while doing a backup to databases. "+e.getMessage());
		}
	}
	
	private void prepareVoucherIntegrationLog(VoucherIntegrationLog voucherIntegrationLog, ReceiptReq request){
		voucherIntegrationLog.setReferenceNumber(request.getReceipt().get(0).getBill().get(0).getBillDetails().get(0).getReceiptNumber());
		ObjectMapper mappper = new ObjectMapper();
		try {
			String jsonReq = mappper.writeValueAsString(request);
			voucherIntegrationLog.setRequestJson(jsonReq);
		} catch (JsonProcessingException e) {
			LOGGER.error("ERROR occurred while parsing the ReceiptRequest "+e.getMessage());
		}
		voucherIntegrationLog.setVoucherNumber(voucherNumber);
		voucherIntegrationLog.setType(RECEIPT_TYPE);
		voucherIntegrationLog.setTenantId(request.getReceipt().get(0).getTenantId());
	}
}

enum Status{
	FAILED,SUCCESS
}
