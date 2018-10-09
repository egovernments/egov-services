package org.egov.collection.consumer;

import java.io.IOException;
import java.util.Date;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.collection.consumer.model.BusinessDetails;
import org.egov.collection.consumer.model.ReceiptRequest;
import org.egov.collection.consumer.model.VoucherResponse;
import org.egov.collection.consumer.service.BusinessDetailsService;
import org.egov.collection.consumer.service.InstrumentService;
import org.egov.collection.consumer.service.VoucherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ReceiptVoucherListener {

    public static final Logger LOGGER = LoggerFactory.getLogger(ReceiptVoucherListener.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BusinessDetailsService businessDetailsService;

    @Autowired
    private VoucherService voucherService;

    @Autowired
    private InstrumentService instrumentService;

    @KafkaListener(id = "${egov.collection.receipt.voucher.save.id}", topics = "${egov.collection.receipt.voucher.save.topic}", group = "${egov.collection.receipt.voucher.save.group}")
    public void process(ConsumerRecord<String, String> record) {
        ReceiptRequest request = null;
        LOGGER.info("key : " + record.key() + "\t\t" + "value : " + record.value());
        VoucherResponse response = null;
        try {
            request = objectMapper.readValue(record.value(), ReceiptRequest.class);
            if (checkVoucherCreation(request)) {

                response = voucherService.createVoucher(request);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        instrumentService.createInstruemtn(request, response);
        System.out.println(response);
    }

    public Boolean checkVoucherCreation(final ReceiptRequest request) {
        Boolean createVoucherForBillingService = Boolean.FALSE;
        if (request != null && request.getReceipt() != null && !request.getReceipt().isEmpty()
                && request.getReceipt().get(0).getBill() != null && !request.getReceipt().get(0).getBill().isEmpty()) {

            BusinessDetails servcie = businessDetailsService.getBusinessDetailsByCode(
                    request.getReceipt().get(0).getBill().get(0).getBillDetails().get(0).getBusinessService(),
                    request.getTenantId());
            if (servcie != null) {
                if (servcie.getVoucherCutoffDate() != null
                        && request.getReceipt().get(0).getBill().get(0).getBillDetails().get(0).getReceiptDate() != null
                        && new Date(request.getReceipt().get(0).getBill().get(0).getBillDetails().get(0).getReceiptDate())
                                .compareTo(new Date(servcie.getVoucherCutoffDate())) > 0) {
                    if (servcie.getVoucherCreation() != null)
                        createVoucherForBillingService = servcie.getVoucherCreation();
                } else if (servcie.getVoucherCutoffDate() == null
                        && servcie.getVoucherCreation() != null)
                    createVoucherForBillingService = servcie.getVoucherCreation();
            }

        }

        return createVoucherForBillingService;
    }

}
