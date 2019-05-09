package org.egov.receipt.consumer.service;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.mdms.service.TokenService;
import org.egov.receipt.consumer.model.InstrumentResponse;
import org.egov.receipt.consumer.model.InstrumentStatusEnum;
import org.egov.receipt.consumer.model.Receipt;
import org.egov.receipt.consumer.model.ReceiptReq;
import org.egov.receipt.consumer.model.VoucherResponse;
import org.egov.reciept.consumer.config.PropertiesManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

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
	
	public static final Logger LOGGER = LoggerFactory.getLogger(EgfKafkaListener.class);
	
	@KafkaListener(topics = "${egov.collection.receipt.voucher.save.topic}")
    public void processToCreateVoucher(ConsumerRecord<String, String> record) {
		ReceiptReq request = null;
        LOGGER.info("topic : " + record.topic() + "\t\t" + "value : " + record.value());
        VoucherResponse voucherResponse = null;
        try {
            request = objectMapper.readValue(record.value(), ReceiptReq.class);
            LOGGER.info("Recieved reciept request : "+request);
               if (voucherService.isVoucherCretionEnabled(request)) {
            	   voucherResponse = voucherService.createVoucher(request);
            	   LOGGER.info("voucherResponse : "+voucherResponse);
            	   if(voucherResponse != null){
            		   String status = voucherResponse.getResponseInfo().getStatus();
            		   if(status.equals("201")){
            			   LOGGER.error("Voucher created successfully with voucher number : "+voucherResponse.getVouchers().get(0).getVoucherNumber());
            		   }
            		   receiptService.updateReceipt(request, voucherResponse);
            		   instrumentService.createInstrument(request, voucherResponse);
            	   }
               }else{
            	   LOGGER.info("Voucher Creation is not enabled for business service code : "+request.getReceipt().get(0).getBill().get(0).getBillDetails().get(0).getBusinessService());
               }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
	
	@KafkaListener(topics = "${egov.collection.receipt.voucher.cancel.topic}")
    public void processToCancelVoucher(ConsumerRecord<String, String> record) {
		ReceiptReq request = null;
        LOGGER.debug("key : " + record.key() + "\t\t" + "value : " + record.value());
        try {
            request = objectMapper.readValue(record.value(), ReceiptReq.class);
            LOGGER.info("Recieved reciept request : "+request);
            if(voucherService.isVoucherExists(request)){
            	voucherService.cancelVoucher(request);
            	instrumentService.cancelOrDishonorInstrument(request);
            }else{
            	LOGGER.info("Voucher is not found for voucherNumber : "+request.getReceipt().get(0).getBill().get(0).getBillDetails().get(0).getVoucherHeader());
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}
