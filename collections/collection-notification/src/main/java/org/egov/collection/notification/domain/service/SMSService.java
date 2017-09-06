package org.egov.collection.notification.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.egov.collection.notification.config.PropertiesManager;
import org.egov.collection.notification.domain.model.City;
import org.egov.collection.notification.persistence.repository.BusinessDetailsRepository;
import org.egov.collection.notification.persistence.repository.TenantRepository;
import org.egov.collection.notification.web.contract.*;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class SMSService {

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private PropertiesManager propertiesManager;

    @Autowired
    private BusinessDetailsRepository businessDetailsRepository;

    @Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    public void send(ReceiptRequest receiptRequest) {
        List<Receipt> receipts = receiptRequest.getReceipt();
        Receipt receipt = receipts.get(0);
        Bill bill = receipt.getBill().get(0);
        String mobileNumber = bill.getMobileNumber();
        String applicantName = bill.getPayeeName();
        final City city = tenantRepository.fetchTenantByCode(receiptRequest.getRequestInfo(),receiptRequest.getTenantId());
        sendSMSNotification(bill, applicantName, mobileNumber, city);

    }

    private void sendSMSNotification(Bill bill, String applicantName, String mobileNumber, City city) {

        final SmsRequest smsRequest = SmsRequest.builder().message(getSMSMessage(bill,applicantName,city)).mobileNumber(mobileNumber).build();

        log.info("Collection SMS details------------" + smsRequest);
        try {
            kafkaTemplate.send(propertiesManager.getSmsNotificationTopic(), propertiesManager.getSmsNotificationTopicKey(),
                    smsRequest);
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

    private String getSMSMessage(final Bill bill,final String applicantName,final City city) {
        StringBuilder smsString = new StringBuilder();
        StringBuilder consumerCodes = new StringBuilder();
        StringBuilder amountPaid = new StringBuilder();

        Long receiptDate = 0l;
        int count = 0;
        for(BillDetail billDetail : bill.getBillDetails()) {
            if(count >= 1 && count != bill.getBillDetails().size()) {
                consumerCodes.append(",");
                amountPaid.append(",");
            }
            consumerCodes.append(billDetail.getConsumerCode());
            amountPaid.append("Rs.").append(billDetail.getAmountPaid());
            receiptDate = billDetail.getReceiptDate();

            count++;
        }
        Date receiptCreateDate = new Date(receiptDate);
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        smsString.append("Dear ").append(bill.getPayeeName()).append(",").append("\n").append("Payment received.Info: Consumer code ").append(consumerCodes.toString())
                .append(" for Amount").append(amountPaid).append(" respectively on ").append(format.format(receiptCreateDate))
                .append("\n").append(city.getName());
        return smsString.toString();
    }
}
